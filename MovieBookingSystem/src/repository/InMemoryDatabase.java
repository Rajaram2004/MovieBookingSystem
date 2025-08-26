package repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import model.Admin;
import model.Movies;
import model.RequestTheatre;
import model.Screen;
import model.Seat;
import model.Show;
import model.Theatre;
import model.TheatreAdmin;
import model.Ticket;
import model.Customer;

public class InMemoryDatabase {
	static HashMap<Long, Customer> userDB = new HashMap<>();
	static HashMap<Long, TheatreAdmin> theatreAdminDB = new HashMap<>();
	static HashMap<Long, Admin> adminDB = new HashMap<>();
	static HashMap<Long, Theatre> theatreDB = new HashMap<>();
	static HashMap<Long, Movies> movieDB = new HashMap<>();
	static HashMap<Long, Show> showDB = new HashMap<>();
	static HashMap<Long, Ticket> ticketDB = new HashMap<>();
	static HashMap<Long, RequestTheatre> RequestTheatreDB = new HashMap<>();

	public InMemoryDatabase() {
		userDB = InMemoryDatabase.getUserDB();
		userDB.put(1L,
				new Customer(1L, "User 1", "user1@example.com", 987654321L, "Chennai", "pass123", 2000, "Asia/Kolkata"));
		userDB.put(2L,
				new Customer(2L, "User 2", "user2@example.com", 123456789L, "Bangalore", "pass456", 20000, "Asia/Kolkata"));
		userDB.put(3L,
				new Customer(3L, "User 3", "user3@example.com", 912345678L, "Mumbai", "pass789", 20000, "Asia/Kolkata"));
		userDB.put(4L, new Customer(4L, "User 4", "user4@example.com", 123456L, "Mumbai", "1", 20000, "Asia/Kolkata"));

		theatreAdminDB.put(1L, new TheatreAdmin(1L, "Admin One", "theatreadmin1@example.com", 987654321L, "pass123",
				new ArrayList<>(), "Asia/Kolkata"));
		theatreAdminDB.put(2L, new TheatreAdmin(2L, "Admin Two", "theatreadmin2@example.com", 123456789L, "pass456",
				new ArrayList<>(), "Asia/Kolkata"));
		theatreAdminDB.put(3L, new TheatreAdmin(3L, "Admin Three", "theatreadmin3@example.com", 912345678L, "pass789",
				new ArrayList<>(), "Asia/Kolkata"));

		adminDB.put(1L, new Admin(1L, "Rajaram", "admin1@example.com", 987654321L, "pass123", "Asia/Kolkata"));
		adminDB.put(2L, new Admin(2L, "Priya", "admin2@example.com", 123456789L, "pass456", "Asia/Kolkata"));
		adminDB.put(3L, new Admin(3L, "Arjun", "admin3@example.com", 912345678L, "pass789", "Asia/Kolkata"));

		initializeMoviesAndTheatres();
	}
//	
//	public void initializeMoviesAndTheatre() {
//		
//		Screen t1s1= new Screen(1,null,10,12);
//		Screen t1s2= new Screen(2,null,10,12);
//		
//		Screen t2s1= new Screen(1,null,10,12);
//		Screen t2s2= new Screen(2,null,10,12);
//		
//		Screen t3s1= new Screen(1,null,10,12);
//		Screen t3s2= new Screen(2,null,10,12);
//		
//		Screen t4s1= new Screen(1,null,10,12);
//		Screen t4s2= new Screen(2,null,10,12);
//		
//		Screen t5s1= new Screen(1,null,10,12);
//		Screen t5s2= new Screen(2,null,10,12);
//		
//		
//		Theatre theatre101 = new Theatre(101L, "Sathyam Cinemas", "Chennai", new ArrayList<>(List.of(t1s1,t1s2)),new ArrayList<>());
//		Theatre theatre102 = new Theatre(102L, "KG Cinemas", "Coimbatore", new ArrayList<>(List.of(t2s1,t2s2)),new ArrayList<>());
//		Theatre theatre103 = new Theatre(103L, "INOX", "Madurai", new ArrayList<>(List.of(t3s1,t3s2)),new ArrayList<>());
//		Theatre theatre104 = new Theatre(104L, "Thangam Cinemas", "Trichy", new ArrayList<>(List.of(t4s1,t4s2)),new ArrayList<>());
//		Theatre theatre105 = new Theatre(105L, "PVR Velachery", "Chennai", new ArrayList<>(List.of(t5s1,t5s2)),new ArrayList<>());
//		
//		Movies m1 = new Movies(1L, "Vikram", 173, "Thriller","Tamil" ,2022, List.of(theatre101,theatre103,theatre104));
//		Movies m2 = new Movies(2L, "Master", 179, "Drama", "Telugu" , 2021, List.of(theatre102,theatre103));
//		Movies m3 = new Movies(3L, "Jailer", 168, "Comedy", "English" , 2023, List.of(theatre101,theatre102));
//		Movies m4 = new Movies(4L, "Leo", 164, "Thriller","Tamil", 2023, List.of(theatre103,theatre105));
//		Movies m5 = new Movies(5L, "RRR", 182, "Action", "Telugu" , 2022, List.of(theatre101,theatre104));
//		Movies m6 = new Movies(6L, "Pushpa: The Rise", 179, "Action", "English", 2021, List.of(theatre101,theatre103,theatre105));
//		Movies m7 = new Movies(7L, "Baahubali: The Beginning", 159, "Drama", "English" , 2015, List.of(theatre101,theatre102));
//		Movies m8 = new Movies(8L, "Baahubali: The Conclusion", 171, "Action"  ,"Tamil", 2017, List.of(theatre103,theatre104));
//		
//		Show t1s1s1 =new Show(1L,theatre101,m1,t1s1,1755682200, null);
//		Show t1s1s2 =new Show(2L,theatre101,m2,t1s2,1755682200, null);
//		Show t1s2s1 =new Show(3L,theatre101,m3,t1s1,1755696600, null);
//		Show t1s2s2 =new Show(4L,theatre101,m5,t1s2,1755696600, null);
//		
//		Show t2s1s1 =new Show(5L,theatre102,m2,t2s1,1755682200, null);
//		Show t2s1s2 =new Show(6L,theatre102,m3,t2s2,1755682200, null);
//		Show t2s2s1 =new Show(7L,theatre102,m4,t2s1,1755696600, null);
//		Show t2s2s2 =new Show(8L,theatre102,m5,t2s2,1755696600, null);
//		
//		Show t3s1s1 =new Show(9L,theatre103,m6,t3s1,1755682200, null);
//		Show t3s1s2 =new Show(10L,theatre103,m7,t3s2,1755682200, null);
//		Show t3s2s1 =new Show(11L,theatre103,m8,t3s1,1755696600, null);
//		Show t3s2s2 =new Show(12L,theatre103,m1,t3s2,1755696600, null);
//		
//		Show t4s1s1 =new Show(13L,theatre104,m4,t4s1,1755682200, null);
//		Show t4s1s2 =new Show(14L,theatre104,m5,t4s2,1755682200, null);
//		Show t4s2s1 =new Show(15L,theatre104,m7,t4s1,1755696600, null);
//		Show t4s2s2 =new Show(16L,theatre104,m8,t4s2,1755696600, null);
//		
//		Show t5s1s1 =new Show(17L,theatre105,m1,t5s1,1755682200, null);
//		Show t5s1s2 =new Show(18L,theatre105,m2,t5s2,1755682200, null);
//		Show t5s2s1 =new Show(19L,theatre105,m6,t5s1,1755696600, null);
//		Show t5s2s2 =new Show(20L,theatre105,m7,t5s2,1755696600, null);
//		
//		theatre101.addShow(t1s1s1);
//		theatre101.addShow(t1s1s2);
//		theatre101.addShow(t1s2s1);
//		theatre101.addShow(t1s2s2);
//		
//		theatre102.addShow(t2s1s1);
//		theatre102.addShow(t2s1s2);
//		theatre102.addShow(t2s2s1);
//		theatre102.addShow(t2s2s2);
//		
//		theatre103.addShow(t3s1s1);
//		theatre103.addShow(t3s1s2);
//		theatre103.addShow(t3s2s1);
//		theatre103.addShow(t3s2s2);
//		
//		theatre104.addShow(t4s1s1);
//		theatre104.addShow(t4s1s2);
//		theatre104.addShow(t4s2s1);
//		theatre104.addShow(t4s2s2);
//
//		theatre105.addShow(t5s1s1);
//		theatre105.addShow(t5s1s2);
//		theatre105.addShow(t5s2s1);
//		theatre105.addShow(t5s2s2);
//		
//		showDB.put(t1s1s1.getShowId(), t1s1s1);
//		showDB.put(t1s1s2.getShowId(), t1s1s2);
//		showDB.put(t1s2s1.getShowId(), t1s2s1);
//		showDB.put(t1s2s2.getShowId(), t1s2s2);
//
//		showDB.put(t2s1s1.getShowId(), t2s1s1);
//		showDB.put(t2s1s2.getShowId(), t2s1s2);
//		showDB.put(t2s2s1.getShowId(), t2s2s1);
//		showDB.put(t2s2s2.getShowId(), t2s2s2);
//
//		showDB.put(t3s1s1.getShowId(), t3s1s1);
//		showDB.put(t3s1s2.getShowId(), t3s1s2);
//		showDB.put(t3s2s1.getShowId(), t3s2s1);
//		showDB.put(t3s2s2.getShowId(), t3s2s2);
//
//		showDB.put(t4s1s1.getShowId(), t4s1s1);
//		showDB.put(t4s1s2.getShowId(), t4s1s2);
//		showDB.put(t4s2s1.getShowId(), t4s2s1);
//		showDB.put(t4s2s2.getShowId(), t4s2s2);
//
//		showDB.put(t5s1s1.getShowId(), t5s1s1);
//		showDB.put(t5s1s2.getShowId(), t5s1s2);
//		showDB.put(t5s2s1.getShowId(), t5s2s1);
//		showDB.put(t5s2s2.getShowId(), t5s2s2);
//		
//		movieDB.put(1L, m1);
//		movieDB.put(2L, m2);
//		movieDB.put(3L, m3);
//		movieDB.put(4L, m4);
//		movieDB.put(5L, m5);
//		movieDB.put(6L, m6);
//		movieDB.put(7L, m7);
//		movieDB.put(8L, m8);
//		
//		theatreDB.put(1L, theatre101);
//		theatreDB.put(2L, theatre102);
//		theatreDB.put(3L, theatre103);
//		theatreDB.put(4L, theatre104);
//		theatreDB.put(5L, theatre105);
//	}

	public void initializeMoviesAndTheatres() {

		// ------------------------- THEATRES -------------------------
		String[] theatreNames = { "Sathyam Cinemas", "KG Cinemas", "INOX", "Thangam Cinemas", "PVR Velachery",
				"PVR Phoenix", "Escape Cinemas", "AVM Cinemas", "SPI Cinemas", "Mayajaal" };
		String[] theatreCities = { "Chennai", "Coimbatore", "Madurai", "Trichy", "Chennai", "Bangalore", "Chennai",
				"Chennai", "Chennai", "Chennai" };

		List<Integer> seatsPerRow = new ArrayList<>(List.of(5, 5, 5, 5, 10, 10, 10, 10, 15, 15, 15));
		List<Double> pricePerRow = new ArrayList<>(List.of(100.0, 100.0, 100.0, 100.0, 100.0, 150.0, 150.0, 150.0,
				150.0, 150.0, 200.0, 200.0, 200.0, 200.0, 200.0));

		List<Theatre> theatres = new ArrayList<>();
		for (int t = 0; t < theatreNames.length; t++) {
			Theatre theatre = null;
			List<Screen> screens = new ArrayList<>();
			for (int s = 1; s <= 3; s++) {
				screens.add(new Screen(s,theatre, seatsPerRow, pricePerRow));
			}

			 theatre = new Theatre(1L + t, theatreNames[t], theatreCities[t], screens, new ArrayList<>(),
					new ArrayList<>(), true);

			theatres.add(theatre);
			theatreDB.put((long) (t + 1), theatre);
		}

		// ------------------------- MOVIES -------------------------
		String[] movieTitles = { "Vikram", "Master", "Jailer", "Leo", "RRR", "Pushpa: The Rise",
				"Baahubali: The Beginning", "Baahubali: The Conclusion", "Pathaan", "Tiger 3", "Drishyam 2",
				"Ponniyin Selvan" };
		String[] genres = { "Action", "Drama", "Comedy", "Thriller", "Action", "Action", "Drama", "Action", "Action",
				"Action", "Thriller", "Historical" };
		String[] languages = { "Tamil", "Telugu", "English", "Tamil", "Telugu", "English", "English", "Tamil", "Hindi",
				"Hindi", "Malayalam", "Tamil" };
		int[] releaseYears = { 2022, 2021, 2023, 2023, 2022, 2021, 2015, 2017, 2023, 2023, 2021, 2022 };

//		long[] releaseEpochs = new long[releaseYears.length];
//
//		for (int i = 0; i < releaseYears.length; i++) {
//			// Create unique dates by using day = index + 1, month = (i % 12) + 1
//			LocalDate date = LocalDate.of(releaseYears[i], (i % 12) + 1, (i % 27) + 1);
//
//			// Convert to epoch seconds
//			releaseEpochs[i] = date.atStartOfDay(ZoneId.systemDefault()).toEpochSecond();
//		}
//
//		// Print release years with their unique epoch timestamps
//		System.out.println("Year  ->  Epoch");
//		for (int i = 0; i < releaseYears.length; i++) {
//			System.out.println(releaseYears[i] + " -> " + releaseEpochs[i]);
//		}

		int[] durations = { 173, 179, 168, 164, 182, 179, 159, 171, 165, 160, 150, 190 };

		List<Movies> movies = new ArrayList<>();
		for (int m = 0; m < movieTitles.length; m++) {
			Movies movie = new Movies((long) (m + 1), movieTitles[m], durations[m], genres[m], languages[m],
					releaseYears[m]);
			movies.add(movie);
			movieDB.put((long) (m + 1), movie);
		}

		// ------------------------- SHOWS -------------------------
		long showId = 1L;
		LocalDate startDate = LocalDate.now().plusDays(1);

		int movieIndex = 0;
		for (Theatre theatre : theatres) {
			for (Screen screen : theatre.getListOfScreen()) {
				Movies movie = movies.get(movieIndex % movies.size());

				if (!theatre.getListOfMovies().contains(movie)) {
					theatre.getListOfMovies().add(movie);
				}
				LocalDate showDate = startDate;
				int[] showHours = { 10, 14, 18 };

				for (int hour : showHours) {
					LocalTime showTime = LocalTime.of(hour, 0);
					ZonedDateTime zdt = ZonedDateTime.of(showDate, showTime, ZoneId.of("Asia/Kolkata"));
					long epoch = (long) zdt.toEpochSecond();

					List<Seat> seats = new ArrayList<>();
					for (List<Seat> row : screen.getSeatsLayout()) {
						for (Seat s : row) {
							seats.add(new Seat(s.getSeatNumber(), s.getPrice()));
						}
					}

					Show show = new Show(showId++, theatre, movie, screen, epoch, seats);
					theatre.addShow(show);
					showDB.put(show.getShowId(), show);
				}

				movieIndex++;
			}
		}

		// ------------------------- ADD PAST SHOWS -------------------------
		long pastShowId = showId;
		LocalDate pastStartDate = LocalDate.now().minusDays(7);

		for (Theatre theatre : theatres) {
			for (Screen screen : theatre.getListOfScreen()) {
				Movies movie = movies.get(movieIndex % movies.size());
				if (!theatre.getListOfMovies().contains(movie)) {
					theatre.getListOfMovies().add(movie);
				}
				for (int day = 0; day < 2; day++) {
					LocalDate pastShowDate = pastStartDate.plusDays(day);
					int[] showHours = { 10, 14, 18 };
					for (int hour : showHours) {
						LocalTime showTime = LocalTime.of(hour, 0);
						ZonedDateTime zdt = ZonedDateTime.of(pastShowDate, showTime, ZoneId.of("Asia/Kolkata"));
						int epoch = (int) zdt.toEpochSecond();
						List<Seat> seats = new ArrayList<>();
						for (List<Seat> row : screen.getSeatsLayout()) {
							for (Seat s : row) {
								seats.add(new Seat(s.getSeatNumber(), s.getPrice()));
							}
						}
						Show pastShow = new Show(pastShowId++, theatre, movie, screen, epoch, seats);
						pastShow.setActive("Completed");
						theatre.addShow(pastShow);
						showDB.put(pastShow.getShowId(), pastShow);
					}
				}
				movieIndex++;
			}
		}

		theatreAdminDB.get(1L).addTheatre(theatreDB.get(1L));
		theatreAdminDB.get(1L).addTheatre(theatreDB.get(2L));

		theatreAdminDB.get(2L).addTheatre(theatreDB.get(3L));
		theatreAdminDB.get(2L).addTheatre(theatreDB.get(4L));

		theatreAdminDB.get(3L).addTheatre(theatreDB.get(5L));
		theatreAdminDB.get(3L).addTheatre(theatreDB.get(6L));
		
		createDummyTickets();

	}

	private void createDummyTickets(){
		Customer user1 = userDB.get(1L);
		Customer user2 = userDB.get(2L);
		Customer user3 = userDB.get(3L);
		
		Show show91 = showDB.get(91L);
		Show show92 = showDB.get(92L);
		Show show93 = showDB.get(93L);
		Show show94 = showDB.get(94L);
		Show show95 = showDB.get(95L);
		
		// Ticket 1

		List<List<Seat>> seatShow91 = show91.getShowSeats();
		List<Seat> seats1 = new ArrayList<>();
		for (List<Seat> row : seatShow91) {
			for (Seat seat : row) {
				if (seat.getSeatNumber().equalsIgnoreCase("A1")) {
					seats1.add(seat);
					seat.setBooked(true);
				}
			}
		}
		Ticket ticket1 = new Ticket(1L, user1, show91, seats1, 400.0,
				"Upcoming");
		ticketDB.put(1L, ticket1);

		List<List<Seat>> seatShow92 = show92.getShowSeats();
		List<Seat> seats2 = new ArrayList<>();
		for (List<Seat> row : seatShow92) {
			for (Seat seat : row) {
				if (seat.getSeatNumber().equalsIgnoreCase("c1") || seat.getSeatNumber().equalsIgnoreCase("c2")
						|| seat.getSeatNumber().equalsIgnoreCase("g1")) {
					seats2.add(seat);
					seat.setBooked(true);
				}
			}
		}
		Ticket ticket2 = new Ticket(2L, user2, show92, seats2,  250.0,
				"Upcoming");
		ticketDB.put(2L, ticket2);

		// Ticket 3
		List<List<Seat>> seatShow93 = show93.getShowSeats();
		List<Seat> seats3 = new ArrayList<>();
		for (List<Seat> row : seatShow93) {
			for (Seat seat : row) {
				if (seat.getSeatNumber().equalsIgnoreCase("b1") || seat.getSeatNumber().equalsIgnoreCase("b2")
						|| seat.getSeatNumber().equalsIgnoreCase("b3")) {
					seats3.add(seat);
					seat.setBooked(true);
				}
			}
		}
		Ticket ticket3 = new Ticket(3L, user3, show93, seats3,  900.0,
				"Upcoming");
		ticketDB.put(3L, ticket3);

		// Ticket 4
		List<List<Seat>> seatShow94 = show94.getShowSeats();
		List<Seat> seats4 = new ArrayList<>();
		for (List<Seat> row : seatShow94) {
			for (Seat seat : row) {
				if (seat.getSeatNumber().equalsIgnoreCase("b1") || seat.getSeatNumber().equalsIgnoreCase("b2")
						|| seat.getSeatNumber().equalsIgnoreCase("b3")) {
					seats4.add(seat);
					seat.setBooked(true);
				}
			}
		}
		Ticket ticket4 = new Ticket(4L, user1, show94, seats4,  180.0,
				"Upcoming");
		ticketDB.put(4L, ticket4);

		// Ticket 5
		List<List<Seat>> seatShow95 = show95.getShowSeats();
		List<Seat> seats5 = new ArrayList<>();
		for (List<Seat> row : seatShow95) {
			for (Seat seat : row) {
				if (seat.getSeatNumber().equalsIgnoreCase("b1") || seat.getSeatNumber().equalsIgnoreCase("b2")
						|| seat.getSeatNumber().equalsIgnoreCase("b3")) {
					seats5.add(seat);
					seat.setBooked(true);
				}
			}
		}
		Ticket ticket5 = new Ticket(5L, user2, show95, seats5,  440.0,
				"Upcoming");
		ticketDB.put(5L, ticket5);

		// Ticket 6

		List<Seat> seats6 = new ArrayList<>();
		for (List<Seat> row : seatShow91) {
			for (Seat seat : row) {
				if (seat.getSeatNumber().equalsIgnoreCase("a4") || seat.getSeatNumber().equalsIgnoreCase("a5")) {
					seats6.add(seat);
					seat.setBooked(true);
				}
			}
		}
		Ticket ticket6 = new Ticket(6L, user3, show91, seats6,  200.0,
				"Upcoming");
		ticketDB.put(6L, ticket6);

		// Ticket 7
		List<Seat> seats7 = new ArrayList<>();
		for (List<Seat> row : seatShow92) {
			for (Seat seat : row) {
				if (seat.getSeatNumber().equalsIgnoreCase("c4") || seat.getSeatNumber().equalsIgnoreCase("c5")) {
					seats7.add(seat);
					seat.setBooked(true);
				}
			}
		}
		Ticket ticket7 = new Ticket(7L, user1, show92, seats7,  520.0,
				"Upcoming");
		ticketDB.put(7L, ticket7);

		// Ticket 8
		List<Seat> seats8 = new ArrayList<>();
		for (List<Seat> row : seatShow93) {
			for (Seat seat : row) {
				if (seat.getSeatNumber().equalsIgnoreCase("d4") || seat.getSeatNumber().equalsIgnoreCase("d5")) {
					seats8.add(seat);
					seat.setBooked(true);
				}
			}
		}
		Ticket ticket8 = new Ticket(8L, user2, show93, seats8,  300.0,
				"Upcoming");
		ticketDB.put(8L, ticket8);

		// Ticket 9
		List<Seat> seats9 = new ArrayList<>();
		for (List<Seat> row : seatShow94) {
			for (Seat seat : row) {
				if (seat.getSeatNumber().equalsIgnoreCase("e4") || seat.getSeatNumber().equalsIgnoreCase("e1")) {
					seats9.add(seat);
					seat.setBooked(true);
				}
			}
		}
		Ticket ticket9 = new Ticket(9L, user3, show94, seats9, 420.0,
				"Upcoming");
		ticketDB.put(9L, ticket9);

		// Ticket 10
		List<Seat> seats10 = new ArrayList<>();
		for (List<Seat> row : seatShow91) {
			for (Seat seat : row) {
				if (seat.getSeatNumber().equalsIgnoreCase("f1") || seat.getSeatNumber().equalsIgnoreCase("f2")) {
					seats10.add(seat);
					seat.setBooked(true);
				}
			}
		}
		Ticket ticket10 = new Ticket(10L, user1, show95, seats10, 280.0,
				"Upcoming");
		ticketDB.put(10L, ticket10);
	}

//	public static void changeShowStatus(){
//		for(Show show:showDB.values()) {
//			if(show.isActive().equalsIgnoreCase("upcoming") && show.getDateTimeEpoch() <System.currentTimeMillis()/1000) {
//				show.setActive("Completed");
//			}
//		}
//	}
	public static void changeShowStatus() {
		long currentEpoch = System.currentTimeMillis() / 1000;

		for (Show show : showDB.values()) {
			long startEpoch = show.getDateTimeEpoch();
			long endEpoch = startEpoch + (show.getMovie().getDuration() * 60);

			if (currentEpoch >= startEpoch && currentEpoch <= endEpoch) {
				show.setActive("Running");
			} else if ((show.isActive().equalsIgnoreCase("upcoming") || (show.isActive().equalsIgnoreCase("Running")))
					&& currentEpoch > endEpoch) {
				show.setActive("Completed");
			}
		}
	}

	public static HashMap<Long, Customer> getUserDB() {
		return userDB;
	}

	public static void setUserDB(HashMap<Long, Customer> userDB) {
		InMemoryDatabase.userDB = userDB;
	}

	public static HashMap<Long, TheatreAdmin> getTheatreAdminDB() {
		return theatreAdminDB;
	}

	public static void setTheatreAdminDB(HashMap<Long, TheatreAdmin> theatreAdminDB) {
		InMemoryDatabase.theatreAdminDB = theatreAdminDB;
	}

	public static HashMap<Long, Admin> getAdminDB() {
		return adminDB;
	}

	public static void setAdminDB(HashMap<Long, Admin> adminDB) {
		InMemoryDatabase.adminDB = adminDB;
	}

	public static HashMap<Long, Theatre> getTheatreDB() {
		return theatreDB;
	}

	public static void setTheatreDB(HashMap<Long, Theatre> theatreDB) {
		InMemoryDatabase.theatreDB = theatreDB;
	}

	public static HashMap<Long, Movies> getMovieDB() {
		return movieDB;
	}

	public static void setMovieDB(HashMap<Long, Movies> movieDB) {
		InMemoryDatabase.movieDB = movieDB;
	}

	public static HashMap<Long, Show> getShowDB() {
		return showDB;
	}

	public static void setShowDB(HashMap<Long, Show> showDB) {
		InMemoryDatabase.showDB = showDB;
	}

	public static HashMap<Long, Ticket> getTicketDB() {
		return ticketDB;
	}

	public static void setTicketDB(HashMap<Long, Ticket> ticketDB) {
		InMemoryDatabase.ticketDB = ticketDB;
	}

	public static HashMap<Long, RequestTheatre> getRequestTheatreDb() {
		return RequestTheatreDB;
	}

	public static void setRequestTheatreDb(HashMap<Long, RequestTheatre> requestTheatreDb) {
		RequestTheatreDB = requestTheatreDb;
	}
}
