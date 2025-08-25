package service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

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
import repository.InMemoryDatabase;
import util.Input;

public class TheatreAdminService {
	static HashMap<Long, TheatreAdmin> theatreAdminDB = InMemoryDatabase.getTheatreAdminDB();
	static HashMap<Long, Theatre> theatreDB = InMemoryDatabase.getTheatreDB();
	static HashMap<Long, Movies> movieDB = InMemoryDatabase.getMovieDB();
	static HashMap<Long, Show> showDB = InMemoryDatabase.getShowDB();
	static HashMap<Long, RequestTheatre> RequestTheatreDB = InMemoryDatabase.getRequestTheatreDb();
	static HashMap<Long, Ticket> ticketDB = InMemoryDatabase.getTicketDB();
	AdminService adminService = new AdminService();
	TheatreService theatreServiceObj = new TheatreService();
	TicketService ticketServiceObj = new TicketService();
	Scanner sc = Input.getScanner();

	public TheatreAdminService() {
	}

	public void editTheatreAdminDetails(TheatreAdmin theatreAdmin) {
		String details[] = { "1 . Edit Theatre Admin Name ", "2 . Edit Theatre Admin Phone Number",
				"3 . Change Theatre Admin Password" };

		for (int i = 0; i < details.length; i++) {
			System.out.println(details[i]);
		}
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
			editTheatreAdminName(theatreAdmin);
			break;
		case 2:
			System.out.println("You Have Selected Edit Theatre Admin Phone Number ");
			editTheatreAdminPhoneNumber(theatreAdmin);
			break;
		case 3:
			System.out.println("You Have Selected Edit Theatre Admin Password ");
			changeTheatreAdminPassword(theatreAdmin);
			break;
		}
	}

	private void changeTheatreAdminPassword(TheatreAdmin theatreAdmin) {
		Scanner sc = new Scanner(System.in);
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
				System.out.print("Enter new password to Confirm ");
				confirmNewPassword = sc.nextLine();
				if (confirmNewPassword.equals(newPassword)) {
					theatreAdmin.setTheatreAdminPassword(newPassword);
					System.out.println("Password updated successfully!");
					break;
				} else {
					System.err.println("Invalid Confirm Password .");
				}
			} else {
				System.err.println("Invalid password format. Please try again.");
			}
		}
	}

	private void editTheatreAdminPhoneNumber(TheatreAdmin theatreAdmin) {
		Scanner sc = new Scanner(System.in);
		String newPhone;
		while (true) {
			System.out.print("Enter new phone number (6 digits) (or Type 'done' to Exit): ");
			newPhone = sc.nextLine();
			if (newPhone.equalsIgnoreCase("done")) {
				System.out.println("------Back------");
				return;
			}
			if (isValidPhoneNumber(newPhone)) {
				theatreAdmin.setTheatreAdminPhoneNumber(Long.parseLong(newPhone));
				System.out.println("Phone number updated successfully!");
				break;
			} else {
				System.out.println("Invalid phone number. Please try again.");
			}
		}
	}

	private void editTheatreAdminName(TheatreAdmin theatreAdmin) {
		Scanner sc = Input.getScanner();
		String newName;
		while (true) {
			System.out.print("Enter new username (only alphabets & spaces allowed) (or Type 'done' to Exit): ");
			newName = sc.nextLine();
			if (newName.equalsIgnoreCase("done")) {
				System.out.println("------Back------");
				return;
			}
			if (isValidName(newName)) {
				theatreAdmin.setTheatreAdminName(newName);
				System.out.println("Name updated successfully!");
				break;
			} else {
				System.out.println("Invalid name. Please try again.");
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
	// -------------------------------------------------------------------------------------------------------------------------------------------------------------------

	public void addShow(TheatreAdmin theatreAdmin) {
		if (theatreAdmin == null || theatreAdmin.getTheatre() == null) {
			System.err.println("No Theatre Available");
			return;
		}
		List<Theatre> theatreList = theatreAdmin.getTheatre();
		Theatre theatre = selectTheatre(theatreList);
		if (theatre == null) {
			System.out.println("------Back------");
			return;
		}
		if (theatre.getListOfScreen().isEmpty() || theatre.getListOfScreen() == null) {
			System.err.println("No Screen Available in this theatre");
			return;
		}
		boolean screenPresent = false;
		for (Screen s : theatre.getListOfScreen()) {
			if (s.isActive() == true) {
				screenPresent = true;
				break;
			}
		}
		if (screenPresent == false) {
			System.err.println("No Screen Available in this theatre");
			return;
		}
		Movies movie = getValidMovie();
		if (movie == null) {
			System.out.println("------Back------");
			return;
		}

		long epochTime = getValidDateTime();
		if (epochTime == 0) {
			System.out.println("------Back------");
			return;
		}
		Screen screen = getValidScreen(theatre, epochTime, movie);
		if (screen == null) {
			System.out.println("------Back------");
			return;
		}
		long showId = (long) showDB.size() + 1;
		Show show = new Show(showId, theatre, movie, screen, (int) epochTime, null);

		theatre.addShow(show);
		if (!theatre.getListOfMovies().contains(movie)) {
			theatre.getListOfMovies().add(movie);
		}
		System.out.println("Show Successfully Created and ID is " + showId);
		showDB.put(showId, show);
	}
	// -------------------------------------------------------------------------------------------------------------------------------------------------------------------

	public void searchShowId(String timeZone) {
		System.out.println("Enter the Show Id You want to Search (or Type '0' to Exit): ");
		Long searchShowId = Input.getLong((long) showDB.size());
		if (searchShowId == 0) {
			System.out.println("------Back------");
			return;
		}
		Show s = showDB.get(searchShowId);

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(ZoneId.of(timeZone));
		System.out.println("+---------+--------------+--------------+-----------+-------------------+");
		System.out.println("| Show ID | Theatre      | Movie        | Screen    | Date & Time       |");
		System.out.println("+---------+--------------+--------------+-----------+-------------------+");

		String dateTime = formatter.format(Instant.ofEpochSecond(s.getDateTimeEpoch()));
		System.out.printf("| %-7d | %-12s | %-12s | %-9s | %-17s |\n", s.getShowId(), s.getTheatre().getTheatreName(),
				s.getMovie().getMovieTitle(), s.getScreen().getScreenNumber(), dateTime);

		System.out.println("+---------+--------------+--------------+-----------+-------------------+");

	}

	private Screen getValidScreen(Theatre theatre, long epochTime, Movies movie) {

		while (true) {

			List<Long> list = new ArrayList<>();
			for (Screen s : theatre.getListOfScreen()) {
				if (s.isActive() == true && isValidShowTime(theatre, s, epochTime, movie.getDuration())) {
					list.add((long) s.getScreenNumber());
				}
			}
			if (list == null || list.isEmpty()) {
				System.err.println("No Screen Available in This Time/date");
				return null;
			}
			System.out.println("Available Screen: ");
			System.out.println(list);
			System.out.print("Enter Screen Number (or Type 'done' to Exit):");

			String input = sc.nextLine();
			if (input.equalsIgnoreCase("done")) {
				System.out.println("------Back------");
				return null;
			}
			try {
				Long screenNumber = Long.parseLong(input);
				for (Screen s : theatre.getListOfScreen()) {
					if (s.getScreenNumber() == screenNumber && list.contains(screenNumber)) {
						return s;
					}
				}
			} catch (NumberFormatException e) {
				System.err.println("NumberFormatException");
			}
			System.err.println("Invalid input ");
		}
	}

	private Movies getValidMovie() {
		TicketService tc = new TicketService();
		while (true) {

			tc.printAllMovies();

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
				if (movieDB.containsKey(movieId)) {
					return movieDB.get(movieId);
				} else {
					System.out.println("Invalid Movie ID. Try again.");
				}
			} catch (NumberFormatException e) {
				System.out.println("Please enter a valid number.");
			}
		}
	}

	private boolean isValidShowTime(Theatre theatre, Screen screen, long newShowStart, int newMovieDuration) {
		long newShowEnd = newShowStart + (newMovieDuration * 60) + (20 * 60);

		for (Show s : theatre.getListOfShow()) {
			if (s.getScreen().equals(screen)) {
				long existingShowStart = s.getDateTimeEpoch();
				long existingShowEnd = existingShowStart + (s.getMovie().getDuration() * 60) + (20 * 60);

				if ((newShowStart >= existingShowStart && newShowStart < existingShowEnd)
						|| (newShowEnd > existingShowStart && newShowEnd <= existingShowEnd)) {
					return false;
				}
			}
		}
		return true;
	}

	private long getValidDateTime() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		while (true) {
			System.out.print("Enter Show Date & Time (yyyy-MM-dd HH:mm) (or Type 'done' to Exit): ");
			String input = sc.nextLine();
			if (input.equalsIgnoreCase("done")) {
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
					return dateTime.atZone(ZoneId.systemDefault()).toEpochSecond();
				}
			} catch (DateTimeParseException e) {
				System.err.println("Invalid date/time format. Please try again.");
			}
		}
	}

	public void printShows(TheatreAdmin theatreAdmin, String timeZone) {
		if (theatreAdmin == null || theatreAdmin.getTheatre() == null) {
			System.err.println("No Theatre Available");
			return;
		}

		List<Theatre> theatreList = theatreAdmin.getTheatre();
		Theatre theatre = selectTheatre(theatreList);

		if (theatre == null) {
			System.out.println("------Back------");
			return;
		}

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(ZoneId.of(timeZone));

		List<Show> sortedShows = showDB.values().stream()
				.filter(s -> s.getTheatre().getTheatreId() == theatre.getTheatreId())
				.sorted(Comparator.comparingLong(Show::getDateTimeEpoch)).toList();

		System.out.println(
				"+---------+-------------------+---------------------------+-----------+----------------------+------------+");
		System.out.println(
				"| Show ID | Theatre           | Movie                     | Screen    | Date & Time          | Status     |");
		System.out.println(
				"+---------+-------------------+---------------------------+-----------+----------------------+------------+");

		for (Show s : sortedShows) {
			String dateTime = formatter.format(Instant.ofEpochSecond(s.getDateTimeEpoch()));

			System.out.printf("| %-7d | %-17s | %-25s | %-9s | %-20s | %-10s |\n", s.getShowId(),
					s.getTheatre().getTheatreName(), s.getMovie().getMovieTitle(), s.getScreen().getScreenNumber(),
					dateTime, s.isActive());
		}

		System.out.println(
				"+---------+-------------------+---------------------------+-----------+----------------------+------------+");
	}

	public void printShowFuture(TheatreAdmin theatreAdmin, String timeZone) {
		if (theatreAdmin == null || theatreAdmin.getTheatre() == null) {
			System.err.println("No Theatre Available");
			return;
		}

		List<Theatre> theatreList = theatreAdmin.getTheatre();
		Theatre theatre = selectTheatre(theatreList);

		if (theatre == null) {
			System.out.println("------Back------");
			return;
		}

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(ZoneId.of(timeZone));

		List<Show> futureShows = new ArrayList<>();
		long currentEpoch = Instant.now().getEpochSecond();

		for (Map.Entry<Long, Show> entry : showDB.entrySet()) {
			Show s = entry.getValue();
			if (s.getTheatre().getTheatreId() == theatre.getTheatreId() && s.getDateTimeEpoch() > currentEpoch
					&& s.isActive().equalsIgnoreCase("upcoming")) {
				futureShows.add(s);
			}
		}

		futureShows.sort(Comparator.comparingLong(Show::getDateTimeEpoch));

		System.out.println(
				"+---------+-------------------+---------------------------+-----------+----------------------+-----------+");
		System.out.println(
				"| Show ID | Theatre           | Movie                     | Screen    | Date & Time          | Status    |");
		System.out.println(
				"+---------+-------------------+---------------------------+-----------+----------------------+-----------+");

		for (Show s : futureShows) {
			String dateTime = formatter.format(Instant.ofEpochSecond(s.getDateTimeEpoch()));
			String status = s.isActive();

			System.out.printf("| %-7d | %-17s | %-25s | %-9s | %-20s | %-9s |\n", s.getShowId(),
					s.getTheatre().getTheatreName(), s.getMovie().getMovieTitle(), s.getScreen().getScreenNumber(),
					dateTime, status);
		}

		System.out.println(
				"+---------+-------------------+---------------------------+-----------+----------------------+-----------+");
	}

	public void printShowAllFuture(String timeZone) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(ZoneId.of(timeZone));

		System.out.println(
				"+---------+-------------------+---------------------------+-----------+----------------------+");
		System.out.println(
				"| Show ID | Theatre           | Movie                     | Screen    | Date & Time          |");
		System.out.println(
				"+---------+-------------------+---------------------------+-----------+----------------------+");

		showDB.values().stream().filter(
				s -> s.getDateTimeEpoch() > Instant.now().getEpochSecond() && s.isActive().equalsIgnoreCase("upcoming"))
				.sorted(Comparator.comparingLong(Show::getDateTimeEpoch)).forEach(s -> {
					String dateTime = formatter.format(Instant.ofEpochSecond(s.getDateTimeEpoch()));
					System.out.printf("| %-7d | %-17s | %-22s | %-9s | %-20s |\n", s.getShowId(),
							s.getTheatre().getTheatreName(), s.getMovie().getMovieTitle(),
							s.getScreen().getScreenNumber(), dateTime);
				});
		System.out.println(
				"+---------+-------------------+---------------------------+-----------+----------------------+");
	}

	public void addScreen(TheatreAdmin theatreAdmin) {
		if (theatreAdmin == null || theatreAdmin.getTheatre() == null) {
			System.err.println("No Theatre Available");
			return;
		}

		List<Theatre> theatreList = theatreAdmin.getTheatre();
		Theatre theatre = selectTheatre(theatreList);
		if (theatre == null) {
			System.out.println("------Back------");
			return;
		}

		System.out.println("Enter the Number of Rows (or Type '0' to Exit): ");
		int totalRows = Input.getInteger(10000);
		if (totalRows == 0) {
			System.out.println("------Back------");
			return;
		}

//		System.out.println("Enter the Number of Columns (or Type '0' to Exit): ");
//		int totalCols = Input.getInteger(10000);
//		if (totalCols == 0) {
//			System.out.println("------Back------");
//			return;
//		}

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

		int screenId = theatre.getListOfScreen().size() + 1;

		Screen screen = new Screen(screenId, seatsPerRow, pricesPerRow);

		theatre.getListOfScreen().add(screen);

		System.out.println("\n Screen " + screenId + " created successfully!");
		System.out.println("Total Rows: " + totalRows);
		System.out.println("Seat Layout:");
		screen.displayLayout();

	}

	public void addNewTheatre(TheatreAdmin theatreAdmin) {
		long theatreId = theatreDB.size() + 1;
		String theatreName, theatreLocation;
		System.out.print("Enter Theatre Name (or Type 'done' to Exit):");
		theatreName = sc.nextLine().trim();
		if (theatreName.equalsIgnoreCase("done")) {
			System.out.println("------Back------");
			return;
		}
		while (theatreName.isEmpty()) {
			System.out.print("Theatre name cannot be empty. Enter again (or Type 'done' to Exit): ");
			theatreName = sc.nextLine().trim();
			if (theatreName.equalsIgnoreCase("done")) {
				System.out.println("------Back------");
				return;
			}
		}

		System.out.print("Enter Theatre Location (or Type 'done' to Exit):");
		theatreLocation = sc.nextLine().trim();
		if (theatreLocation.equalsIgnoreCase("done")) {
			System.out.println("------Back------");
			return;
		}

		while (theatreLocation.isEmpty()) {
			System.out.print("Theatre location cannot be empty. Enter again (or Type 'done' to Exit): ");
			theatreLocation = sc.nextLine().trim();
			if (theatreLocation.equalsIgnoreCase("done")) {
				System.out.println("------Back------");
				return;
			}
		}

		ArrayList<Screen> screen = new ArrayList<>();
		Theatre newTheatre = new Theatre(theatreId, theatreName, theatreLocation, screen, new ArrayList<>(),
				new ArrayList<>(), true);

		int count = 1;
		while (true) {
			System.out.print("1 . Add Screen \n2 . Exit");
			int currChoice = Input.getInteger(2);
			if (currChoice == 1) {
				screen.add(addScreen1(theatreAdmin, count, newTheatre));
				count++;
			} else {
				break;
			}
		}
		newTheatre.setListOfScreen(screen);
		long requestId = RequestTheatreDB.size() + 1;
		RequestTheatreDB.put(requestId, new RequestTheatre(requestId, newTheatre, theatreAdmin, false));

		System.out.println("New Theatre Requested to Admin and Request Id : " + requestId);

	}

	public Screen addScreen1(TheatreAdmin theatreAdmin, int ScreenNumber, Theatre theatre) {

		System.out.println("Enter the Number of Rows (or Type '0' to Exit):");
		int totalRows = Input.getInteger(10000);
		if (totalRows == 0) {
			System.out.println("------Back------");
			return null;
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

		int screenId = theatre.getListOfScreen().size() + 1;

		Screen screen = new Screen(screenId, seatsPerRow, pricesPerRow);

		theatre.getListOfScreen().add(screen);

		System.out.println("\n Screen " + screenId + " created successfully!");
		System.out.println("Total Rows: " + totalRows);
		System.out.println("Seat Layout:");
		screen.displayLayout();
		return screen;

	}

	public Theatre selectTheatre(List<Theatre> theatreList) {
		if (theatreList == null || theatreList.isEmpty()) {
			System.out.println("No theatres available.");
			return null;
		}

		System.out.println("-----------------------------------------------------------");
		System.out.printf("| %-8s | %-20s | %-20s |%n", "ID", "Name", "Location");
		System.out.println("-----------------------------------------------------------");
		int count = 1;
		for (Theatre theatre : theatreList) {
			if (theatre.isActive() == true) {
				System.out.printf("| %-8d | %-20s | %-20s |%n", count, theatre.getTheatreName(),
						theatre.getTheatreLocation());
				count++;
			}

		}
		System.out.println("-----------------------------------------------------------");

		System.out.print("Enter Theatre ID to select (or Type '0' to Exit): ");
		Long theatreId = Input.getLong((long) count - 1);
		if (theatreId == 0) {
			System.out.println("------Back------");
			return null;
		}
		int countTheatre = 1;
		for (Theatre theatre : theatreList) {
			if (countTheatre == theatreId) {
				return theatre;
			}
			countTheatre++;
		}

		System.out.println("Invalid Theatre ID. Please try again.");
		return null;
	}

	public void AddTheatreToTheatreAdmin() {
		adminService.viewAllTheatreAdmins();
		System.out.println("Enter the Theatre Admin id (or Type '0' to Exit): ");
		long theatreAdminId = Input.getLong((long) theatreAdminDB.size());
		if (theatreAdminId == 0) {
			System.out.println("------Back------");
			return;
		}
		TheatreAdmin theatreAdmin = theatreAdminDB.get(theatreAdminId);

		theatreServiceObj.printAllTheatres();
		System.out.println("Enter the Theatre id (or Type '0' to Exit): ");
		long theatreId = Input.getLong((long) theatreDB.size());
		if (theatreId == 0) {
			System.out.println("------Back------");
			return;
		}
		Theatre theatre = theatreDB.get(theatreId);
		if (theatre.isActive() == false) {
			System.err.println("This Thatre Deleted , you can't assign permission");
		}
		if (theatreAdmin.getTheatre().contains(theatre)) {
			System.out.println("Thetare already present");
		} else {
			theatreAdmin.addTheatre(theatre);
			System.out.println("Theatre Added To Theatre Admin ");
		}

	}

	public void approveNewTheatre(Admin admin) {

		if (RequestTheatreDB.size() == 0) {
			System.err.println("No Request Raised");
			return;
		}
		String format = "| %-10s | %-25s | %-25s | %-15s | %-10s | %-15s |%n";

		System.out.println(
				"+------------+---------------------------+---------------------------+-----------------+------------+-----------------+");
		System.out.format(format, "Request ID", "Theatre Name", "Location", "Admin ID", "Approved", "Approved By");
		System.out.println(
				"+------------+---------------------------+---------------------------+-----------------+------------+-----------------+");

		for (RequestTheatre req : RequestTheatreDB.values()) {
			String approvedBy = (req.isApproved() && req.getApprovedAdmin() != null)
					? String.valueOf(req.getApprovedAdmin().getAdminId())
					: "-";

			System.out.format(format, req.getRequestId(), req.getTheatre().getTheatreName(),
					req.getTheatre().getTheatreLocation(), req.getTheatreAdmin().getTheatreAdminId(),
					req.isApproved() ? "Yes" : "No", approvedBy);
		}

		System.out.println(
				"+------------+---------------------------+---------------------------+-----------------+------------+-----------------+");

		while (true) {
			System.out.println("Enter the Request id (or Type '0' to Exit): ");
			long requestId = Input.getLong((long) 100000);
			if (requestId == 0) {
				System.out.println("------Back------");
				return;
			}
			if (RequestTheatreDB.containsKey(requestId)) {

				if (RequestTheatreDB.get(requestId).isApproved() == false) {
					System.out.println("Approve Request id : " + requestId
							+ " if yes type '1' or else press any Number to Exit):");
					if (Input.getInteger(100000) == 1) {
						RequestTheatreDB.get(requestId).setApproved(true);
						theatreDB.put(RequestTheatreDB.get(requestId).getTheatre().getTheatreId(),
								RequestTheatreDB.get(requestId).getTheatre());
						Theatre theatre = RequestTheatreDB.get(requestId).getTheatre();

						RequestTheatreDB.get(requestId).getTheatreAdmin().addTheatre(theatre);
						RequestTheatreDB.get(requestId).setApprovedAdmin(admin);
						System.out.println("Theatre Approved");
					} else {
						System.out.println("------Rejected------");
					}
					return;
				} else {
					System.err.println("Already Approved");
					return;
				}

			} else {
				System.err.println("Invalid Reques Id ");
			}
		}

	}

	public void cancelShow(TheatreAdmin theatreAdmin) {
		Theatre theatre = selectTheatre(theatreAdmin.getTheatre());
		HashMap<Long, Show> listOfShow = printShowFuturewithReturn(theatreAdmin, theatreAdmin.getTimeZone(), theatre);
		if (listOfShow == null) {
			System.err.println("No Shows Available");
			return;
		}
		if (theatre == null) {
			return;
		}
		Movies movie = null;
		while (true) {
			System.out.println("Enter the Show id (or type '0' to Exit): ");
			long showId = Input.getLong(Long.MAX_VALUE);
			if (showId == 0) {
				return;
			}
			boolean flag = false;

			if (listOfShow.get(showId) != null) {
				flag = true;
				if (showDB.get(showId).isActive().equalsIgnoreCase("upcoming")) {
					for (Ticket ticket : ticketDB.values()) {
						if (ticket.getShow().getShowId() == showId) {
							cancelTicket(ticket.getUser(), ticket);
						}
					}
					showDB.get(showId).setActive("Completed");
					movie = showDB.get(showId).getMovie();
					System.out.println("Show Cancelled");
					boolean moviePresent = false;
					for (Show show : theatre.getListOfShow()) {
						if (show.getMovie() == movie && show.isActive().equalsIgnoreCase("upcoming")) {
							System.out.println(show.getShowId());
							moviePresent = true;
							break;
						}
					}
					if (moviePresent == false) {
						theatre.removeMovieFromList(movie);
					}

				} else {
					System.err.println("Show Already Cancelled");
				}

			}
			if (flag == false)
				System.err.println("Invalid Input");
			else {
				break;
			}

		}
	}

	public void cancelTicket(User currentUser, Ticket ticket) {

		if (ticket == null || !ticket.getUser().equals(currentUser)) {
			System.err.println("No ticket found with this ID for your account.");
			return;
		}

		if (ticket.getStatus().equalsIgnoreCase("cancelled")) {
			System.err.println("Ticket is already cancelled.");
			return;
		}

		ticket.setStatus("Refunded");
		for (Seat seat : ticket.getSeats()) {
			seat.setBooked(false);
		}

		double totalAmount = ticket.getTotalAmount();
		double userAmount = currentUser.getBalance();
		userAmount += totalAmount;
		currentUser.setBalance(userAmount);
		System.out.println("Ticket ID " + ticket.getTicketId() + " has been successfully cancelled.");
	}

	public HashMap<Long, Show> printShowFuturewithReturn(TheatreAdmin theatreAdmin, String timeZone, Theatre theatre) {
		if (theatreAdmin == null || theatreAdmin.getTheatre() == null) {
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

		for (Map.Entry<Long, Show> entry : showDB.entrySet()) {
			Show s = entry.getValue();
			if (s.getTheatre().getTheatreId() == theatre.getTheatreId()
					&& s.getDateTimeEpoch() > Instant.now().getEpochSecond()
					&& s.isActive().equalsIgnoreCase("upcoming")) {
				String dateTime = formatter.format(Instant.ofEpochSecond(s.getDateTimeEpoch()));
				System.out.printf("| %-7d | %-17s | %-22s | %-9s | %-20s |\n", s.getShowId(),
						s.getTheatre().getTheatreName(), s.getMovie().getMovieTitle(), s.getScreen().getScreenNumber(),
						dateTime);
				listOfShow.put(s.getShowId(), s);
			}
		}
		System.out.println(
				"+---------+-------------------+---------------------------+-----------+----------------------+");
		return listOfShow;
	}

	public void deleteScreen(TheatreAdmin theatreAdmin) {

		Theatre theatre = selectTheatre(theatreAdmin.getTheatre());
		if (theatre == null)
			return;
		Screen screen = selectScreen(theatre);
		if (screen == null)
			return;

		for (Theatre the : theatreDB.values()) {
			if (the.getListOfScreen().contains(screen)) {
				for (Screen s : the.getListOfScreen()) {
					if (s == screen) {
						s.setActive(false);
					}
				}
			}
		}
		screen.setActive(false);
		System.out.println("Screen Deleted");
	}

	private Screen selectScreen(Theatre theatre) {
		List<Screen> listOfScreen = theatre.getListOfScreen();
		if (listOfScreen == null && listOfScreen.isEmpty()) {
			System.err.println("No Screen available");
			return null;
		}
		List<Integer> list = new ArrayList<>();
		for (Screen s : listOfScreen) {
			if (s.isActive() == true) {
				list.add(s.getScreenNumber());
			}

		}
		while (true) {
			boolean flag = false;
			System.out.println("Available Screen : " + list);
			System.out.println("Enter Screen Number (or type '0' to Exit): ");
//			int size = listOfScreen.size();
			int screenNumber = Input.getInteger(30);
			if (screenNumber == 0) {
				return null;
			}
			for (Screen s : listOfScreen) {
				if (s.getScreenNumber() == screenNumber) {
					flag = true;
					return s;
				}
			}
			if (flag == false) {
				System.err.println("Invalid Screen Number");
			}
		}
	}

	public void checkRequestStatus(TheatreAdmin theatreAdmin) {
		String format = "| %-10s | %-25s | %-25s | %-10s |%n";

		System.out.println("+------------+---------------------------+---------------------------+------------+");
		System.out.format(format, "Request ID", "Theatre Name", "Location", "Approved");
		System.out.println("+------------+---------------------------+---------------------------+------------+");

		for (RequestTheatre req : RequestTheatreDB.values()) {
			if (req.getTheatreAdmin() == theatreAdmin)
				System.out.format(format, req.getRequestId(), req.getTheatre().getTheatreName(),
						req.getTheatre().getTheatreLocation(), req.isApproved() ? "Yes" : "No");
		}

		System.out.println("+------------+---------------------------+---------------------------+------------+");

	}

	public void deleteTheatre(TheatreAdmin theatreAdmin) {
		Theatre theatre = selectTheatre(theatreAdmin.getTheatre());
		if (theatre == null) {
			System.out.println("No Thetare Available");
			return;
		}
		theatre.setActive(false);
		theatreAdmin.removeTheatre(theatre);
		System.out.println("Theatre Deleted ");

	}

	public void displayAllTicket(String timeZone) {
		// TODO Auto-generated method stub
		TicketService tc = new TicketService();
		tc.changeTicketStatus();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");
		System.out.printf(
				"+----+----------------------+----------------------+--------+----------------------+-----------------------+----------------------+%n");
		System.out.printf(
				"| ID | Movie                | Theatre              | Screen | Show Date & Time     |  Status               | Seats                |%n");
		System.out.printf(
				"+----+----------------------+----------------------+--------+----------------------+-----------------------+----------------------+%n");

		for (Ticket ticket : ticketDB.values()) {

			List<String> seatNumber = ticket.getSeats().stream().map(s -> s.getSeatNumber())
					.collect(Collectors.toList());
			ZonedDateTime zdt = Instant.ofEpochSecond(ticket.getShow().getDateTimeEpoch()).atZone(ZoneId.of(timeZone));

			String active = ticket.getStatus();

			System.out.printf("| %-2d | %-20s | %-20s | %-6s | %-20s | %-20s | %-20s |%n", ticket.getTicketId(),
					ticket.getMovie().getMovieTitle(), ticket.getTheatre().getTheatreName(),
					ticket.getShow().getScreen().getScreenNumber(), zdt.format(formatter), active, seatNumber);
		}
		System.out.printf(
				"+----+----------------------+----------------------+--------+----------------------+-----------------------+----------------------+%n");
	}

	public void printRunningShow(TheatreAdmin theatreAdmin, String timeZone) {
		if (theatreAdmin == null || theatreAdmin.getTheatre() == null) {
			System.err.println("No Theatre Available");
			return;
		}

		List<Theatre> theatreList = theatreAdmin.getTheatre();
		Theatre theatre = selectTheatre(theatreList);

		if (theatre == null) {
			System.out.println("------Back------");
			return;
		}

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(ZoneId.of(timeZone));

		List<Show> futureShows = new ArrayList<>();
		long currentEpoch = Instant.now().getEpochSecond();

		for (Map.Entry<Long, Show> entry : showDB.entrySet()) {
			Show s = entry.getValue();
			if (s.getTheatre().getTheatreId() == theatre.getTheatreId() && s.isActive().equalsIgnoreCase("Running")) {
				futureShows.add(s);
			}
		}
		if (futureShows.isEmpty() || futureShows == null) {
			System.err.println("No Running Show");
			return;
		}

		futureShows.sort(Comparator.comparingLong(Show::getDateTimeEpoch));

		System.out.println(
				"+---------+-------------------+---------------------------+-----------+----------------------+-----------+");
		System.out.println(
				"| Show ID | Theatre           | Movie                     | Screen    | Date & Time          | Status    |");
		System.out.println(
				"+---------+-------------------+---------------------------+-----------+----------------------+-----------+");

		for (Show s : futureShows) {
			String dateTime = formatter.format(Instant.ofEpochSecond(s.getDateTimeEpoch()));
			String status = s.isActive(); // Upcoming / Completed / Running

			System.out.printf("| %-7d | %-17s | %-25s | %-9s | %-20s | %-9s |\n", s.getShowId(),
					s.getTheatre().getTheatreName(), s.getMovie().getMovieTitle(), s.getScreen().getScreenNumber(),
					dateTime, status);
		}

		System.out.println(
				"+---------+-------------------+---------------------------+-----------+----------------------+-----------+");
	}

	public void displayAllRunningShow(String timeZone) {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(ZoneId.of(timeZone));

		System.out.println(
				"+---------+--------------------------+------------------------+-----------+-------------------+-----------+");
		System.out.println(
				"| Show ID | Theatre                  | Movie                  | Screen    | Date & Time       | Status    |");
		System.out.println(
				"+---------+--------------------------+------------------------+-----------+-------------------+-----------+");

		for (Map.Entry<Long, Show> entry : showDB.entrySet()) {
			Show s = entry.getValue();
			if (s.isActive().equalsIgnoreCase("Running")) {
				String dateTime = formatter.format(Instant.ofEpochSecond(s.getDateTimeEpoch()));

				System.out.printf("| %-7d | %-20s | %-26s | %-9s | %-17s | %-9s |\n", s.getShowId(),
						s.getTheatre().getTheatreName(), s.getMovie().getMovieTitle(), s.getScreen().getScreenNumber(),
						dateTime, s.isActive());
			}

		}

		System.out.println(
				"+---------+--------------------------+------------------------+-----------+-------------------+-----------+");

	}

	public void viewSeatAllocationForPastShows(TheatreAdmin theatreAdmin) {
		Theatre theatre = selectTheatre(theatreAdmin.getTheatre());
		if (theatre == null)
			return;
		Show show = displayPastShow(theatre, theatreAdmin.getTimeZone());
		if (show == null) {
			return;
		}
		ticketServiceObj.displaySeatsByCategory(show);

	}

	private Show displayPastShow(Theatre theatre, String timeZone) {
		HashMap<Long, Show> pastShow = new HashMap<>();
		for (Show show : showDB.values()) {
			if (show.getTheatre() == theatre && show.isActive().equalsIgnoreCase("completed")) {
				pastShow.put(show.getShowId(), show);
			}
		}
		if(pastShow.isEmpty()) {
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

			String dateTime = formatter.format(Instant.ofEpochSecond(s.getDateTimeEpoch()));

			System.out.printf("| %-7d | %-20s | %-26s | %-9s | %-17s | %-9s |\n", s.getShowId(),
					s.getTheatre().getTheatreName(), s.getMovie().getMovieTitle(), s.getScreen().getScreenNumber(),
					dateTime, s.isActive());
		}

		System.out.println(
				"+---------+--------------------------+------------------------+-----------+-------------------+-----------+");

		System.out.println("Enter Show Id (or type '0' to Exit):");
		long showId = Input.getLong((long) showDB.size());
		if (showId == 0) {
			return null;
		}
		Show show = pastShow.get(showId);
		if (show == null) {
			return null;
		}
		return show;

	}

}
