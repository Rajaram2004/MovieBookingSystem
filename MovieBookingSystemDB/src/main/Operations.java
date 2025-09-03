package main;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

import dao.ShowDAO;
import dao.TicketDAO;
import dao.UserDAO;
import model.User;
import service.MovieService;
import service.PrinterService;
import service.TheatreAdminService;
import service.TheatreService;
import service.TicketService;
import util.Input;
import util.TimeZoneConverter;

public class Operations {

	public static void operation(User user) {
		try {
			int userChoice = Features();
			String timeZone = user.getTimeZone();
			TicketService ticketServiceObj1 = new TicketService();
			MovieService movieServiceObj1 = new MovieService();
			TheatreService theatreServiceObj1 = new TheatreService();
			PrinterService printerServiceObj = new PrinterService();
			ShowDAO.changeShowStatus();
			TicketDAO.updateCompletedTickets();
			TheatreAdminService.updateMovies();
			switch (userChoice) {
			case 1:
				System.out.println("You have Selected display All Movie");
				Connection conn = Input.getConnection();
				printerServiceObj.printAllMovies(conn);
				conn.close();
				Operations.operation(user);
				break;
			case 2:
				System.out.println("You have Selected Search Movie By Name");
				movieServiceObj1.searchByMovieName();
				Operations.operation(user);
				break;
			case 3:
				System.out.println("You have Selected View Movie By Genre");
				movieServiceObj1.viewMoviesByGenre();
				Operations.operation(user);
				break;
			case 4:
				System.out.println("You have Selected Display All Theatre");
				Connection conn4 = Input.getConnection();
				theatreServiceObj1.printAllTheatres(conn4);
				Operations.operation(user);
				break;
			case 5:
				System.out.println("You have Selected Display Seat Availability");
				ticketServiceObj1.userDisplaySeatAvailability(user);
				Operations.operation(user);
				break;
			case 6:
				System.out.println("You have Selected Book Ticket");
				ticketServiceObj1.bookTicket(user);
				Operations.operation(user);
				break;
			case 7:
				System.out.println("You have Selected Check Ticket Status");
				ticketServiceObj1.checkTicketStatus(user);
				Operations.operation(user);
				break;
			case 8:
				System.out.println("You have Selected View My Booking");
				ticketServiceObj1.viewMyBooking(user);
				Operations.operation(user);
				break;
			case 9:
				System.out.println("You have Selected ,Cancel Ticket");
				ticketServiceObj1.cancelTicket(user);
				Operations.operation(user);
			case 10:
				System.out.println("You have Selected Filter Movies by Language");
				printerServiceObj.filterMoviesByLanguage();
				Operations.operation(user);
				break;
			case 11:
				System.out.println("You have Selected Balance Top up");
				System.out.println("Current Available Balance is " + user.getBalance());
				System.out.println("Enter the Amount to Top Up (or Type '0' to Exit): ");
				Connection conn11 = Input.getConnection();
				int addAmount = Input.getInteger(1000000000);
				if (addAmount != 0) {
					double currBalance = ticketServiceObj1.getUserBalanceFromDB(conn11, user.getId());
					currBalance += (double) addAmount;
					user.newBalance(conn11, currBalance);
					System.out.println("Available Balance is " + user.getBalance());
				} else {
					System.out.println("------Back------");
				}
				Operations.operation(user);
				break;
			case 12:
				System.out.println("You have Selected Filter Movies by Language");
				timeZone = TimeZoneConverter.selectTimeZone();
				if (timeZone != null) {
					user.setTimeZone(timeZone);
					Connection conn12 = Input.getConnection();
					UserDAO.updateUserTimeZone(conn12, user.getId(), timeZone);
					try {
						conn12.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					System.out.println("Time Zone Changed");
				}
				Operations.operation(user);
				break;
			case 13:
				System.out.println("You have Selected View Balance");
				Connection conn13 = Input.getConnection();
				System.out
						.println("Available balance : " + ticketServiceObj1.getUserBalanceFromDB(conn13, user.getId()));
				Operations.operation(user);
				break;
			case 14:
				System.out.println("You have Selected Book Ticket via Theatre");
				ticketServiceObj1.BookTicketViaTheatre(user);
				Operations.operation(user);
				break;
			case 15:
				System.out.println("\nYou have Selected Exit");
				System.out.println("----------------------------------------------------");
				System.out.println("Thanks for visiting. See you next time!");
				System.out.println("----------------------------------------------------");
				int choice = MainMenu.mainMenu();
				try {
					MainMenu.call(choice);
				} catch (Exception e) {
					e.printStackTrace();
					System.err.println("----");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static int Features() {
		System.out.println("\n========= Welcome to Movie Booking System =========");
		System.out.println("Please select one of the following options:\n");

		String[] features = { "1 . View All Movies", "2 . Search Movie By Name", "3 . View Movie By Genre",
				"4 . Display All Theatre", "5 . Display Seat Availability", "6 . Book Ticket", "7 . Ticket Status",
				"8 . View Booking", "9 . Cancel Movie Ticket", "10. Filter Movie By Language", "11. Top-Up",
				"12. Change Time Zone", "13. View Balance", "14. Book Ticket via Theatre", "15. Exit" };

		int n = features.length;
		for (int i = 0; i < features.length; i++) {
			System.out.println("  " + features[i]);
		}

		System.out.println("\n====================================================\n");

		Scanner sc = Input.getScanner();
		System.out.print("Enter Your Choice : ");
		String input = null;
		input = sc.nextLine();
		try {
			int num = Integer.parseInt(input);
			if (num > 0 && num <= n) {
				return num;
			} else {
				System.err.println("Please enter a valid number.");
				return Features();
			}
		} catch (Exception e) {
			System.err.println("Not an integer.");
			return Features();
		}
	}

}
