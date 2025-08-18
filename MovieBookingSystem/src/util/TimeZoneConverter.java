package util;

import java.util.Scanner;

public class TimeZoneConverter {

	public static String selectTimeZone() {
		Scanner sc = Input.getScanner();

		String[] timeZones = { "Asia/Kolkata", "America/New_York", "America/Los_Angeles", "Europe/London",
				"Europe/Paris", "Asia/Tokyo", "Australia/Sydney" };

		System.out.println("Select your time zone:");
		for (int i = 0; i < timeZones.length; i++) {
			System.out.println((i + 1) + ". " + timeZones[i]);
		}
		int choice = 0;
		while (choice < 1 || choice > timeZones.length) {
			System.out.print("Enter choice (1-" + timeZones.length + "): ");
			String input = sc.nextLine(); 
			try {
				choice = Integer.parseInt(input);
			} catch (NumberFormatException e) {
				System.err.println("Invalid Input ");
				selectTimeZone(); 
			}
		}

		return timeZones[choice - 1];
	}
}
