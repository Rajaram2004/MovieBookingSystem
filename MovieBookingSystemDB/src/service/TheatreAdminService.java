package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import dao.MovieDAO;
import dao.RequestTheatreDAO;
import dao.ScreenDAO;
import dao.ShowDAO;
import dao.TheatreDAO;
import dao.TicketDAO;
import dao.UserDAO;
import model.Admin;
import model.Movies;
import model.RequestTheatre;
import model.Screen;
import model.Seat;
import model.Show;
import model.Theatre;
import model.TheatreAdmin;
import model.Ticket;
import model.User;
import util.Helper;
import util.Input;

public class TheatreAdminService {
	PrinterService printerServiceObj = new PrinterService();
	TicketService ticketServiceObj = new TicketService();
	QueryService queryServiceObj = new QueryService();
	Scanner sc = Input.getScanner();

	// -------------------------------------------------------------------------------------------------------------------------------------------------------------------
	public static void updateMovies() {
		Connection conn = Input.getConnection();
		MovieDAO.updateMoviesIsActive(conn);
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void addShow(TheatreAdmin theatreAdmin) {
		Connection conn = Input.getConnection();
		try {
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (theatreAdmin == null || theatreAdmin.getTheatre(conn) == null) {
			System.err.println("No Theatre Available");
			return;
		}
		List<Theatre> theatreList = theatreAdmin.getTheatre(conn);
		Theatre theatre = selectTheatre(theatreList);
		if (theatre == null) {
			return;
		}
		List<Screen> listOfScreen = theatre.getListOfScreen(conn, theatre);
		if (listOfScreen.isEmpty() || listOfScreen == null) {
			System.err.println("No Screen Available in this theatre");
			return;
		}
		boolean screenPresent = false;
		for (Screen s : listOfScreen) {
			if (s.getStatus().equalsIgnoreCase("active")) {
				screenPresent = true;
				break;
			}
		}
		if (screenPresent == false) {
			System.err.println("No Screen Available in this theatre");
			return;
		}

		Movies movie = getValidMovie(conn);
		if (movie == null) {
			System.out.println("------Back------");
			return;
		}

		long epochTime = getValidDateTime();
		if (epochTime == 0) {
			System.out.println("------Back------");
			return;
		}
		Screen screen = getValidScreen(conn, theatre, epochTime, movie, listOfScreen);
		if (screen == null) {
			System.out.println("------Back------");
			return;
		}
		Show show = new Show((long) 0, movie, screen, theatre, (long) epochTime, "active");
		long showId = queryServiceObj.addShowToDB(conn, show);
		queryServiceObj.cloneSeatsForShow(conn, showId, screen.getScreenId());
		try {
			if (showId > 0) {
				System.out.println("Show Successfully Created and ID is " + showId);
				conn.commit();
			} else {
				System.err.println("Something is wrong");
				conn.rollback();
			}
		} catch (SQLException e) {
			System.err.println("last exception");
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException ignored) {
			}
		}
	}
	// -------------------------------------------------------------------------------------------------------------------------------------------------------------------

	boolean isValidShowTime(Connection conn, Theatre theatre, Screen screen, long newShowStartMillis,
			int newMovieDurationMinutes) {

		long newShowEndMillis = newShowStartMillis + (newMovieDurationMinutes * 60_000L) + (20 * 60_000L);

		List<Show> listOfShow = ShowDAO.getActiveShowsByTheatreId(conn, theatre.getTheatreId());

		for (Show s : listOfShow) {
			if (s.getScreen().getScreenId() == screen.getScreenId()) {

				long existingShowStartMillis = s.getShowDateTime(); // must already be in millis
				long existingShowEndMillis = existingShowStartMillis + (s.getMovie().getDuration() * 60_000L)
						+ (20 * 60_000L);

				boolean overlap = (newShowStartMillis >= existingShowStartMillis
						&& newShowStartMillis < existingShowEndMillis)
						|| (newShowEndMillis > existingShowStartMillis && newShowEndMillis <= existingShowEndMillis)
						|| (newShowStartMillis <= existingShowStartMillis && newShowEndMillis >= existingShowEndMillis);

				if (overlap) {
					return false;
				}
			}
		}
		return true;
	}

	private long getValidDateTime() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		while (true) {
			System.out.print("Enter Show Date & Time (yyyy-MM-dd HH:mm) (or Type '0' to Exit): ");
			String input = sc.nextLine();
			if (input.equalsIgnoreCase("0")) {
				System.out.println("------Back------");
				return 0;
			}
			try {
				LocalDateTime dateTime = LocalDateTime.parse(input, formatter);
				LocalDateTime maxDate = LocalDateTime.now().plusMonths(3);

				if (dateTime.isBefore(LocalDateTime.now())) {
					System.err.println("Date/time must be in the future. Please try again.");
				} else if (dateTime.isAfter(maxDate)) {
					System.err.println("Show must be With in 3 months from now.");
					System.out.println("Last allowed date: " + maxDate.format(formatter));
				} else {
					return dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
				}
			} catch (DateTimeParseException e) {
				System.err.println("Invalid date/time format. Please try again.");
			}
		}
	}

	private Screen getValidScreen(Connection conn, Theatre theatre, long epochTime, Movies movie,
			List<Screen> listOfScreen) {
		try {
			if (conn.isClosed()) {
				System.err.println("----------------------getValidScreen");
			}
		} catch (Exception e) {

		}
		while (true) {
			List<Screen> list = new ArrayList<>();
			for (Screen s : listOfScreen) {
				if (s.getStatus().equalsIgnoreCase("active")
						&& isValidShowTime(conn, theatre, s, epochTime, movie.getDuration())) {
					list.add(s);
				}
			}
			if (list == null || list.isEmpty()) {
				System.err.println("No Screen Available in This Time/date");
				return null;
			}
			System.out.println("Available Screen: ");
			int count = 1;
			for (Screen f : list) {
				System.out.println(count + " . " + f.getScreenName());
				count++;
			}
			System.out.print("Enter Screen Number (or Type '0' to Exit):");

			String input = sc.nextLine();
			if (input.equalsIgnoreCase("0")) {
				System.out.println("------Back------");
				return null;
			}
			try {
				Long screenNumber = Long.parseLong(input);
				int index = (int) (screenNumber - 1);
				return list.get(index);
			} catch (NumberFormatException e) {
				System.err.println("NumberFormatException");
			} catch (Exception n) {
				System.err.println("Invalid Screen number");
				return null;
			}
			System.err.println("Invalid input ");
		}
	}

	private Movies getValidMovie(Connection conn) {
		while (true) {

			List<Movies> movieList = printerServiceObj.printAllMovies1(conn, 1);

			System.out.print("Enter Movie ID (or Type '0' to Exit):");
			String input = sc.nextLine();
			if (input.equalsIgnoreCase("0")) {
				System.out.println("------Back------");
				return null;
			}
			try {
				long movieId = Integer.parseInt(input);
				if (movieId == 0) {
					System.out.println("------Back------");
					return null;
				}

				for (Movies movie : movieList) {
					if (movie.getMovieId() == movieId) {
						return movie;
					}
				}
				System.out.println("Invalid Movie ID. Try again.");
			} catch (NumberFormatException e) {
				System.out.println("Please enter a valid number.");
			}
		}
	}

	public Theatre selectTheatre(List<Theatre> theatreList) {
		if (theatreList == null || theatreList.isEmpty()) {
			System.out.println("No theatres available.");
			return null;
		}

		System.out.println("-----------------------------------------------------------");
		System.out.printf("| %-8s | %-20s | %-20s |%n", "ID", "Name", "Location");
		System.out.println("-----------------------------------------------------------");
		for (Theatre theatre : theatreList) {
			if (theatre.getStatus().equalsIgnoreCase("active")) {
				System.out.printf("| %-8d | %-20s | %-20s |%n", theatre.getTheatreId(), theatre.getTheatreName(),
						theatre.getTheatreLocation());

			}

		}
		System.out.println("-----------------------------------------------------------");

		System.out.print("Enter Theatre ID to select (or Type '0' to Exit): ");
		Long theatreId = Input.getLong((long) 1000000000);
		if (theatreId == 0) {
			System.out.println("------Back------");
			return null;
		}

		for (Theatre theatre : theatreList) {
			if (theatre.getTheatreId() == theatreId && theatre.getStatus().equalsIgnoreCase("active")) {
				System.out.println(theatre.getTheatreName());
				return theatre;
			} else if (theatre.getTheatreId() == theatreId) {
				System.err.println("Deleted Theatre you can't Access");
				return null;
			}

		}

		System.out.println("Invalid Theatre ID. Please try again.");
		return null;
	}

	public void printShows(Connection conn, TheatreAdmin theatreAdmin, String timeZone) {
		if (theatreAdmin == null || theatreAdmin.getTheatre(conn) == null) {
			System.err.println("No Theatre Available");
			return;
		}
		List<Theatre> theatreList = theatreAdmin.getTheatre(conn);
		Theatre theatre = selectTheatre(theatreList);

		if (theatre == null) {
			System.out.println("------Back------");
			return;
		}
		List<Show> sortedShows = ShowDAO.getAllShowsByTheatreId(conn, theatre.getTheatreId());
		printShow(sortedShows, theatreAdmin.getTimeZone());
	}

	public void printShow(List<Show> showList, String timeZone) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(ZoneId.of(timeZone));
		System.out.println(
				"+---------+-------------------+---------------------------+-----------+----------------------+------------+");
		System.out.println(
				"| Show ID | Theatre           | Movie                     | Screen    | Date & Time          | Status     |");
		System.out.println(
				"+---------+-------------------+---------------------------+-----------+----------------------+------------+");

		for (Show s : showList) {
			String dateTime = formatter.format(Instant.ofEpochMilli(s.getShowDateTime()));

			System.out.printf("| %-7d | %-17s | %-25s | %-9s | %-20s | %-10s |\n", s.getShowId(),
					s.getTheatre().getTheatreName(), s.getMovie().getMovieTitle(), s.getScreen().getScreenName(),
					dateTime, s.getStatus());
		}

		System.out.println(
				"+---------+-------------------+---------------------------+-----------+----------------------+------------+");
	}

	public void viewSeatAllocationForPastShows(TheatreAdmin theatreAdmin) {
		Connection conn = Input.getConnection();
		Theatre theatre = selectTheatre(theatreAdmin.getTheatre(conn));
		if (theatre == null)
			return;
		Show show = displayPastShow(theatre, theatreAdmin.getTimeZone());
		if (show == null) {
			return;
		}
		ticketServiceObj.displaySeatsByCategory(conn, show);
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private Show displayPastShow(Theatre theatre, String timeZone) {
		Connection conn = Input.getConnection();
		HashMap<Long, Show> pastShow = new HashMap<>();
		List<Show> showList = ShowDAO.getAllShowsByTheatreId(conn, theatre.getTheatreId());
		for (Show show : showList) {
			if (show.getStatus().equalsIgnoreCase("completed")) {
				pastShow.put(show.getShowId(), show);
			}
		}
		if (pastShow.isEmpty()) {
			System.err.println("No Past Show Available");
			return null;
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(ZoneId.of(timeZone));
		System.out.println(
				"+---------+--------------------------+------------------------+-----------+-------------------+-----------+");
		System.out.println(
				"| Show ID | Theatre                  | Movie                  | Screen    | Date & Time       | Status    |");
		System.out.println(
				"+---------+--------------------------+------------------------+-----------+-------------------+-----------+");

		for (Show s : pastShow.values()) {

			String dateTime = formatter.format(Instant.ofEpochMilli

			(s.getShowDateTime()));

			System.out.printf("| %-7d | %-20s | %-26s | %-9s | %-17s | %-9s |\n", s.getShowId(),
					s.getTheatre().getTheatreName(), s.getMovie().getMovieTitle(), s.getScreen().getScreenName(),
					dateTime, s.getStatus());
		}

		System.out.println(
				"+---------+--------------------------+------------------------+-----------+-------------------+-----------+");

		System.out.println("Enter Show Id (or type '0' to Exit):");
		long showId = Input.getLong((long) 1000000000);
		if (showId == 0) {
			return null;
		}
		Show show = pastShow.get(showId);
		if (show == null) {
			System.err.println("Invalid Show ID");
			return null;
		}
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return show;

	}

	public void theatreAdminSeatAvailability(TheatreAdmin theatreAdmin, TheatreAdminService theatreAdminService,
			String timeZone) {
		Connection conn = Input.getConnection();
		if (theatreAdmin == null || theatreAdmin.getTheatre(conn) == null) {
			System.err.println("No Theatre Available");
			return;
		}
		List<Theatre> theatreList = theatreAdmin.getTheatre(conn);
		Theatre theatreObj = theatreAdminService.selectTheatre(theatreList);
		if (theatreObj == null) {
			System.out.println("------Back------");
			return;
		}

		Movies movieObj = selectTheatreMovie1(conn, theatreAdmin, theatreObj);
		if (movieObj == null) {
			System.out.println("------Back------");
			return;
		}

		List<Show> shows = ticketServiceObj.getShowsForMovieAndTheatre(conn, movieObj, theatreObj);
		Show show = printerServiceObj.getShow(shows, timeZone);
		if (show == null) {
			System.err.println("No show selected.");
		} else {
			ticketServiceObj.displaySeatsByCategory(conn, show);
		}
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Movies selectTheatreMovie1(Connection conn, TheatreAdmin theatreAdmin, Theatre theatre) {
		List<Movies> movieList = MovieDAO.getActiveMoviesByTheatreId(conn, theatre.getTheatreId());
		if (movieList.isEmpty()) {
			System.err.println("No Movies Available");
			return null;
		}
		printTheatrelMovies1(conn, theatreAdmin, theatre, movieList);

		System.out.print("Please Enter the Movie ID (or Type '0' to Exit): ");

		long movieId = Input.getLong((long) movieList.getLast().getMovieId());
		if (movieId == 0) {
			return null;
		}
		for (Movies m : movieList) {
			if (m.getMovieId() == movieId) {
				return m;
			}
		}
		System.out.println("Invalid Movie ID.");
		return null;
	}

	public void printTheatrelMovies1(Connection conn, TheatreAdmin theatreAdmin, Theatre theatre,
			List<Movies> movieList) {
		if (theatre == null) {
			System.out.println("------Back------");
			return;
		}
		String format = "| %-4s | %-27s | %-10s | %-10s | %-10s | %-6s |%n";

		System.out.println(
				"+------+-----------------------------+------------+------------+------------+--------+--------------------------------+");
		System.out.format(format, "ID", "Title", "Duration", "Genre", "Language", "Year", "Theatres");
		System.out.println(
				"+------+-----------------------------+------------+------------+------------+--------+--------------------------------+");
		List<Show> show = ShowDAO.getActiveShowsByTheatreId(conn, theatre.getTheatreId());
		List<Long> list = new ArrayList<>();

		for (Show sh : show) {
			list.add(sh.getMovie().getMovieId());
		}
		for (Movies m : movieList) {

			int hours = m.getDuration() / 60;
			int minutes = m.getDuration() % 60;
			String durationFormatted = String.format("%d hr %02d min", hours, minutes);

			System.out.format(format, m.getMovieId(), m.getMovieTitle(), durationFormatted, m.getGenre(),
					m.getLanguage(), m.getReleaseYear());

		}
		System.out.println(
				"+------+-----------------------------+------------+------------+------------+--------+--------------------------------+");
	}

	public void addScreen(TheatreAdmin theatreAdmin) {
		Connection conn = Input.getConnection();
		try {
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (theatreAdmin == null || theatreAdmin.getTheatre(conn) == null) {
			System.err.println("No Theatre Available");
			return;
		}
		List<Theatre> theatreList = theatreAdmin.getTheatre(conn);
		Theatre theatre = selectTheatre(theatreList);
		if (theatre == null) {
			System.out.println("------Back------");
			return;
		}
		System.out.println("Enter the Number of Rows (or Type '0' to Exit):");
		int totalRows = Input.getInteger(10000);
		if (totalRows == 0) {
			System.out.println("------Back------");
			return;
		}
		List<Integer> seatsPerRow = new ArrayList<>();
		List<Double> pricesPerRow = new ArrayList<>();

		for (int r = 1; r <= totalRows; r++) {
			System.out.println("Enter the number of seats in Row " + r + ": ");
			int seatCount = Input.getInteger(100);
			seatsPerRow.add(seatCount);

			System.out.println("Enter the price for Row " + r + ": ");
			double price = Input.getDouble(10000);
			pricesPerRow.add(price);
		}

		String screenName = "Screen " + (theatre.getListOfScreen(conn, theatre).size() + 1);

		Screen screen = new Screen(screenName, theatre, "active");

		int screenId = queryServiceObj.insertScreen(conn, screen, theatre);
		screen.setScreenId(screenId);

		queryServiceObj.insertSeats(conn, screenId, totalRows, seatsPerRow, pricesPerRow);

		List<Seat> seatList = queryServiceObj.getSeatListByScreen(conn, screen.getScreenId());
		displaySeatLayout(seatList);

		System.out.println("\nScreen " + screenName + " created successfully!");
		System.out.println("Total Rows: " + totalRows);
		try {
			conn.setAutoCommit(true);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException ignored) {
			}
		}
	}

	String toRowLabel(int rowNumber) {
		StringBuilder label = new StringBuilder();
		while (rowNumber > 0) {
			rowNumber--;
			char letter = (char) ('A' + (rowNumber % 26));
			label.insert(0, letter);
			rowNumber /= 26;
		}
		return label.toString();
	}

	public void displaySeatLayout(List<Seat> seatList) {
		if (seatList == null || seatList.isEmpty()) {
			System.out.println("No seats available!");
			return;
		}
		seatList.sort((s1, s2) -> {
			String seatNum1 = s1.getSeatNumber();
			String seatNum2 = s2.getSeatNumber();

			String row1 = seatNum1.replaceAll("[0-9]", "");
			String row2 = seatNum2.replaceAll("[0-9]", "");
			int rowCompare = row1.compareTo(row2);
			if (rowCompare != 0)
				return rowCompare;
			int num1 = Integer.parseInt(seatNum1.replaceAll("[^0-9]", ""));
			int num2 = Integer.parseInt(seatNum2.replaceAll("[^0-9]", ""));
			return Integer.compare(num1, num2);
		});

		String currentRow = "";
		double oldPrice = 0, newPrice = 0;
		int count = 0;

		System.out.println("\n--------- SCREEN SEAT LAYOUT ---------");

		for (Seat seat : seatList) {
			String seatNumber = seat.getSeatNumber();
			String row = seatNumber.replaceAll("[0-9]", "");
			newPrice = seat.getPrice();
			if (!row.equals(currentRow)) {
				currentRow = row;
				if (count != 0)
					System.out.println("  <------ : " + oldPrice);
				System.out.print(row + " ");
				oldPrice = newPrice;
			}
			System.out.print(" [" + seatNumber + "] ");
			count = 1;
		}
		System.out.println("  <------ : " + oldPrice);
		System.out.println("\n-------------------------------------");
	}

	public void editTheatreAdminDetails(TheatreAdmin theatreAdmin) {
		Connection conn = Input.getConnection();
		String details[] = { "1 . Edit Theatre Admin Name ", "2 . Edit Theatre Admin Phone Number",
				"3 . Change Theatre Admin Password" };
		System.out.println("-----------------------------------------------");
		for (int i = 0; i < details.length; i++) {
			System.out.println(details[i]);
		}
		System.out.println("-----------------------------------------------");
		int choice = 0;
		System.out.println("Enter Your Choice (or Type '0' to Exit): ");
		choice = Input.getInteger(details.length);
		if (choice == 0) {
			System.out.println("------Back------");
			return;
		}
		switch (choice) {
		case 1:
			System.out.println("You Have Selected Edit Theatre Admin Name ");
			editTheatreAdminName(conn, theatreAdmin);
			break;
		case 2:
			System.out.println("You Have Selected Edit Theatre Admin Phone Number ");
			editTheatreAdminPhoneNumber(conn, theatreAdmin);
			break;
		case 3:
			System.out.println("You Have Selected Edit Theatre Admin Password ");
			changeTheatreAdminPassword(conn, theatreAdmin);
			break;
		}
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void changeTheatreAdminPassword(Connection conn, TheatreAdmin theatreAdmin) {
		String newPassword, confirmNewPassword;
		while (true) {
			System.out.print(
					"Enter new password (min 6 chars, at least 1 digit, 1 uppercase) (or Type 'done' to Exit): ");
			newPassword = sc.nextLine();
			if (newPassword.equalsIgnoreCase("done")) {
				System.out.println("------Back------");
				return;
			}
			if (isValidPassword(newPassword)) {
				System.out.print("Confirm new password: ");
				confirmNewPassword = sc.nextLine();

				if (confirmNewPassword.equals(newPassword)) {
					String updateQuery = "UPDATE users SET password = ? WHERE id = ?";
					try (PreparedStatement ps = conn.prepareStatement(updateQuery)) {
						ps.setString(1, newPassword);
						ps.setLong(2, theatreAdmin.getTheatreAdminId());

						int rowsUpdated = ps.executeUpdate();
						if (rowsUpdated > 0) {
							theatreAdmin.setTheatreAdminPassword(newPassword);
							System.out.println("Password updated successfully in database!");
						} else {
							System.err.println("Failed to update password in database. Please try again.");
						}
					} catch (Exception e) {
						System.err.println("Error updating password: " + e.getMessage());
					}
					break;
				} else {
					System.err.println("Confirm password does not match!");
				}
			} else {
				System.err.println("Invalid password format. Please try again.");
			}
		}
	}

	private void editTheatreAdminName(Connection conn, TheatreAdmin theatreAdmin) {
		Scanner sc = Input.getScanner();
		String newName;

		while (true) {
			System.out.print("Enter new username (only alphabets & spaces allowed) (or Type '0' to Exit): ");
			newName = sc.nextLine();

			if (newName.equalsIgnoreCase("0")) {
				System.out.println("------Back------");
				return;
			}

			if (isValidName(newName)) {
				theatreAdmin.setTheatreAdminName(newName);
				String updateQuery = "UPDATE users SET name = ? WHERE id = ?";
				try (PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
					pstmt.setString(1, newName);
					pstmt.setLong(2, theatreAdmin.getId());
					int rowsUpdated = pstmt.executeUpdate();
					if (rowsUpdated > 0) {
						System.out.println("Name updated successfully in both TheatreAdmin & User table!");
					} else {
						System.err.println("Failed to update name in User table!");
					}
				} catch (SQLException e) {
					System.err.println("Error while updating name: " + e.getMessage());
				}
				break;
			} else {
				System.err.println("Invalid name. Please try again.");
			}
		}
	}

	private void editTheatreAdminPhoneNumber(Connection conn, TheatreAdmin theatreAdmin) {
		String newPhone;
		while (true) {
			System.out.print("Enter new phone number (6 digits) (or Type '0' to Exit): ");
			newPhone = sc.nextLine();
			if (newPhone.equalsIgnoreCase("0")) {
				System.out.println("------Back------");
				return;
			}
			if (isValidPhoneNumber(newPhone)) {
				try {
					theatreAdmin.setTheatreAdminPhoneNumber(Long.parseLong(newPhone));
					String updateQuery = "UPDATE users SET phoneNumber = ? WHERE id = ?";
					try (PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
						pstmt.setLong(1, Long.parseLong(newPhone));
						pstmt.setLong(2, theatreAdmin.getId());
						int rowsUpdated = pstmt.executeUpdate();
						if (rowsUpdated > 0) {
							System.out.println("Phone number updated successfully in database!");
						} else {
							System.err.println("Failed to update phone number in database.");
						}
					}
				} catch (Exception e) {
					System.err.println("Error updating phone number: " + e.getMessage());
				}
				break;
			} else {
				System.err.println("Invalid phone number. Please try again.");
			}
		}
	}

	private boolean isValidPassword(String password) {
		return password != null && password.matches("^(?=.*[0-9])(?=.*[A-Z]).{6,}$");
	}

	private boolean isValidPhoneNumber(String phone) {
		return phone != null && phone.matches("^[0-9]{6,12}$");
	}

	private boolean isValidName(String name) {
		return name != null && name.matches("^[A-Za-z ]+$");
	}

	public void printShowFuture(TheatreAdmin theatreAdmin, String timeZone) {
		Connection conn = Input.getConnection();
		if (theatreAdmin == null || theatreAdmin.getTheatre(conn) == null) {
			System.err.println("No Theatre Available");
			return;
		}
		List<Theatre> theatreList = theatreAdmin.getTheatre(conn);
		Theatre theatre = selectTheatre(theatreList);
		if (theatre == null) {
			System.out.println("------Back------");
			return;
		}
		List<Show> futureShows = ShowDAO.getActiveShowsByTheatreId(conn, theatre.getTheatreId());
		futureShows = futureShows.stream().filter(s -> s.getShowDateTime() > System.currentTimeMillis())
				.collect(Collectors.toList());
		printShow(futureShows, theatreAdmin.getTimeZone());
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addNewTheatre(TheatreAdmin theatreAdmin) {
		Connection conn = Input.getConnection();
		try {
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String theatreName, theatreLocation;
		System.out.print("Enter Theatre Name (or Type '0' to Exit):");
		theatreName = sc.nextLine().trim();
		if (theatreName.equalsIgnoreCase("0")) {
			System.out.println("------🔙------");
			return;
		}
		while (theatreName.isEmpty()) {
			System.out.print("Theatre name cannot be empty. Enter again (or Type '0' to Exit): ");
			theatreName = sc.nextLine().trim();
			if (theatreName.equalsIgnoreCase("0")) {
				System.out.println("------🔙------");
				return;
			}
		}
		System.out.print("Enter Theatre Location (or Type '0' to Exit):");
		theatreLocation = sc.nextLine().trim();
		if (theatreLocation.equalsIgnoreCase("0")) {
			System.out.println("------🔙------");
			return;
		}
		while (theatreLocation.isEmpty()) {
			System.out.print("Theatre location cannot be empty. Enter again (or Type '0' to Exit): ");
			theatreLocation = sc.nextLine().trim();
			if (theatreLocation.equalsIgnoreCase("0")) {
				System.out.println("------Back------");
				return;
			}
		}
		Long theatreId = TheatreDAO.insertTheatreAndGetId(conn, theatreName, theatreLocation, "Requested");
		if (theatreId == -1) {
			System.out.println("theatre creation failled");
			return;
		}
		long requestId = RequestTheatreDAO.insertRequestTheatre(conn, theatreId, theatreAdmin.getTheatreAdminId());
		if (requestId == -1) {
			System.out.println("request creation failled");

			return;
		}
		System.out.println("📝New Theatre Requested to Admin and Request Id : " + requestId);
		try {
			conn.setAutoCommit(true);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException ignored) {
			}
		}
	}

	public void deleteScreen(TheatreAdmin theatreAdmin) {
		Connection conn = Input.getConnection();
		try {
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Theatre theatre = selectTheatre(theatreAdmin.getTheatre(conn));
		if (theatre == null)
			return;
		Screen screen = selectScreen(conn, theatre);
		if (screen == null) {
			System.err.println("Invalid Screen Number");
			return;
		}
		if (updateScreenStatus(conn, screen.getScreenId(), "Deleted")) {
			try {
				conn.setAutoCommit(true);

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if (conn != null)
						conn.close();
				} catch (SQLException ignored) {
				}
			}
		}
		System.out.println("Screen Deleted. You can no longer add shows to this screen.");

	}

	public boolean updateScreenStatus(Connection connection, long screenId, String newStatus) {
		String query = "UPDATE screen SET status = ? WHERE screenId = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, newStatus);
			pstmt.setLong(2, screenId);

			int rowsUpdated = pstmt.executeUpdate();
			return rowsUpdated > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	private Screen selectScreen(Connection conn, Theatre theatre) {
		List<Screen> listOfScreen = theatre.getListOfScreen(conn, theatre);
		if (listOfScreen == null || listOfScreen.isEmpty()) {
			System.err.println("No Screen available");
			return null;
		}
		HashMap<Integer, Screen> map = new HashMap<>();
		int index = 0;
		for (Screen s : listOfScreen) {
			if (s.getStatus().equalsIgnoreCase("active")) {
				index++;
				map.put(index, s);
			}
		}
		while (true) {
			System.out.println("Available Screen ");
			index = 1;
			for (Screen screen : map.values()) {
				System.out.println(index + " . " + screen.getScreenName());
				index++;
			}
			System.out.println("Enter Screen Number (or type '0' to Exit): ");
			int screenNumber = Input.getInteger(30);
			if (screenNumber == 0) {
				return null;
			}
			return map.get(screenNumber);
		}
	}

	public void cancelShow(TheatreAdmin theatreAdmin) {
		Connection conn = Input.getConnection();
		try {
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Theatre theatre = selectTheatre(theatreAdmin.getTheatre(conn));
		HashMap<Long, Show> listOfShow = printShowFuturewithReturn(conn, theatreAdmin, theatreAdmin.getTimeZone(),
				theatre);
		if (listOfShow == null) {
			System.err.println("No Shows Available");
			return;
		}
		if (theatre == null) {
			return;
		}
		System.out.println("Enter the Show id (or type '0' to Exit): ");
		long showId = Input.getLong(Long.MAX_VALUE);
		if (showId == 0) {
			return;
		}
		Show show = listOfShow.get(showId);
		List<Ticket> ticketList = TicketDAO.getTicketsByShowId(conn, show.getShowId());
		if (show != null) {
			if (show.getStatus().equalsIgnoreCase("active")) {
				for (Ticket ticket : ticketList) {
					cancelTicket(conn, ticket.getUser(), ticket);
				}
			}
			ShowDAO.updateShowStatus(conn, show.getShowId(), "Cancelled");
		}
		if (show.getStatus().equalsIgnoreCase("cancelled")) {
			System.err.println("Show Already Cancelled");
		}

		try {
			System.out.println("Show Cancelled");
			conn.setAutoCommit(true);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException ignored) {
			}
		}
	}

	public double calculateAmount(List<Seat> seatList) {
		double total = 0;
		for (Seat seat : seatList) {
			total += seat.getPrice();
		}
		return total;
	}

	public void cancelTicket(Connection conn, User currentUser, Ticket ticket) {
		if (ticket == null || !ticket.getUser().equals(currentUser)) {
			System.err.println("No ticket found with this ID for your account.");
			return;
		}
		if (ticket.getStatus().equalsIgnoreCase("cancelled")) {
			System.err.println(ticket.getTicketId() + " Ticket is already cancelled.");
			return;
		}
		ticket.setStatus("Refunded");
		List<Seat> seatList = TicketDAO.getSeatsByTicketId(conn, ticket.getTicketId());
		ticketServiceObj.releaseBookedSeats(conn, ticket.getTicketId());
		double totalTicketAmount = calculateAmount(seatList);
		double userAmount = ticketServiceObj.getUserBalanceFromDB(conn, currentUser.getId());
		userAmount = userAmount + totalTicketAmount;
		currentUser.newBalance(conn, userAmount);
		Helper.updateTicketStatus(conn, ticket.getTicketId(), "Refunded");
		System.out.println("Ticket ID " + ticket.getTicketId() + " has been successfully cancelled.");
	}

	public HashMap<Long, Show> printShowFuturewithReturn(Connection conn, TheatreAdmin theatreAdmin, String timeZone,
			Theatre theatre) {
		if (theatreAdmin == null || theatreAdmin.getTheatre(conn) == null) {
			System.err.println("No Theatre Available");
			return null;
		}
		if (theatre == null) {
			System.out.println("------Back------");
			return null;
		}
		HashMap<Long, Show> listOfShow = new HashMap<>();

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(ZoneId.of(timeZone));
		System.out.println(
				"+---------+-------------------+---------------------------+-----------+----------------------+");
		System.out.println(
				"| Show ID | Theatre           | Movie                     | Screen    | Date & Time          |");
		System.out.println(
				"+---------+-------------------+---------------------------+-----------+----------------------+");
		List<Show> showList = ShowDAO.getActiveShowsByTheatreId(conn, theatre.getTheatreId());
		for (Show s : showList) {
			if (s.getShowDateTime() > Instant.now().getEpochSecond() && s.getStatus().equalsIgnoreCase("active")) {
				String dateTime = formatter.format(Instant.ofEpochMilli(s.getShowDateTime()));
				System.out.printf("| %-7d | %-17s | %-22s | %-9s | %-20s |\n", s.getShowId(),
						s.getTheatre().getTheatreName(), s.getMovie().getMovieTitle(), s.getScreen().getScreenName(),
						dateTime);
				listOfShow.put(s.getShowId(), s);
			}
		}
		System.out.println(
				"+---------+-------------------+---------------------------+-----------+----------------------+");
		return listOfShow;
	}

	public void deleteTheatre(TheatreAdmin theatreAdmin) {
		Connection conn = Input.getConnection();
		Theatre theatre = selectTheatre(theatreAdmin.getTheatre(conn));

		if (theatre == null) {
			System.out.println("No Theatre Available");
			return;
		}
		String query = "UPDATE theatre SET status='Deleted' WHERE theatreId = ?";

		try {
			PreparedStatement pst = conn.prepareStatement(query);
			pst.setLong(1, theatre.getTheatreId());
			int rowsAffected = pst.executeUpdate();
			if (rowsAffected > 0) {
				System.out.println(theatre.getTheatreName() + " Theatre Deleted");
			} else {
				System.out.println("No Theatre was deleted. Please check the ID.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
	}

	public void checkRequestStatus(TheatreAdmin theatreAdmin) {
		Connection conn = Input.getConnection();
		String format = "| %-10s | %-25s | %-25s | %-10s |%n";

		System.out.println("+------------+---------------------------+---------------------------+------------+");
		System.out.format(format, "Request ID", "Theatre Name", "Location", "Approved");
		System.out.println("+------------+---------------------------+---------------------------+------------+");
		List<RequestTheatre> requestList = RequestTheatreDAO.getRequestTheatre(conn, theatreAdmin.getTheatreAdminId());
		for (RequestTheatre req : requestList) {
			System.out.format(format, req.getRequestId(), req.getTheatre().getTheatreName(),
					req.getTheatre().getTheatreLocation(), req.isApproved() ? "Yes" : "No");
			System.out.println("+------------+---------------------------+---------------------------+------------+");

		}
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void printRunningShow(TheatreAdmin theatreAdmin, String timeZone) {
		Connection conn = Input.getConnection();
		if (theatreAdmin == null || theatreAdmin.getTheatre(conn) == null) {
			System.err.println("No Theatre Available");
			return;
		}
		List<Theatre> theatreList = theatreAdmin.getTheatre(conn);
		Theatre theatre = selectTheatre(theatreList);

		if (theatre == null) {
			System.out.println("------Back------");
			return;
		}

		List<Show> futureShows = ShowDAO.getAllShowsByTheatreId(conn, theatre.getTheatreId());
		futureShows = futureShows.stream().filter(s -> s.getStatus().equalsIgnoreCase("Running"))
				.collect(Collectors.toList());
		if (futureShows.isEmpty() || futureShows == null) {
			System.err.println("No Running Show");
			return;
		}
		futureShows.sort(Comparator.comparingLong(Show::getShowDateTime));
		printShow(futureShows, timeZone);
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void searchShowId(String timeZone) {
		System.out.println("Enter the Show Id You want to Search (or Type '0' to Exit): ");
		Long searchShowId = Input.getLong((long) 1000000000);
		if (searchShowId == 0) {
			System.out.println("------Back------");
			return;
		}
		Connection conn = Input.getConnection();
		Show s = ShowDAO.getShowById(conn, searchShowId);
		if (s == null) {
			System.out.println("Invalid Show Id");
			return;
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(ZoneId.of(timeZone));
		String dateTime = formatter.format(Instant.ofEpochMilli(s.getShowDateTime()));

		System.out.println("+---------+--------------+--------------+-----------+-------------------+");
		System.out.println("| Show ID | Theatre      | Movie        | Screen    | Date & Time       |");
		System.out.println("+---------+--------------+--------------+-----------+-------------------+");

		System.out.printf("| %-7d | %-12s | %-12s | %-9s | %-17s |\n", s.getShowId(), s.getTheatre().getTheatreName(),
				s.getMovie().getMovieTitle(), s.getScreen().getScreenName(), dateTime);

		System.out.println("+---------+--------------+--------------+-----------+-------------------+");
	}

	public void printShowAllFuture(String timeZone) {
		Connection conn = Input.getConnection();
		List<Show> showList = ShowDAO.getAllShows(conn);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(ZoneId.of(timeZone));

		System.out.println(
				"+---------+-------------------+---------------------------+-----------+----------------------+");
		System.out.println(
				"| Show ID | Theatre           | Movie                     | Screen    | Date & Time          |");
		System.out.println(
				"+---------+-------------------+---------------------------+-----------+----------------------+");

		showList.stream().filter(
				s -> s.getShowDateTime() > Instant.now().getEpochSecond() && "active".equalsIgnoreCase(s.getStatus()))
				.sorted(Comparator.comparingLong(Show::getShowDateTime)).forEach(s -> {
					String dateTime = formatter.format(Instant.ofEpochMilli(s.getShowDateTime()));

					System.out.printf("| %-7d | %-17s | %-25s | %-9s | %-20s |\n", s.getShowId(),
							s.getTheatre().getTheatreName(), s.getMovie().getMovieTitle(),
							s.getScreen().getScreenName(), dateTime);
				});

		System.out.println(
				"+---------+-------------------+---------------------------+-----------+----------------------+");
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void displayAllTicket(String timeZone) {
		Connection conn = Input.getConnection();
		List<Ticket> ticketList = TicketDAO.getAllTicket(conn);
		printTicket(conn, timeZone, ticketList);
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void displayDateWiseTicket(String timeZone) {
		Connection conn = Input.getConnection();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate date = null;
		while (true) {
			System.out.print("Enter date (yyyy-MM-dd) or 0 to go back: ");
			String input = sc.nextLine().trim();

			if (input.equals("0")) {
				System.out.println("----------- back-------------");
				break;
			}

			try {
				date = LocalDate.parse(input, formatter);
				System.out.println("Valid date: " + date);
				break;
			} catch (DateTimeParseException e) {
				System.out.println("Invalid date format! Please try again.");
			}
		}

		List<Ticket> ticketList = TicketDAO.getTicketsByDate(conn, date);
		if (ticketList.isEmpty()) {
			System.err.println("No Tickets Available in this date " + date);
			return;
		}
		printTicket(conn, timeZone, ticketList);
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void printTicket(Connection conn, String timeZone, List<Ticket> ticketList) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");
		System.out.printf(
				"+----+----------------------+----------------------+----------+------------------------+-----------------------+----------------------+%n");
		System.out.printf(
				"| ID | Movie                | Theatre              | Screen   | Show Date & Time       |  Status               | Seats                |%n");
		System.out.printf(
				"+----+----------------------+----------------------+----------+------------------------+-----------------------+----------------------+%n");
		for (Ticket ticket : ticketList) {
			List<String> seatNumber = ScreenDAO.getSeat(conn, ticket.getTicketId());
			ZonedDateTime zdt = Instant.ofEpochMilli(ticket.getShow().getShowDateTime()).atZone(ZoneId.of(timeZone));
			String active = ticket.getStatus();
			System.out.printf("| %-2d | %-20s | %-20s | %-6s | %-20s  | %-20s  | %-20s |%n", ticket.getTicketId(),
					ticket.getShow().getMovie().getMovieTitle(), ticket.getShow().getTheatre().getTheatreName(),
					ticket.getShow().getScreen().getScreenName(), zdt.format(formatter), active, seatNumber);
		}
		System.out.printf(
				"+----+----------------------+----------------------+----------+------------------------+-----------------------+----------------------+%n");

	}

	public void displayAllRunningShow(String timeZone) {
		Connection conn = Input.getConnection();
		List<Show> showList = ShowDAO.getRunningShow(conn);

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(ZoneId.of(timeZone));
		System.out.println(
				"+---------+--------------------------+------------------------+-----------+-------------------+-----------+");
		System.out.println(
				"| Show ID | Theatre                  | Movie                  | Screen    | Date & Time       | Status    |");
		System.out.println(
				"+---------+--------------------------+------------------------+-----------+-------------------+-----------+");
		for (Show s : showList) {
			if (s.getStatus().equalsIgnoreCase("Running")) {
				String dateTime = formatter.format(Instant.ofEpochMilli(s.getShowDateTime()));
				System.out.printf("| %-7d | %-20s | %-26s | %-9s | %-17s | %-9s |\n", s.getShowId(),
						s.getTheatre().getTheatreName(), s.getMovie().getMovieTitle(), s.getScreen().getScreenName(),
						dateTime, s.getStatus());
			}
		}
		System.out.println(
				"+---------+--------------------------+------------------------+-----------+-------------------+-----------+");
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void approveNewTheatre(Admin admin) {
		Connection conn = Input.getConnection();
		try {
			List<RequestTheatre> requestList = RequestTheatreDAO.getAllRequestTheatre(conn);
			if (requestList.isEmpty()) {
				System.err.println("No Theatre Requests Found.");
				return;
			}
			String format = "| %-10s | %-25s | %-25s | %-15s | %-10s | %-15s |%n";
			System.out.println(
					"+------------+---------------------------+---------------------------+-----------------+------------+-----------------+");
			System.out.format(format, "Request ID", "Theatre Name", "Location", "Admin ID", "Approved", "Approved By");
			System.out.println(
					"+------------+---------------------------+---------------------------+-----------------+------------+-----------------+");
			for (RequestTheatre req : requestList) {
				String approvedBy = (req.isApproved() && req.getApprovedAdmin() != null)
						? String.valueOf(req.getApprovedAdmin().getId())
						: "-";

				System.out.format(format, req.getRequestId(), req.getTheatre().getTheatreName(),
						req.getTheatre().getTheatreLocation(), req.getTheatreAdmin().getId(),
						req.isApproved() ? "Yes" : "No", approvedBy);
			}
			System.out.println(
					"+------------+---------------------------+---------------------------+-----------------+------------+-----------------+");

			while (true) {
				System.out.print("Enter the Request ID to approve (or Type '0' to Exit): ");
				long requestId = Input.getLong((long) 100000);

				if (requestId == 0) {
					System.out.println("------Back------");
					return;
				}
				RequestTheatre request = null;
				for (RequestTheatre r : requestList) {
					if (r.getRequestId() == requestId) {
						request = r;
						break;
					}
				}
				if (request == null) {
					System.err.println("Invalid Request ID. Please try again.");
					continue;
				}
				if (request.isApproved()) {
					System.err.println("This theatre is already approved.");
					continue;
				}

				System.out.print(
						"Approve Request ID: " + requestId + " ? Type '1' to Approve or any other key to Reject: ");
				int choice = Input.getInteger(100000);

				if (choice == 1) {
					try {
						conn.setAutoCommit(false);

						boolean updated = RequestTheatreDAO.approveRequest(conn, requestId, admin.getId());
						if (!updated) {
							conn.rollback();
							System.err.println("Failed to approve request.");
							continue;
						}

						Theatre theatre = request.getTheatre();
						boolean inserted = TheatreDAO.activeTheatre(conn, theatre);
						if (!inserted) {
							conn.rollback();
							System.err.println("Failed to insert theatre.");
							continue;
						}

						TheatreDAO.assignTheatreToAdmin(conn, theatre.getTheatreId(),
								request.getTheatreAdmin().getId());

						conn.commit();
						System.out.println("Theatre Approved Successfully");
						break;
					} catch (SQLException e) {
						try {
							conn.rollback();
						} catch (SQLException ex) {
							ex.printStackTrace();
						}
						e.printStackTrace();
					} finally {
						try {
							conn.setAutoCommit(true);
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				} else {
					System.out.println("------Request Rejected------");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void AddTheatreToTheatreAdmin() {
		AdminService adminService = new AdminService();
		TheatreService theatreServiceObj = new TheatreService();
		adminService.viewAllTheatreAdmins();
		Connection conn = Input.getConnection();
		List<User> userList = UserDAO.getAllUser(conn);
		List<TheatreAdmin> theatreAdminList = userList.stream()
				.filter(s -> s.getUserType().equalsIgnoreCase("theatreadmin")).map(u -> {
					TheatreAdmin t = UserDAO.getTheatreAdminById(conn, u.getId());
					t.setTheatre(t.getTheatre(conn));
					return t;
				}).collect(Collectors.toList());
		System.out.print("Enter Theatre Admin ID (or press 0 to Exit): ");
		Long id = Input.getLong((long) 1000000000);
		if (id == 0) {
			System.out.println("------Back------");
			return;
		}
		TheatreAdmin admin = null;
		for (TheatreAdmin ta : theatreAdminList) {
			if (ta.getTheatreAdminId() == id) {
				admin = ta;
				break;
			}
		}
		if (admin == null) {
			System.out.println("No Theatre Admin found with ID: " + id);
			return;
		}

		theatreServiceObj.printAllTheatres(conn);
		List<Theatre> theatreList = TheatreDAO.getTheatreList(conn);
		System.out.println("Enter the Theatre id (or Type '0' to Exit): ");
		long theatreId = Input.getLong((long) 1000000000);
		if (theatreId == 0) {
			System.out.println("------Back------");
			return;
		}

		Theatre theatre = null;
		for (Theatre t : theatreList) {
			if (t.getTheatreId() == theatreId) {
				theatre = t;
				break;
			}
		}
		if (theatre == null) {
			System.err.println("Invalid Theatre Id");
		}
		if (theatre.getStatus().equalsIgnoreCase("deleted")) {
			System.err.println("This Thatre Deleted , you can't assign permission");
		}

		TheatreDAO.assignTheatreToAdmin(conn, theatreId, id);
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void setMovieIsActiveStatus() {
		Connection conn = Input.getConnection();
		try {
			List<Movies> movieList = printerServiceObj.printAllMovies1(conn, 0);
			if (movieList == null || movieList.isEmpty()) {
				System.out.println("No movies found.");
				return;
			}
			Long movieId = Input.getLong((long) 1000000000);
			for (Movies movie : movieList) {
				if (movie.getMovieId() == movieId) {
					MovieDAO.updateMoviesActive(conn, movieId);
					System.out.println("Movie Id " + movieId + " is Set Active");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

}

//	public void approveNewTheatre(Admin admin) {
//		Connection conn = Input.getConnection();
//		List<RequestTheatre> requestList = RequestTheatreDAO.getAllRequestTheatre(conn);
//
//		if (requestList.size() == 0) {
//			System.err.println("No Request Raised");
//			return;
//		}
//		String format = "| %-10s | %-25s | %-25s | %-15s | %-10s | %-15s |%n";
//
//		System.out.println(
//				"+------------+---------------------------+---------------------------+-----------------+------------+-----------------+");
//		System.out.format(format, "Request ID", "Theatre Name", "Location", "Admin ID", "Approved", "Approved By");
//		System.out.println(
//				"+------------+---------------------------+---------------------------+-----------------+------------+-----------------+");
//
//		for (RequestTheatre req : requestList) {
//			String approvedBy = (req.isApproved() && req.getApprovedAdmin() != null)
//					? String.valueOf(req.getApprovedAdmin().getId())
//					: "-";
//
//			System.out.format(format, req.getRequestId(), req.getTheatre().getTheatreName(),
//					req.getTheatre().getTheatreLocation(), req.getTheatreAdmin().getId(),
//					req.isApproved() ? "Yes" : "No", approvedBy);
//		}
//
//		System.out.println(
//				"+------------+---------------------------+---------------------------+-----------------+------------+-----------------+");
//
//		while (true) {
//			System.out.println("Enter the Request id (or Type '0' to Exit): ");
//			long requestId = Input.getLong((long) 100000);
//			if (requestId == 0) {
//				System.out.println("------Back------");
//				return;
//			}
//			if (RequestTheatreDB.containsKey(requestId)) {
//
//				if (RequestTheatreDB.get(requestId).isApproved() == false) {
//					System.out.println("Approve Request id : " + requestId
//							+ " if yes type '1' or else press any Number to Exit):");
//					if (Input.getInteger(100000) == 1) {
//						RequestTheatreDB.get(requestId).setApproved(true);
//						theatreDB.put(RequestTheatreDB.get(requestId).getTheatre().getTheatreId(),
//								RequestTheatreDB.get(requestId).getTheatre());
//						Theatre theatre = RequestTheatreDB.get(requestId).getTheatre();
//
//						RequestTheatreDB.get(requestId).getTheatreAdmin().addTheatre(theatre);
//						RequestTheatreDB.get(requestId).setApprovedAdmin(admin);
//						System.out.println("Theatre Approved");
//					} else {
//						System.out.println("------Rejected------");
//					}
//					return;
//				} else {
//					System.err.println("Already Approved");
//					return;
//				}
//
//			} else {
//				System.err.println("Invalid Reques Id ");
//			}
//		}
//
//	}

//	* public void addScreen(TheatreAdmin theatreAdmin) { if (theatreAdmin == null
//			 * || theatreAdmin.getTheatre() == null) {
//			 * System.err.println("No Theatre Available"); return; }
//			 * 
//			 * List<Theatre> theatreList = theatreAdmin.getTheatre(); Theatre theatre =
//			 * selectTheatre(theatreList); if (theatre == null) {
//			 * System.out.println("------Back------"); return; }
//			 * 
//			 * System.out.println("Enter the Number of Rows (or Type '0' to Exit): "); int
//			 * totalRows = Input.getInteger(10000); if (totalRows == 0) {
//			 * System.out.println("------Back------"); return; }
//			 * 
//			 * // System.out.println("Enter the Number of Columns (or Type '0' to Exit): ");
//			 * // int totalCols = Input.getInteger(10000); // if (totalCols == 0) { //
//			 * System.out.println("------Back------"); // return; // }
//			 * 
//			 * List<Integer> seatsPerRow = new ArrayList<>(); List<Double> pricesPerRow =
//			 * new ArrayList<>();
//			 * 
//			 * for (int r = 1; r <= totalRows; r++) {
//			 * System.out.println("Enter the number of seats in Row " + r + ": "); int
//			 * seatCount = Input.getInteger(100); seatsPerRow.add(seatCount);
//			 * 
//			 * System.out.println("Enter the price for Row " + r + ": "); double price =
//			 * Input.getDouble(10000); pricesPerRow.add(price); }
//			 * 
//			 * int screenId = theatre.getListOfScreen().size() + 1;
//			 * 
//			 * Screen screen = new Screen(screenId, theatre, seatsPerRow, pricesPerRow);
//			 * 
//			 * theatre.getListOfScreen().add(screen);
//			 * 
//			 * System.out.println("\n Screen " + screenId + " created successfully!");
//			 * System.out.println("Total Rows: " + totalRows);
//			 * System.out.println("Seat Layout:"); screen.displayLayout();
//			 * 
//			 * }
