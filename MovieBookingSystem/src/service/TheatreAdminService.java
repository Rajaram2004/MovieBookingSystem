package service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import model.Movies;
import model.Screen;
import model.Show;
import model.Theatre;
import model.TheatreAdmin;
import repository.InMemoryDatabase;
import util.Input;

public class TheatreAdminService {
	static HashMap<Long, TheatreAdmin> theatreAdminDB = InMemoryDatabase.getTheatreAdminDB();
	static HashMap<Long, Theatre> theatreDB = InMemoryDatabase.getTheatreDB();
	static HashMap<Long, Movies> movieDB = InMemoryDatabase.getMovieDB();
	static HashMap<Long, Show> showDB = InMemoryDatabase.getShowDB();
	Scanner sc = Input.getScanner();

	public TheatreAdminService() {
	}

	public void editTheatreAdminDetails(TheatreAdmin theatreAdmin) {
		String details[] = { "1 . Edit Theatre Admin Name ", "2 . Edit Theatre Admin Phone Number",
				"3 . Change Theatre Admin Password" };

		for (int i = 0; i < details.length; i++) {
			System.out.println(details[i]);
		}
		int choice = 0;
		System.out.println("Enter Your Choice : ");
		choice = Input.getInteger(details.length);
		switch (choice) {
		case 1:
			System.out.println("You Have Selected Edit Theatre Admin Name ");
			editTheatreAdminName(theatreAdmin);
			break;
		case 2:
			System.out.println("You Have Selected Edit Theatre Admin Phone Number ");
			editTheatreAdminPhoneNumber(theatreAdmin);
			break;
		case 3:
			System.out.println("You Have Selected Edit Theatre Admin Password ");
			changeTheatreAdminPassword(theatreAdmin);
			break;
		}
	}

	private void changeTheatreAdminPassword(TheatreAdmin theatreAdmin) {
		Scanner sc = new Scanner(System.in);
		String newPassword, confirmNewPassword;
		while (true) {
			System.out.print("Enter new password (min 6 chars, at least 1 digit, 1 uppercase): ");
			newPassword = sc.nextLine();
			if (isValidPassword(newPassword)) {
				System.out.print("Enter new password to Confirm ");
				confirmNewPassword = sc.nextLine();
				if (confirmNewPassword.equals(newPassword)) {
					theatreAdmin.setTheatreAdminPassword(newPassword);
					System.out.println("Password updated successfully!");
					break;
				} else {
					System.err.println("Invalid Confirm Password .");
				}
			} else {
				System.err.println("Invalid password format. Please try again.");
			}
		}
	}

	private void editTheatreAdminPhoneNumber(TheatreAdmin theatreAdmin) {
		Scanner sc = new Scanner(System.in);
		String newPhone;
		while (true) {
			System.out.print("Enter new phone number (6 digits): ");
			newPhone = sc.nextLine();
			if (isValidPhoneNumber(newPhone)) {
				theatreAdmin.setTheatreAdminPhoneNumber(Long.parseLong(newPhone));
				System.out.println("Phone number updated successfully!");
				break;
			} else {
				System.out.println("Invalid phone number. Please try again.");
			}
		}
	}

	private void editTheatreAdminName(TheatreAdmin theatreAdmin) {
		Scanner sc = Input.getScanner();
		String newName;
		while (true) {
			System.out.print("Enter new username (only alphabets & spaces allowed): ");
			newName = sc.nextLine();
			if (isValidName(newName)) {
				theatreAdmin.setTheatreAdminName(newName);
				System.out.println("Name updated successfully!");
				break;
			} else {
				System.out.println("Invalid name. Please try again.");
			}
		}
	}

	private boolean isValidPassword(String password) {
		return password != null && password.matches("^(?=.*[0-9])(?=.*[A-Z]).{6,}$");
	}

	private boolean isValidPhoneNumber(String phone) {
		return phone != null && phone.matches("^[0-9]{6,12}$");
	}

	private boolean isValidName(String name) {
		return name != null && name.matches("^[A-Za-z ]+$");
	}
	// -------------------------------------------------------------------------------------------------------------------------------------------------------------------

	public void addShow(TheatreAdmin theatreAdmin) {
		Theatre theatre = theatreAdmin.getTheatre();
		Movies movie = getValidMovie();
		long epochTime = getValidDateTime();
		Screen screen = getValidScreen(theatre);

		long showId = (long) showDB.size() + 1;
		Show show = new Show(showId, theatre, movie, screen, (int) epochTime, null);

		if (isValidShowTime(theatre, screen, epochTime, movie.getDuration())) {
			theatre.addShow(show);
			movie.addTheatreToListOfThetare(theatre);
			System.out.println("Show Successfully Created and ID is " + showId);
			showDB.put(showId, show);
		} else {
			System.err.println("Error: Show time collides with an existing show on this screen!");
		}
	}
	// -------------------------------------------------------------------------------------------------------------------------------------------------------------------

	public void searchShowId(String timeZone) {
		System.out.println("Enter the Show Id You want to Search : ");
		Long searchShowId = Input.getLong((long) showDB.size());
		Show s = showDB.get(searchShowId);

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(ZoneId.of(timeZone));
		System.out.println("+---------+--------------+--------------+-----------+-------------------+");
		System.out.println("| Show ID | Theatre      | Movie        | Screen    | Date & Time       |");
		System.out.println("+---------+--------------+--------------+-----------+-------------------+");

		String dateTime = formatter.format(Instant.ofEpochSecond(s.getDateTimeEpoch()));
		System.out.printf("| %-7d | %-12s | %-12s | %-9s | %-17s |\n", s.getShowId(), s.getTheatre().getTheatreName(),
				s.getMovie().getMovieTitle(), s.getScreen().getScreenNumber(), dateTime);

		System.out.println("+---------+--------------+--------------+-----------+-------------------+");

	}

	private Screen getValidScreen(Theatre theatre) {

		while (true) {
			System.out.println("Available Screen: ");
			List<Integer> list = new ArrayList<>();
			for (Screen s : theatre.getListOfScreen()) {
				list.add(s.getScreenNumber());
			}
			System.out.println(list);
			System.out.print("Enter Screen Number : ");

			String input = sc.nextLine();
			try {
				Long screenNumber = Long.parseLong(input);
				for (Screen s : theatre.getListOfScreen()) {
					if (s.getScreenNumber() == screenNumber) {
						return s;
					}
				}
			}catch(NumberFormatException e) {
				System.err.println("NumberFormatException");
			}
			System.err.println("Invalid input ");
		}
	}

	private Movies getValidMovie() {
		while (true) {
			System.out.println("Available Movies: ");
			movieDB.forEach((id, m) -> System.out.println(id + " - " + m.getMovieTitle()));
			System.out.print("Enter Movie ID: ");

			String input = sc.nextLine();
			try {
				long movieId = Integer.parseInt(input);
				if (movieDB.containsKey(movieId)) {
					return movieDB.get(movieId);
				} else {
					System.out.println("Invalid Movie ID. Try again.");
				}
			} catch (NumberFormatException e) {
				System.out.println("Please enter a valid number.");
			}
		}
	}

	private boolean isValidShowTime(Theatre theatre, Screen screen, long newShowStart, int newMovieDuration) {
		for (Show s : theatre.getListOfShow()) {
			if (s.getScreen().equals(screen)) {
				long lastShowStart = s.getDateTimeEpoch();
				long existingShowEnd = lastShowStart + (s.getMovie().getDuration() * 60) + (20 * 60);
				if (newShowStart < existingShowEnd && newShowStart >= lastShowStart) {
					return false;
				}
			}
		}
		return true;
	}

	private long getValidDateTime() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		while (true) {
			System.out.print("Enter Show Date & Time (yyyy-MM-dd HH:mm): ");
			String input = sc.nextLine();
			try {
				LocalDateTime dateTime = LocalDateTime.parse(input, formatter);
				if (dateTime.isAfter(LocalDateTime.now())) {
					return dateTime.atZone(ZoneId.systemDefault()).toEpochSecond();
				} else {
					System.out.println("Date/time must be in the future. Please try again.");
				}
			} catch (DateTimeParseException e) {
				System.out.println("Invalid date/time format. Please try again.");
			}
		}
	}

	public void printShows(TheatreAdmin theatreAdmin, String timeZone) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(ZoneId.of(timeZone));
		System.out.println(
				"+---------+-------------------+---------------------------+-----------+----------------------+");
		System.out.println(
				"| Show ID | Theatre           | Movie                     | Screen    | Date & Time          |");
		System.out.println(
				"+---------+-------------------+---------------------------+-----------+----------------------+");

		for (Map.Entry<Long, Show> entry : showDB.entrySet()) {
			Show s = entry.getValue();
			if (s.getTheatre().getTheatreId() == theatreAdmin.getTheatre().getTheatreId()) {
				String dateTime = formatter.format(Instant.ofEpochSecond(s.getDateTimeEpoch()));
				System.out.printf("| %-7d | %-17s | %-22s | %-9s | %-20s |\n", s.getShowId(),
						s.getTheatre().getTheatreName(), s.getMovie().getMovieTitle(), s.getScreen().getScreenNumber(),
						dateTime);
			}
		}
		System.out.println(
				"+---------+-------------------+---------------------------+-----------+----------------------+");
	}

	public void printShowFuture(TheatreAdmin theatreAdmin, String timeZone) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(ZoneId.of(timeZone));
		System.out.println(
				"+---------+-------------------+---------------------------+-----------+----------------------+");
		System.out.println(
				"| Show ID | Theatre           | Movie                     | Screen    | Date & Time          |");
		System.out.println(
				"+---------+-------------------+---------------------------+-----------+----------------------+");

		for (Map.Entry<Long, Show> entry : showDB.entrySet()) {
			Show s = entry.getValue();
			if (s.getTheatre().getTheatreId() == theatreAdmin.getTheatre().getTheatreId()
					&& s.getDateTimeEpoch() > Instant.now().getEpochSecond()) {
				String dateTime = formatter.format(Instant.ofEpochSecond(s.getDateTimeEpoch()));
				System.out.printf("| %-7d | %-17s | %-22s | %-9s | %-20s |\n", s.getShowId(),
						s.getTheatre().getTheatreName(), s.getMovie().getMovieTitle(), s.getScreen().getScreenNumber(),
						dateTime);
			}
		}
		System.out.println(
				"+---------+-------------------+---------------------------+-----------+----------------------+");
	}

	public void printShowAllFuture(String timeZone) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(ZoneId.of(timeZone));
		System.out.println(
				"+---------+-------------------+---------------------------+-----------+----------------------+");
		System.out.println(
				"| Show ID | Theatre           | Movie                     | Screen    | Date & Time          |");
		System.out.println(
				"+---------+-------------------+---------------------------+-----------+----------------------+");

		for (Map.Entry<Long, Show> entry : showDB.entrySet()) {
			Show s = entry.getValue();
			if (s.getDateTimeEpoch() > Instant.now().getEpochSecond()) {
				String dateTime = formatter.format(Instant.ofEpochSecond(s.getDateTimeEpoch()));
				System.out.printf("| %-7d | %-17s | %-22s | %-9s | %-20s |\n", s.getShowId(),
						s.getTheatre().getTheatreName(), s.getMovie().getMovieTitle(), s.getScreen().getScreenNumber(),
						dateTime);
			}
		}
		System.out.println(
				"+---------+-------------------+---------------------------+-----------+----------------------+");
	}

	public void addScreen(TheatreAdmin theatreAdmin) {
		Theatre thetare = theatreAdmin.getTheatre();

		System.out.println("Enter the Number of Rows : ");
		int row = Input.getInteger(15);
		System.out.println("Enter the Number of columns : ");
		int col = Input.getInteger(15);

		thetare.addScreen(row, col);

	}

	public void addNewTheatre() {
		long theatreId = theatreDB.size()+1;
		 String theatreName,theatreLocation;
		 System.out.print("Enter Theatre Name: ");
		     theatreName = sc.nextLine().trim();
		    while (theatreName.isEmpty()) {
		        System.out.print("Theatre name cannot be empty. Enter again: ");
		        theatreName = sc.nextLine().trim();
		    }

		    System.out.print("Enter Theatre Location: ");
		     theatreLocation = sc.nextLine().trim();
		    while (theatreLocation.isEmpty()) {
		        System.out.print("Theatre location cannot be empty. Enter again: ");
		        theatreLocation = sc.nextLine().trim();
		    }
		    
		    ArrayList<Screen> screen = new ArrayList<>();
		    int count =1;
		    while(true) {
		    	System.out.print("1 . Add Screen \n2 . Exit");
		    	int currChoice=Input.getInteger(2);
		    	if(currChoice==1) {
		    		System.out.println("Enter the Number of row : ");
			    	int row = Input.getInteger(15);
			    	System.out.println("Enter the Number of col : ");
			    	int col = Input.getInteger(15);
			    	screen.add(new Screen(count,null,row,col));
			    	count++;
		    	}else {
		    		break;
		    	}
		    	
		    }
		    
		    theatreDB.put(theatreId, new Theatre(theatreId,theatreName,theatreLocation,screen,new ArrayList<>()));
		    System.out.println("Theatre successfully Created and Theatre Id : "+theatreId);
		
	}
}
