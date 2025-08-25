package service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
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
import util.TimeZoneConverter;

public class AdminService {
	static HashMap<Long, User> userDB = InMemoryDatabase.getUserDB();
	static HashMap<Long, TheatreAdmin> theatreAdminDB = InMemoryDatabase.getTheatreAdminDB();
	static HashMap<Long, Movies> movieDB = InMemoryDatabase.getMovieDB();
	static HashMap<Long, Theatre> theatreDB = InMemoryDatabase.getTheatreDB();
	static HashMap<Long, Show> showDB = InMemoryDatabase.getShowDB();

	TicketService ticketServiceObj = new TicketService();
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
				"----------------------------------------------------------------------------------------------------------------------");
		System.out.printf("| %-8s | %-20s | %-25s | %-15s | %-20s | %-10s |%n", "User ID", "Name", "Email", "Phone",
				"Preferred Location", "Balance");
		System.out.println(
				"----------------------------------------------------------------------------------------------------------------------");
		for (User user : userDB.values()) {
			System.out.printf("| %-8d | %-20s | %-25s | %-15d | %-20s | %-10.2f |%n", user.getUserId(),
					user.getUserName(), user.getUserEmailId(), user.getUserPhoneNumber(),
					user.getUserPreferredLocation(), user.getBalance());
		}

		System.out.println(
				"----------------------------------------------------------------------------------------------------------------------");
	}

	public void editMovieDetails() {
		ticketServiceObj.printAllMovies();
		System.out.println("Enter the Movie Id : ");
		Long movieId = Input.getLong((long) movieDB.size());
		Movies movie = movieDB.get(movieId);
		String[] editFeatures = { "1 . Change Movie Title", "2 . Change Movie Duration", "3 . Change Movie Genre",
				"4 . Change Release Year", "5 . Exit" };
		for (String edit : editFeatures) {
			System.out.println(edit);
		}
		int choice = Input.getInteger(editFeatures.length);
		switch (choice) {
		case 1:
			System.out.println("You Have Selected Change Movie Title");
			System.out.println("Old Title : " + movie.getMovieTitle());
			Scanner sc = Input.getScanner();
			System.out.println("Enter the New Title (or Type 'done' to Exit): ");
			String newTitle = sc.nextLine();
			if (newTitle.equalsIgnoreCase("done")) {
				System.out.println("------Back------");
				return;
			}
			movie.setMovieTitle(newTitle);
			System.out.println("Movie Title Changed");
			break;
		case 2:
			System.out.println("You Have Selected Change Movie Duration");
			System.out.println("Enter the Duration (or Type '0' to Exit):");
			int newDuration = Input.getInteger(500);
			if (newDuration == 0) {
				System.out.println("------Back------");
				return;
			}

			movie.setDuration(newDuration);
			System.out.println("Movie Duration Changed");
			break;
		case 3:
			System.out.println("You Have Selected Change Movie Genre");
			HashSet<String> genres = new HashSet<>();
			for (Movies movies : movieDB.values()) {
				genres.add(movies.getGenre());
			}
			int count = 1;
			HashMap<Integer, String> getmap = new HashMap<>();
			for (String str : genres) {
				System.out.println(count + " " + str);
				getmap.put(count, str);
				count++;
			}
			System.out.println("Enter genre Number (or Type '0' to Exit): ");
			int genre = Input.getInteger(count - 1);

			if (genre == 0) {
				System.out.println("------Back------");
				return;
			}
			movie.setGenre(getmap.get(genre));
			System.out.println("Movie Genre Changed");
			break;
		case 4:
			System.out.println("You Have Selected Change Release Year (or Type '0' to Exit):");
			int newReleaseYear = Input.getInteger(2030);
			if (newReleaseYear == 0) {
				System.out.println("------Back------");
				return;
			}
			movie.setReleaseYear(newReleaseYear);
			System.out.println("Movie Release Year Changed");
			break;
		case 5:
			System.out.println("You Have Selected Exit");
			break;

		}

	}

	public void searchUserByName() {
		boolean found = false;
		Scanner sc = Input.getScanner();
		String name;
		System.out.println("Enter User Name (or Type 'done' to Exit):");
		while (true) {
			name = sc.nextLine();
			if (name.equalsIgnoreCase("Done")) {
				System.out.println("------Back------");
				return;
			}
			if (name != null && name.length() > 1) {
				break;
			} else {
				System.err.println("Invalid input please ReEnter the Name ");
			}
		}
		System.out.println(
				"------------------------------------------------------------------------------------------------------------------------");
		System.out.printf("| %-8s | %-20s | %-25s | %-15s | %-20s | %-10s |%n", "User ID", "Name", "Email", "Phone",
				"Preferred Location", "Balance");
		System.out.println(
				"------------------------------------------------------------------------------------------------------------------------");
		for (User user : userDB.values()) {
			if (user.getUserName().toLowerCase().startsWith(name.toLowerCase())) {
				found = true;
				System.out.printf("| %-8d | %-20s | %-25s | %-15d | %-20s | %-10.2f |%n", user.getUserId(),
						user.getUserName(), user.getUserEmailId(), user.getUserPhoneNumber(),
						user.getUserPreferredLocation(), user.getBalance());
			}
		}
		System.out.println(
				"------------------------------------------------------------------------------------------------------------------------");
		if (!found) {
			System.out.println("No users found with name: " + name);
		}
	}

	public void searchUserById() {
		System.out.println("Enter the User ID (or press 0 to Exit): ");
		Long userId = Input.getLong((long) userDB.size());
		if (userId == 0) {
			System.out.println("------Back------");
			return;
		}
		if (userDB.containsKey(userId)) {
			User user = userDB.get(userId);

			System.out.println(
					"------------------------------------------------------------------------------------------------------------------------");
			System.out.printf("| %-8s | %-20s | %-25s | %-15s | %-20s | %-10s |%n", "User ID", "Name", "Email", "Phone",
					"Preferred Location", "Balance");
			System.out.println(
					"------------------------------------------------------------------------------------------------------------------------");

			System.out.printf("| %-8d | %-20s | %-25s | %-15d | %-20s | %-10.2f |%n", user.getUserId(),
					user.getUserName(), user.getUserEmailId(), user.getUserPhoneNumber(),
					user.getUserPreferredLocation(), user.getBalance());
			System.out.println(
					"------------------------------------------------------------------------------------------------------------------------");
		} else {
			System.out.println("User with ID " + userId + " not found.");
		}
	}

	public void viewAllTheatreAdmins() {
		System.out.println(LINE);
		System.out.printf(HEADER_FMT, "ID", "Name", "Email", "Phone", "TID", "Theatre", "City");
		System.out.println(LINE);

		for (TheatreAdmin admin : theatreAdminDB.values()) {
			List<Theatre> theatres = admin.getTheatre();
			if (theatres == null || theatres.isEmpty()) {
				System.out.printf(ROW_FMT, admin.getTheatreAdminId(), safe(admin.getTheatreAdminName()),
						safe(admin.getTheatreAdminEmailId()), String.valueOf(admin.getTheatreAdminPhoneNumber()), "-",
						"N/A", "N/A");
				continue;
			}
			for (Theatre t : theatres) {
				System.out.printf(ROW_FMT, admin.getTheatreAdminId(), safe(admin.getTheatreAdminName()),
						safe(admin.getTheatreAdminEmailId()), String.valueOf(admin.getTheatreAdminPhoneNumber()),
						(t != null ? String.valueOf(t.getTheatreId()) : "-"),
						(t != null ? safe(t.getTheatreName()) : "N/A"),
						(t != null ? safe(t.getTheatreLocation()) : "N/A"));
			}
		}

		System.out.println(LINE);
	}

	public void searchTheatreAdminById() {
		System.out.print("Enter Theatre Admin ID (or press 0 to Exit): ");
		Long id = Input.getLong((long) theatreAdminDB.size());
		if (id == 0) {
			System.out.println("------Back------");
			return;
		}
		TheatreAdmin admin = theatreAdminDB.get(id);
		if (admin == null) {
			System.out.println("No Theatre Admin found with ID: " + id);
			return;
		}
		List<Theatre> theatre = admin.getTheatre();
		System.out.println(LINE);
		System.out.printf(HEADER_FMT, "ID", "Name", "Email", "Phone", "TID", "Theatre", "City");
		System.out.println(LINE);
		for (Theatre t : theatre) {
			System.out.printf(ROW_FMT, admin.getTheatreAdminId(), safe(admin.getTheatreAdminName()),
					safe(admin.getTheatreAdminEmailId()), String.valueOf(admin.getTheatreAdminPhoneNumber()),
					(t != null ? String.valueOf(t.getTheatreId()) : "-"),
					(t != null ? safe(t.getTheatreName()) : "N/A"), (t != null ? safe(t.getTheatreLocation()) : "N/A"));
		}

		System.out.println(LINE);
	}

	public void searchTheatreAdminByName() {
		System.out.print("Enter Theatre Admin Name (or Type 'done' to Exit ): ");
		Scanner sc = Input.getScanner();
		String name;
		while (true) {
			name = sc.nextLine();
			if (name.equalsIgnoreCase("Done")) {
				System.out.println("------Back------");
				return;
			}
			if (name != null && name.trim().length() > 1)
				break;
			System.err.print("Invalid input. Please re-enter a name (min 2 chars): ");
		}
		String q = name.trim();

		boolean found = false;
		System.out.println(LINE);
		System.out.printf(HEADER_FMT, "ID", "Name", "Email", "Phone", "TID", "Theatre", "City");
		System.out.println(LINE);

		for (TheatreAdmin admin : theatreAdminDB.values()) {
			if (admin.getTheatreAdminName() != null
					&& admin.getTheatreAdminName().toLowerCase().startsWith(name.toLowerCase())) {

				List<Theatre> theatre = admin.getTheatre();
				for (Theatre t : theatre) {
					System.out.printf(ROW_FMT, admin.getTheatreAdminId(), safe(admin.getTheatreAdminName()),
							safe(admin.getTheatreAdminEmailId()), String.valueOf(admin.getTheatreAdminPhoneNumber()),
							(t != null ? String.valueOf(t.getTheatreId()) : "-"),
							(t != null ? safe(t.getTheatreName()) : "N/A"),
							(t != null ? safe(t.getTheatreLocation()) : "N/A"));
					found = true;
				}
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
			System.out.print("Enter Movie Title (or Type 'done' to Exit ): ");
			title = sc.nextLine().trim();
			if (title.equalsIgnoreCase("done")) {
				System.out.println("------Back------");
				return;
			}
			if (!title.isEmpty())
				break;
			System.err.println("Title cannot be empty. Please try again.");
		}
		int duration;
		while (true) {
			System.out.print("Enter Duration (in minutes) (or Type 'done' to Exit ): ");
			String line = sc.nextLine().trim();
			if (line.equalsIgnoreCase("done")) {
				System.out.println("------Back------");
				return;
			}
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
			System.out.print(
					"Enter choice (1-" + allowedGenres.size() + ") or type the genre (or Type 'done' to Exit ): ");
			String g = sc.nextLine().trim();
			if (g.equalsIgnoreCase("done")) {
				System.out.println("------Back------");
				return;
			}
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
		String language = null;
		while (true) {
			System.out.println("Choose a Language (or Type 'done' to Exit ):");
			for (int i = 0; i < validLanguages.size(); i++) {
				System.out.println((i + 1) + ". " + validLanguages.get(i));
			}
			System.out.print("Enter language (number or name) (or Type 'done' to Exit ): ");
			String input = sc.nextLine().trim();
			if (input.equalsIgnoreCase("done")) {
				System.out.println("------Back------");
				return;
			}
			if (input.matches("\\d+")) {
				int choice = Integer.parseInt(input);
				if (choice >= 1 && choice <= validLanguages.size()) {
					language = validLanguages.get(choice - 1);
					break;
				}
			} else {
				boolean isValid = validLanguages.stream().anyMatch(lang -> lang.equalsIgnoreCase(input));

				if (isValid) {
					for (String lang : validLanguages) {
						if (lang.equalsIgnoreCase(input)) {
							language = lang;
							break;
						}
					}
					break;
				}
			}

			System.err.println("Invalid choice. Please enter a number or valid language name.");
		}
		int releaseYear;
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
		while (true) {
			System.out.print("Enter Release Year (or Type 'done' to Exit ): ");
			String line = sc.nextLine().trim();
			if (line.equalsIgnoreCase("done")) {
				System.out.println("------Back------");
				return;
			}
			try {
				releaseYear = Integer.parseInt(line);
				if (releaseYear >= 1900 && releaseYear <= currentYear + 1)
					break;
				System.err.printf("Release year must be between 1900 and %d.%n", currentYear + 1);
			} catch (NumberFormatException e) {
				System.err.println("Please enter a valid year.");
			}
		}
		Movies movie = new Movies(movieId, title, duration, genre, language, releaseYear);

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
			System.out.print("Enter Theatre Admin Name (or Type 'done' to Exit): ");
			name = sc.nextLine().trim();
			if (name.equalsIgnoreCase("done")) {
				System.out.println("------Back------");
				return;
			}
			if (!name.isEmpty()) {
				admin.setTheatreAdminName(name);
				break;
			}
			System.err.println("Name cannot be empty. Please re-enter.");
		}
		String email;
		while (true) {
			System.out.print("Enter Theatre Admin Email (or Type 'done' to Exit): ");
			email = sc.nextLine().trim();
			if (email.equalsIgnoreCase("done")) {
				System.out.println("------Back------");
				return;
			}
			if (isValidEmail(email)) {
				admin.setTheatreAdminEmailId(email);
				break;
			}
			System.err.println("Invalid email format. Please re-enter.");
		}
		Long phone = null;
		while (true) {
			System.out.print("Enter Theatre Admin Phone (or Type 'done' to Exit): ");
			String input = sc.nextLine().trim();
			if (input.equalsIgnoreCase("done")) {
				System.out.println("------Back------");
				return;
			}
			if (input.matches("\\d{6,12}")) {
				phone = Long.parseLong(input);

				admin.setTheatreAdminPhoneNumber(phone);
				break;
			}
			System.err.println("Invalid phone number. Must be 10 digits.");
		}
		String password;
		while (true) {
			System.out.print("Enter Theatre Admin Password (min 6 chars) (or Type 'done' to Exit): ");
			password = sc.nextLine().trim();
			if (password.equalsIgnoreCase("done")) {
				System.out.println("------Back------");
				return;
			}
			if (password.length() >= 6) {
				admin.setTheatreAdminPassword(password);
				break;
			}
			System.err.println("Password must be at least 6 characters.");
		}
		Theatre theatre = null;

		String timeZone = TimeZoneConverter.selectTimeZone();
		admin.setTimeZone(timeZone);
		theatreAdminDB.put(admin.getTheatreAdminId(), admin);
		System.out.println("Theatre Admin added successfully: " + admin.getTheatreAdminName());
	}

	private static boolean isValidEmail(String email) {
		String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
		Pattern pattern = Pattern.compile(regex);
		return pattern.matcher(email).matches();
	}

	public void printAllShows(String timeZone) {
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
	            .withZone(ZoneId.of(timeZone));

	    System.out.println("+---------+--------------------------+------------------------+-----------+-------------------+-----------+");
	    System.out.println("| Show ID | Theatre                  | Movie                  | Screen    | Date & Time       | Status    |");
	    System.out.println("+---------+--------------------------+------------------------+-----------+-------------------+-----------+");

	    for (Map.Entry<Long, Show> entry : showDB.entrySet()) {
	        Show s = entry.getValue();

	        String dateTime = formatter.format(Instant.ofEpochSecond(s.getDateTimeEpoch()));
	       

	        System.out.printf("| %-7d | %-20s | %-26s | %-9s | %-17s | %-9s |\n",
	                s.getShowId(),
	                s.getTheatre().getTheatreName(),
	                s.getMovie().getMovieTitle(),
	                s.getScreen().getScreenNumber(),
	                dateTime,
	                s.isActive());
	    }

	    System.out.println("+---------+--------------------------+------------------------+-----------+-------------------+-----------+");
	}

}
