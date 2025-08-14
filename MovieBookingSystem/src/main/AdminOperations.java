package main;

import model.Admin;
import util.Input;

public class AdminOperations {
	AdminOperations() {

	}

	public static void adminOperations(Admin admin) {
		int adminChoice = adminFeatures();
		switch (adminChoice) {
		case 1:
			System.out.println("You Have Selected print All Bookings");
			adminOperations(admin);
			break;
		case 2:
			System.out.println("You Have Selected add New Theatre");
			adminOperations(admin);
			break;
		case 3:
			System.out.println("You Have Selected Display All Theatres Seat Availability");
			adminOperations(admin);
			break;

		case 4:
			System.out.println("You Have Selected Exit ");
			int choice = MainMenu.mainMenu();
			MainMenu.call(choice);
			break;
		}
	}

	public static int adminFeatures() {
		System.out.println("\n========= Welcome to Movie Booking System Admin =============");
		System.out.println("Please select one of the following options:\n");

		String[] features = { "1 . View All Bookings ", "2 . Add New Theatre ",
				"3 . Display All Theatres Seat Availability", "4 . Exit" };

		int n = features.length;
		for (int i = 0; i < n; i++) {
			System.out.println("  " + features[i]);
		}

		System.out.println("\n===========================================================\n");

		return Input.getInteger(n);
	}

}
