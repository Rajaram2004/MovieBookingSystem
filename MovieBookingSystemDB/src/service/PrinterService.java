package service;

import java.sql.Connection;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;

import dao.MovieDAO;
import dao.ScreenDAO;
import model.Movies;
import model.Show;
import model.Theatre;
import model.Ticket;
import model.User;
import util.Helper;
import util.Input;

public class PrinterService {
	static TicketService ticketServiceObj = new TicketService();

	public List<Movies> printAllMovies1(Connection conn,long active) {
		List<Movies> movieList = Helper.getActiveMovie(conn,active);
		String format = "| %-4s | %-27s | %-12s | %-12s | %-12s | %-6s |%n";

		System.out.println(
				"+------+-----------------------------+--------------+--------------+--------------+--------+");
		System.out.format(format, "ID", "Title", "Duration", "Genre", "Language", "Year");
		System.out.println(
				"+------+-----------------------------+--------------+--------------+--------------+--------+");
		for (Movies m : movieList) {
			int hours = m.getDuration() / 60;
			int minutes = m.getDuration() % 60;
			String durationFormatted = String.format("%d hr %02d min", hours, minutes);

			System.out.format(format, m.getMovieId(), m.getMovieTitle(), durationFormatted, m.getGenre(),
					m.getLanguage(), m.getReleaseYear());
		}

		System.out.println(
				"+------+-----------------------------+--------------+--------------+--------------+--------+");
		return movieList;
	}
	public List<Movies> printAllMovies(Connection conn) {
		List<Movies> movieList = Helper.getMovieList(conn);
		movieList.sort(Comparator.comparing(Movies::getMovieId));
		String format = "| %-4s | %-27s | %-12s | %-12s | %-12s | %-6s |%n";
		System.out.println(
				"+------+-----------------------------+--------------+--------------+--------------+--------+");
		System.out.format(format, "ID", "Title", "Duration", "Genre", "Language", "Year");
		System.out.println(
				"+------+-----------------------------+--------------+--------------+--------------+--------+");

		for (Movies m : movieList) {
			int hours = m.getDuration() / 60;
			int minutes = m.getDuration() % 60;
			String durationFormatted = String.format("%d hr %02d min", hours, minutes);
			System.out.format(format, m.getMovieId(), m.getMovieTitle(), durationFormatted, m.getGenre(),
					m.getLanguage(), m.getReleaseYear());
		}
		System.out.println(
				"+------+-----------------------------+--------------+--------------+--------------+--------+");
		return movieList;
	}

	public Show getShow(List<Show> shows, String timeZone) {
		try {
		if (shows == null || shows.isEmpty()) {
			System.out.println("No shows available for this selection.");
			return null;
		}
		shows.sort(Comparator.comparingLong(Show::getShowDateTime));
		int count = 1;
		System.out.println(
				"+----+---------------------------+----------------------+----------------+----------------------+---------------+");
		System.out.println(
				"| ID |         Date & Time       | Theatre Name         | City           |      Movie Title     |   Language    |");
		System.out.println(
				"+----+---------------------------+----------------------+----------------+----------------------+---------------+");

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");

		for (Show show : shows) {
			ZonedDateTime zdt = Instant.ofEpochMilli(show.getShowDateTime()).atZone(ZoneId.of(timeZone));
			String formattedDateTime = zdt.format(formatter);
			System.out.printf("| %-3d | %-25s | %-20s | %-14s | %-20s | %-13s |\n", count++, formattedDateTime,
					show.getTheatre().getTheatreName(), show.getTheatre().getTheatreLocation(),
					show.getMovie().getMovieTitle(), show.getMovie().getLanguage());
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
		catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void printTicket(Connection conn, Ticket ticket, User user) {
		List<String> seatList = ScreenDAO.getSeat(conn, ticket.getTicketId());
		System.out.println("\n----------- Ticket Details -----------");
		System.out.println("Ticket ID       : " + ticket.getTicketId());
		System.out.println("User Name       : " + ticket.getUser().getName());
		System.out.println(
				"Show Time       : " + formatBookingDate(ticket.getShow().getShowDateTime(), user.getTimeZone()));
		System.out.println("Screen Name     : " + ticket.getShow().getScreen().getScreenName());
		System.out.println("Amount Paid     : " + ticket.getAmount());
		System.out.println("Booked On       : " + formatBookingDate(ticket.getTicketBookedDate(), user.getTimeZone()));
		System.out.println("Show Status     : " + ticket.getStatus());
		System.out.println("Booked Seats    : " + seatList);
		System.out.println("\n-------------------------------------");
	}

	public static String formatBookingDate(long bookedDateMillis, String timeZone) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		return Instant.ofEpochMilli(bookedDateMillis).atZone(ZoneId.of(timeZone)).format(formatter);
	}

	public Theatre selectTheatreForMovie(Connection conn, Movies movie) {
		List<Theatre> theatres = ticketServiceObj.getTheatresByMovie(conn, movie);
		if (theatres.isEmpty()) {
			System.err.println("No theatre Available");
			return null;
		}
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
		Theatre selectedTheatre = theatres.get(choice - 1);
		System.out.println(
				"You have selected: " + selectedTheatre.getTheatreName() + ", " + selectedTheatre.getTheatreLocation());
		return selectedTheatre;
	}

	public void printAllTicket(Connection conn, List<Ticket> userTickets, User currentUser) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");
		System.out.printf(
				"+----+----------------------+----------------------+-------------+--------------------------+-----------------------+----------------------+%n");
		System.out.printf(
				"| ID | Movie                | Theatre              | Screen      | Show Date & Time         |  Status               | Seats                |%n");
		System.out.printf(
				"+----+----------------------+----------------------+-------------+--------------------------+-----------------------+----------------------+%n");
		for (Ticket ticket : userTickets) {
			ZonedDateTime zdt = Instant.ofEpochMilli(ticket.getShow().getShowDateTime())
					.atZone(ZoneId.of(currentUser.getTimeZone()));
			String formattedDate = zdt.format(formatter);
			String active = ticket.getStatus();
			List<String> seatNumber = ScreenDAO.getSeat(conn, ticket.getTicketId());
			System.out.printf("| %-2d | %-20s | %-20s | %-11s | %-24s | %-20s | %-20s |%n", ticket.getTicketId(),
					ticket.getShow().getMovie().getMovieTitle(), ticket.getShow().getTheatre().getTheatreName(),
					ticket.getShow().getScreen().getScreenName(), formattedDate, active, seatNumber);
		}
		System.out.printf(
				"+----+----------------------+----------------------+-------------+--------------------------+-----------------------+----------------------+%n");
	}

	public void filterMoviesByLanguage() {
		Connection conn = Input.getConnection();
		List<Movies> movieList = Helper.getMovieList(conn);
		LinkedHashSet<String> languages = new LinkedHashSet<>();
		for (Movies m : movieList) {
			languages.add(m.getLanguage());
		}
		int count = 1;
		System.out.println("Available Languages:");
		System.out.println("+--------+----------------+");
		System.out.printf("| %-6s | %-14s |\n", "S.No", "Language");
		System.out.println("+--------+----------------+");
		for (String lan : languages) {
			System.out.printf("| %-6d | %-14s |\n", count, lan);
			count++;
		}
		System.out.println("+--------+----------------+");
		System.out.print("Enter Your Choice (or Type '0' to Exit): ");
		int choice = Input.getInteger(count - 1);
		if (choice == 0) {
			System.out.println("------Back------");
			return;
		}
		String selectedLanguage = null;
		int index = 1;
		for (String str : languages) {
			if (index == choice) {
				selectedLanguage = str;
				break;
			}
			index++;
		}
		if (selectedLanguage == null) {
			System.out.println("Invalid choice.");
			return;
		}
		System.out.println("+----+-----------------------------+----------------+--------+");
		System.out.println("| ID | Title                       | Duration       | Year   |");
		System.out.println("+----+-----------------------------+----------------+--------+");
		for (Movies m : movieList) {
			if (m.getLanguage().equalsIgnoreCase(selectedLanguage)) {
				int duration = m.getDuration();
				int hours = duration / 60;
				int minutes = duration % 60;
				String dur = hours + " hr " + minutes + " min";

				System.out.printf("| %-2d | %-27s | %-14s | %-6d |\n", m.getMovieId(), m.getMovieTitle(), dur,
						m.getReleaseYear());
			}
		}

		System.out.println("+----+-----------------------------+----------------+--------+");
	}

}
