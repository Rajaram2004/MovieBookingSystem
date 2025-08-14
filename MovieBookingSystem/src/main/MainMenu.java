package main;

import java.util.HashMap;
import java.util.Scanner;

import model.Admin;
import model.TheatreAdmin;
import model.Ticket;
import model.User;
import repository.InMemoryDatabase;
import service.MovieService;
import service.TicketService;
import util.Input;
import util.TimeZoneConverter;

public class MainMenu {
	public static void Start() {
		InMemoryDatabase DB = new InMemoryDatabase();
		int choice = mainMenu();
		call(choice);
	}

	public static void call(int choice) {
		TicketService ticketServiceObj = new TicketService();
		MovieService movieServiceObj = new MovieService();
		HashMap<Long,Ticket> ticketDB = InMemoryDatabase.getTicketDB();
		switch (choice) {
		case 1:
			String timeZone = TimeZoneConverter.selectTimeZone();
			User user = null;
			int num1 = printloginRegister();
			if (num1 == 1) {
				 user = callUser();
			} else if (num1 == 2) {
				 user = createNewUser();
			}
			Operations.operation(user,ticketServiceObj,movieServiceObj,timeZone,ticketDB);

			break;
		case 2:
			TheatreAdmin theatreAdmin = callTheatreAdmin();
			TheatreAdminOperations.theatreAminOperations(theatreAdmin);

			break;
		case 3:
			Admin admin = callAdmin();
			AdminOperations.adminOperations(admin);
			break;
		case 4:
			System.out.println("You Have Selected Exit , Thank You");
			System.exit(0);
			break;
		}
	}

	public static Admin callAdmin() {

		System.out.println("========================================");
		System.out.println("|            🎬 User Login 🎬          |");
		System.out.println("========================================");
		System.out.println("|	1. login via phone Number      |");
		System.out.println("|	2. login via Email Address     |");
		System.out.println("========================================");
		System.out.println("Enter Your Choice : ");
		int choice = Input.getInteger(2);
		Scanner sc = Input.getScanner();
		HashMap<Long, Admin> AdminDB = InMemoryDatabase.getAdminDB();
		Admin foundUser = null;
		if (choice == 1) {
			// Phone number validation and search
			while (true) {
				System.out.print("Enter Your Phone Number (6–12 digits): ");
				String input = sc.nextLine().trim();

				if (input.matches("\\d{6,12}")) {
					for (Admin u : AdminDB.values()) {
						if (String.valueOf(u.getAdminPhoneNumber()).equals(input)) {
							foundUser = u;
							break;
						}
					}
					if (foundUser == null) {
						System.err.println(" Phone number not found!");
					} else {
						break; // valid phone number found
					}
				} else {
					System.err.println("Invalid input! Please enter 6–12 digits only.");
				}
			}

			// Password validation

		} else if (choice == 2) {
			while (true) {
				System.out.print("Enter Your Email Address: ");
				String email = sc.nextLine().trim();

				if (email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
					boolean found = false;
					for (Admin u : AdminDB.values()) {
						if (u.getAdminEmailId().equalsIgnoreCase(email)) {
							found = true;
							foundUser = u;
							break;
						}
					}
					if (!found) {
						System.err.println("Invalid Email Address");
					} else {
						break;
					}
				} else {
					System.err.println("Invalid Email Format! Example: user@example.com");
				}
			}
		}

		while (true) {
			System.out.print("Enter Your Password: ");
			String password = sc.nextLine().trim();

			if (foundUser.getAdminPassword().equals(password)) {
				System.out.println("Logged In Successfully!");
				System.out.println("Hai " + foundUser.getAdminName());
				break;
			} else {
				System.err.println("Incorrect password!");
			}
		}

		return foundUser;
	}

	private static User callUser() {

		System.out.println("========================================");
		System.out.println("|            🎬 User Login 🎬          |");
		System.out.println("========================================");
		System.out.println("|	1. login via phone Number      |");
		System.out.println("|	2. login via Email Address     |");
		System.out.println("========================================");
		System.out.println("Enter Your Choice : ");
		int choice = Input.getInteger(2);
		Long PhoneNumber;

		Scanner sc = Input.getScanner();
		HashMap<Long, User> userDB = InMemoryDatabase.getUserDB();
		System.out.println(userDB);
		User foundUser = null;
		if (choice == 1) {
			// Phone number validation and search
			while (true) {
				System.out.print("Enter Your Phone Number (6–12 digits): ");
				String input = sc.nextLine().trim();

				if (input.matches("\\d{6,12}")) {
					for (User u : userDB.values()) {
						if (String.valueOf(u.getUserPhoneNumber()).equals(input)) {
							foundUser = u;
							break;
						}
					}
					if (foundUser == null) {
						System.err.println(" Phone number not found!");
					} else {
						break; // valid phone number found
					}
				} else {
					System.err.println("Invalid input! Please enter 6–12 digits only.");
				}
			}

			// Password validation

		} else if (choice == 2) {
			while (true) {
				System.out.print("Enter Your Email Address: ");
				String email = sc.nextLine().trim();

				if (email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
					boolean found = false;
					for (User u : userDB.values()) {
						if (u.getUserEmailId().equalsIgnoreCase(email)) {
							found = true;
							foundUser = u;
							break;
						}
					}
					if (!found) {
						System.err.println("Invalid Email Address");
					} else {
						break;
					}
				} else {
					System.err.println("Invalid Email Format! Example: user@example.com");
				}
			}
		}

		while (true) {
			System.out.print("Enter Your Password: ");
			String password = sc.nextLine().trim();

			if (foundUser.getUserPassword().equals(password)) {
				System.out.println("Logged In Successfully!");
				System.out.println("Hai " + foundUser.getUserName());
				break;
			} else {
				System.err.println("Incorrect password!");
			}
		}

		return foundUser;
	}

	public static int printloginRegister() {
		System.out.println("========================================");
		System.out.println("|               🎬 User 🎬             |");
		System.out.println("========================================");
		System.out.println("|	1. Login                       |");
		System.out.println("|	2. New User                    |");
		System.out.println("========================================");
		System.out.println("Enter The Choice : ");
		return Input.getInteger(2);

	}

	private static TheatreAdmin callTheatreAdmin() {

		System.out.println("========================================");
		System.out.println("|     🎬 Theatre Admin Login 🎬          |");
		System.out.println("========================================");
		System.out.println("|	1. login via phone Number      |");
		System.out.println("|	2. login via Email Address     |");
		System.out.println("========================================");
		System.out.println("Enter Your Choice : ");
		int choice = Input.getInteger(2);
		int PhoneNumber;

		Scanner sc = Input.getScanner();
		HashMap<Long, TheatreAdmin> theatreDb = InMemoryDatabase.getTheatreAdminDB();

		TheatreAdmin foundAdmin = null;
		if (choice == 1) {
			// Phone number validation and search
			while (true) {
				System.out.print("Enter Your Phone Number (6–12 digits): ");
				String input = sc.nextLine().trim();

				if (input.matches("\\d{6,12}")) {
					for (TheatreAdmin u : theatreDb.values()) {
						if (String.valueOf(u.getTheatreAdminPhoneNumber()).equals(input)) {
							foundAdmin = u;
							break;
						}
					}
					if (foundAdmin == null) {
						System.err.println(" Phone number not found!");
					} else {
						break; // valid phone number found
					}
				} else {
					System.err.println("Invalid input! Please enter 6–12 digits only.");
				}
			}

			// Password validation

		} else if (choice == 2) {
			System.out.println(theatreDb);
			while (true) {
				System.out.print("Enter Your Email Address: ");
				String email = sc.nextLine().trim();

				if (email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
					boolean found = false;
					for (TheatreAdmin u : theatreDb.values()) {
						if (u.getTheatreAdminEmailId().equalsIgnoreCase(email)) {
							found = true;
							foundAdmin = u;
							break;
						}
					}
					if (!found) {
						System.err.println("Invalid Email Address");
					} else {
						break;
					}
				} else {
					System.err.println("Invalid Email Format! Example: user@example.com");
				}
			}
		}

		while (true) {
			System.out.print("Enter Your Password: ");
			String password = sc.nextLine().trim();

			if (foundAdmin.getTheatreAdminPassword().equals(password)) {
				System.out.println("Logged In Successfully!");
				System.out.println("Hai Theatre " + foundAdmin.getTheatreAdminName());
				break;
			} else {
				System.err.println("Incorrect password!");
			}
		}

		return foundAdmin;
	}

	public static int mainMenu() {
		System.out.println("====================================================");
		System.out.println("|            🎬 BookMyShow Console App 🎬          |");
		System.out.println("====================================================");
		System.out.println("|	  1.User                                   |");
		System.out.println("|	  2.Theatre Admin                          |");
		System.out.println("|         3.Movie Booking System Admin             |");
		System.out.println("|	  4.Exit                                   |");
		System.out.println("====================================================");
		System.out.println("Enter the Choice : ");

		Scanner sc = Input.getScanner();
		String input = sc.nextLine();
		try {
			int num = Integer.parseInt(input);
			if (num > 0 && num <= 4) {
				return num;

			} else {
				System.err.println("Please Enter a Valid Number.");
				return mainMenu();
			}
		} catch (NumberFormatException e) {
			System.err.println("Not an integer.");
			return mainMenu();
		}
	}

	public static User createNewUser() {
		Scanner sc = Input.getScanner();
		HashMap<Long, User> userDB = InMemoryDatabase.getUserDB();

		User newUser = new User();

		// Auto-generate User ID
		Long newUserId = (long) (userDB.size() + 1);
		newUser.setUserId(newUserId);

		// Get User Name (Only alphabets and spaces, min 3 characters)
		while (true) {
			System.out.print("Enter Your Name: ");
			String name = sc.nextLine().trim();
			if (name.matches("^[A-Za-z ]{3,}$")) {
				newUser.setUserName(name);
				break;
			} else {
				System.err.println("Invalid Name! Only alphabets and min 3 characters allowed.");
			}
		}

		// Get Email (Basic pattern check)
		while (true) {
			System.out.print("Enter Your Email: ");
			String email = sc.nextLine().trim();
			if (email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
				boolean exists = false;
				for (User u : userDB.values()) {
					if (u.getUserEmailId().equalsIgnoreCase(email)) {
						exists = true;
						break;
					}
				}
				if (exists) {
					System.err.println("Email already registered! Try another.");
				} else {
					newUser.setUserEmailId(email);
					break;
				}
			} else {
				System.err.println("Invalid Email format!");
			}
		}

		// Get Phone Number (6–12 digits)
		while (true) {
			System.out.print("Enter Your Phone Number (6–12 digits): ");
			String phone = sc.nextLine().trim();
			if (phone.matches("\\d{6,12}")) {
				long phoneNum = Long.parseLong(phone);

				boolean exists = false;
				for (User u : userDB.values()) {
					if (u.getUserPhoneNumber() == phoneNum) {
						exists = true;
						break;
					}
				}
				if (exists) {
					System.err.println("Phone Number already registered! Try another.");
				} else {
					newUser.setUserPhoneNumber(phoneNum);
					break;
				}
			} else {
				System.err.println("Invalid Phone Number! Enter 6–12 digits only.");
			}
		}

		// Get Preferred Location (Min 2 chars)
		while (true) {
			System.out.print("Enter Your Preferred Location: ");
			String location = sc.nextLine().trim();
			if (location.length() >= 4) {
				newUser.setUserPreferredLocation(location);
				break;
			} else {
				System.err.println("Preferred location must have at least 2 characters!");
			}
		}

		// Get Password (Min 6 chars, at least 1 digit, 1 special char, 1 uppercase)
		while (true) {
			System.out.print("Enter Your Password: ");
			String password = sc.nextLine().trim();
			if (password.matches("^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=]).{6,}$")) {
				newUser.setUserPassword(password);
				break;
			} else {
				System.err.println(
						"Password must be at least 6 characters, contain 1 uppercase, 1 number, and 1 special character.");
			}
		}

		// Add to DB
		userDB.put(newUser.getUserId(), newUser);
		System.out.println(" User Registered Successfully! ");
		return newUser;
	}

}
