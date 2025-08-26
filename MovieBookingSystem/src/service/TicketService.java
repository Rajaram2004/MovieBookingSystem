package service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import model.Movies;
import model.Screen;
import model.Seat;
import model.Show;
import model.Theatre;
import model.TheatreAdmin;
import model.Ticket;
import model.Customer;
import repository.InMemoryDatabase;
import util.Input;

public class TicketService {
	static HashMap<Long, Movies> movieDB = InMemoryDatabase.getMovieDB();
	static HashMap<Long, Ticket> ticketDB = InMemoryDatabase.getTicketDB();
	static HashMap<Long, Theatre> theatreDB = InMemoryDatabase.getTheatreDB();

	public TicketService() {

	}

	// ------------------------------------------------------------------------------------------------------------------------------------------------------------------

	public void bookTicket(Customer user, String timeZone, HashMap<Long, Ticket> ticketDB) {

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
		List<Seat> selectSeat = selectSeat(show);
		System.out.println("Selected Seat are");
		Collections.sort(selectSeat,Comparator.comparing(Seat::getSeatNumber));
		for (Seat se : selectSeat) {
			System.out.print(se.getSeatNumber()+" ");
		}
		System.out.println();
		if (selectSeat == null) {
			System.out.println("------Back------");
			return;
		}
		double amount = calculateAmount(selectSeat);
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
			markBooked(selectSeat, false);
			System.out.println("------Back------");
			return;
		}
		markBooked(selectSeat, true);
		currBalance -= amount;
		user.setBalance(currBalance);
		System.out.println("New Available Balance is " + user.getBalance());
		Ticket ticket = new Ticket((long) (ticketDB.size() + 1), user, show, selectSeat,
			 amount, "Upcoming");
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

	// ----don't delete
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
		
		List<Show> show = theatre.getListOfShow();
		List<Long> list = new ArrayList<>();
		for (Show sh : show) {
			list.add(sh.getMovie().getMovieId());
		}
		for (Movies m : movieDB.values()) {
			if (list.contains(m.getMovieId())) {

				int hours = m.getDuration() / 60;
				int minutes = m.getDuration() % 60;
				String durationFormatted = String.format("%d hr %02d min", hours, minutes);

				System.out.format(format, m.getMovieId(), m.getMovieTitle(), durationFormatted, m.getGenre(),
						m.getLanguage(), m.getReleaseYear());

			}

		}
		System.out.println(
				"+------+-----------------------------+------------+------------+------------+--------+--------------------------------+");
	}

	public void markBooked(List<Seat> selectSeat, boolean bool) {
		for (Seat seat : selectSeat) {
			seat.setBooked(bool);
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

		String format = "| %-4s | %-27s | %-12s | %-12s | %-12s | %-6s |%n";

		// Print table header
		System.out.println(
				"+------+-----------------------------+--------------+--------------+--------------+--------+");
		System.out.format(format, "ID", "Title", "Duration", "Genre", "Language", "Year");
		System.out.println(
				"+------+-----------------------------+--------------+--------------+--------------+--------+");

		for (Movies m : movieDB.values()) {

			int hours = m.getDuration() / 60;
			int minutes = m.getDuration() % 60;
			String durationFormatted = String.format("%d hr %02d min", hours, minutes);

			System.out.format(format, m.getMovieId(), m.getMovieTitle(), durationFormatted, m.getGenre(),
					m.getLanguage(), m.getReleaseYear());

		}

		System.out.println(
				"+------+-----------------------------+--------------+--------------+--------------+--------+");
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

	public List<Theatre> getTheatresByMovie(Movies movie) {
		List<Theatre> availableTheatres = new ArrayList<>();

		for (Theatre theatre : theatreDB.values()) {
			if (theatre.getListOfMovies().contains(movie)) {
				availableTheatres.add(theatre);
			}
		}

		return availableTheatres;
	}

	public Theatre selectTheatreForMovie(Movies movie) {
//		List<Theatre> theatres = theatreDB.values().stream().filter(m->m.getListOfMovies().contains(movie)).collect(Collectors.toList());

		List<Theatre> theatres = getTheatresByMovie(movie);
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
			if (s.isActive().equalsIgnoreCase("upcoming") && s.getMovie().getMovieId().equals(movie.getMovieId())) {
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
			if (show.isActive().equalsIgnoreCase("upcoming")) {
				ZonedDateTime zdt = Instant.ofEpochSecond(show.getDateTimeEpoch()).atZone(ZoneId.of(timeZone));
				String formattedDateTime = zdt.format(formatter);

				System.out.printf("| %-3d | %-25s | %-20s | %-14s | %-20s | %-13s |\n", count++, formattedDateTime,
						show.getTheatre().getTheatreName(), show.getTheatre().getTheatreLocation(),
						show.getMovie().getMovieTitle(), show.getMovie().getLanguage());
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
		show.displayLayout();
	}

	public List<Seat> selectSeat(Show show) {
		Scanner sc = new Scanner(System.in);
		List<Seat> bookedSeats = new ArrayList<>();

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

				for (List<Seat> row : show.getShowSeats()) {
					for (Seat seat : row) {
						if (seat.getSeatNumber().equalsIgnoreCase(seatNumber)) {
							seatFound = true;

							if (seat.isBooked()) {
								System.out.println("⚠ Seat " + seatNumber + " is already booked!");
							} else {
								seat.setBooked(true);
								bookedSeats.add(seat);
								System.out.println("Seat " + seatNumber + " Selected successfully!");
							}
							break;
						}
					}
					if (seatFound)
						break;
				}
				if (!seatFound) {
					System.err.println("Seat " + seatNumber + " not found!");
				}
			}

		}
	}

	public double calculateAmount(List<Seat> selectedSeats) {
		double total = 0;
		for (Seat seat : selectedSeats) {
			total += seat.getPrice();
		}
		return total;
	}

	public void checkTicketStatus(String timeZone, Customer user) {
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
		System.out.printf("Status        : %s%n", ticket.getStatus());

		System.out.println("========================================================\n");
	}

	public void TicketStatus(Ticket ticket, String timeZone, Customer user) {

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

	public void viewMyBooking(Customer currentUser, String timeZone) {
		
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
	public void changeTicketStatus() {
		for(Ticket ticket:ticketDB.values()) {
			if(ticket.getStatus().equalsIgnoreCase("upcoming")){
				if(ticket.getBookingDateEpoch()<System.currentTimeMillis()/1000) {
					ticket.setStatus("Completed");
				}
			}
		}
	}

	public void cancelTicket(Customer currentUser) {
		System.out.println("----------- Ticket Cancellation Policy -----------");
		System.out.println("1. Cancel 24 hours before the show → 75% refund");
		System.out.println("2. Cancel between 2 hours and 24 hours before → 50% refund");
		System.out.println("3. Cancel between 20 minutes and 2 hours before → 25% refund");
		System.out.println("4. Less than 20 minutes before the show → Cancellation not allowed");
		System.out.println("--------------------------------------------------");

		List<Ticket> upcomingTicket = displayUpcomingTicket(currentUser);
		if(upcomingTicket==null) return;

		System.out.print("Enter Ticket ID to cancel (or Type '0' to Exit): ");
		long size = (long) ticketDB.size();
		if (size == 0) {
			System.err.println("No Tickets Available for cancel");
			return;
		}
		long ticketId;
		while(true) {
			ticketId = Input.getLong(size);
			if (ticketId == 0) {
				System.out.println("------Back------");
				return;
			}
			boolean found=false;
			for( Ticket t:upcomingTicket ) {
				if(t.getTicketId()==ticketId) {
					found=true;
				}
			}
			if(found==true)break;
		}
		
		
		Ticket ticket = ticketDB.get(ticketId);
		if (ticket == null || !ticket.getUser().equals(currentUser)) {
			System.err.println("No ticket found with this ID for your account.");
			return;
		}
		long currentTime = System.currentTimeMillis(); // Current time in ms
		long showTimeMillis = ticket.getBookingDateEpoch() * 1000L; // Convert epoch seconds to ms

		long diff = showTimeMillis - currentTime; // Time difference in ms
		double reducePercentage = 0.0;

		if (diff > 24 * 60 * 60 * 1000L) {
		    System.out.println("Cancellation: More than 24 hours before the show → 25% deduction.");
		    reducePercentage = 0.25;
		} 
		else if (diff <= 2 * 60 * 60 * 1000L && diff > 20 * 60 * 1000L) {
		    System.out.println("Cancellation: Between 2 hours and 20 minutes before the show → 75% deduction.");
		    reducePercentage = 0.75;
		} 
		else if (diff <= 20 * 60 * 1000L && diff > 0) {
		    System.out.println("Cancellation: Less than 20 minutes before the show → No refund.");
		    reducePercentage = 1.0; // No refund
		} 
		else if (diff <= 0) {
		    System.err.println("Show time has already started or expired. Cancellation not allowed.");
		    return;
		} 
		else {
		    System.out.println("Cancellation: Between 24 hours and 2 hours before the show → 50% deduction.");
		    reducePercentage = 0.50;
		}


		if (ticket.getStatus().equalsIgnoreCase("cancelled")) {
			System.err.println("Ticket is already cancelled.");
			return;
		}
		if (ticket.getStatus().equalsIgnoreCase("Refunded")) {
			System.err.println("Show Cancelled so ticket's cancelled and Refunded");
			return;
		}

		ticket.setStatus("cancelled");

		for (Seat seat : ticket.getSeats()) {
			seat.setBooked(false);
		}

		double totalAmount = ticket.getTotalAmount();
		double detectAmount = totalAmount * reducePercentage;
		double finalAmount = totalAmount - detectAmount;
		double userAmount = currentUser.getBalance();
		userAmount += finalAmount;
		currentUser.setBalance(userAmount);
		System.out.println(reducePercentage*100+"% deducted from your ticket price. Remaining Amount credited to user");
		System.out.println("Ticket ID " + ticketId + " has been successfully cancelled.");
	}

	private List<Ticket> displayUpcomingTicket(Customer currentUser) {
		boolean found = false;

		String line = "+-------+----------------------+-----------------+---------------+----------------------+------------+------------+";
		String headerFormat = "| %-5s | %-20s | %-15s | %-13s | %-20s | %-10s | %-10s |\n";
		String rowFormat = "| %-5d | %-20s | %-15s | %-13s | %-20s | %-10s | %-10.2f |\n";

		System.out.println("\n" + line);
		System.out.printf(headerFormat, "ID", "Movie", "Theatre", "City", "Show Date & Time", "Seats", "Price");
		System.out.println(line);
		List<Ticket> upcomingTicket =new ArrayList<>();
		for (Ticket ticket : ticketDB.values()) {
			if (ticket.getUser() == currentUser 
				    && ticket.getBookingDateEpoch() > System.currentTimeMillis()/1000
				    && "upcoming".equalsIgnoreCase(ticket.getStatus().trim())) {
				   
			
				upcomingTicket.add(ticket);
				found = true;

				List<String> seatNumbers= ticket.getSeats().stream().map(s->s.getSeatNumber()) 
								.collect(Collectors.toList());
				
				String dateTime = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm")
						.format(new java.util.Date(ticket.getBookingDateEpoch()*1000));

				System.out.printf(rowFormat, ticket.getTicketId(), ticket.getMovie().getMovieTitle(),
						ticket.getTheatre().getTheatreName(), ticket.getTheatre().getTheatreLocation(), dateTime,
						seatNumbers, ticket.getTotalAmount());
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
		List<Show> show = theatre.getListOfShow();
		List<Long> list = new ArrayList<>();
		for (Show sh : show) {
			list.add(sh.getMovie().getMovieId());
		}
		for (Movies m : movieDB.values()) {

			if (list.contains(m.getMovieId())) {

				int hours = m.getDuration() / 60;
				int minutes = m.getDuration() % 60;
				String durationFormatted = String.format("%d hr %02d min", hours, minutes);

				System.out.format(format, m.getMovieId(), m.getMovieTitle(), durationFormatted, m.getGenre(),
						m.getLanguage(), m.getReleaseYear());
			}

		}
		System.out.println(
				"+------+-----------------------------+------------+------------+------------+--------+--------------------------------+");
	}

	public void displayScreen(TheatreAdmin theatreAdmin) {
		TheatreAdminService tas = new TheatreAdminService();
		Theatre theatre = tas.selectTheatre(theatreAdmin.getTheatre());
		if(theatre==null)return;
		List<Screen> listOfScreen = theatre.getListOfScreen();
		List<Integer> list = new ArrayList<>();
		for (Screen s : listOfScreen) {
			if (s.isActive() == true) {
				list.add(s.getScreenNumber());
			}

		}
		System.out.println(list);
	}

	public void bookTicketViaTheatre(List<Movies> listOfMovies, Theatre theatreObj, Customer user, String timeZone) {

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
		List<Seat> selectSeat = selectSeat(show);
		if (selectSeat == null) {
			System.out.println("------Back------");
			return;
		}
		double amount = calculateAmount(selectSeat);
		long bookedEpoch = show.getDateTimeEpoch();
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
			markBooked(selectSeat, false);
			System.out.println("------Back------");
			return;
		}
		markBooked(selectSeat, true);
		currBalance -= amount;
		user.setBalance(currBalance);
		System.out.println("New Available Balance is " + user.getBalance());
		Ticket ticket = new Ticket((long) (ticketDB.size() + 1), user, show, selectSeat,
				amount, "Upcoming");
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
		HashMap<Long, Movies> movieDb = new HashMap<>();
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
		long movieId = 0;
		while (true) {
			System.out.println("Enter the Movie id (or type '0' to Exit):");
			movieId = Input.getLong((long) 1000000);
			if (movieId == 0) {
				return null;
			}
			if (movieDb.containsKey(movieId)) {
				return movieDb.get(movieId);
			} else {
				System.err.println("Invalid Movie Id");
			}
		}
	}

}
