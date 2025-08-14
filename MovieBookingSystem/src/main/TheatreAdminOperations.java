package main;

import model.TheatreAdmin;
import util.Input;

public class TheatreAdminOperations {
	public static void theatreAminOperations(TheatreAdmin theatreAdmin) {
		int choice = theatreAdminFeatures();
		switch(choice) {
		case 1:
			
			break;
		case 2:
			
			break;
		case 3:
			int choice1 = MainMenu.mainMenu();
			MainMenu.call(choice1);
			break;
		}
	}
	
	public static int theatreAdminFeatures(){
		
		System.out.println("\n========= Welcome to Theatre Admin Operations =============");
		System.out.println("Please select one of the following options:\n");

		String[] features = { "1 . Add New Screen ", 
				"2 . Display Theatres Seat Availability", "3 . Exit" };

		int n = features.length;
		for (int i = 0; i < n; i++) {
			System.out.println("  " + features[i]);
		}
		return Input.getInteger(n);
		
	}
}
