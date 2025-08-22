package main;

import java.util.List;

import model.Movies;
import model.Show;
import model.Theatre;
import model.TheatreAdmin;
import service.TheatreAdminService;
import service.TicketService;
import util.Input;
import util.TimeZoneConverter;

public class TheatreAdminOperations {
	static TicketService ticketServiceObj = new TicketService();

	public static void theatreAminOperations(TheatreAdmin theatreAdmin) {
		TheatreAdminService theatreAdminService = new TheatreAdminService();
		int choice = theatreAdminFeatures();
		String timeZone = theatreAdmin.getTimeZone();
		switch (choice) {
		case 1:
			System.out.println("You have Selected Add Show");
			theatreAdminService.addShow(theatreAdmin);
			theatreAminOperations(theatreAdmin);
			break;
		case 2:
			System.out.println("You have Selected Print All Shows In This Theatre");
			theatreAdminService.printShows(theatreAdmin, timeZone);
			theatreAminOperations(theatreAdmin);
			break;
		case 3:
			System.out.println("You have Selected Display Seat Availability");
			theatreAdminSeatAvailability(theatreAdmin, theatreAdminService, timeZone);
			theatreAminOperations(theatreAdmin);
			break;
		case 4:
			System.out.println("You have Selected Add Screen");
			theatreAdminService.addScreen(theatreAdmin);
			theatreAminOperations(theatreAdmin);
			break;
		case 5:
			System.out.println("You have Selected Edit Theatre Admin Details");
			theatreAdminService.editTheatreAdminDetails(theatreAdmin);
			theatreAminOperations(theatreAdmin);
			break;
		case 6:
			System.out.println("You have Selected Print future Shows");
			theatreAdminService.printShowFuture(theatreAdmin, timeZone);
			theatreAminOperations(theatreAdmin);
			break;
		case 7:
			System.out.println("You have Selected Change Time Zone");
			timeZone = TimeZoneConverter.selectTimeZone();
			if (timeZone != null) {
				theatreAdmin.setTimeZone(timeZone);
				System.out.println("Time Zone Chnaged");
			}
			theatreAminOperations(theatreAdmin);
			break;
		case 8:
			System.out.println("You have Selected Request New Theatre");
			theatreAdminService.addNewTheatre(theatreAdmin);
			theatreAminOperations(theatreAdmin);
			break;
		case 9:
			System.out.println("You have Selected Delete Screen");
			theatreAdminService.deleteScreen(theatreAdmin);
			theatreAminOperations(theatreAdmin);
			break;
		case 10:
			System.out.println("You have Selected Cancel Show");
			theatreAdminService.cancelShow(theatreAdmin);
			theatreAminOperations(theatreAdmin);
			break;
		case 11:
			System.out.println("You have Selected Display All Movies");
			ticketServiceObj.printAllMovies();
			theatreAminOperations(theatreAdmin);
			break;
		case 12:
			System.out.println("You have Selected Display Screen");
			ticketServiceObj.displayScreen(theatreAdmin);
			theatreAminOperations(theatreAdmin);
			break;
		case 13:
			System.out.println("You have Selected Check Request Status");
			theatreAdminService.checkRequestStatus(theatreAdmin);
			theatreAminOperations(theatreAdmin);
			break;
		case 14:
			System.out.println("You have Selected Delete Theatre");
			theatreAdminService.deleteTheatre(theatreAdmin);
			theatreAminOperations(theatreAdmin);
			break;

		case 15:
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
				"3 . Display Display Seat Availability", "4 . Add Screen ", "5 . Edit Theatre Admin Details",
				"6 . Print future Shows", "7 . Change Time Zone", "8 . Request New Theatre", "9 . Delete Screen",
				"10. Cancel Show", "11. Display All Movies", "12. Display Screen","13. Check Request Status","14. Delete Theatre","15. Exit" };

		int n = features.length;
		for (int i = 0; i < n; i++) {
			System.out.println("  " + features[i]);
		}
		System.out.println("Enter Your Choice : ");
		return Input.getInteger(n);

	}

	public static void theatreAdminSeatAvailability(TheatreAdmin theatreAdmin, TheatreAdminService theatreAdminService,
			String timeZone) {
		if (theatreAdmin == null || theatreAdmin.getTheatre() == null) {
			System.err.println("No Theatre Available");
			return;
		}

		List<Theatre> theatreList = theatreAdmin.getTheatre();
		Theatre theatreObj = theatreAdminService.selectTheatre(theatreList);
		if (theatreObj == null) {
			System.out.println("------Back------");
			return;
		}

		Movies movieObj = ticketServiceObj.selectTheatreMovie1(theatreAdmin,theatreObj);
		if (movieObj == null) {
			System.out.println("------Back------");
			return;
		}

		List<Show> shows = ticketServiceObj.getShowsForMovieAndTheatre(movieObj, theatreObj);
		Show show = ticketServiceObj.getShow(shows, timeZone);
		if (show == null) {
			System.err.println("No show selected.");
		} else {
			ticketServiceObj.displaySeatsByCategory(show);
		}
	}

}
