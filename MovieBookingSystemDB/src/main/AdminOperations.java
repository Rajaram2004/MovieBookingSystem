package main;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import dao.MovieDAO;
import dao.ShowDAO;
import dao.TicketDAO;
import dao.UserDAO;
import model.Admin;
import model.Movies;
import model.Show;
import model.Theatre;
import service.AdminService;
import service.PrinterService;
import service.TheatreAdminService;
import service.TheatreService;
import service.TicketService;
import util.Input;
import util.TimeZoneConverter;

public class AdminOperations {
	static TicketService ticketServiceObj = new TicketService();
	static TheatreService theatreServiceObj = new TheatreService();
	static TheatreAdminService theatreAdminServiceObj = new TheatreAdminService();
	static PrinterService printerServiceObj = new PrinterService();
	
	public static void adminOperations(Admin admin) {
		int adminChoice = adminFeatures();
		String timeZone = admin.getTimeZone();
		AdminService adminService = new AdminService();
		TicketDAO.updateCompletedTickets();
		ShowDAO.changeShowStatus();
		TheatreAdminService.updateMovies();
		switch (adminChoice) {
		case 0:
			System.out.println("You Have Selected Exit ");
			int choice0 = MainMenu.mainMenu();
			MainMenu.call(choice0);
			break;
		case 1:
			System.out.println("You Have Selected print All Bookings");
			adminService.viewAllUsers();
			adminOperations(admin);
			break;
		case 2:
			System.out.println("You Have Selected Search User By ID");
			adminService.searchUserById();
			adminOperations(admin);
			break;
		case 3:
			System.out.println("You Have Selected Search User By Name");
			adminService.searchUserByName();
			adminOperations(admin);
			break;
		case 4:
			System.out.println("You Have Selected View All Theatre Admins");
			adminService.viewAllTheatreAdmins();
			adminOperations(admin);
			break;
		case 5:
			System.out.println("You Have Selected Search Theatre AdminBy Id");
			adminService.searchTheatreAdminById();
			adminOperations(admin);
			break;
		case 6:
			System.out.println("You Have Selected Search Theatre Admin By Name");
			adminService.searchTheatreAdminByName();
			adminOperations(admin);
			break;
		case 7:
			System.out.println("You Have Selected Add New Movie");
			adminService.addMovie();
			adminOperations(admin);
			break;
		case 8:
			System.out.println("You Have Selected Seat Availability");
			System.out.println(timeZone);
			userDisplaySeatAvailability(ticketServiceObj,timeZone);
			adminOperations(admin);
			break;
		case 9:
			System.out.println("You Have Selected Print All Shows ");
			adminService.printAllShows(timeZone);
			adminOperations(admin);
			break;
		case 10:
			System.out.println("You Have Selected Print All Movies ");
			try {
				Connection conn10=Input.getConnection();
				printerServiceObj.printAllMovies(conn10);
				conn10.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
			
			adminOperations(admin);
			break;
		case 11:
			System.out.println("You Have Selected Print All Theatres ");
			try {
				Connection conn11=Input.getConnection();
				theatreServiceObj.printAllTheatres(conn11);
				conn11.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
			
			adminOperations(admin);
			break;
		case 12:
			System.out.println("You Have Selected Search Show By Id ");
			theatreAdminServiceObj.searchShowId(timeZone);
			adminOperations(admin);
			break;
		case 13:
			System.out.println("You Have Selected Edit Movie Details ");
			adminService.editMovieDetails();
			adminOperations(admin);
			break;
		case 14:
			System.out.println("You Have Selected Edit Movie Details ");
			theatreAdminServiceObj.printShowAllFuture(timeZone);
			adminOperations(admin);
			break;
		case 15:
			System.out.println("You Have Selected Approve New Theatre ");
			theatreAdminServiceObj.approveNewTheatre(admin);
			adminOperations(admin);
			break;
		case 16:
			System.out.println("You Have Selected Change Time Zone ");
			timeZone = TimeZoneConverter.selectTimeZone();
			if(timeZone!=null) {
				admin.setTimeZone(timeZone);
				Connection conn = Input.getConnection();
				UserDAO.updateUserTimeZone(conn,admin.getId(),timeZone);
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				System.out.println("Time Zone Changed");
			}
			adminOperations(admin);
			break;
		case 17:
			System.out.println("You Have Selected Add Theatre to Thate Admin ");
			theatreAdminServiceObj.AddTheatreToTheatreAdmin();
			adminOperations(admin);
		case 18:
			System.out.println("You Have Selected Display all Ticket");
			theatreAdminServiceObj.displayAllTicket(admin.getTimeZone());
			adminOperations(admin);
		case 19:
			System.out.println("You Have Selected Display DateWise Ticket");
			theatreAdminServiceObj.displayDateWiseTicket(admin.getTimeZone());
			adminOperations(admin);
		case 20:
			System.out.println("You Have Selected Display all Running Show");
			theatreAdminServiceObj.displayAllRunningShow(admin.getTimeZone());
			adminOperations(admin);
		case 21:
			System.out.println("You Have Selected set Movie Is Active Status");
			theatreAdminServiceObj.setMovieIsActiveStatus();
			adminOperations(admin);
		case 22:
			System.out.println("You Have Selected Exit ");
			int choice = MainMenu.mainMenu();
			MainMenu.call(choice);
			break;
		}
	}
	
	

	public static int adminFeatures() {
		System.out.println("\n========= Welcome to Movie Booking System Admin =============");
		System.out.println("Please select one of the following options:\n");

		String[] features = { "1 . View All Users ", "2 . Search User By Id ", "3 . Search User By Name",
				"4 . View All Theatre Admins", "5 . Search Theatre Admin By Id", "6 . Search Theatre Admin By Name",
				"7 . add New Movie", "8 . Seat Availability ", "9 . Print All Shows", "10. Print All Movies",
				"11. Print All Theatres ", "12. Search Show By Id", "13. Edit Movie Details",
				"14. Display All Future Show", "15. Approve New Theatre", "16. Change Time Zone",
				"17. Add Theatre to Theatre Admin", "18. Display all Ticket","19. Display DateWise Ticket","20. Display All Running Show","21. Set Movie Is Active Status ","22. Exit" };

		int n = features.length;
		for (int i = 0; i < n; i++) {
			System.out.println("  " + features[i]);
		}

		System.out.println("\n===========================================================\n");
		System.out.println("Please enter your choice (or press 0 to exit):");
		return Input.getInteger(n);
	}
	
	public static void userDisplaySeatAvailability(TicketService ticketServiceObj, String timeZone) {
		Connection conn=Input.getConnection();
		Movies movieObj = ticketServiceObj.selectMovie(conn);
		if(movieObj==null) return;
		Theatre theatreObj = printerServiceObj.selectTheatreForMovie(conn,movieObj);
		if(theatreObj==null) return;
		List<Show> shows = ticketServiceObj.getShowsForMovieAndTheatre(conn,movieObj, theatreObj);
		Show show = printerServiceObj.getShow(shows, timeZone);
		if (show == null) {
			System.err.println("cancelled. No show selected.");
		} else {
			ticketServiceObj.displaySeatsByCategory(conn,show);
		}
	}
	
	

}
