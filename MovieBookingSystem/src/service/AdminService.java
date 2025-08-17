package service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

import model.Movies;
import model.Show;
import model.Theatre;
import model.TheatreAdmin;
import model.User;
import repository.InMemoryDatabase;
import util.Input;

public class AdminService {
	static HashMap<Long, User> userDB = InMemoryDatabase.getUserDB();
	static HashMap<Long, TheatreAdmin> theatreAdminDB = InMemoryDatabase.getTheatreAdminDB();
	static HashMap<Long, Movies> movieDB = InMemoryDatabase.getMovieDB();
	static HashMap<Long, Theatre> theatreDB = InMemoryDatabase.getTheatreDB();
	static HashMap<Long, Show> showDB = InMemoryDatabase.getShowDB();
	List<String> validLanguages = Arrays.asList("Hindi", "English", "Tamil", "Telugu", "Bengali", "Marathi", "Gujarati",
			"Urdu", "Kannada", "Malayalam");
	private static final String LINE = "+-----+----------------------+---------------------------+-----------------+------+------------------------+-----------------+";
	private static final String HEADER_FMT = "| %-3s | %-20s | %-25s | %-15s | %-4s | %-22s | %-15s |%n";
	private static final String ROW_FMT = "| %-3d | %-20s | %-25s | %-15s | %-4s | %-22s | %-15s |%n";

	public AdminService() {
	}

	public void viewAllUsers() {
		if (userDB.isEmpty()) {
			System.err.println("No users found in the system.");
			return;
		}

		System.out.println(
				"---------------------------------------------------------------------------------------------------------");
		System.out.printf("| %-8s | %-20s | %-25s | %-15s | %-20s |%n", "User ID", "Name", "Email", "Phone",
				"Preferred Location");
		System.out.println(
				"---------------------------------------------------------------------------------------------------------");

		for (User user : userDB.values()) {
			System.out.printf("| %-8d | %-20s | %-25s | %-15d | %-20s |%n", user.getUserId(), user.getUserName(),
					user.getUserEmailId(), user.getUserPhoneNumber(), user.getUserPreferredLocation());
		}

		System.out.println(
				"---------------------------------------------------------------------------------------------------------");
	}

	public void searchUserByName() {
		boolean found = false;
		Scanner sc = Input.getScanner();
		String name;
		while (true) {
			name = sc.nextLine();
			if (name.length() > 2 && name != null) {
				break;
			} else {
				System.err.println("Invalid input please ReEnter the Name ");
			}

		}
		System.out.println(
				"---------------------------------------------------------------------------------------------------------");
		System.out.printf("| %-8s | %-20s | %-25s | %-15s | %-20s |%n", "User ID", "Name", "Email", "Phone",
				"Preferred Location");
		System.out.println(
				"---------------------------------------------------------------------------------------------------------");

		for (User user : userDB.values()) {
			if (user.getUserName().equalsIgnoreCase(name)) {
				found = true;
				System.out.printf("| %-8d | %-20s | %-25s | %-15d | %-20s |%n", user.getUserId(), user.getUserName(),
						user.getUserEmailId(), user.getUserPhoneNumber(), user.getUserPreferredLocation());
			}
		}

		System.out.println(
				"---------------------------------------------------------------------------------------------------------");

		if (!found) {
			System.out.println("No users found with name: " + name);
		}
	}

	public void searchUserById() {
		System.out.println("Enter the User ID : ");
		Long userId = Input.getLong((long) userDB.size());
		if (userDB.containsKey(userId)) {
			User user = userDB.get(userId);

			System.out.println(
					"---------------------------------------------------------------------------------------------------------");
			System.out.printf("| %-8s | %-20s | %-25s | %-15s | %-20s |%n", "User ID", "Name", "Email", "Phone",
					"Preferred Location");
			System.out.println(
					"---------------------------------------------------------------------------------------------------------");

			System.out.printf("| %-8d | %-20s | %-25s | %-15d | %-20s |%n", user.getUserId(), user.getUserName(),
					user.getUserEmailId(), user.getUserPhoneNumber(), user.getUserPreferredLocation());

			System.out.println(
					"---------------------------------------------------------------------------------------------------------");
		} else {
			System.out.println("User with ID " + userId + " not found.");
		}
	}

	public void viewAllTheatreAdmins() {
		System.out.println(LINE);
		System.out.printf(HEADER_FMT, "ID", "Name", "Email", "Phone", "TID", "Theatre", "City");
		System.out.println(LINE);

		for (TheatreAdmin admin : theatreAdminDB.values()) {
			Theatre t = admin.getTheatre();
			System.out.printf(ROW_FMT, admin.getTheatreAdminId(), safe(admin.getTheatreAdminName()),
					safe(admin.getTheatreAdminEmailId()), String.valueOf(admin.getTheatreAdminPhoneNumber()),
					(t != null ? String.valueOf(t.getTheatreId()) : "-"),
					(t != null ? safe(t.getTheatreName()) : "N/A"), (t != null ? safe(t.getTheatreLocation()) : "N/A"));
		}
		System.out.println(LINE);
	}

	public void searchTheatreAdminById() {
		System.out.print("Enter Theatre Admin ID: ");
		Long id = Input.getLong((long) theatreAdminDB.size());
		TheatreAdmin admin = theatreAdminDB.get(id);

		if (admin == null) {
			System.out.println("No Theatre Admin found with ID: " + id);
			return;
		}

		Theatre t = admin.getTheatre();
		System.out.println(LINE);
		System.out.printf(HEADER_FMT, "ID", "Name", "Email", "Phone", "TID", "Theatre", "City");
		System.out.println(LINE);
		System.out.printf(ROW_FMT, admin.getTheatreAdminId(), safe(admin.getTheatreAdminName()),
				safe(admin.getTheatreAdminEmailId()), String.valueOf(admin.getTheatreAdminPhoneNumber()),
				(t != null ? String.valueOf(t.getTheatreId()) : "-"), (t != null ? safe(t.getTheatreName()) : "N/A"),
				(t != null ? safe(t.getTheatreLocation()) : "N/A"));
		System.out.println(LINE);
	}

	public void searchTheatreAdminByName() {
		System.out.print("Enter Theatre Admin Name: ");
		Scanner sc = Input.getScanner();
		String name;
		while (true) {
			name = sc.nextLine();
			if (name != null && name.trim().length() > 2)
				break;
			System.err.print("Invalid input. Please re-enter a name (min 3 chars): ");
		}
		String q = name.trim();

		boolean found = false;
		System.out.println(LINE);
		System.out.printf(HEADER_FMT, "ID", "Name", "Email", "Phone", "TID", "Theatre", "City");
		System.out.println(LINE);

		for (TheatreAdmin admin : theatreAdminDB.values()) {
			if (admin.getTheatreAdminName() != null && admin.getTheatreAdminName().equalsIgnoreCase(q)) {

				Theatre t = admin.getTheatre();
				System.out.printf(ROW_FMT, admin.getTheatreAdminId(), safe(admin.getTheatreAdminName()),
						safe(admin.getTheatreAdminEmailId()), String.valueOf(admin.getTheatreAdminPhoneNumber()),
						(t != null ? String.valueOf(t.getTheatreId()) : "-"),
						(t != null ? safe(t.getTheatreName()) : "N/A"),
						(t != null ? safe(t.getTheatreLocation()) : "N/A"));
				found = true;
			}
		}
		System.out.println(LINE);

		if (!found) {
			System.out.println("No Theatre Admins found with Name: " + q);
		}
	}

	private String safe(String s) {
		return (s == null ? "" : s);
	}

	public void addMovie() {
		Scanner sc = Input.getScanner();

		long movieId = movieDB.size() + 1L;
		while (movieDB.containsKey(movieId)) {
			movieId++;
		}

		String title;
		while (true) {
			System.out.print("Enter Movie Title: ");
			title = sc.nextLine().trim();
			if (!title.isEmpty())
				break;
			System.err.println("Title cannot be empty. Please try again.");
		}

		int duration;
		while (true) {
			System.out.print("Enter Duration (in minutes): ");
			String line = sc.nextLine().trim();
			try {
				duration = Integer.parseInt(line);
				if (duration > 0 && duration <= 500)
					break;
				System.err.println("Duration must be between 1 and 500 minutes.");
			} catch (NumberFormatException e) {
				System.err.println("Please enter a valid number for duration.");
			}
		}

		final List<String> allowedGenres = Arrays.asList("Action", "Drama", "Thriller", "Comedy", "Historical");
		String genre = null;
		while (true) {
			System.out.println("Select Genre:");
			for (int i = 0; i < allowedGenres.size(); i++) {
				System.out.printf("%d. %s%n", i + 1, allowedGenres.get(i));
			}
			System.out.print("Enter choice (1-" + allowedGenres.size() + ") or type the genre: ");
			String g = sc.nextLine().trim();

			try {
				int idx = Integer.parseInt(g);
				if (idx >= 1 && idx <= allowedGenres.size()) {
					genre = allowedGenres.get(idx - 1);
					break;
				} else {
					System.err.println("Invalid choice. Please try again.");
				}
			} catch (NumberFormatException ignore) {
				for (String ag : allowedGenres) {
					if (ag.equalsIgnoreCase(g)) {
						genre = ag;
						break;
					}
				}
				if (genre != null)
					break;
				System.err.println("Genre must be one of " + allowedGenres + ". Please try again.");
			}
		}

		// Language

		String language = null;
		while (true) {
			System.out.println("Choose a Language:");
			for (int i = 0; i < validLanguages.size(); i++) {
				System.out.println((i + 1) + ". " + validLanguages.get(i));
			}
			System.out.print("Enter language (number or name): ");
			String input = sc.nextLine().trim();

			// Check if input is a number
			if (input.matches("\\d+")) {
				int choice = Integer.parseInt(input);
				if (choice >= 1 && choice <= validLanguages.size()) {
					language = validLanguages.get(choice - 1);
					break;
				}
			} else {
				// Case-insensitive name check
				boolean isValid = validLanguages.stream().anyMatch(lang -> lang.equalsIgnoreCase(input));

				if (isValid) {
					for (String lang : validLanguages) {
						if (lang.equalsIgnoreCase(input)) {
							language = lang; // Normalize
							break;
						}
					}
					break;
				}
			}

			System.err.println("Invalid choice. Please enter a number or valid language name.");
		}

		// Release Year
		int releaseYear;
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
		while (true) {
			System.out.print("Enter Release Year: ");
			String line = sc.nextLine().trim();
			try {
				releaseYear = Integer.parseInt(line);
				if (releaseYear >= 1900 && releaseYear <= currentYear + 1)
					break;
				System.err.printf("Release year must be between 1900 and %d.%n", currentYear + 1);
			} catch (NumberFormatException e) {
				System.err.println("Please enter a valid year.");
			}
		}
		Movies movie = new Movies(movieId, title, duration, genre, language, releaseYear, new ArrayList<>());

		movieDB.put(movieId, movie);
		System.out.println("\nMovie added successfully!");
		System.out.println("+------+------------------------------+----------+------------+-----------+------+\n"
				+ "| ID   | Title                        | Duration | Genre      | Language  | Year |\n"
				+ "+------+------------------------------+----------+------------+-----------+------+\n"
				+ String.format("| %-4d | %-28s | %-8s | %-10s | %-9s | %-4d |\n", movie.getMovieId(),
						truncate(movie.getMovieTitle(), 28), formatDuration(movie.getDuration()), movie.getGenre(),
						truncate(movie.getLanguage(), 9), movie.getReleaseYear())
				+ "+------+------------------------------+----------+------------+-----------+------+\n");
	}

	private String formatDuration(int minutes) {
		int h = minutes / 60;
		int m = minutes % 60;
		return String.format("%d hr %02d min", h, m);
	}

	private String truncate(String s, int max) {
		if (s == null)
			return "";
		return s.length() <= max ? s : s.substring(0, max - 3) + "...";
	}

	public void addTheatreAdmin() {
		TheatreAdmin admin = new TheatreAdmin();
		Scanner sc = Input.getScanner();
		long newTheatreId = (long) (theatreAdminDB.size() + 1);
		admin.setTheatreAdminId(newTheatreId);

		String name;
		while (true) {
			System.out.print("Enter Theatre Admin Name: ");
			name = sc.nextLine().trim();
			if (!name.isEmpty()) {
				admin.setTheatreAdminName(name);
				break;
			}
			System.err.println("Name cannot be empty. Please re-enter.");
		}

		String email;
		while (true) {
			System.out.print("Enter Theatre Admin Email: ");
			email = sc.nextLine().trim();
			if (isValidEmail(email)) {
				admin.setTheatreAdminEmailId(email);
				break;
			}
			System.err.println("Invalid email format. Please re-enter.");
		}
		Long phone = null;
		while (true) {
			System.out.print("Enter Theatre Admin Phone (10 digits): ");
			String input = sc.nextLine().trim();
			if (input.matches("\\d{10}")) {
				phone = Long.parseLong(input);
				admin.setTheatreAdminPhoneNumber(phone);
				break;
			}
			System.err.println("Invalid phone number. Must be 10 digits.");
		}

		// Password validation
		String password;
		while (true) {
			System.out.print("Enter Theatre Admin Password (min 6 chars): ");
			password = sc.nextLine().trim();
			if (password.length() >= 6) {
				admin.setTheatreAdminPassword(password);
				break;
			}
			System.err.println("Password must be at least 6 characters.");
		}

		Theatre theatre = null;
		while (true) {
			System.out.print("Enter Theatre ID to assign: ");
			String input = sc.nextLine().trim();
			try {
				long theatreId = Long.parseLong(input);
				theatre = theatreDB.get(theatreId);
				if (theatre != null) {
					admin.setTheatre(theatre);
					break;
				} else {
					System.err.println("Invalid Theatre ID. Please re-enter.");
				}
			} catch (NumberFormatException e) {
				System.err.println("Invalid input. Enter a valid numeric Theatre ID.");
			}
		}

		// Save admin
		theatreAdminDB.put(admin.getTheatreAdminId(), admin);
		System.out.println("Theatre Admin added successfully: " + admin.getTheatreAdminName());
	}

	private static boolean isValidEmail(String email) {
		String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
		Pattern pattern = Pattern.compile(regex);
		return pattern.matcher(email).matches();
	}

	public void printAllShows(String timeZone) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(ZoneId.of(timeZone));

		System.out.println("+---------+--------------+--------------+-----------+-------------------+");
		System.out.println("| Show ID | Theatre      | Movie        | Screen    | Date & Time       |");
		System.out.println("+---------+--------------+--------------+-----------+-------------------+");

		for (Map.Entry<Long, Show> entry : showDB.entrySet()) {
			Show s = entry.getValue();

			String dateTime = formatter.format(Instant.ofEpochSecond(s.getDateTimeEpoch()));

			System.out.printf("| %-7d | %-12s | %-12s | %-9s | %-17s |\n", s.getShowId(),
					s.getTheatre().getTheatreName(), s.getMovie().getMovieTitle(), s.getScreen().getScreenNumber(),
					dateTime);

		}

		System.out.println("+---------+--------------+--------------+-----------+-------------------+");
	}
}
