package main;

import java.util.HashMap;
import java.util.Scanner;

import model.Admin;
import model.TheatreAdmin;
import model.Ticket;
import model.User;
import repository.InMemoryDatabase;
import service.AdminService;
import service.MovieService;
import service.TheatreService;
import service.TicketService;
import util.Input;
import util.TimeZoneConverter;

public class MainMenu {
	static TicketService ticketServiceObj = new TicketService();
	static MovieService movieServiceObj = new MovieService();
	static TheatreService theatreService = new TheatreService();
	static HashMap<Long, Ticket> ticketDB = InMemoryDatabase.getTicketDB();

	public static void Start() {
		InMemoryDatabase DB = new InMemoryDatabase();
		int choice = mainMenu();
		call(choice);
	}

	public static void call(int choice) {

		switch (choice) {
		case 0:
			System.out.println("You Have Selected Exit , Thank You");
			System.exit(0);
			break;
		case 1:

			User user = null;
			int num1 = printloginRegister();
			if (num1 == 1) {
				user = callUser();

			} else if (num1 == 2) {
				createNewUser();
				user = callUser();
			}
			if (user == null) {
				Start();
			} else {
				String timeZone = user.getTimeZone();
				Operations.operation(user, ticketServiceObj, movieServiceObj, theatreService, ticketDB);
			}
			break;
		case 2:
			TheatreAdmin theatreAdmin = callTheatreAdminLoginOrRegister();
			if (theatreAdmin == null) {
				Start();
			} else {
				TheatreAdminOperations.theatreAminOperations(theatreAdmin);
			}
			break;
		case 3:
			Admin admin = callAdmin();
			if (admin == null) {
				Start();
			} else {
				AdminOperations.adminOperations(admin);
			}
			break;
		case 4:
			System.out.println("You Have Selected Exit , Thank You");
			System.exit(0);
			break;
		}
	}

	public static Admin callAdmin() {

		Scanner sc = Input.getScanner();
		HashMap<Long, Admin> AdminDB = InMemoryDatabase.getAdminDB();
		Admin foundUser = null;
		boolean flag = false;
		while (true) {
			System.out.println("Please Enter your Phone Number or Email (or press 0 to exit): ");
			String input = sc.nextLine().trim();
			if (input.equalsIgnoreCase("0")) {
				return null; // exit
			}
			for (Admin u : AdminDB.values()) {
				if (String.valueOf(u.getAdminPhoneNumber()).equals(input)
						|| u.getAdminEmailId().equalsIgnoreCase(input)) {
					foundUser = u;
					flag = true;
					break;
				}
			}
			if (flag == true) {
				break;
			} else {
				System.err.println("Invalid Phone Number or Emial");
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

	private static User callUser() { // 0

		System.out.println("-----------------------------------------------------------------");
		System.out.println("|                        Login                                   |");
		System.out.println("-----------------------------------------------------------------");
		Scanner sc = Input.getScanner();
		HashMap<Long, User> userDB = InMemoryDatabase.getUserDB();
		User foundUser = null;
		boolean flag = false;
		while (true) {
			System.out.print("Enter Your Phone Number or Email Id (or type '0' to Exit): ");
			String input = sc.nextLine().trim();
			if (input.equalsIgnoreCase("0")) {
				return null;
			}
			for (User u : userDB.values()) {
				if (String.valueOf(u.getUserPhoneNumber()).equals(input)
						|| u.getUserEmailId().equalsIgnoreCase(input)) {
					flag = true;
					foundUser = u;
					break;

				}
			}
			if (flag == true)
				break;
			else
				System.err.println("Invalid Phone Number or Email Id");

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

	private static TheatreAdmin callTheatreAdminLoginOrRegister() {
		AdminService adminService = new AdminService();
		System.out.println("========================================");
		System.out.println("|     🎬 Theatre Admin  🎬          |");
		System.out.println("========================================");
		System.out.println("|	1. login                          |");
		System.out.println("|	2. Register                     |");
		System.out.println("========================================");
		System.out.println("Please enter your choice (or press 0 to exit): ");
		int choice = Input.getInteger(2);
		if (choice == 0) {
			System.out.println("---Back---");
		} else if (choice == 1) {
			return callTheatreAdmin();
		} else if (choice == 2) {
			adminService.addTheatreAdmin();
			return callTheatreAdmin();
		}
		return null;
	}

	private static TheatreAdmin callTheatreAdmin() {

		System.out.println("========================================");
		System.out.println("|     🎬 Theatre Admin Login 🎬          |");
		System.out.println("========================================");

		Scanner sc = Input.getScanner();
		HashMap<Long, TheatreAdmin> theatreDb = InMemoryDatabase.getTheatreAdminDB();

		TheatreAdmin foundAdmin = null;

		
		boolean found =false;
		while(true) {
			System.out.println("Enter your Phone Number or Email Id (or type 'o' to Exit");
			String input = sc.nextLine().trim();
			if(input.equalsIgnoreCase("0")) return null;
			for (TheatreAdmin u : theatreDb.values()) {
				if (String.valueOf(u.getTheatreAdminPhoneNumber()).equals(input)
						|| u.getTheatreAdminEmailId().equalsIgnoreCase(input)) {
					foundAdmin = u;
					found = true;
					break;

				}
			}
			if(found == true)break;
			else System.err.println("Invalid Phoone Number or Email Id");
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

	public static void createNewUser() {
		Scanner sc = Input.getScanner();
		HashMap<Long, User> userDB = InMemoryDatabase.getUserDB();

		User newUser = new User();
		Long newUserId = (long) (userDB.size() + 1);
		newUser.setUserId(newUserId);

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

		while (true) {
			System.out.print("Enter Your Preferred Location: ");
			String location = sc.nextLine().trim();
			if (location.length() >= 4) {
				newUser.setUserPreferredLocation(location);
				break;
			} else {
				System.err.println("Preferred location must have at least 4 characters!");
			}
		}

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
		System.out.println("Enter the Amount : ");
		int amount = Input.getInteger(1000000000);

		String timeZone = TimeZoneConverter.selectTimeZone();
		newUser.setBalance((double) amount);
		newUser.setTimeZone(timeZone);
		userDB.put(newUser.getUserId(), newUser);
		System.out.println(" User Registered Successfully! ");

	}

}
