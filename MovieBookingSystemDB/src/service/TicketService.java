package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import dao.MovieDAO;
import dao.ScreenDAO;
import dao.TheatreDAO;
import model.BookingSeat;
import model.Movies;
import model.Screen;
import model.Show;
import model.Theatre;
import model.TheatreAdmin;
import model.Ticket;
import model.User;
import util.DBHelper;
import util.Helper;
import util.Input;

public class TicketService {
	TheatreService theatreServiceObj1 = new TheatreService();
	PrinterService printerServiceObj = new PrinterService();

	// ------------------------------------------------------------------------------------------------------------------------------------------------------------------

	public void bookTicket(User user) {
		Connection conn = Input.getConnection();
		try {
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Movies movieObj = selectMovie(conn);
		if (movieObj == null)
			return;
		Theatre theatreObj = printerServiceObj.selectTheatreForMovie(conn, movieObj);
		if (theatreObj == null)
			return;
		bookTicketViaTheatre(conn, movieObj, theatreObj, user);
	}

	// ------------------------------------------------------------------------------------------------------------------------------------------------------------------

	private void updateBookedTicket(Connection conn, long ticketId, List<BookingSeat> selectSeat) {
		String sql = "INSERT INTO bookedTicket (ticketId, bookingSeatId) VALUES (?, ?)";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			for (BookingSeat seat : selectSeat) {
				ps.setLong(1, ticketId);
				ps.setLong(2, seat.getBookingSeatId());
				ps.addBatch();
			}
			ps.executeBatch();
			System.out.println("Booked tickets inserted successfully.");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void checkTicketStatus(User user) {
		Connection conn = Input.getConnection();
		Scanner sc = new Scanner(System.in);
		while (true) {
			System.out.print("Enter Ticket ID to check status (or type '0' to Exit): ");
			Long ticketId = null;
			try {
				ticketId = Long.parseLong(sc.nextLine());
				if (ticketId == 0)
					return;
			} catch (NumberFormatException e) {
				System.err.println("Invalid input! Please enter a valid ticket ID.");
				continue;
			}
			Ticket ticket = Helper.getTicketById(conn, ticketId);
			if (ticket == null) {
				System.err.println("Invalid Ticket ID! Please try again.");
				continue;
			}
			if (!ticket.getUser().getId().equals(user.getId())) {
				System.err.println("You don't have permission to view this ticket.");
				continue;
			}
			printerServiceObj.printTicket(conn, ticket, user);
			break;
		}
	}

	private long createTicket(Connection conn, Ticket ticket, long time) {
		String sql = "INSERT INTO ticket (userId, showId, amount, ticketBookedDate,status) VALUES (?, ?, ?, ?,?)";
		long generatedId = -1;
		try {

			PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.setLong(1, ticket.getUser().getId());
			ps.setLong(2, ticket.getShow().getShowId());
			ps.setDouble(3, ticket.getAmount());
			ps.setLong(4, time);
			ps.setString(5, "active");
			int rowsAffected = ps.executeUpdate();

			if (rowsAffected > 0) {
				try (ResultSet rs = ps.getGeneratedKeys()) {
					if (rs.next()) {
						generatedId = rs.getLong(1);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return generatedId;
	}

	public void markSeatBooked(Connection conn, List<BookingSeat> selectSeat) {
		List<Long> bookingSeatIds = selectSeat.stream().map(BookingSeat::getBookingSeatId).collect(Collectors.toList());

		if (bookingSeatIds.isEmpty()) {
			System.out.println("No seats selected to mark as booked.");
			return;
		}
		String sql = "UPDATE bookingSeat SET isBooked = ? WHERE bookingSeatId = ?";
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);

			for (Long id : bookingSeatIds) {
				pstmt.setBoolean(1, true);
				pstmt.setLong(2, id);
				pstmt.addBatch();
			}

			int[] updated = pstmt.executeBatch();
			System.out.println(updated.length + " seat(s) marked as booked successfully.");

		} catch (SQLException e) {
			System.out.println("Error while marking seats as booked: " + e.getMessage());
		}

	}

	public void setUserBalance(Connection conn, long userId, double newBalance) {
		String query = "UPDATE users SET balance = ? WHERE id = ?";
		DBHelper.executeUpdate(conn, query, newBalance, userId);
		System.out.println("Balance updated successfully for User ID: " + userId);
	}

	public double calculateAmount(List<BookingSeat> selectedSeats, Show show) {
		double total = 0;
		for (BookingSeat bs : selectedSeats) {
			total += bs.getSeat().getPrice();
		}
		return total;
	}

	public double getUserBalanceFromDB(Connection conn, Long userId) {
		double balance = 0.0;
		String query = "SELECT balance FROM users WHERE id = ?";
		try {
			ResultSet rs = DBHelper.executeQuery(conn, query, userId);
			if (rs.next()) {
				balance = rs.getDouble("balance");
			}

		} catch (SQLException e) {
			System.err.println("Error fetching user balance: " + e.getMessage());
		}
		return balance;
	}

	public List<BookingSeat> displaySeatsByCategory(Connection conn, Show show) {

		List<BookingSeat> bookingSeatList = show.getBookingSeat(conn, show);
		show.displayLayout(bookingSeatList);
		return bookingSeatList;
	}

	public Movies selectMovie(Connection conn) {
		while (true) {
			List<Movies> movieList = printerServiceObj.printAllMovies(conn);
			movieList = movieList.stream().sorted(Comparator.comparingLong(Movies::getMovieId))
					.collect(Collectors.toList());

			System.out.print("Please enter the Movie ID you wish to book (or Type '0' to Exit): ");
			int movieId = Input.getInteger(1000000000);
			if (movieId == 0) {
				System.out.println("------Back------");
				return null;
			}

			for (Movies movie : movieList) {
				if (movie.getMovieId() == movieId) {
					return movie;
				}
			}
			System.out.println("Invalid Movie ID.");
		}

	}

	public List<Theatre> getTheatresByMovie(Connection conn, Movies movie) {
		List<Theatre> availableTheatres = new ArrayList<>();
		String query = "SELECT DISTINCT t.theatreId, t.theatreName, t.theatreLocation, t.status " + "FROM theatre t "
				+ "INNER JOIN shows s ON t.theatreId = s.theatreId "
				+ "WHERE s.movieId = ? AND LOWER(s.status) = 'active'";

		try {
			ResultSet rs = DBHelper.executeQuery(conn, query, movie.getMovieId());
			while (rs.next()) {
				Theatre theatre = new Theatre(rs.getLong("theatreId"), rs.getString("theatreName"),
						rs.getString("theatreLocation"), rs.getString("status"));
				availableTheatres.add(theatre);
			}

		} catch (SQLException e) {
			System.out.println("Error while fetching theatres: " + e.getMessage());
			e.printStackTrace();
		}
		return availableTheatres;
	}

	public List<Show> getShowsForMovieAndTheatre(Connection conn, Movies movie, Theatre theatre) {
		List<Show> shows = new ArrayList<>();

		String query = "SELECT s.showId, s.showDateTime, s.status AS showStatus, "
				+ "sc.screenId, sc.screenName, sc.status AS screenStatus " + "FROM shows s "
				+ "JOIN screen sc ON s.screenId = sc.screenId "
				+ "WHERE s.movieId = ? AND s.theatreId = ? AND LOWER(s.status) = 'active'";

		try {
			ResultSet rs = DBHelper.executeQuery(conn, query, movie.getMovieId(), theatre.getTheatreId());
			while (rs.next()) {
				Screen screenObj = new Screen(rs.getLong("screenId"), rs.getString("screenName"), theatre,
						rs.getString("screenStatus"));
				Show show = new Show(rs.getLong("showId"), movie, screenObj, theatre, rs.getLong("showDateTime"),
						rs.getString("showStatus"));
				shows.add(show);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return shows;
	}

	public List<BookingSeat> selectSeat(Show show, List<BookingSeat> bookingSeatList) {
		Scanner sc = new Scanner(System.in);
		List<BookingSeat> bookedSeats = new ArrayList<>();

		while (true) {
			System.out.print("Enter seat numbers (comma-separated, e.g. A1,A2,B3) or press 0 to cancel: ");
			String input = sc.nextLine().trim();

			if (input.equals("0")) {
				return bookedSeats;
			}

			String[] seatNumbers = input.split(",");

			for (String seatNumber : seatNumbers) {
				seatNumber = seatNumber.trim();
				boolean seatFound = false;

				for (BookingSeat bookingSeat : bookingSeatList) {
					if (bookingSeat.getSeat().getSeatNumber().equalsIgnoreCase(seatNumber)) {
						seatFound = true;

						if (bookingSeat.isBooked()) {
							System.err.println("🔴Seat " + seatNumber + " is already booked!");
						} else {
							bookingSeat.setBooked(true);
							bookedSeats.add(bookingSeat);
							System.out.println("✅ Seat " + seatNumber + " selected successfully!");
						}

						break;
					}
				}
				if (!seatFound) {
					System.err.println("Invalid Seat Number: " + seatNumber);
				}
			}
		}
	}

	public void userDisplaySeatAvailability(User user) {
		Connection conn = Input.getConnection();
		Movies movieObj = selectMovie(conn);
		if (movieObj == null) {
			return;
		}
		Theatre theatreObj = printerServiceObj.selectTheatreForMovie(conn, movieObj);
		if (theatreObj == null) {
			return;
		}
		List<Show> shows = getShowsForMovieAndTheatre(conn, movieObj, theatreObj);
		for (Show s : shows) {
			System.out.println(s.getMovie().getMovieTitle());
		}

		Show show = printerServiceObj.getShow(shows, "Asia/Kolkata");
		if (show == null) {
			System.err.println("Booking cancelled. No show selected.");
			return;
		}
		displaySeatsByCategory(conn, show);
		try {
			conn.close();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}

	public void viewMyBooking(User currentUser) {
		Connection conn = Input.getConnection();
		List<Ticket> userTickets = Helper.getAllTicket(conn, currentUser);
		if (userTickets.isEmpty()) {
			System.err.println("No booking available.");
			return;
		}
		printerServiceObj.printAllTicket(conn, userTickets, currentUser);
		try {
			conn.close();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}

	public void BookTicketViaTheatre(User user) {
		Connection conn = Input.getConnection();
		try {
			conn.setAutoCommit(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<Theatre> theatreList = TheatreDAO.getTheatreList(conn);
		theatreServiceObj1.printAllTheatres(conn);
		Theatre theatre = null;
		while (true) {
			System.out.println("Enter Theatre Id : ");
			Long theatreId = Input.getLong(theatreList.get(theatreList.size() - 1).getTheatreId());
			for (Theatre t : theatreList) {
				if (t.getTheatreId() == theatreId) {
					theatre = t;
					break;
				}
			}
			if (theatre != null) {
				break;
			} else {
				System.err.println("Invalid Theatre Id");
			}
		}

		List<Movies> listOfMovies = MovieDAO.getActiveMoviesByTheatreId(conn, theatre.getTheatreId());
		if (listOfMovies.size() == 0) {
			System.err.println("No Movies Available In THis Theatre");
			return;
		}
		Movies movie = null;
		String format = "| %-4s | %-27s | %-12s | %-12s | %-12s | %-6s |%n";

		System.out.println(
				"+------+-----------------------------+--------------+--------------+--------------+--------+");
		System.out.format(format, "ID", "Title", "Duration", "Genre", "Language", "Year");
		System.out.println(
				"+------+-----------------------------+--------------+--------------+--------------+--------+");

		for (Movies m : listOfMovies) {

			int hours = m.getDuration() / 60;
			int minutes = m.getDuration() % 60;
			String durationFormatted = String.format("%d hr %02d min", hours, minutes);

			System.out.format(format, m.getMovieId(), m.getMovieTitle(), durationFormatted, m.getGenre(),
					m.getLanguage(), m.getReleaseYear());

		}

		System.out.println(
				"+------+-----------------------------+--------------+--------------+--------------+--------+");

		while (true) {
			System.out.println("Enter Movie Id : ");
			Long movieId = Input.getLong((long) 100000000);
			if (movieId == 0) {
				return;
			}
			for (Movies t : listOfMovies) {
				if (t.getMovieId() == movieId) {
					movie = t;
					break;
				}
			}
			if (movie != null) {
				break;
			} else {
				System.err.println("Invalid Movie Id");
			}
		}
		bookTicketViaTheatre(conn, movie, theatre, user);
		try {
			conn.close();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}

	public void bookTicketViaTheatre(Connection conn, Movies movie, Theatre theatre, User user) {

		List<Show> shows = getShowsForMovieAndTheatre(conn, movie, theatre);
		for (Show s : shows) {
			System.out.println(s.getMovie().getMovieTitle());
		}
		Show show = printerServiceObj.getShow(shows, user.getTimeZone());
		if (show == null) {
			System.err.println("Booking cancelled. No show selected.");
			return;
		}
		List<BookingSeat> bookingSeatList = displaySeatsByCategory(conn, show);
		List<BookingSeat> selectSeat = selectSeat(show, bookingSeatList);

		System.out.print("Selected Seat are { ");
		Collections.sort(selectSeat, Comparator.comparing(b -> b.getSeat().getSeatNumber()));
		for (BookingSeat se : selectSeat) {
			System.out.print(se.getSeat().getSeatNumber() + " ");
		}
		System.out.println("}");
		if (selectSeat == null || selectSeat.isEmpty()) {
			System.out.println("------Back------");
			return;
		}

		double amount = calculateAmount(selectSeat, show);
		double currBalance = getUserBalanceFromDB(conn, user.getId());

		if (currBalance < amount) {
			System.err.println("Insufficient balance! Required: " + amount + ", Available: " + currBalance);
			return;
		}

		System.out.println("Current Balance : " + currBalance);
		System.out.println("Ticket price    : " + amount);
		System.out.println(
				"Are you sure you want to proceed with booking your ticket? if Type '1' to continue and '0' to Exit");
		if (Input.getInteger(1) == 0) {
			System.out.println("------Back------");
			return;
		}

		currBalance -= amount;
		setUserBalance(conn, user.getId(), currBalance);
		System.out.println("💰New Available Balance is " + currBalance);
		markSeatBooked(conn, selectSeat);

		Ticket ticket = new Ticket();
		ticket.setUser(user);
		ticket.setShow(show);
		ticket.setAmount(amount);
		ticket.setStatus("active");
		long time = System.currentTimeMillis();
		ticket.setTicketBookedDate(time);
		long ticketid = createTicket(conn, ticket, time);
		ticket.setTicketId(ticketid);
		System.out.println("Your Ticket Successfully Booked , Ticket id : " + ticketid);
		updateBookedTicket(conn, ticketid, selectSeat);

		printerServiceObj.printTicket(conn, ticket, user);
		try {
			conn.setAutoCommit(true);
		} catch (Exception e) {
			System.err.println("Commit fail");
			e.printStackTrace();
		}
		System.out.println("Ticket Booking Finished");
	}

	public void cancelTicket(User currentUser) {
		System.out.println("----------- Ticket Cancellation Policy -----------");
		System.out.println("1. Cancel 24 hours before the show → 75% refund");
		System.out.println("2. Cancel between 2 hours and 24 hours before → 50% refund");
		System.out.println("3. Cancel between 20 minutes and 2 hours before → 25% refund");
		System.out.println("4. Less than 20 minutes before the show → Cancellation not allowed");
		System.out.println("--------------------------------------------------");
		Connection conn = Input.getConnection();
		try {
			conn.setAutoCommit(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<Ticket> userTickets = Helper.getAllTicket(conn, currentUser);
		if (userTickets.isEmpty()) {
			System.err.println("No booking available.");
			return;
		}

		List<Ticket> upcomingTicket = displayUpcomingTicket(conn, currentUser, userTickets);
		if (upcomingTicket == null) {
			System.err.println("No upcoming Tickets");
			return;
		}
		System.out.print("Enter Ticket ID to cancel (or Type '0' to Exit): ");
		long size = (long) upcomingTicket.size();
		if (size == 0) {
			System.err.println("No Tickets Available for cancel");
			return;
		}
		long ticketId;
		while (true) {
			ticketId = Input.getLong(upcomingTicket.getLast().getTicketId());
			if (ticketId == 0) {
				System.out.println("------Back------");
				return;
			}
			boolean found = false;
			for (Ticket t : upcomingTicket) {
				if (t.getTicketId() == ticketId) {
					found = true;
				}
			}
			if (found == true)
				break;
		}
		Ticket ticket = null;
		for (Ticket t : upcomingTicket) {
			if (t.getTicketId() == ticketId) {
				ticket = t;
				break;
			}
		}

		if (ticket == null) {
			System.err.println("No ticket found with this ID for your account.");
			return;
		}
		long currentTime = System.currentTimeMillis();
		long showTimeMillis = ticket.getShow().getShowDateTime();

		long diff = showTimeMillis - currentTime;
		double reducePercentage = 0.0;
		if (diff <= 0) {
			System.err.println("Show time has already started or expired. Cancellation not allowed.");
			return;
		}
		if (diff > 24 * 60 * 60 * 1000L) {
			System.out.println("Cancellation: More than 24 hours before the show → 25% deduction.");
			reducePercentage = 0.25;
		} else if (diff > 2 * 60 * 60 * 1000L) {
			System.out.println("Cancellation: Between 24 hours and 2 hours before the show → 50% deduction.");
			reducePercentage = 0.50;
		} else if (diff > 20 * 60 * 1000L) {
			System.out.println("Cancellation: Between 2 hours and 20 minutes before the show → 75% deduction.");
			reducePercentage = 0.75;
		} else {
			System.out.println("Cancellation: Less than 20 minutes before the show → No refund.");
			reducePercentage = 1.0;
		}
		if (ticket.getStatus().equalsIgnoreCase("cancelled")) {
			System.err.println("Ticket is already cancelled.");
			return;
		}

		if (ticket.getStatus().equalsIgnoreCase("refunded")) {
			System.err.println("Show cancelled, so ticket is already refunded.");
			return;
		}
		double refundAmount = ticket.getAmount() * (1 - reducePercentage);
		System.out.println("Refund amount: ₹" + refundAmount);

		double userAmount = getUserBalanceFromDB(conn, currentUser.getId());
		userAmount += refundAmount;
		currentUser.newBalance(conn, userAmount); // balance update
		Helper.updateTicketStatus(conn, (long) ticket.getTicketId(), "cancelled");
		releaseBookedSeats(conn, (long) ticket.getTicketId());
		System.out.println(
				reducePercentage * 100 + ("% deducted from your ticket price. Remaining Amount credited to user"));
		System.out.println("Ticket ID " + ticketId + " has been successfully cancelled.");
		try {
			conn.setAutoCommit(true);
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private List<Ticket> displayUpcomingTicket(Connection conn, User currentUser, List<Ticket> userTickets) {
		boolean found = false;

		String line = "+-------+----------------------+-----------------+---------------+----------------------+------------+------------+";
		String headerFormat = "| %-5s | %-20s | %-15s | %-13s | %-20s | %-10s | %-10s |\n";
		String rowFormat = "| %-5d | %-20s | %-15s | %-13s | %-20s | %-10s | %-10.2f |\n";

		System.out.println("\n" + line);
		System.out.printf(headerFormat, "ID", "Movie", "Theatre", "City", "Show Date & Time", "Seats", "Price");
		System.out.println(line);

		List<Ticket> upcomingTicket = new ArrayList<>();
		long currentTimeMillis = System.currentTimeMillis();

		for (Ticket ticket : userTickets) {
			if (ticket.getShow().getShowDateTime() > currentTimeMillis
					&& "active".equalsIgnoreCase(ticket.getStatus().trim())) {

				upcomingTicket.add(ticket);
				found = true;

				List<String> seatNumbers = ScreenDAO.getSeat(conn, ticket.getTicketId());

				String dateTime = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm")
						.format(new java.util.Date(ticket.getShow().getShowDateTime()));

				System.out.printf(rowFormat, ticket.getTicketId(), ticket.getShow().getMovie().getMovieTitle(),
						ticket.getShow().getTheatre().getTheatreName(),
						ticket.getShow().getTheatre().getTheatreLocation(), dateTime, String.join(",", seatNumbers),
						ticket.getAmount());
			}
		}

		if (!found) {
			System.out.println(
					"|                                         No upcoming tickets found!                                               |");
			System.out.println(line + "\n");
			return null;
		}

		System.out.println(line + "\n");
		return upcomingTicket;
	}

	public void releaseBookedSeats(Connection conn, long ticketId) {
		String fetchQuery = "SELECT bookingSeatId FROM bookedTicket WHERE ticketId = ?";
		String updateQuery = "UPDATE bookingSeat SET isBooked = false WHERE bookingSeatId = ?";

		try {

			PreparedStatement fetchStmt = conn.prepareStatement(fetchQuery);
			PreparedStatement updateStmt = conn.prepareStatement(updateQuery);

			fetchStmt.setLong(1, ticketId);
			ResultSet rs = fetchStmt.executeQuery();

			while (rs.next()) {
				int bookingSeatId = rs.getInt("bookingSeatId");
				updateStmt.setInt(1, bookingSeatId);
				updateStmt.executeUpdate();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void displayScreen(TheatreAdmin theatreAdmin) {
		Connection conn = Input.getConnection();
		TheatreAdminService tas = new TheatreAdminService();
		Theatre theatre = tas.selectTheatre(theatreAdmin.getTheatre(conn));

		if (theatre == null) {
			System.out.println("No theatre found.");
			return;
		}

		List<Screen> listOfScreen = theatre.getListOfScreen(conn, theatre);

		System.out.println("+------------+---------------------+--------------+");
		System.out.println("| Theatre ID | Screen Name         | Status       |");
		System.out.println("+------------+---------------------+--------------+");

		for (Screen s : listOfScreen) {
			System.out.printf("| %-10s | %-19s | %-13s |\n", s.getTheatre().getTheatreName(), s.getScreenName(),
					s.getStatus());
		}

		System.out.println("+------------+---------------------+--------------+");
		try {
			conn.close();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}

	/*
	 * 
	 * private List<Ticket> displayUpcomingTicket(User currentUser, List<Ticket>
	 * userTickets) { boolean found = false;
	 * 
	 * String line =
	 * "+-------+----------------------+-----------------+---------------+----------------------+------------+------------+";
	 * String headerFormat =
	 * "| %-5s | %-20s | %-15s | %-13s | %-20s | %-10s | %-10s |\n"; String
	 * rowFormat = "| %-5d | %-20s | %-15s | %-13s | %-20s | %-10s | %-10.2f |\n";
	 * 
	 * System.out.println("\n" + line); System.out.printf(headerFormat, "ID",
	 * "Movie", "Theatre", "City", "Show Date & Time", "Seats", "Price");
	 * System.out.println(line); List<Ticket> upcomingTicket = new ArrayList<>();
	 * for (Ticket ticket : userTickets) { if (ticket.getUser().getId() ==
	 * currentUser.getId() && ticket.getShow().getShowDateTime() >
	 * System.currentTimeMillis() /1000 &&
	 * "active".equalsIgnoreCase(ticket.getStatus().trim())) {
	 * 
	 * upcomingTicket.add(ticket); found = true;
	 * 
	 * List<String> seatNumbers = Helper.getSeat(ticket.getTicketId());
	 * 
	 * String dateTime = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm")
	 * .format(new java.util.Date(ticket.getShow().getShowDateTime() * 1000));
	 * 
	 * System.out.printf(rowFormat, ticket.getTicketId(),
	 * ticket.getShow().getMovie().getMovieTitle(),
	 * ticket.getShow().getTheatre().getTheatreName(),
	 * ticket.getShow().getTheatre().getTheatreLocation(), dateTime, seatNumbers,
	 * ticket.getAmount()); } }
	 * 
	 * if (!found) { System.out.println(
	 * "|                                         No upcoming tickets found!                                               |"
	 * ); System.out.println(line + "\n"); return null; }
	 * 
	 * System.out.println(line + "\n"); return upcomingTicket; }
	 * 
	 * public Movies selectTheatreMovie(TheatreAdmin theatreAdmin) {
	 * printTheatrelMovies(theatreAdmin);
	 * System.out.print("Please Enter the Movie ID (or Type '0' to Exit): "); int
	 * movieId = Input.getInteger(movieDB.size()); if (movieId == 0) { return null;
	 * } for (Movies m : movieDB.values()) { if (m.getMovieId() == movieId) { return
	 * m; } } System.out.println("Invalid Movie ID."); return null; }
	 * 
	 * // ----don't delete public void printTheatrelMovies(TheatreAdmin
	 * theatreAdmin) { TheatreAdminService theatreAdminService = new
	 * TheatreAdminService();
	 * 
	 * List<Theatre> theatreList = theatreAdmin.getTheatre(); Theatre theatre =
	 * theatreAdminService.selectTheatre(theatreList);
	 * 
	 * if (theatre == null) { System.out.println("------Back------"); return; }
	 * String format = "| %-4s | %-27s | %-10s | %-10s | %-10s | %-6s | %-32s |%n";
	 * 
	 * System.out.println(
	 * "+------+-----------------------------+------------+------------+------------+--------+--------------------------------+"
	 * ); System.out.format(format, "ID", "Title", "Duration", "Genre", "Language",
	 * "Year", "Theatres"); System.out.println(
	 * "+------+-----------------------------+------------+------------+------------+--------+--------------------------------+"
	 * );
	 * 
	 * List<Show> show = theatre.getListOfShow(); List<Long> list = new
	 * ArrayList<>(); for (Show sh : show) { list.add(sh.getMovie().getMovieId()); }
	 * for (Movies m : movieDB.values()) { if (list.contains(m.getMovieId())) {
	 * 
	 * int hours = m.getDuration() / 60; int minutes = m.getDuration() % 60; String
	 * durationFormatted = String.format("%d hr %02d min", hours, minutes);
	 * 
	 * System.out.format(format, m.getMovieId(), m.getMovieTitle(),
	 * durationFormatted, m.getGenre(), m.getLanguage(), m.getReleaseYear());
	 * 
	 * }
	 * 
	 * } System.out.println(
	 * "+------+-----------------------------+------------+------------+------------+--------+--------------------------------+"
	 * ); }
	 * 
	 * 
	 * 
	 * public void TicketStatus(Ticket ticket, String timeZone, User user) {
	 * 
	 * if (ticket == null) { System.err.println("No ticket "); return; }
	 * 
	 * if (ticket.getUser().getUserId() != user.getUserId()) {
	 * System.err.println("You Don't Have Permission to Access This Ticket");
	 * return; }
	 * 
	 * List<String> seats = ticket.getSeats().stream().map(s ->
	 * s.getSeatNumber()).collect(Collectors.toList()); DateTimeFormatter formatter
	 * = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a"); ZonedDateTime zdt =
	 * Instant.ofEpochSecond(ticket.getShow().getDateTimeEpoch()).atZone(ZoneId.of(
	 * timeZone)); System.out.
	 * println("\n==================== TICKET DETAILS ====================");
	 * System.out.printf("Ticket ID     : %d%n", ticket.getTicketId());
	 * System.out.printf("Movie         : %s%n", ticket.getMovie().getMovieTitle());
	 * System.out.printf("Theatre       : %s (%s)%n",
	 * ticket.getTheatre().getTheatreName(),
	 * ticket.getTheatre().getTheatreLocation());
	 * System.out.printf("Screen        : %s%n",
	 * ticket.getShow().getScreen().getScreenNumber());
	 * System.out.printf("Seats         : %s%n", seats);
	 * System.out.printf("Show DateTime : %s%n", zdt.format(formatter));
	 * System.out.printf("Total Amount  : ₹%.2f%n", ticket.getTotalAmount());
	 * System.out.printf("Status        : %s%n", ticket.getStatus());
	 * System.out.println(
	 * "========================================================\n"); }
	 * 
	 * 
	 * 
	 * public Movies getMovieFromTheatre(List<Movies> listOfMovies) {
	 * 
	 * String format = "| %-4s | %-27s | %-12s | %-12s | %-12s | %-6s |%n";
	 * 
	 * System.out.println(
	 * "+------+-----------------------------+--------------+--------------+--------------+--------+"
	 * ); System.out.format(format, "ID", "Title", "Duration", "Genre", "Language",
	 * "Year"); System.out.println(
	 * "+------+-----------------------------+--------------+--------------+--------------+--------+"
	 * ); HashMap<Long, Movies> movieDb = new HashMap<>(); for (Movies m :
	 * listOfMovies) {
	 * 
	 * int hours = m.getDuration() / 60; int minutes = m.getDuration() % 60; String
	 * durationFormatted = String.format("%d hr %02d min", hours, minutes);
	 * movieDb.put(m.getMovieId(), m); System.out.format(format, m.getMovieId(),
	 * m.getMovieTitle(), durationFormatted, m.getGenre(), m.getLanguage(),
	 * m.getReleaseYear()); }
	 * 
	 * System.out.println(
	 * "+------+-----------------------------+--------------+--------------+--------------+--------+"
	 * ); long movieId = 0; while (true) {
	 * System.out.println("Enter the Movie id (or type '0' to Exit):"); movieId =
	 * Input.getLong((long) 1000000); if (movieId == 0) { return null; } if
	 * (movieDb.containsKey(movieId)) { return movieDb.get(movieId); } else {
	 * System.err.println("Invalid Movie Id"); } } }
	 * 
	 * private double getSeatPriceFromDB(List<Long> seatIdList) { double total =
	 * 0.0; String query = "SELECT SUM(price) FROM seat WHERE seatId IN (%s)";
	 * String inParams =
	 * seatIdList.stream().map(String::valueOf).collect(Collectors.joining(","));
	 * query = String.format(query, inParams);
	 * 
	 * try (Connection conn = Input.getConnection(); PreparedStatement ps =
	 * conn.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
	 * 
	 * if (rs.next()) { total = rs.getDouble(1); } } catch (SQLException e) {
	 * System.err.println("Error fetching seat price: " + e.getMessage()); } return
	 * total; }
	 * 
	 */

}
