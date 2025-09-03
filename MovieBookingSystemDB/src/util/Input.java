package util;

import java.util.Scanner;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class Input {
	private static final Scanner SCANNER = new Scanner(System.in);

	private Input() {
	}

	public static Scanner getScanner() {
		return SCANNER;
	}

	public static void closeScanner() {
		SCANNER.close();
	}

	public static int getInteger(int maxValue) {
		Scanner sc = Input.getScanner();
		String input = sc.nextLine();
		try {

			int num = Integer.parseInt(input);

			if (num > -1 && num <= maxValue) {
				return num;

			} else {
				System.err.println("Please Enter a Valid Number or Enter 0 to Exit.");
				return getInteger(maxValue);
			}
		} catch (NumberFormatException e) {
			System.err.println("Not an integer.");
			return getInteger(maxValue);
		}
	}

	public static long getLong(Long maxValue) {
		while (true) {
			try {
				String input = SCANNER.nextLine().trim();
				Long num = Long.parseLong(input);
				if (num >= 0 && num <= maxValue) {
					return num;
				} else {
					System.err.println("Invalid input");
				}

			} catch (NumberFormatException e) {
				System.err.print("Invalid input. Please enter a valid number: ");
			}
		}
	}

	public static double getDouble(double maxValue) {
		while (true) {
			try {
				String input = SCANNER.nextLine().trim();
				double num = Double.parseDouble(input);
				if (num >= 0 && num <= maxValue) {
					return num;
				} else {
					System.err.println("Invalid input. Please enter a number between 0 and " + maxValue + ": ");
				}
			} catch (NumberFormatException e) {
				System.err.print("Invalid input. Please enter a valid decimal number: ");
			}
		}
	}

	private static final String URL = "jdbc:mysql://localhost:3306/MovieBookingSystem";
	private static final String USER = "root"; 
	private static final String PASSWORD = "Raja1@Strong";

	private static Connection connection;
	public static Connection getConnection() {
		try {
			if (connection == null || connection.isClosed()) {
				Class.forName("com.mysql.cj.jdbc.Driver");
				connection = DriverManager.getConnection(URL, USER, PASSWORD);
			}
		} catch (Exception e) {
			System.out.println("❌ Connection Failed!");
			e.printStackTrace();
		}
		return connection;
	}
	public static void closeConnection() {
		try {
			if (connection != null && !connection.isClosed()) {
				connection.close();
				System.out.println("🔌 Connection Closed!");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
