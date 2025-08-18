package main;

import model.Admin;
import service.AdminService;
import service.TheatreAdminService;
import service.TheatreService;
import service.TicketService;
import util.Input;

public class AdminOperations {
	static TicketService ticketServiceObj = new TicketService();
	static TheatreService theatreServiceObj = new TheatreService();
	static TheatreAdminService theatreAdminServiceObj = new TheatreAdminService();

	AdminOperations() {
	}

	public static void adminOperations(Admin admin, String timeZone) {
		int adminChoice = adminFeatures();
		AdminService adminService = new AdminService();
		switch (adminChoice) {
		case 1:
			System.out.println("You Have Selected print All Bookings");
			adminService.viewAllUsers();
			adminOperations(admin, timeZone);
			break;
		case 2:
			System.out.println("You Have Selected Search User By ID");
			adminService.searchUserById();
			adminOperations(admin, timeZone);
			break;
		case 3:
			System.out.println("You Have Selected Search User By Name");
			adminService.searchUserByName();
			adminOperations(admin, timeZone);
			break;
		case 4:
			System.out.println("You Have Selected View All Theatre Admins");
			adminService.viewAllTheatreAdmins();
			adminOperations(admin, timeZone);
			break;
		case 5:
			System.out.println("You Have Selected Search Theatre AdminBy Id");
			adminService.searchTheatreAdminById();
			adminOperations(admin, timeZone);
			break;
		case 6:
			System.out.println("You Have Selected Search Theatre Admin By Name");
			adminService.searchTheatreAdminByName();
			adminOperations(admin, timeZone);
			break;
		case 7:
			System.out.println("You Have Selected Add New Movie");
			adminService.addMovie();
			adminOperations(admin, timeZone);
			break;
		case 8:
			System.out.println("You Have Selected Add Theatre Admin ");
			adminService.addTheatreAdmin();
			adminOperations(admin, timeZone);
			break;
		case 9:
			System.out.println("You Have Selected Print All Shows ");
			adminService.printAllShows(timeZone);
			adminOperations(admin, timeZone);
			break;
		case 10:
			System.out.println("You Have Selected Print All Movies ");
			ticketServiceObj.printAllMovies();
			adminOperations(admin, timeZone);
			break;
		case 11:
			System.out.println("You Have Selected Print All Theatres ");
			theatreServiceObj.printAllTheatres();
			adminOperations(admin, timeZone);
			break;

		case 12:
			System.out.println("You Have Selected Search Show By Id ");
			theatreAdminServiceObj.searchShowId(timeZone);
			adminOperations(admin, timeZone);
			break;
		case 13:
			System.out.println("You Have Selected Edit Movie Details ");
			adminService.editMovieDetails();
			adminOperations(admin, timeZone);
			break;
		case 14:
			System.out.println("You Have Selected Edit Movie Details ");
			theatreAdminServiceObj.printShowAllFuture(timeZone);
			adminOperations(admin, timeZone);
			break;
		case 15:
			System.out.println("You Have Selected Add New Theatre ");
			theatreAdminServiceObj.addNewTheatre();
			adminOperations(admin, timeZone);
			break;
		case 16:
			System.out.println("You Have Selected Exit ");
			int choice = MainMenu.mainMenu();
			MainMenu.call(choice);
			break;
		}
	}

	public static int adminFeatures() {
		System.out.println("\n========= Welcome to Movie Booking System Admin =============");
		System.out.println("Please select one of the following options:\n");

		String[] features = { "1 . View All Users ", "2 . Search User By Id ", "3 . Search User By Name",
				"4 . View All Theatre Admins", "5 . Search Theatre Admin By Id", "6 . Search Theatre Admin By Name",
				"7 . add New Movie", "8 . Add Theatre Admin ", "9 . Print All Shows", "10. Print All Movies",
				"11. Print All Theatres ", "12. Search Show By Id", "13. Edit Movie Details",
				"14. Display All Future Show", "15. Add New Theatre", "16. Exit" };

		int n = features.length;
		for (int i = 0; i < n; i++) {
			System.out.println("  " + features[i]);
		}

		System.out.println("\n===========================================================\n");
		System.out.println("Enter Your Choice : ");
		return Input.getInteger(n);
	}

}
