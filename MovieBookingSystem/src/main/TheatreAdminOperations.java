package main;

import java.util.List;

import model.Movies;
import model.Show;
import model.Theatre;
import model.TheatreAdmin;
import service.TheatreAdminService;
import service.TicketService;
import util.Input;

public class TheatreAdminOperations {
	static TicketService ticketServiceObj = new TicketService();

	public static void theatreAminOperations(TheatreAdmin theatreAdmin, String timeZone) {
		TheatreAdminService theatreAdminService = new TheatreAdminService();
		int choice = theatreAdminFeatures();
		switch (choice) {
		case 1:
			System.out.println("You have Selected Add Show");
			theatreAdminService.addShow(theatreAdmin);
			theatreAminOperations(theatreAdmin, timeZone);
			break;
		case 2:
			System.out.println("You have Selected Print All Shows In This Theatre");
			theatreAdminService.printShows(theatreAdmin, timeZone);
			theatreAminOperations(theatreAdmin, timeZone);
			break;
		case 3:
			System.out.println("You have Selected Display Seat Availability");
			Movies movieObj = ticketServiceObj.selectTheatreMovie(theatreAdmin);
			Theatre theatreObj = theatreAdmin.getTheatre();
			List<Show> shows = ticketServiceObj.getShowsForMovieAndTheatre(movieObj, theatreObj);
			Show show = ticketServiceObj.getShow(shows, timeZone);
			if (show == null) {
				System.err.println("Booking cancelled. No show selected.");
			} else {
				ticketServiceObj.displaySeatsByCategory(show);
			}
			theatreAminOperations(theatreAdmin, timeZone);
			break;
		case 4:
			System.out.println("You have Selected Add Screen");
			theatreAdminService.addScreen(theatreAdmin);
			theatreAminOperations(theatreAdmin, timeZone);
			break;
		case 5:
			System.out.println("You have Selected Edit Theatre Admin Details");
			theatreAdminService.editTheatreAdminDetails(theatreAdmin);
			theatreAminOperations(theatreAdmin, timeZone);
			break;
		case 6:
			System.out.println("You have Selected Print future Shows");
			theatreAdminService.printShowFuture(theatreAdmin,timeZone);
			theatreAminOperations(theatreAdmin, timeZone);
			break;
			
		case 7:
			System.out.println("You have Selected Exit");
			int choice1 = MainMenu.mainMenu();
			MainMenu.call(choice1);
			break;
		}
	}

	public static int theatreAdminFeatures() {

		System.out.println("\n========= Welcome to Theatre Admin Operations =============");
		System.out.println("Please select one of the following options:\n");

		String[] features = { "1 . Add Show ", "2 . Print All Shows In This Theatre",
				"3 . Display Display Seat Availability", "4 . Add Screen ", "5 . Edit Theatre Admin Details","6 . Print future Shows" ,"7 . Exit"};

		int n = features.length;
		for (int i = 0; i < n; i++) {
			System.out.println("  " + features[i]);
		}
		System.out.println("Enter Your Choice : ");
		return Input.getInteger(n);

	}

}
