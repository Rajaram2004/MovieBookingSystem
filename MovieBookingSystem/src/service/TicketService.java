package service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import model.Movies;
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
		Theatre theatreObj = selectTheatreForMovie(movieObj);
		List<Show> shows = getShowsForMovieAndTheatre(movieObj, theatreObj);
		Show show = getShow(shows, timeZone);
		if (show == null) {
			System.err.println("Booking cancelled. No show selected.");
			return;
		}
		displaySeatsByCategory(show);
		List<Seat> selectSeat = selectSeats(show);
		double amount = calculateAmount(selectSeat);
		markBooked(selectSeat);
		int bookedEpoch = show.getDateTimeEpoch();
		ticketDB.put((long) (ticketDB.size() + 1), new Ticket((long) (ticketDB.size() + 1), user, movieObj, theatreObj,
				show, selectSeat, bookedEpoch, amount, true, true));
		System.out.println("Your Ticket Successfully Booked");
		System.out.println("Ticket ID: " + ticketDB.size());

	}

	// ------------------------------------------------------------------------------------------------------------------------------------------------------------------

	public Movies selectTheatreMovie(TheatreAdmin theatreAdmin) {
		printTheatrelMovies(theatreAdmin);
		System.out.print("Please enter the Movie ID you wish to book: ");
		int movieId = Input.getInteger(movieDB.size());

		for (Movies m : movieDB.values()) {
			if (m.getMovieId() == movieId) {
				return m;
			}
		}
		System.out.println("Invalid Movie ID.");
		return null;
	}

	public void printTheatrelMovies(TheatreAdmin theatreAdmin) {
		String format = "| %-4s | %-27s | %-10s | %-10s | %-10s | %-6s | %-32s |%n";

		System.out.println(
				"+------+-----------------------------+------------+------------+------------+--------+--------------------------------+");
		System.out.format(format, "ID", "Title", "Duration", "Genre", "Language", "Year", "Theatres");
		System.out.println(
				"+------+-----------------------------+------------+------------+------------+--------+--------------------------------+");
		List<Show> show = theatreAdmin.getTheatre().getListOfShow();
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

				if (theatres.length() > 30) {
					theatres = theatres.substring(0, 27) + "...";
				}

				System.out.format(format, m.getMovieId(), m.getMovieTitle(), durationFormatted, m.getGenre(),
						m.getLanguage(), m.getReleaseYear(), theatres);
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
		System.out.println("Enter Your Choice : ");
		int choice = Input.getInteger(count - 1);
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
		System.out.println("+----+-----------------------------+------------+--------+");
		System.out.println("| ID | Title                       | Duration   | Year   |");
		System.out.println("+----+-----------------------------+------------+--------+");

		for (Movies m : movieDB.values()) {
			if (m.getLanguage().equalsIgnoreCase(selectedLanguage)) {
				System.out.printf("| %-2d | %-27s | %-10s | %-6d |\n", m.getMovieId(), m.getMovieTitle(),
						m.getDuration(), m.getReleaseYear());
			}
		}

		System.out.println("+----+-----------------------------+------------+--------+");
	}

	public void printAllMovies() {
		String format = "| %-4s | %-27s | %-10s | %-10s | %-10s | %-6s | %-32s |%n";

		System.out.println(
				"+------+-----------------------------+------------+------------+------------+--------+--------------------------------+");
		System.out.format(format, "ID", "Title", "Duration", "Genre", "Language", "Year", "Theatres");
		System.out.println(
				"+------+-----------------------------+------------+------------+------------+--------+--------------------------------+");

		for (Movies m : movieDB.values()) {
			String theatres = m.getListOfTheatre().stream().map(Theatre::getTheatreName).reduce((a, b) -> a + ", " + b)
					.orElse("No Theatre");

			int hours = m.getDuration() / 60;
			int minutes = m.getDuration() % 60;
			String durationFormatted = String.format("%d hr %02d min", hours, minutes);

			if (theatres.length() > 30) {
				theatres = theatres.substring(0, 27) + "...";
			}

			System.out.format(format, m.getMovieId(), m.getMovieTitle(), durationFormatted, m.getGenre(),
					m.getLanguage(), m.getReleaseYear(), theatres);
		}
		System.out.println(
				"+------+-----------------------------+------------+------------+------------+--------+--------------------------------+");
	}

	public Movies selectMovie() {
		printAllMovies();
		System.out.print("Please enter the Movie ID you wish to book: ");
		int movieId = Input.getInteger(movieDB.size());

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
		System.out.println("Available Theatres for \"" + movie.getMovieTitle() + "\":");
		System.out.println("+----+---------------------+------------+");
		System.out.println("| ID | Theatre Name        | City       |");
		System.out.println("+----+---------------------+------------+");
		for (int i = 0; i < theatres.size(); i++) {
			Theatre t = theatres.get(i);
			System.out.printf("| %-2d | %-19s | %-10s |\n", i + 1, t.getTheatreName(), t.getTheatreLocation());
		}
		System.out.println("+----+---------------------+------------+");

		int choice = Input.getInteger(theatres.size());
		Theatre selected = theatres.get(choice - 1);
		System.out.println("You have selected: " + selected.getTheatreName() + ", " + selected.getTheatreLocation());
		return selected;
	}

	public List<Show> getShowsForMovieAndTheatre(Movies movie, Theatre theatre) {
		List<Show> availableShows = new ArrayList<>();
		for (Show s : theatre.getListOfShow()) {
			if (s.getMovie().getMovieId().equals(movie.getMovieId())) {
				availableShows.add(s);
			}
		}
		return availableShows;
	}

	public Show getShow(List<Show> shows, String timeZone) {
		if (shows == null || shows.isEmpty()) {
			System.out.println("No shows available for this selection.");
			return null;
		}
		int count = 1;
		System.out.println("+----+---------------------------+----------------------+----------------+");
		System.out.println("| ID |         Date & Time       | Theatre Name         | City           |");
		System.out.println("+----+---------------------------+----------------------+----------------+");

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");

		for (Show show : shows) {
			ZonedDateTime zdt = Instant.ofEpochSecond(show.getDateTimeEpoch()).atZone(ZoneId.of(timeZone));
			String formattedDateTime = zdt.format(formatter);

			System.out.printf("| %-3d | %-25s | %-20s | %-14s |\n", count++, formattedDateTime,
					show.getTheatre().getTheatreName(), show.getTheatre().getTheatreLocation());
		}

		System.out.println("+----+---------------------------+----------------------+----------------+");
		System.out.println("Select the show Using Id: ");
		int selectShow = Input.getInteger(count - 1);

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
		List<Seat> seats = show.getSeats();
		int seatsPerRow = 12;
		char rowLabel = 'A';

		Map<String, List<Seat>> categorySeats = new HashMap<>();
		for (Seat seat : seats) {
			categorySeats.computeIfAbsent(seat.getSeatType(), k -> new ArrayList<>()).add(seat);
		}

		for (String category : List.of("Premium", "Regular", "Economy")) {
			List<Seat> catSeats = categorySeats.get(category);
			if (catSeats == null || catSeats.isEmpty())
				continue;

			System.out.println("\n" + category + " Seats:");
			int seatCount = 0;
			int availableCount = 0;

			for (Seat seat : catSeats) {
				if (seatCount % seatsPerRow == 0) {
					if (seatCount != 0)
						System.out.println();
					System.out.print(rowLabel + "  ");
					rowLabel++;
				}

				if (seat.isSeatStatus()) {
					System.err.printf("%-5s", "[X]" + seat.getSeatNumber());
				} else {
					System.out.printf("%-5s", "[ ]" + seat.getSeatNumber());
					availableCount++;
				}

				seatCount++;
			}
			System.out.println("\nAvailable in " + category + ": " + availableCount + "/" + catSeats.size());
		}

		long totalAvailable = seats.stream().filter(s -> !s.isSeatStatus()).count();
		System.out.println("\nTotal Available Seats: " + totalAvailable + "/" + seats.size());
	}

	public List<Seat> selectSeats(Show show) {
		Scanner sc = new Scanner(System.in);
		List<Seat> seats = show.getSeats();
		List<Seat> selectedSeats = new ArrayList<>();

		while (true) {
			System.out.println("Enter seat numbers to book (comma separated, e.g., A1,A2,B5):");
			String input = sc.nextLine().trim();
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
			switch (seat.getSeatType()) {
			case "Premium":
				total += 250;
				break;
			case "Regular":
				total += 180;
				break;
			case "Economy":
				total += 120;
				break;
			}
		}
		return total;
	}

	public void checkTicketStatus(String timeZone, User user) {
		System.out.print("Enter your Ticket ID: ");
		long ticketId = Input.getLong((long) ticketDB.size());
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
		System.out.printf("Status        : %s%n", ticket.isActive() ? "Active" : "Cancelled");
		System.out.printf("Show Complete : %s%n", ticket.isComplete() ? "No" : "Yes");
		System.out.println("========================================================\n");
	}

	public void viewMyBooking(User currentUser, String timeZone) {
		List<Ticket> userTickets = ticketDB.values().stream().filter(t -> t.getUser().equals(currentUser))
				.collect(Collectors.toList());

		if (userTickets.isEmpty()) {
			System.err.println("You have no bookings.");
			return;
		}

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");
		System.out.printf(
				"+----+----------------------+----------------------+--------+----------------------+-----------+-----------+%n");
		System.out.printf(
				"| ID | Movie                | Theatre              | Screen | Show Date & Time     | Active    | Status    |%n");
		System.out.printf(
				"+----+----------------------+----------------------+--------+----------------------+-----------+-----------+%n");

		for (Ticket ticket : userTickets) {
			ZonedDateTime zdt = Instant.ofEpochSecond(ticket.getShow().getDateTimeEpoch()).atZone(ZoneId.of(timeZone));

			String active = ticket.isActive() ? "Yes" : "No";
			String status = ticket.isComplete() ? "No" : "Completed";

			System.out.printf("| %-2d | %-20s | %-20s | %-6d | %-20s | %-9s | %-9s |%n", ticket.getTicketId(),
					ticket.getMovie().getMovieTitle(), ticket.getTheatre().getTheatreName(),
					ticket.getShow().getScreen().getScreenNumber(), zdt.format(formatter), active, status);
		}

		System.out.printf(
				"+----+----------------------+----------------------+--------+----------------------+-----------+-----------+%n");
	}

	public void cancelTicket(User currentUser) {
		System.out.print("Enter Ticket ID to cancel: ");
		long ticketId = Input.getLong((long) ticketDB.size());

		Ticket ticket = ticketDB.get(ticketId);
		if (ticket == null || !ticket.getUser().equals(currentUser)) {
			System.err.println("No ticket found with this ID for your account.");
			return;
		}

		if (!ticket.isActive()) {
			System.out.println("Ticket is already cancelled.");
			return;
		}

		ticket.setActive(false);

		for (Seat seat : ticket.getSeats()) {
			seat.setSeatStatus(false);
		}

		System.out.println("Ticket ID " + ticketId + " has been successfully cancelled.");
	}

}
