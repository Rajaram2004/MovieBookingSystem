package service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import model.Movies;
import model.Seat;
import model.Show;
import model.Theatre;
import model.Ticket;
import model.User;
import repository.InMemoryDatabase;
import util.Input;

public class TicketService {
	static HashMap<Long, Movies> movieDB = InMemoryDatabase.getMovieDB();

	public TicketService() {

	}

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

	private Long ticketId;
	private User user;
	private Movies movie;
	private Theatre theatre;
	private Show show;
	private List<Seat> seats;
	private int bookingDateEpoch;
	private double totalAmount;
	private boolean isActive;
	private boolean isComplete;

	public void markBooked(List<Seat> selectSeat) {
		for (Seat seat : selectSeat) {
			seat.setSeatStatus(true);
		}

	}

	public void printAllMovies() {
		// Table format
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
		printAllMovies(); // prints movies in a table
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

				if (seat.isSeatStatus()) { // booked
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

}
