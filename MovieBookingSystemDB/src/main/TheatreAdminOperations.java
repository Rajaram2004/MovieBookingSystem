package main;

import java.sql.Connection;
import java.sql.SQLException;

import dao.ShowDAO;
import dao.TicketDAO;
import dao.UserDAO;
import model.TheatreAdmin;
import service.PrinterService;
import service.TheatreAdminService;
import service.TicketService;
import util.Input;
import util.TimeZoneConverter;

public class TheatreAdminOperations {
	static TicketService ticketServiceObj = new TicketService();
	static PrinterService printerServiceObj = new PrinterService();
	static TheatreAdminService theatreAdminService = new TheatreAdminService();

	public static void theatreAminOperations(TheatreAdmin theatreAdmin) {

		PrinterService PrinterServiceObj = new PrinterService();
		int choice = theatreAdminFeatures();
		String timeZone = theatreAdmin.getTimeZone();
		TicketDAO.updateCompletedTickets();
		ShowDAO.changeShowStatus();
		TheatreAdminService.updateMovies();
		switch (choice) {
		case 1:
			System.out.println("You have Selected Add Show");
			theatreAdminService.addShow(theatreAdmin);
			theatreAminOperations(theatreAdmin);
			break;
		case 2:
			System.out.println("You have Selected Print All Shows In This Theatre");
			Connection conn2 = Input.getConnection();
			theatreAdminService.printShows(conn2, theatreAdmin, timeZone);
			theatreAminOperations(theatreAdmin);
			break;
		case 3:
			System.out.println("You have Selected Display Seat Availability");
			theatreAdminService.theatreAdminSeatAvailability(theatreAdmin, theatreAdminService, timeZone);
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
				Connection conn7 = Input.getConnection();
				UserDAO.updateUserTimeZone(conn7, theatreAdmin.getId(), timeZone);
				try {
					conn7.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				System.out.println("Time Zone Changed");
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
			Connection conn11 = Input.getConnection();
			PrinterServiceObj.printAllMovies1(conn11,1);
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
			System.out.println("You have Selected Display Running Show");
			theatreAdminService.printRunningShow(theatreAdmin, theatreAdmin.getTimeZone());
			theatreAminOperations(theatreAdmin);
			break;
		case 16:
			System.out.println("You have selected to view seat allocations for past shows.");
			theatreAdminService.viewSeatAllocationForPastShows(theatreAdmin);
			theatreAminOperations(theatreAdmin);
			break;
		case 17:
			System.out.println("You have Selected Exit");
			int choice1 = MainMenu.mainMenu();
			try {
				MainMenu.call(choice1);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case 0:
			System.out.println("You have Selected Exit");
			int choice0 = MainMenu.mainMenu();
			try {
				MainMenu.call(choice0);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		}
	}

	public static int theatreAdminFeatures() {
		System.out.println("\n========= Welcome to Theatre Admin Operations =============");
		System.out.println("Please select one of the following options:\n");
		String[] features = { "1 . Add Show ", "2 . Print All Shows In This Theatre",
				"3 . Display Display Seat Availability", "4 . Add Screen ", "5 . Edit Theatre Admin Details",
				"6 . Print future Shows", "7 . Change Time Zone", "8 . Request New Theatre", "9 . Delete Screen",
				"10. Cancel Show", "11. Display All Movies", "12. Display Screen", "13. Check Request Status",
				"14. Delete Theatre", "15. Display Running Show", "16. view Seat Allocation For Past Shows",
				"17. Exit" };
		int n = features.length;
		for (int i = 0; i < n; i++) {
			System.out.println("  " + features[i]);
		}
		System.out.println("Enter Your Choice : ");
		return Input.getInteger(n);
	}

}
