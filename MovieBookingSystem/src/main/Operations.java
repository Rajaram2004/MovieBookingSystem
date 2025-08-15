package main;

import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import model.Movies;
import model.Show;
import model.Theatre;
import model.Ticket;
import model.User;
import service.MovieService;
import service.TheatreService;
import service.TicketService;
import util.Input;

public class Operations {

	public static void operation(User user, TicketService ticketServiceObj, MovieService movieServiceObj,
			TheatreService theatreServiceObj, String timeZone, HashMap<Long, Ticket> ticketDB) {
		int userChoice = Features();

		switch (userChoice) { 
		case 1:
			System.out.println("You have Selected display All Movie");
			ticketServiceObj.printAllMovies();
			Operations.operation(user, ticketServiceObj, movieServiceObj, theatreServiceObj, timeZone, ticketDB);
			break;
		case 2:
			System.out.println("You have Selected Search Movie By Name");
			movieServiceObj.searchByMovieName();
			Operations.operation(user, ticketServiceObj, movieServiceObj, theatreServiceObj, timeZone, ticketDB);
			break;
		case 3:
			System.out.println("You have Selected View Movie By Genre");
			movieServiceObj.viewMoviesByGenre();
			Operations.operation(user, ticketServiceObj, movieServiceObj, theatreServiceObj, timeZone, ticketDB);
			break;
		case 4:
			System.out.println("You have Selected Display All Theatre");
			theatreServiceObj.printAllTheatres();
			Operations.operation(user, ticketServiceObj, movieServiceObj, theatreServiceObj, timeZone, ticketDB);
			break;
		case 5:
			System.out.println("You have Selected Display Seat Availability");
			Movies movieObj = ticketServiceObj.selectMovie();
			Theatre theatreObj = ticketServiceObj.selectTheatreForMovie(movieObj);
			List<Show> shows = ticketServiceObj.getShowsForMovieAndTheatre(movieObj, theatreObj);
			Show show = ticketServiceObj.getShow(shows, timeZone);
			if (show == null) {
				System.err.println("Booking cancelled. No show selected.");
			}else {
				ticketServiceObj.displaySeatsByCategory(show);
			}
			Operations.operation(user, ticketServiceObj, movieServiceObj, theatreServiceObj, timeZone, ticketDB);
			break;
		case 6:
			System.out.println("You have Selected Book Ticket");
			ticketServiceObj.bookTicket(user, timeZone, ticketDB);
			Operations.operation(user, ticketServiceObj, movieServiceObj, theatreServiceObj, timeZone, ticketDB);
			break;
		case 7:
			System.out.println("You have Selected Check Ticket Status");
			ticketServiceObj.checkTicketStatus(timeZone,user);
			Operations.operation(user, ticketServiceObj, movieServiceObj, theatreServiceObj, timeZone, ticketDB);
			break;
		case 8:
			System.out.println("You have Selected View My Booking");
			ticketServiceObj.viewMyBooking(user,timeZone);
			Operations.operation(user, ticketServiceObj, movieServiceObj, theatreServiceObj, timeZone, ticketDB);
			break;
		case 9:
			System.out.println("You have Selected ,Cancel Ticket");
			ticketServiceObj.cancelTicket(user);
			Operations.operation(user, ticketServiceObj, movieServiceObj, theatreServiceObj, timeZone, ticketDB);
		case 10:
			System.out.println("You have Selected Filter Movies by Language");
			ticketServiceObj.filterMoviesByLanguage();
			Operations.operation(user, ticketServiceObj, movieServiceObj, theatreServiceObj, timeZone, ticketDB);
			break;
		case 11:
			System.out.println("\nYou have Selected Exit");
			System.out.println("----------------------------------------------------");
			System.out.println("Thanks for visiting. See you next time!");
			System.out.println("----------------------------------------------------");
			// System.exit(0);
			int choice = MainMenu.mainMenu();
			MainMenu.call(choice);
		}
	}

	public static int Features() {
		System.out.println("\n========= Welcome to Movie Booking System =========");
		System.out.println("Please select one of the following options:\n");

		String[] features = { "1 . View All Movies", "2 . Search Movie By Name", "3 . View Movie By Genre",
				"4 . Display All Theatre", "5 . Display Seat Availability", "6 . Book Ticket", "7 . Ticket Status",
				"8 . View Booking", "9 . Cancel Movie Ticket", "10. Filter Movie By Language",
				 "11. Exit" };

		int n = features.length;
		for (int i = 0; i < features.length; i++) {
			System.out.println("  " + features[i]);
		}

		System.out.println("\n====================================================\n");

		Scanner sc = Input.getScanner();
		System.out.print("Enter Your Choice : ");

		String input = sc.nextLine();

		try {
			int num = Integer.parseInt(input);
			if (num > 0 && num <= n) {
				return num;

			} else {
				System.err.println("Please enter a valid number.");
				return Features();
			}
		} catch (NumberFormatException e) {
			System.err.println("Not an integer.");
			return Features();
		}
	}

}
