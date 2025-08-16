package main;

import model.Admin;
import service.AdminService;
import util.Input;

public class AdminOperations {
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
				"7 . add New Movie","8 . Exit" };

		int n = features.length;
		for (int i = 0; i < n; i++) {
			System.out.println("  " + features[i]);
		}

		System.out.println("\n===========================================================\n");
		System.out.println("Enter Your Choice : ");
		return Input.getInteger(n);
	}

}
