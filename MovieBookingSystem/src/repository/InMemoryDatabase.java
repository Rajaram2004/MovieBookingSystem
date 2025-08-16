package repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import model.Admin;
import model.Movies;
import model.Screen;
import model.Seat;
import model.Show;
import model.Theatre;
import model.TheatreAdmin;
import model.Ticket;
import model.User;

public class InMemoryDatabase {
	static HashMap<Long, User> userDB = new HashMap<>();
	static HashMap<Long, TheatreAdmin> theatreAdminDB = new HashMap<>();
	static HashMap<Long, Admin> adminDB = new HashMap<>();
	static HashMap<Long,Theatre> theatreDB = new HashMap<>();
	static HashMap<Long,Movies> movieDB = new HashMap<>();
	static HashMap<Long,Show> showDB = new HashMap<>();
	static HashMap<Long,Ticket> ticketDB =new HashMap<>();


	public InMemoryDatabase() {
		userDB = InMemoryDatabase.getUserDB();
		userDB.put(1L, new User(1L, "User 1", "user1@example.com", 987654321L, "Chennai", "pass123"));
		userDB.put(2L, new User(2L, "User 2", "user2@example.com", 123456789L, "Bangalore", "pass456"));
		userDB.put(3L, new User(3L, "User 3", "user3@example.com", 912345678L, "Mumbai", "pass789"));
		userDB.put(4L, new User(4L, "User 4", "user4@example.com", 123456L, "Mumbai", "1"));

		Theatre t = null;

		theatreAdminDB.put(1L, new TheatreAdmin(1L, "Admin One", "theatreadmin1@example.com", 987654321L, "pass123", t));
		theatreAdminDB.put(2L, new TheatreAdmin(2L, "Admin Two", "theatreadmin2@example.com", 123456789L, "pass456", t));
		theatreAdminDB.put(3L, new TheatreAdmin(3L, "Admin Three", "theatreadmin3@example.com", 912345678L, "pass789", t));

		adminDB.put(1L, new Admin(1L, "Rajaram", "admin1@example.com", 987654321L, "pass123"));
		adminDB.put(2L, new Admin(2L, "Priya", "admin2@example.com", 123456789L, "pass456"));
		adminDB.put(3L, new Admin(3L, "Arjun", "admin3@example.com", 912345678L, "pass789"));
		
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
	    String[] theatreNames = {
	        "Sathyam Cinemas", "KG Cinemas", "INOX", "Thangam Cinemas", "PVR Velachery",
	        "PVR Phoenix", "Escape Cinemas", "AVM Cinemas", "SPI Cinemas", "Mayajaal"
	    };
	    String[] theatreCities = {
	        "Chennai", "Coimbatore", "Madurai", "Trichy", "Chennai",
	        "Bangalore", "Chennai", "Chennai", "Chennai", "Chennai"
	    };

	    List<Theatre> theatres = new ArrayList<>();
	    for (int t = 0; t < theatreNames.length; t++) {
	        List<Screen> screens = new ArrayList<>();
	        for (int s = 1; s <= 3; s++) {
	            screens.add(new Screen(s, null, 10, 12));
	        }
	        Theatre theatre = new Theatre(101L + t, theatreNames[t], theatreCities[t], screens, new ArrayList<>());
	        theatres.add(theatre);
	        theatreDB.put((long) (t + 1), theatre);
	    }

	    String[] movieTitles = {
	        "Vikram", "Master", "Jailer", "Leo", "RRR", "Pushpa: The Rise",
	        "Baahubali: The Beginning", "Baahubali: The Conclusion",
	        "Pathaan", "Tiger 3", "Drishyam 2", "Ponniyin Selvan"
	    };
	    String[] genres = {
	        "Action", "Drama", "Comedy", "Thriller", "Action", "Action", "Drama", "Action",
	        "Action", "Action", "Thriller", "Historical"
	    };
	    String[] languages = {
	        "Tamil", "Telugu", "English", "Tamil", "Telugu", "English",
	        "English", "Tamil", "Hindi", "Hindi", "Malayalam", "Tamil"
	    };
	    int[] releaseYears = {
	        2022, 2021, 2023, 2023, 2022, 2021, 2015, 2017, 2023, 2023, 2021, 2022
	    };
	    int[] durations = {
	        173, 179, 168, 164, 182, 179, 159, 171, 165, 160, 150, 190
	    };

	    List<Movies> movies = new ArrayList<>();
	    Random rand = new Random();
	    for (int m = 0; m < movieTitles.length; m++) {
	        int numTheatres = 2 + rand.nextInt(3); 
	        List<Theatre> movieTheatres = new ArrayList<>();
	        while (movieTheatres.size() < numTheatres) {
	            Theatre th = theatres.get(rand.nextInt(theatres.size()));
	            if (!movieTheatres.contains(th)) movieTheatres.add(th);
	        }

	        Movies movie = new Movies((long) (m + 1), movieTitles[m], durations[m], genres[m], languages[m], releaseYears[m], movieTheatres);
	        movies.add(movie);
	        movieDB.put((long) (m + 1), movie);
	    }

	   
	    long showId = 1L;
	    LocalDate startDate = LocalDate.now().plusDays(1); 
	    for (Movies movie : movies) {
	        for (Theatre theatre : movie.getListOfTheatre()) {
	            List<Screen> theatreScreens = theatre.getListOfScreen();
	            for (int i = 0; i < theatreScreens.size(); i++) {
	                Screen screen = theatreScreens.get(i);
	                LocalDate showDate = startDate.plusDays(rand.nextInt(5));
	                LocalTime showTime = LocalTime.of(10 + rand.nextInt(9), rand.nextBoolean() ? 0 : 30); 
	                ZonedDateTime zdt = ZonedDateTime.of(showDate, showTime, ZoneId.of("Asia/Kolkata"));
	                int epoch = (int) zdt.toEpochSecond();
	                List<Seat> seats = new ArrayList<>();
	                for (int r = 1; r <= 10; r++) {
	                    for (int c = 1; c <= 12; c++) {
	                        String seatNum = "R" + r + "C" + c;
	                        seats.add(new Seat(seatNum, r, c, "Regular", 150, true));
	                    }
	                }
	                Show show = new Show(showId++, theatre, movie, screen, epoch, seats);
	                theatre.addShow(show);
	                showDB.put(show.getShowId(), show);
	            }
	        }
	    }
	    theatreAdminDB.get(1L).setTheatre(theatreDB.get(1L));
	    theatreAdminDB.get(2L).setTheatre(theatreDB.get(2L));
	    theatreAdminDB.get(3L).setTheatre(theatreDB.get(3L));
	    
	}




	public static HashMap<Long, User> getUserDB() {
		return userDB;
	}
	public static void setUserDB(HashMap<Long, User> userDB) {
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
	
}
