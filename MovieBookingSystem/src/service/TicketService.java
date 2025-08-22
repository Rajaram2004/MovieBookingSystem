package service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import model.Movies;
import model.Screen;
import model.Seat;
import model.Show;
import model.Theatre;
import model.TheatreAdmin;
import model.Ticket;
import model.User;
import repository.InMemoryDatabase;
import util.Input;

public class TicketService {
	static HashMap<Long, Movies> movieDB = InMemoryDatabase.getMovieDB();
	static HashMap<Long, Ticket> ticketDB = InMemoryDatabase.getTicketDB();

	public TicketService() {

	}

	// ------------------------------------------------------------------------------------------------------------------------------------------------------------------

	public void bookTicket(User user, String timeZone, HashMap<Long, Ticket> ticketDB) {

		Movies movieObj = selectMovie();
		if (movieObj == null) {
			return;
		}
		Theatre theatreObj = selectTheatreForMovie(movieObj);
		if (theatreObj == null) {
			return;
		}
		List<Show> shows = getShowsForMovieAndTheatre(movieObj, theatreObj);
		Show show = getShow(shows, timeZone);
		if (show == null) {
			System.err.println("Booking cancelled. No show selected.");
			return;
		}
		displaySeatsByCategory(show);
		List<Seat> selectSeat = selectSeats(show);
		if (selectSeat == null) {
			System.out.println("------Back------");
			return;
		}
		double amount = calculateAmount(selectSeat);
		int bookedEpoch = show.getDateTimeEpoch();
		double currBalance = user.getBalance();
		if (currBalance < amount) {
			System.err.println("insufficient Amount ,Ticket Booking Cancelled ");
			return;
		}

		System.out.println("Current Balance : " + user.getBalance());
		System.out.println("Ticket price    : " + amount);
		System.out.println(
				"Are you sure you want to proceed with booking your ticket? if Type '1' to continue and '0' to Exit");
		if (Input.getInteger(1) == 0) {
			System.out.println("------Back------");
			return;
		}
		markBooked(selectSeat);
		currBalance -= amount;
		user.setBalance(currBalance);
		System.out.println("New Available Balance is " + user.getBalance());
		Ticket ticket = new Ticket((long) (ticketDB.size() + 1), user, movieObj, theatreObj, show, selectSeat,
				bookedEpoch, amount, "Upcoming");
		ticketDB.put((long) (ticketDB.size() + 1), ticket);
		System.out.println("Your Ticket Successfully Booked");
		TicketStatus(ticket, user.getTimeZone(), user);

	}

	// ------------------------------------------------------------------------------------------------------------------------------------------------------------------

	public Movies selectTheatreMovie(TheatreAdmin theatreAdmin) {
		printTheatrelMovies(theatreAdmin);
		System.out.print("Please Enter the Movie ID (or Type '0' to Exit): ");
		int movieId = Input.getInteger(movieDB.size());
		if (movieId == 0) {
			return null;
		}
		for (Movies m : movieDB.values()) {
			if (m.getMovieId() == movieId) {
				return m;
			}
		}
		System.out.println("Invalid Movie ID.");
		return null;
	}

	public void printTheatrelMovies(TheatreAdmin theatreAdmin) {
		TheatreAdminService theatreAdminService = new TheatreAdminService();

		List<Theatre> theatreList = theatreAdmin.getTheatre();
		Theatre theatre = theatreAdminService.selectTheatre(theatreList);

		if (theatre == null) {
			System.out.println("------Back------");
			return;
		}
		String format = "| %-4s | %-27s | %-10s | %-10s | %-10s | %-6s | %-32s |%n";

		System.out.println(
				"+------+-----------------------------+------------+------------+------------+--------+--------------------------------+");
		System.out.format(format, "ID", "Title", "Duration", "Genre", "Language", "Year", "Theatres");
		System.out.println(
				"+------+-----------------------------+------------+------------+------------+--------+--------------------------------+");
		int theatreColWidth = 32;
		List<Show> show = theatre.getListOfShow();
		List<Long> list = new ArrayList<>();
		for (Show sh : show) {
			list.add(sh.getMovie().getMovieId());
		}
		for (Movies m : movieDB.values()) {
			if (list.contains(m.getMovieId())) {
				String theatres = m.getListOfTheatre().stream().map(Theatre::getTheatreName)
						.reduce((a, b) -> a + ", " + b).orElse("No Theatre");

				int hours = m.getDuration() / 60;
				int minutes = m.getDuration() % 60;
				String durationFormatted = String.format("%d hr %02d min", hours, minutes);

				List<String> theatreLines = wrapText(theatres, theatreColWidth);

				System.out.format(format, m.getMovieId(), m.getMovieTitle(), durationFormatted, m.getGenre(),
						m.getLanguage(), m.getReleaseYear(), theatreLines.get(0));

				for (int i = 1; i < theatreLines.size(); i++) {
					System.out.format(format, "", "", "", "", "", "", theatreLines.get(i));
				}
			}

		}
		System.out.println(
				"+------+-----------------------------+------------+------------+------------+--------+--------------------------------+");
	}

	public void markBooked(List<Seat> selectSeat) {
		for (Seat seat : selectSeat) {
			seat.setSeatStatus(true);
		}

	}

	public void filterMoviesByLanguage() {
		HashSet<String> languages = new HashSet<>();
		for (Movies m : movieDB.values()) {
			languages.add(m.getLanguage());
		}
		int count = 1;
		System.out.println("Available Languages:");
		for (String lan : languages) {
			System.out.println(count + ". " + lan);
			count++;
		}
		System.out.println("Enter Your Choice (or Type '0' to Exit): ");
		int choice = Input.getInteger(count - 1);
		if (choice == 0) {
			System.out.println("------Back------");
			return;
		}
		count = 1;
		String selectedLanguage = null;
		for (String str : languages) {
			if (count == choice) {
				selectedLanguage = str;
				break;
			}
			count++;
		}
		if (selectedLanguage == null) {
			System.out.println("Invalid choice.");
			return;
		}
		System.out.println("+----+-----------------------------+----------------+--------+");
		System.out.println("| ID | Title                       | Duration       | Year   |");
		System.out.println("+----+-----------------------------+----------------+--------+");

		for (Movies m : movieDB.values()) {
			if (m.getLanguage().equalsIgnoreCase(selectedLanguage)) {

				int duration = m.getDuration();
				int hours = duration / 60; // Calculate hours
				int minutes = duration % 60;
				String dur = hours + " hr " + minutes + " min";
				System.out.printf("| %-2d | %-27s | %-14s | %-6d |\n", m.getMovieId(), m.getMovieTitle(), dur,
						m.getReleaseYear());
			}
		}

		System.out.println("+----+-----------------------------+----------------+--------+");
	}

	public void printAllMovies() {
		// Table column widths
		int theatreColWidth = 32;

		String format = "| %-4s | %-27s | %-12s | %-12s | %-12s | %-6s | %-" + theatreColWidth + "s |%n";

		// Print table header
		System.out.println(
				"+------+-----------------------------+--------------+--------------+--------------+--------+----------------------------------+");
		System.out.format(format, "ID", "Title", "Duration", "Genre", "Language", "Year", "Theatres");
		System.out.println(
				"+------+-----------------------------+--------------+--------------+--------------+--------+----------------------------------+");

		for (Movies m : movieDB.values()) {
			String theatres = m.getListOfTheatre().stream().map(Theatre::getTheatreName)
					.collect(Collectors.joining(", "));

			int hours = m.getDuration() / 60;
			int minutes = m.getDuration() % 60;
			String durationFormatted = String.format("%d hr %02d min", hours, minutes);

			List<String> theatreLines = wrapText(theatres, theatreColWidth);

			System.out.format(format, m.getMovieId(), m.getMovieTitle(), durationFormatted, m.getGenre(),
					m.getLanguage(), m.getReleaseYear(), theatreLines.get(0));

			for (int i = 1; i < theatreLines.size(); i++) {
				System.out.format(format, "", "", "", "", "", "", theatreLines.get(i));
			}
		}

		System.out.println(
				"+------+-----------------------------+--------------+--------------+--------------+--------+----------------------------------+");
	}

	private List<String> wrapText(String text, int width) {
		List<String> lines = new ArrayList<>();
		String[] words = text.split(" ");
		StringBuilder currentLine = new StringBuilder();

		for (String word : words) {
			if (currentLine.length() + word.length() + 1 > width) {
				lines.add(currentLine.toString());
				currentLine = new StringBuilder();
			}
			if (currentLine.length() > 0)
				currentLine.append(" ");
			currentLine.append(word);
		}

		if (currentLine.length() > 0) {
			lines.add(currentLine.toString());
		}
		return lines;
	}

	public Movies selectMovie() {
		printAllMovies();
		System.out.print("Please enter the Movie ID you wish to book (or Type '0' to Exit): ");
		int movieId = Input.getInteger(movieDB.size());

		if (movieId == 0) {
			System.out.println("------Back------");
			return null;
		}
		for (Movies m : movieDB.values()) {
			if (m.getMovieId() == movieId) {
				return m;
			}
		}
		System.out.println("Invalid Movie ID.");
		return null;
	}

	public Theatre selectTheatreForMovie(Movies movie) {
		List<Theatre> theatres = movie.getListOfTheatre();
		theatres = theatres.stream().distinct().toList();
		System.out.println("Available Theatres for \"" + movie.getMovieTitle() + "\":");
		System.out.println("+----+---------------------+------------+");
		System.out.println("| ID | Theatre Name        | City       |");
		System.out.println("+----+---------------------+------------+");
		for (int i = 0; i < theatres.size(); i++) {
			Theatre t = theatres.get(i);
			System.out.printf("| %-2d | %-19s | %-10s |\n", i + 1, t.getTheatreName(), t.getTheatreLocation());
		}
		System.out.println("+----+---------------------+------------+");
		System.out.println("Enter theatre Id (or Type '0' to Exit):");
		int choice = Input.getInteger(theatres.size());
		if (choice == 0) {
			System.out.println("------Back------");
			return null;
		}
		Theatre selected = theatres.get(choice - 1);
		System.out.println("You have selected: " + selected.getTheatreName() + ", " + selected.getTheatreLocation());
		return selected;
	}

	public List<Show> getShowsForMovieAndTheatre(Movies movie, Theatre theatre) {
		List<Show> availableShows = new ArrayList<>();
		for (Show s : theatre.getListOfShow()) {
			if (s.isActive() == true && s.getMovie().getMovieId().equals(movie.getMovieId())) {
				availableShows.add(s);
			}
		}
		return availableShows;
	}

//	public Show getShow(List<Show> shows, String timeZone) {
//		if (shows == null || shows.isEmpty()) {
//			System.out.println("No shows available for this selection.");
//			return null;
//		}
//
//		int count = 1;
//		System.out.println(
//				"+----+---------------------------+----------------------+----------------+----------------------+---------------+");
//		System.out.println(
//				"| ID |         Date & Time       | Theatre Name         | City           |      Movie Title     |   Language    |");
//		System.out.println(
//				"+----+---------------------------+----------------------+----------------+----------------------+---------------+");
//
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");
//
//		for (Show show : shows) {
//			if (show.isActive()) {
//				ZonedDateTime zdt = Instant.ofEpochSecond(show.getDateTimeEpoch()).atZone(ZoneId.of(timeZone));
//				String formattedDateTime = zdt.format(formatter);
//
//				System.out.printf("| %-3d | %-25s | %-20s | %-14s | %-20s | %-13s |\n", count++, formattedDateTime,
//						show.getTheatre().getTheatreName(), show.getTheatre().getTheatreLocation(),
//						show.getMovie().getMovieTitle(), show.getMovie().getLanguage());
//			}
//		}
//
//		System.out.println(
//				"+----+---------------------------+----------------------+----------------+----------------------+---------------+");
//		System.out.println("Select the show Using Id (or Type '0' to Exit):");
//
//		int selectShow = Input.getInteger(count - 1);
//		if (selectShow == 0) {
//			System.out.println("------Back------");
//			return null;
//		}
//
//		int loopCount = 1;
//		for (Show s : shows) {
//			if (loopCount == selectShow) {
//				return s;
//			}
//			loopCount++;
//		}
//		return null;
//	}
	public Show getShow(List<Show> shows, String timeZone) {
	    if (shows == null || shows.isEmpty()) {
	        System.out.println("No shows available for this selection.");
	        return null;
	    }

	    
	    shows.sort(Comparator.comparingLong(Show::getDateTimeEpoch));

	    int count = 1;
	    System.out.println(
	            "+----+---------------------------+----------------------+----------------+----------------------+---------------+");
	    System.out.println(
	            "| ID |         Date & Time       | Theatre Name         | City           |      Movie Title     |   Language    |");
	    System.out.println(
	            "+----+---------------------------+----------------------+----------------+----------------------+---------------+");

	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");

	    for (Show show : shows) {
	        if (show.isActive()) {
	            ZonedDateTime zdt = Instant.ofEpochSecond(show.getDateTimeEpoch()).atZone(ZoneId.of(timeZone));
	            String formattedDateTime = zdt.format(formatter);

	            System.out.printf("| %-3d | %-25s | %-20s | %-14s | %-20s | %-13s |\n",
	                    count++, formattedDateTime,
	                    show.getTheatre().getTheatreName(),
	                    show.getTheatre().getTheatreLocation(),
	                    show.getMovie().getMovieTitle(),
	                    show.getMovie().getLanguage());
	        }
	    }

	    System.out.println(
	            "+----+---------------------------+----------------------+----------------+----------------------+---------------+");
	    System.out.println("Select the show Using Id (or Type '0' to Exit):");

	    int selectShow = Input.getInteger(count - 1);
	    if (selectShow == 0) {
	        System.out.println("------Back------");
	        return null;
	    }

	    int loopCount = 1;
	    for (Show s : shows) {
	        if (loopCount == selectShow) {
	            return s;
	        }
	        loopCount++;
	    }
	    return null;
	}


	public void displaySeatsByCategory(Show show) {
		int rows = show.getScreen().getCols();
		int cols = show.getScreen().getRows();
		int totalSeats = rows ;
		int regular = show.getScreen().getRegularRows();
		int premium = show.getScreen().getPremiumRows();
		int economy = totalSeats - (regular + premium);
		List<Seat> seat = show.getSeats();
		int count = 0;
		int availableSeat=0;
		for (int i = 0; i < rows; i++) {
			if (i == 0) {
				System.out.println("---------------------Economy---------------------");
			} else if (i == premium-1) {
				System.out.println("---------------------Regular---------------------");
			} else if (i == (premium + economy)) {
				System.out.println("---------------------premium---------------------");
			}
			char rowChar = (char) ('A' + i);
			System.out.print(rowChar + "  ");

			for (int j = 1; j <= cols; j++) {
				if (seat.get(count).isSeatStatus() == true) {
					System.out.print("[x]" + rowChar + j + " ");
				} else {
					System.out.print("[ ]" + rowChar + j + " ");
					availableSeat++;
				}
				count++;
			}
			System.out.println();
		}

		System.out.println("\nTotal Available Seats: " + (availableSeat) + "/" + (rows * cols));
	}

	public List<Seat> selectSeats(Show show) {
		Scanner sc = new Scanner(System.in);
		List<Seat> seats = show.getSeats();
		List<Seat> selectedSeats = new ArrayList<>();

		while (true) {
			System.out
					.println("Enter seat numbers to book (comma separated, e.g., A1,A2,B5): (or Type 'done' to Exit):");
			String input = sc.nextLine().trim();
			if (input.equalsIgnoreCase("done")) {
				System.out.println("------Back------");
				return null;
			}
			String[] seatNumbers = input.split(",");

			boolean allValid = true;
			selectedSeats.clear();

			for (String seatInput : seatNumbers) {
				String seatNo = seatInput.trim();

				Seat seat = seats.stream().filter(s -> s.getSeatNumber().equalsIgnoreCase(seatNo)).findFirst()
						.orElse(null);

				if (seat == null) {
					System.out.println(seatNo + " is invalid. Please enter valid seat numbers.");
					allValid = false;
					break;
				} else if (seat.isSeatStatus()) {
					System.out.println(seatNo + " is already booked. Choose another seat.");
					allValid = false;
					break;
				} else {
					selectedSeats.add(seat);
				}
			}

			if (allValid)
				break;
		}

		return selectedSeats;
	}

	public double calculateAmount(List<Seat> selectedSeats) {
		double total = 0;
		for (Seat seat : selectedSeats) {
			total+=seat.getSeatPrice();
		}
		return total;
	}
	public void checkTicketStatus(String timeZone, User user) {
		if (ticketDB.isEmpty()) {
			System.err.println("No Tickets Available");
			return;
		}
		System.out.print("Enter your Ticket ID (or Type '0' to Exit): ");
		long ticketId = Input.getLong((long) ticketDB.size());
		if (ticketId == 0) {
			System.out.println("------Back------");
			return;
		}
		Ticket ticket = ticketDB.get(ticketId);

		if (ticket == null) {
			System.err.println("No ticket found with ID: " + ticketId);
			return;
		}

		if (ticket.getUser().getUserId() != user.getUserId()) {
			System.err.println("You Don't Have Permission to Access This Ticket");
			return;
		}

		List<String> seats = ticket.getSeats().stream().map(s -> s.getSeatNumber()).collect(Collectors.toList());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");
		ZonedDateTime zdt = Instant.ofEpochSecond(ticket.getShow().getDateTimeEpoch()).atZone(ZoneId.of(timeZone));
		System.out.println("\n==================== TICKET DETAILS ====================");
		System.out.printf("Ticket ID     : %d%n", ticket.getTicketId());
		System.out.printf("Movie         : %s%n", ticket.getMovie().getMovieTitle());
		System.out.printf("Theatre       : %s (%s)%n", ticket.getTheatre().getTheatreName(),
				ticket.getTheatre().getTheatreLocation());
		System.out.printf("Screen        : %s%n", ticket.getShow().getScreen().getScreenNumber());
		System.out.printf("Seats         : %s%n", seats);
		System.out.printf("Show DateTime : %s%n", zdt.format(formatter));
		System.out.printf("Total Amount  : ₹%.2f%n", ticket.getTotalAmount());
		System.out.printf("Status        : %s%n", ticket.getStatus() );
		
		System.out.println("========================================================\n");
	}

	public void TicketStatus(Ticket ticket, String timeZone, User user) {

		if (ticket == null) {
			System.err.println("No ticket ");
			return;
		}

		if (ticket.getUser().getUserId() != user.getUserId()) {
			System.err.println("You Don't Have Permission to Access This Ticket");
			return;
		}

		List<String> seats = ticket.getSeats().stream().map(s -> s.getSeatNumber()).collect(Collectors.toList());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");
		ZonedDateTime zdt = Instant.ofEpochSecond(ticket.getShow().getDateTimeEpoch()).atZone(ZoneId.of(timeZone));
		System.out.println("\n==================== TICKET DETAILS ====================");
		System.out.printf("Ticket ID     : %d%n", ticket.getTicketId());
		System.out.printf("Movie         : %s%n", ticket.getMovie().getMovieTitle());
		System.out.printf("Theatre       : %s (%s)%n", ticket.getTheatre().getTheatreName(),
				ticket.getTheatre().getTheatreLocation());
		System.out.printf("Screen        : %s%n", ticket.getShow().getScreen().getScreenNumber());
		System.out.printf("Seats         : %s%n", seats);
		System.out.printf("Show DateTime : %s%n", zdt.format(formatter));
		System.out.printf("Total Amount  : ₹%.2f%n", ticket.getTotalAmount());
		System.out.printf("Status        : %s%n", ticket.getStatus());
		System.out.println("========================================================\n");
	}

	public void viewMyBooking(User currentUser, String timeZone) {
		List<Ticket> userTickets = ticketDB.values().stream().filter(t -> t.getUser().equals(currentUser))
				.collect(Collectors.toList());

		if (userTickets.isEmpty()) {
			System.err.println("No booking Available.");
			return;
		}

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");
		System.out.printf(
				"+----+----------------------+----------------------+--------+----------------------+-----------------------+----------------------+%n");
		System.out.printf(
				"| ID | Movie                | Theatre              | Screen | Show Date & Time     |  Status               | Seats                |%n");
		System.out.printf(
				"+----+----------------------+----------------------+--------+----------------------+-----------------------+----------------------+%n");

		for (Ticket ticket : userTickets) {

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

	public void cancelTicket(User currentUser) {
		System.out.print("Enter Ticket ID to cancel (or Type '0' to Exit): ");
		long size = (long) ticketDB.size();
		if (size == 0) {
			System.err.println("No Tickets Available for cancel");
			return;
		}
		long ticketId = Input.getLong(size);
		if (ticketId == 0) {
			System.out.println("------Back------");
			return;
		}
		Ticket ticket = ticketDB.get(ticketId);
		if (ticket == null || !ticket.getUser().equals(currentUser)) {
			System.err.println("No ticket found with this ID for your account.");
			return;
		}

		if (ticket.getStatus().equalsIgnoreCase("cancelled")  ) {
			System.err.println("Ticket is already cancelled.");
			return;
		}
		if(ticket.getStatus().equalsIgnoreCase("Refunded")) {
			System.err.println("Show Cancelled so ticket's cancelled and Refunded");
			return;
		}

		ticket.setStatus("cancelled");

		for (Seat seat : ticket.getSeats()) {
			seat.setSeatStatus(false);
		}

		double totalAmount = ticket.getTotalAmount();
		double detectAmount = totalAmount * 0.25;
		double finalAmount = totalAmount - detectAmount;
		double userAmount = currentUser.getBalance();
		userAmount += finalAmount;
		currentUser.setBalance(userAmount);
		System.out.println("25% deducted from your ticket price. Remaining Amount credited to user");
		System.out.println("Ticket ID " + ticketId + " has been successfully cancelled.");
	}

	public Movies selectTheatreMovie1(TheatreAdmin theatreAdmin, Theatre theatre) {
		printTheatrelMovies1(theatreAdmin, theatre);
		System.out.print("Please Enter the Movie ID (or Type '0' to Exit): ");
		int movieId = Input.getInteger(movieDB.size());
		if (movieId == 0) {
			return null;
		}
		for (Movies m : movieDB.values()) {
			if (m.getMovieId() == movieId) {
				return m;
			}
		}
		System.out.println("Invalid Movie ID.");
		return null;
	}

	public void printTheatrelMovies1(TheatreAdmin theatreAdmin, Theatre theatre) {
		TheatreAdminService theatreAdminService = new TheatreAdminService();

		if (theatre == null) {
			System.out.println("------Back------");
			return;
		}
		String format = "| %-4s | %-27s | %-10s | %-10s | %-10s | %-6s | %-32s |%n";

		System.out.println(
				"+------+-----------------------------+------------+------------+------------+--------+--------------------------------+");
		System.out.format(format, "ID", "Title", "Duration", "Genre", "Language", "Year", "Theatres");
		System.out.println(
				"+------+-----------------------------+------------+------------+------------+--------+--------------------------------+");
		int theatreColWidth = 32;
		List<Show> show = theatre.getListOfShow();
		List<Long> list = new ArrayList<>();
		for (Show sh : show) {
			list.add(sh.getMovie().getMovieId());
		}
		for (Movies m : movieDB.values()) {

			if (list.contains(m.getMovieId())) {

				String theatres = m.getListOfTheatre().stream().map(Theatre::getTheatreName)
						.reduce((a, b) -> a + ", " + b).orElse("No Theatre");

				int hours = m.getDuration() / 60;
				int minutes = m.getDuration() % 60;
				String durationFormatted = String.format("%d hr %02d min", hours, minutes);

				List<String> theatreLines = wrapText(theatres, theatreColWidth);

				System.out.format(format, m.getMovieId(), m.getMovieTitle(), durationFormatted, m.getGenre(),
						m.getLanguage(), m.getReleaseYear(), theatreLines.get(0));

				for (int i = 1; i < theatreLines.size(); i++) {
					System.out.format(format, "", "", "", "", "", "", theatreLines.get(i));
				}
			}

		}
		System.out.println(
				"+------+-----------------------------+------------+------------+------------+--------+--------------------------------+");
	}

	public void displayScreen(TheatreAdmin theatreAdmin) {
		TheatreAdminService tas = new TheatreAdminService();
		Theatre theatre = tas.selectTheatre(theatreAdmin.getTheatre());
		List<Screen> listOfScreen = theatre.getListOfScreen();
		List<Integer> list = new ArrayList<>();
		for (Screen s : listOfScreen) {
			if (s.isActive() == true) {
				list.add(s.getScreenNumber());
			}

		}
		System.out.println(list);
	}

	public void bookTicketViaTheatre(List<Movies> listOfMovies, Theatre theatreObj,User user,String timeZone) {
		
		Movies movieObj = getMovieFromTheatre(listOfMovies);
		if (movieObj == null) {
			return;
		}
		
		List<Show> shows = getShowsForMovieAndTheatre(movieObj, theatreObj);
		Show show = getShow(shows, timeZone);
		if (show == null) {
			System.err.println("Booking cancelled. No show selected.");
			return;
		}
		displaySeatsByCategory(show);
		List<Seat> selectSeat = selectSeats(show);
		if (selectSeat == null) {
			System.out.println("------Back------");
			return;
		}
		double amount = calculateAmount(selectSeat);
		int bookedEpoch = show.getDateTimeEpoch();
		double currBalance = user.getBalance();
		if (currBalance < amount) {
			System.err.println("insufficient Amount ,Ticket Booking Cancelled ");
			return;
		}

		System.out.println("Current Balance : " + user.getBalance());
		System.out.println("Ticket price    : " + amount);
		System.out.println(
				"Are you sure you want to proceed with booking your ticket? if Type '1' to continue and '0' to Exit");
		if (Input.getInteger(1) == 0) {
			System.out.println("------Back------");
			return;
		}
		markBooked(selectSeat);
		currBalance -= amount;
		user.setBalance(currBalance);
		System.out.println("New Available Balance is " + user.getBalance());
		Ticket ticket = new Ticket((long) (ticketDB.size() + 1), user, movieObj, theatreObj, show, selectSeat,
				bookedEpoch, amount, "Upcoming");
		ticketDB.put((long) (ticketDB.size() + 1), ticket);
		System.out.println("Your Ticket Successfully Booked");
		TicketStatus(ticket, user.getTimeZone(), user);
		
	}
	public Movies getMovieFromTheatre(List<Movies> listOfMovies) {
		

		String format = "| %-4s | %-27s | %-12s | %-12s | %-12s | %-6s |%n";

	
		System.out.println(
				"+------+-----------------------------+--------------+--------------+--------------+--------+");
		System.out.format(format, "ID", "Title", "Duration", "Genre", "Language", "Year");
		System.out.println(
				"+------+-----------------------------+--------------+--------------+--------------+--------+");
		HashMap<Long,Movies> movieDb = new HashMap<>(); 
		for (Movies m : listOfMovies) {
			
			int hours = m.getDuration() / 60;
			int minutes = m.getDuration() % 60;
			String durationFormatted = String.format("%d hr %02d min", hours, minutes);
			movieDb.put(m.getMovieId(), m);
			System.out.format(format, m.getMovieId(), m.getMovieTitle(), durationFormatted, m.getGenre(),
					m.getLanguage(), m.getReleaseYear());
		}

		System.out.println(
				"+------+-----------------------------+--------------+--------------+--------------+--------+");
		long movieId=0;
		while(true) {
			System.out.println("Enter the Movie id (or type '0' to Exit):");
			movieId=Input.getLong((long)1000000);
			if(movieId==0) {
				return null;
			}
			if(movieDb.containsKey(movieId)) {
				return movieDb.get(movieId);
			}else {
				System.err.println("Invalid Movie Id");
			}
		}
	}

}
