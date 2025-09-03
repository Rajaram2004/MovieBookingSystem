package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import dao.MovieDAO;
import dao.ShowDAO;
import dao.UserDAO;
import model.Movies;
import model.Show;
import model.Theatre;
import model.TheatreAdmin;
import model.User;
import util.Input;

public class AdminService {

	PrinterService PrinterServiceObj = new PrinterService();

	TicketService ticketServiceObj = new TicketService();
	private static final String LINE = "+-----+----------------------+---------------------------+-----------------+------+------------------------+-----------------+";
	private static final String HEADER_FMT = "| %-3s | %-20s | %-25s | %-15s | %-4s | %-22s | %-15s |%n";
	private static final String ROW_FMT = "| %-3d | %-20s | %-25s | %-15s | %-4s | %-22s | %-15s |%n";

	public AdminService() {
	}

	public void viewAllUsers() {
		Connection conn = Input.getConnection();
		try {
			conn.setAutoCommit(true);

			List<User> userList = UserDAO.getAllUser(conn);
			if (userList.isEmpty()) {
				System.err.println("No users found in the system.");
				return;
			}
			System.out.println(
					"----------------------------------------------------------------------------------------------------------------------");
			System.out.printf("| %-8s | %-20s | %-25s | %-15s | %-20s | %-10s |%n", "User ID", "Name", "Email", "Phone",
					" Role ", "Balance");
			System.out.println(
					"----------------------------------------------------------------------------------------------------------------------");
			for (User user : userList) {
				System.out.printf("| %-8d | %-20s | %-25s | %-15d | %-20s | %-10.2f |%n", user.getId(), user.getName(),
						user.getEmailId(), user.getPhoneNumber(), user.getUserType(), user.getBalance());
			}
			System.out.println(
					"----------------------------------------------------------------------------------------------------------------------");
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void searchUserById() {
		System.out.println("Enter the User ID (or press 0 to Exit): ");
		try {
			Connection conn = Input.getConnection();
			conn.setAutoCommit(false);
			List<User> userList = UserDAO.getAllUser(conn);

			Long userId = Input.getLong((long) 1000000000);
			if (userId == 0) {
				System.out.println("------Back------");
				return;
			}
			User user = null;
			boolean found = false;
			for (User u : userList) {
				if (u.getId() == userId) {
					found = true;
					user = u;
					break;
				}
			}
			if (found == false) {
				System.out.println("User with ID " + userId + " not found.");
				return;
			}
			System.out.println(
					"------------------------------------------------------------------------------------------------------------------------");
			System.out.printf("| %-8s | %-20s | %-25s | %-15s | %-20s | %-10s |%n", "User ID", "Name", "Email", "Phone",
					" Role ", "Balance");
			System.out.println(
					"------------------------------------------------------------------------------------------------------------------------");

			System.out.printf("| %-8d | %-20s | %-25s | %-15d | %-20s | %-10.2f |%n", user.getId(), user.getName(),
					user.getEmailId(), user.getPhoneNumber(), user.getUserType(), user.getBalance());
			System.out.println(
					"------------------------------------------------------------------------------------------------------------------------");
			conn.setAutoCommit(true);
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void searchUserByName() {
		Connection conn = Input.getConnection();
		try {
			List<User> userList = UserDAO.getAllUser(conn);
			if (userList.isEmpty()) {
				System.err.println("No users found in the system.");
				return;
			}
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
					" Role ", "Balance");
			System.out.println(
					"------------------------------------------------------------------------------------------------------------------------");
			for (User user : userList) {
				if (user.getName().toLowerCase().startsWith(name.toLowerCase())) {
					found = true;
					System.out.printf("| %-8d | %-20s | %-25s | %-15d | %-20s | %-10.2f |%n", user.getId(),
							user.getName(), user.getEmailId(), user.getPhoneNumber(), user.getUserType(),
							user.getBalance());
				}
			}
			System.out.println(
					"------------------------------------------------------------------------------------------------------------------------");
			if (!found) {
				System.out.println("No users found with name: " + name);
			}
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void editMovieDetails() {
		Connection conn = null;
		try {
			conn = Input.getConnection();
			List<Movies> movieList = PrinterServiceObj.printAllMovies1(conn,1);
			if (movieList == null || movieList.isEmpty()) {
				System.out.println("No movies found in the database.");
				return;
			}
			System.out.print("Enter the Movie ID: ");
			Long movieId = Input.getLong((long) movieList.getLast().getMovieId());

			Movies movie = null;
			for (Movies m : movieList) {
				if (m.getMovieId() == movieId) {
					movie = m;
					break;
				}
			}
			if (movie == null) {
				System.err.println("Movie with ID " + movieId + " not found!");
				return;
			}
			String[] editFeatures = { "1. Change Movie Title", "2. Change Movie Duration", "3. Change Movie Genre",
					"4. Change Release Date", "5. Exit" };
			for (String edit : editFeatures) {
				System.out.println(edit);
			}
			int choice = Input.getInteger(editFeatures.length);
			Scanner sc = Input.getScanner();
			String sql = null;
			PreparedStatement pstmt = null;
			switch (choice) {
			case 1:
				System.out.println("You Have Selected Change Movie Title");
				System.out.println("Old Title: " + movie.getMovieTitle());
				System.out.print("Enter the New Title (or Type 'done' to Exit): ");
				String newTitle = sc.nextLine();
				if (newTitle.equalsIgnoreCase("done")) {
					System.out.println("------Back------");
					return;
				}
				sql = "UPDATE movies SET movieName = ? WHERE movieId = ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, newTitle);
				pstmt.setLong(2, movieId);
				pstmt.executeUpdate();
				System.out.println("Movie Title Updated Successfully!");
				break;
			case 2:
				System.out.println("You Have Selected Change Movie Duration");
				System.out.print("Enter the Duration in Minutes (or Type '0' to Exit): ");
				int newDuration = Input.getInteger(500);
				if (newDuration == 0) {
					System.out.println("------Back------");
					return;
				}
				sql = "UPDATE movies SET duration = ? WHERE movieId = ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, newDuration);
				pstmt.setLong(2, movieId);
				pstmt.executeUpdate();
				System.out.println("Movie Duration Updated Successfully!");
				break;

			case 3:
				System.out.println("You Have Selected Change Movie Genre");
				System.out.println("Available Genres:");
				List<String> genres = getAllGenres(conn);
				for (int i = 0; i < genres.size(); i++) {
					System.out.println((i + 1) + ". " + genres.get(i));
				}
				System.out.print("Enter Genre Number (or Type '0' to Exit): ");
				int genreChoice = Input.getInteger(genres.size());
				if (genreChoice == 0) {
					System.out.println("------Back------");
					return;
				}
				String newGenre = genres.get(genreChoice - 1);
				sql = "UPDATE movies SET genre = ? WHERE movieId = ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, newGenre);
				pstmt.setLong(2, movieId);
				pstmt.executeUpdate();
				System.out.println("Movie Genre Updated Successfully!");
				break;

			case 4:
				System.out.println("You Have Selected Change Release Date");
				System.out.print("Enter Release Date (yyyy-MM-dd) or Type '0' to Exit: ");
				String releaseInput = sc.nextLine().trim();
				if (releaseInput.equalsIgnoreCase("0")) {
					System.out.println("------Back------");
					return;
				}
				try {
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
					LocalDate releaseDate = LocalDate.parse(releaseInput, formatter);
					long epochMillis = releaseDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
					sql = "UPDATE movies SET releaseDate = ? WHERE movieId = ?";
					pstmt = conn.prepareStatement(sql);
					pstmt.setLong(1, epochMillis);
					pstmt.setLong(2, movieId);
					pstmt.executeUpdate();
					System.out.println("Movie Release Date Updated Successfully!");
				} catch (DateTimeParseException e) {
					System.err.println("Invalid date format! Please use yyyy-MM-dd.");
				}
				break;

			case 5:
				System.out.println("Exiting...");
				return;

			default:
				System.err.println("Invalid choice!");
				return;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private List<String> getAllGenres(Connection conn) throws SQLException {
		List<String> genres = new ArrayList<>();
		String sql = "SELECT DISTINCT genre FROM movies";
		try (PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
			while (rs.next()) {
				genres.add(rs.getString("genre"));
			}
		}
		return genres;
	}

	public void viewAllTheatreAdmins() {
		try (Connection conn = Input.getConnection()) {
			System.out.println(LINE);
			System.out.printf(HEADER_FMT, "ID", "Name", "Email", "Phone", "TID", "Theatre", "City");
			System.out.println(LINE);
			List<User> userList = UserDAO.getAllUser(conn);
			List<TheatreAdmin> theatreAdminList = userList.stream()
					.filter(s -> s.getUserType().equalsIgnoreCase("theatreadmin")).map(u -> {
						TheatreAdmin t = UserDAO.getTheatreAdminById(conn, u.getId());
						t.setTheatre(t.getTheatre(conn));
						return t;
					}).collect(Collectors.toList());
			for (TheatreAdmin admin : theatreAdminList) {
				List<Theatre> theatres = admin.returnListOfTheatre();
				if (theatres == null || theatres.isEmpty()) {
					System.out.printf(ROW_FMT, admin.getTheatreAdminId(), safe(admin.getTheatreAdminName()),
							safe(admin.getTheatreAdminEmailId()), String.valueOf(admin.getTheatreAdminPhoneNumber()),
							"-", "N/A", "N/A");
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void searchTheatreAdminById() {
		Connection conn = Input.getConnection();
		List<User> userList = UserDAO.getAllUser(conn);
		List<TheatreAdmin> theatreAdminList = userList.stream()
				.filter(s -> s.getUserType().equalsIgnoreCase("theatreadmin")).map(u -> {
					TheatreAdmin t = UserDAO.getTheatreAdminById(conn, u.getId());
					t.setTheatre(t.getTheatre(conn)); // Set the associated theatre
					return t;
				}).collect(Collectors.toList());
		System.out.print("Enter Theatre Admin ID (or press 0 to Exit): ");
		Long id = Input.getLong((long) 1000000000);
		if (id == 0) {
			System.out.println("------Back------");
			return;
		}
		TheatreAdmin admin = null;
		for (TheatreAdmin ta : theatreAdminList) {
			if (ta.getTheatreAdminId() == id) {
				admin = ta;
				break;
			}
		}
		if (admin == null) {
			System.out.println("No Theatre Admin found with ID: " + id);
			return;
		}
		List<Theatre> theatre = admin.getTheatre(conn);
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
		try {
			conn.close();
		} catch (SQLException e) {

			e.printStackTrace();
		}
	}

	public void searchTheatreAdminByName() {
		Connection conn = Input.getConnection();
		List<User> userList = UserDAO.getAllUser(conn);
		List<TheatreAdmin> theatreAdminList = userList.stream()
				.filter(s -> s.getUserType().equalsIgnoreCase("theatreadmin")).map(u -> {
					TheatreAdmin t = UserDAO.getTheatreAdminById(conn, u.getId());
					t.setTheatre(t.getTheatre(conn));
					return t;
				}).collect(Collectors.toList());
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

		for (TheatreAdmin admin : theatreAdminList) {
			if (admin.getTheatreAdminName() != null
					&& admin.getTheatreAdminName().toLowerCase().startsWith(name.toLowerCase())) {

				List<Theatre> theatre = admin.getTheatre(conn);
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
		try {
			conn.close();
		} catch (SQLException e) {

			e.printStackTrace();
		}
	}

	private String safe(String s) {
		return (s == null ? "" : s);
	}

	public void addMovie() {
		Scanner sc = Input.getScanner();
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
		List<String> validLanguages = Arrays.asList("Tamil", "Telugu", "English", "Hindi", "Malayalam");
		String language = null;

		while (true) {
			System.out.println("Choose a Language (or Type '0' to Exit):");
			for (int i = 0; i < validLanguages.size(); i++) {
				System.out.println((i + 1) + ". " + validLanguages.get(i));
			}

			System.out.print("Enter language (number or name): ");
			String input = sc.nextLine().trim();

			if (input.equalsIgnoreCase("0")) {
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
				for (String lang : validLanguages) {
					if (lang.equalsIgnoreCase(input)) {
						language = lang;
						break;
					}
				}
				if (language != null)
					break;
			}

			System.err.println("Invalid choice. Please enter a number or a valid language name.");
		}
		System.out.println("Selected Language: " + language);

		LocalDate releaseDate = null;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
		long epochSeconds = 0;
		while (true) {
			System.out.print("Enter Release Date (yyyy-MM-dd) or Type '0' to Exit: ");
			String line = sc.nextLine().trim();

			if (line.equalsIgnoreCase("0")) {
				System.out.println("------Back------");
				return;
			}
			try {
				releaseDate = LocalDate.parse(line, formatter);
				int releaseYear = releaseDate.getYear();
				if (releaseYear >= 1900 && releaseYear <= currentYear + 1) {

					epochSeconds = releaseDate.atStartOfDay(ZoneId.systemDefault()).toEpochSecond();
					break;
				} else {
					System.err.printf("Release year must be between 1900 and %d.%n", currentYear + 1);
				}

			} catch (DateTimeParseException e) {
				System.err.println("Invalid date format! Please enter in yyyy-MM-dd format.");
			}
		}
		Movies movie = new Movies(title, duration, genre, language, epochSeconds);

		Connection conn = Input.getConnection();
		long movieId = MovieDAO.insertMovie(conn, movie);
		if (movieId <= 0) {
			System.err.println("Movie Not Added");
			return;
		}
		movie.setMovieId(movieId);
		System.out.println("\nMovie added successfully!");
		System.out.println("+------+------------------------------+----------+------------+-----------+------+\n"
				+ "| ID   | Title                        | Duration | Genre      | Language  | Year |\n"
				+ "+------+------------------------------+----------+------------+-----------+------+\n"
				+ String.format("| %-4d | %-28s | %-8s | %-10s | %-9s | %-4d |\n", movie.getMovieId(),
						truncate(movie.getMovieTitle(), 28), formatDuration(movie.getDuration()), movie.getGenre(),
						truncate(movie.getLanguage(), 9), movie.getReleaseYear())
				+ "+------+------------------------------+----------+------------+-----------+------+\n");
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
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

	

	public void printAllShows(String timeZone) {
		Connection conn = Input.getConnection();
		List<Show> showList = ShowDAO.getAllShows(conn);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(ZoneId.of(timeZone));

		System.out.println(
				"+---------+--------------------------+------------------------+-----------+-------------------+-----------+");
		System.out.println(
				"| Show ID | Theatre                  | Movie                  | Screen    | Date & Time       | Status    |");
		System.out.println(
				"+---------+--------------------------+------------------------+-----------+-------------------+-----------+");

		for (Show s : showList) {

			String dateTime = formatter.format(Instant.ofEpochMilli(s.getShowDateTime()));

			System.out.printf("| %-7d | %-20s | %-26s | %-9s | %-17s | %-9s |\n", s.getShowId(),
					s.getTheatre().getTheatreName(), s.getMovie().getMovieTitle(), s.getScreen().getScreenName(),
					dateTime, s.getStatus());
		}

		System.out.println(
				"+---------+--------------------------+------------------------+-----------+-------------------+-----------+");
	}

}
