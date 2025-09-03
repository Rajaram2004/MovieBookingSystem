package main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Scanner;

import model.Admin;
import model.TheatreAdmin;
import model.Ticket;
import model.User;
import service.MovieService;
import service.TheatreService;
import service.TicketService;
import util.Input;
import util.TimeZoneConverter;

public class MainMenu {

	public static void Start() {
		int choice = mainMenu();
		call(choice);
	}

	public static void call(int choice) {
		switch (choice) {
		case 0:
		case 4:
			System.out.println("You have selected Exit. Thank you!");
			System.exit(0);
			break;

		case 1: {
			User user = null;
			int option = printloginRegister();

			if (option == 1) {
				user = callUser();
			} else if (option == 2) {
				createNewUser();
				user = callUser();
			}

			if (user == null) {
				Start();
			} else {
				Operations.operation(user);
			}
			break;
		}

		case 2: {
			TheatreAdmin theatreAdmin = callTheatreAdminLoginOrRegister();
			if (theatreAdmin == null) {
				Start();
			} else {
				TheatreAdminOperations.theatreAminOperations(theatreAdmin);
			}
			break;
		}

		case 3: {
			Admin admin = callAdmin();
			if (admin == null) {
				Start();
			} else {
				AdminOperations.adminOperations(admin);
			}
			break;
		}

		default:
			System.out.println("Invalid choice! Please try again.");
			Start();
			break;
		}
	}

	public static Admin callAdmin() {
		Scanner sc = Input.getScanner();
		Admin foundUser = null;

		while (true) {
			System.out.println("Please Enter your Phone Number or Email (or press 0 to exit): ");
			String input = sc.nextLine().trim();

			if (input.equalsIgnoreCase("0")) {
				return null;
			}

			try (Connection conn = Input.getConnection()) {

				boolean isNumeric = input.matches("\\d+");
				String query = isNumeric ? "SELECT * FROM users WHERE phoneNumber = ? AND role = ?"
						: "SELECT * FROM users WHERE LOWER(emailId) = LOWER(?) AND LOWER(role) = LOWER(?)";

				try (PreparedStatement pstmt = conn.prepareStatement(query)) {
					if (isNumeric) {
						pstmt.setLong(1, Long.parseLong(input));
						pstmt.setString(2, "Admin");
					} else {
						pstmt.setString(1, input);
						pstmt.setString(2, "Admin");
					}
					ResultSet rs = pstmt.executeQuery();
					if (rs.next()) {
						foundUser = new Admin();
						foundUser.setAdminId(rs.getLong("id"));
						foundUser.setAdminName(rs.getString("name"));
						foundUser.setAdminEmailId(rs.getString("emailId"));
						foundUser.setAdminPhoneNumber(rs.getLong("phoneNumber"));
						foundUser.setAdminPassword(rs.getString("password"));
						foundUser.setTimeZone(rs.getString("timeZone"));
						break;
					} else {
						System.err.println("Invalid Phone Number or Email!");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
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

		System.out.println("-----------------------------------------------------------------");
		System.out.println("|                        Login                                   |");
		System.out.println("-----------------------------------------------------------------");

		User foundUser = null;
		Scanner sc = new Scanner(System.in);
		while (true) {

			try (Connection conn = Input.getConnection()) {

				System.out.print("Enter Phone Number or Email (or typr '0' to Exit): ");
				String inputVal = sc.nextLine().trim();
				if (inputVal.equalsIgnoreCase("0")) {
					return null;
				}
				boolean isNumeric = inputVal.matches("\\d+");
				String query = isNumeric ? "SELECT * FROM users WHERE phoneNumber = ? AND role = ?"
						: "SELECT * FROM users WHERE LOWER(emailId) = LOWER(?) AND LOWER(role) = LOWER(?)";

				try (PreparedStatement pstmt = conn.prepareStatement(query)) {
					if (isNumeric) {

						pstmt.setLong(1, Long.parseLong(inputVal));
						pstmt.setString(2, "customer");
					} else {
						pstmt.setString(1, inputVal);
						pstmt.setString(2, "customer");
					}

					try (ResultSet rs = pstmt.executeQuery()) {
						if (rs.next()) {
							foundUser = new User();
							foundUser.setId(rs.getLong("id"));
							foundUser.setName(rs.getString("name"));
							foundUser.setEmailId(rs.getString("emailId"));
							foundUser.setPhoneNumber(rs.getLong("phoneNumber"));
							foundUser.setPassword(rs.getString("password"));
							foundUser.setTimeZone(rs.getString("timeZone"));
							foundUser.setBalance(rs.getDouble("balance"));
							foundUser.setIsActive(rs.getBoolean("active"));
							break;
						} else {
							System.out.println("No user found with the given email or phone number!");
						}
					}
				}
			} catch (SQLException e) {
				System.out.println("⚠ Database Error: " + e.getMessage());
			} catch (Exception e) {
				System.out.println("⚠ Unexpected Error: " + e.getMessage());
			}
		}
		if (foundUser != null) {
			int attempts = 3;
			while (attempts > 0) {
				System.out.print("Enter Your Password (or type '0' to Exit): ");
				String password = sc.nextLine().trim();
				if (password.equalsIgnoreCase("0")) {
					return null;
				}
				if (foundUser.getPassword().equals(password)) {
					System.out.println("Logged In Successfully!");
					System.out.println("Welcome, " + foundUser.getName() + "!");
					return foundUser;
				} else {
					attempts--;
					System.err.println("Incorrect password! Attempts left: " + attempts);
				}
			}
			System.err.println("Too many failed attempts. Please try again later.");
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
		// AdminService adminService = new AdminService();
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
			addTheatreAdmin();
			return callTheatreAdmin();
		}
		return null;
	}

	public static void addTheatreAdmin() {
		Scanner sc = Input.getScanner();
		TheatreAdmin admin = new TheatreAdmin();

		try (Connection conn = Input.getConnection()) {
			while (true) {
				System.out.print("Enter Theatre Admin Name (or Type '0' to Exit): ");
				String name = sc.nextLine().trim();
				if (name.equalsIgnoreCase("0"))
					return;
				if (!name.isEmpty()) {
					admin.setTheatreAdminName(name);
					break;
				}
				System.err.println("Name cannot be empty. Please re-enter.");
			}
			while (true) {
				System.out.print("Enter Theatre Admin Email (or Type '0' to Exit): ");
				String email = sc.nextLine().trim();
				if (email.equalsIgnoreCase("0"))
					return;

				if (isValidEmail(email)) {
					String emailCheck = "SELECT COUNT(*) FROM users WHERE emailId = ?";
					try (PreparedStatement pstmt = conn.prepareStatement(emailCheck)) {
						pstmt.setString(1, email);
						try (ResultSet rs = pstmt.executeQuery()) {
							rs.next();
							if (rs.getInt(1) > 0) {
								System.err.println("Email already registered! Try another.");
								continue;
							}
						}
					}
					admin.setTheatreAdminEmailId(email);
					break;
				}
				System.err.println("Invalid email format. Please re-enter.");
			}

			while (true) {
				System.out.print("Enter Theatre Admin Phone (6–12 digits) (or Type '0' to Exit): ");
				String input = sc.nextLine().trim();
				if (input.equalsIgnoreCase("0"))
					return;

				if (input.matches("\\d{6,12}")) {
					long phone = Long.parseLong(input);

					String phoneCheck = "SELECT COUNT(*) FROM users WHERE phoneNumber = ?";
					try (PreparedStatement pstmt = conn.prepareStatement(phoneCheck)) {
						pstmt.setLong(1, phone);
						try (ResultSet rs = pstmt.executeQuery()) {
							rs.next();
							if (rs.getInt(1) > 0) {
								System.err.println("Phone Number already registered! Try another.");
								continue;
							}
						}
					}

					admin.setTheatreAdminPhoneNumber(phone);
					break;
				}
				System.err.println("Invalid phone number. Must be 6–12 digits.");
			}
			while (true) {
				System.out.print("Enter Theatre Admin Password (min 6 chars) (or Type '0' to Exit): ");
				String password = sc.nextLine().trim();
				if (password.equalsIgnoreCase("0"))
					return;
				if (password.length() >= 6) {
					admin.setTheatreAdminPassword(password);
					break;
				}
				System.err.println("Password must be at least 6 characters.");
			}

			String timeZone = TimeZoneConverter.selectTimeZone();
			admin.setTimeZone(timeZone);

			String insertQuery = "INSERT INTO users (name, emailId, phoneNumber, password, role, timeZone, balance, active) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
			try (PreparedStatement pstmt = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
				pstmt.setString(1, admin.getTheatreAdminName());
				pstmt.setString(2, admin.getTheatreAdminEmailId());
				pstmt.setLong(3, admin.getTheatreAdminPhoneNumber());
				pstmt.setString(4, admin.getTheatreAdminPassword());
				pstmt.setString(5, "theatreAdmin");
				pstmt.setString(6, admin.getTimeZone());
				pstmt.setDouble(7, 0.0);
				pstmt.setBoolean(8, true);

				int rows = pstmt.executeUpdate();
				if (rows > 0) {
					try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
						if (generatedKeys.next()) {
							long id = generatedKeys.getLong(1);
							admin.setId(id);
							System.out.println("Theatre Admin added successfully! ID: " + id);
						}
					}
				} else {
					System.err.println("Failed to add Theatre Admin. Please try again.");
				}
			}

		} catch (SQLException e) {
			System.err.println("⚠ Database Error: " + e.getMessage());
		} catch (Exception e) {
			System.err.println("⚠ Unexpected Error: " + e.getMessage());
		}
	}

	private static boolean isValidEmail(String email) {

		String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
		return email.matches(emailRegex);
	}

	private static TheatreAdmin callTheatreAdmin() {

		System.out.println("========================================");
		System.out.println("|     🎬 Theatre Admin Login 🎬          |");
		System.out.println("========================================");

		Scanner sc = Input.getScanner();
		TheatreAdmin foundAdmin = null;

		try (Connection conn = Input.getConnection()) {

			while (true) {
				System.out.println("Enter your Phone Number or Email Id (or type '0' to Exit):");
				String input = sc.nextLine().trim();
				if (input.equals("0"))
					return null;

				String query = "SELECT * FROM users WHERE (phoneNumber = ? OR emailId = ?) AND role = 'theatreAdmin'";
				try (PreparedStatement pstmt = conn.prepareStatement(query)) {
					pstmt.setString(1, input);
					pstmt.setString(2, input);

					try (ResultSet rs = pstmt.executeQuery()) {
						if (rs.next()) {
							foundAdmin = new TheatreAdmin();
							foundAdmin.setId(rs.getLong("id"));
							foundAdmin.setTheatreAdminName(rs.getString("name"));
							foundAdmin.setTheatreAdminEmailId(rs.getString("emailId"));
							foundAdmin.setTheatreAdminPhoneNumber(rs.getLong("phoneNumber"));
							foundAdmin.setTheatreAdminPassword(rs.getString("password"));
							foundAdmin.setTimeZone(rs.getString("timeZone"));
							break;
						} else {
							System.err.println("Invalid Phone Number or Email Id!");
						}
					}
				}
			}

			while (true) {
				System.out.print("Enter Your Password (or type '0' to Exit): ");
				String password = sc.nextLine().trim();
				if (password.equals("0"))
					return null;

				if (foundAdmin.getTheatreAdminPassword().equals(password)) {
					System.out.println("Logged In Successfully!");
					System.out.println("Hai Theatre " + foundAdmin.getTheatreAdminName());
					break;
				} else {
					System.err.println("Incorrect password!");
				}
			}

		} catch (SQLException e) {
			System.err.println("⚠ Database Error: " + e.getMessage());
		} catch (Exception e) {
			System.err.println("⚠ Unexpected Error: " + e.getMessage());
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

		try (Connection conn = Input.getConnection()) {

			User newUser = new User();

			Long newUserId = System.currentTimeMillis();
			newUser.setId(newUserId);
			while (true) {
				System.out.print("Enter Your Name (or type '0' to exit): ");
				String name = sc.nextLine().trim();

				if (name.equals("0")) {
					System.out.println("Exiting registration...");
					return null;
				}

				if (name.matches("^[A-Za-z ]{3,}$")) {
					newUser.setName(name);
					break;
				} else {
					System.err.println("Invalid Name! Only alphabets and min 3 characters allowed.");
				}
			}
			while (true) {
				System.out.print("Enter Your Email (or type '0' to exit): ");
				String email = sc.nextLine().trim();

				if (email.equals("0")) {
					System.out.println("Exiting registration...");
					return null;
				}

				if (email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
					String emailCheck = "SELECT COUNT(*) FROM users WHERE emailId = ?";
					try (PreparedStatement pstmt = conn.prepareStatement(emailCheck)) {
						pstmt.setString(1, email);
						try (ResultSet rs = pstmt.executeQuery()) {
							rs.next();
							if (rs.getInt(1) > 0) {
								System.err.println("Email already registered! Try another.");
								continue;
							}
						}
					}
					newUser.setEmailId(email);
					break;
				} else {
					System.err.println("Invalid Email format!");
				}
			}
			while (true) {
				System.out.print("Enter Your Phone Number (6–12 digits) (or type '0' to exit): ");
				String phone = sc.nextLine().trim();

				if (phone.equals("0")) {
					System.out.println("Exiting registration...");
					return null;
				}

				if (phone.matches("\\d{6,12}")) {
					long phoneNum = Long.parseLong(phone);

					String phoneCheck = "SELECT COUNT(*) FROM users WHERE phoneNumber = ?";
					try (PreparedStatement pstmt = conn.prepareStatement(phoneCheck)) {
						pstmt.setLong(1, phoneNum);
						try (ResultSet rs = pstmt.executeQuery()) {
							rs.next();
							if (rs.getInt(1) > 0) {
								System.err.println("Phone Number already registered! Try another.");
								continue;
							}
						}
					}
					newUser.setPhoneNumber(phoneNum);
					break;
				} else {
					System.err.println("Invalid Phone Number! Enter 6–12 digits only.");
				}
			}

			while (true) {
				System.out.print("Enter Your Password (or type '0' to exit): ");
				String password = sc.nextLine().trim();

				if (password.equals("0")) {
					System.out.println("Exiting registration...");
					return null;
				}

				if (password.matches("^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=]).{6,}$")) {
					newUser.setPassword(password);
					break;
				} else {
					System.err.println(
							"Password must be at least 6 characters, contain 1 uppercase, 1 number, and 1 special character.");
				}
			}
			String balanceInput;
			while (true) {

				System.out.print("Enter the Initial Balance (or type '0' to exit): ");
				balanceInput = sc.nextLine().trim();

				if (balanceInput.equals("0")) {
					System.out.println("Exiting registration...");
					return null;
				}
				if (balanceInput.matches("\\d{1,12}")) {
					break;
				} else {
					System.err.println("Invalid Input");
				}

			}
			double balance = Double.parseDouble(balanceInput);
			newUser.setBalance(balance);
			System.out.println("Select Time Zone (or type '0' to exit): ");
			String timeZone = TimeZoneConverter.selectTimeZone();
			if (timeZone == null || timeZone.equals("0")) {
				System.out.println("Exiting registration...");
				return null;
			}
			newUser.setTimeZone(timeZone);
			String insertQuery = "INSERT INTO users (name, emailId, phoneNumber, password, role, timeZone, balance, active) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?)";
			try (PreparedStatement pstmt = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
				pstmt.setString(1, newUser.getName());
				pstmt.setString(2, newUser.getEmailId());
				pstmt.setLong(3, newUser.getPhoneNumber());
				pstmt.setString(4, newUser.getPassword());
				pstmt.setString(5, "customer");
				pstmt.setString(6, newUser.getTimeZone());
				pstmt.setDouble(7, newUser.getBalance());
				pstmt.setBoolean(8, true);

				int rows = pstmt.executeUpdate();
				if (rows > 0) {
					try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
						if (generatedKeys.next()) {
							long generatedId = generatedKeys.getLong(1);
							newUser.setId(generatedId);
							System.out.println("User Registered Successfully! Your User ID is: " + generatedId);
						}
					}
				} else {
					System.err.println("Failed to register user. Please try again.");
				}
			}

			return newUser;

		} catch (SQLException e) {
			System.err.println("⚠ Database Error: " + e.getMessage());
		} catch (Exception e) {
			System.err.println("⚠ Unexpected Error: " + e.getMessage());
		}

		return null;
	}

}
