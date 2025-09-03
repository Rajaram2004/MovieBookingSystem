package model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import util.DBHelper;

public class Show {
	private Long showId;
	private Movies movie;
	private Screen screen;
	private Theatre theatre;
	private long showDateTime;
	private String status;

	public Show() {
	}

	public Show(Long showId, Movies movie, Screen screen, Theatre theatre, long showDateTime, String status) {
		this.showId = showId;
		this.movie = movie;
		this.screen = screen;
		this.theatre = theatre;
		this.showDateTime = showDateTime;
		this.status = status;
	}

	public Long getShowId() {
		return showId;
	}

	public void setShowId(Long showId) {
		this.showId = showId;
	}

	public Movies getMovie() {
		return movie;
	}

	public void setMovie(Movies movie) {
		this.movie = movie;
	}

	public void setScreen(Screen screen) {
		this.screen = screen;
	}

	public void setTheatre(Theatre theatre) {
		this.theatre = theatre;
	}

	public Theatre getTheatre() {
		return theatre;
	}

	public long getShowDateTime() {
		return showDateTime;
	}

	public void setShowDateTime(long showDateTime) {
		this.showDateTime = showDateTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Screen getScreen() {
		return screen;
	}

	public List<BookingSeat> getBookingSeat(Connection conn,Show show) {
		List<BookingSeat> bookingSeatList = new ArrayList<>();

		String query = """
				    SELECT bs.bookingSeatId, bs.isBooked,s.price, s.seatId, s.seatNumber, s.seatType
				    FROM bookingSeat bs
				    JOIN seat s ON bs.seatId = s.seatId
				    WHERE bs.showId = ?
				""";

		try {
			ResultSet rs = DBHelper.executeQuery(conn,query, show.getShowId());
			while (rs.next()) {
				BookingSeat bookingSeat = new BookingSeat();
				bookingSeat.setBookingSeatId(rs.getLong("bookingSeatId"));
				bookingSeat.setBooked(rs.getBoolean("isBooked"));
				Seat seat = new Seat();
				seat.setSeatId(rs.getLong("seatId"));
				seat.setSeatNumber(rs.getString("seatNumber"));
				seat.setSeatType(rs.getString("seatType"));
				seat.setPrice(rs.getDouble("price"));

				bookingSeat.setSeat(seat);
				bookingSeat.setShow(show);

				bookingSeatList.add(bookingSeat);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return bookingSeatList;
	}

	public void displayLayout(List<BookingSeat> bookingSeatList) {
		if (bookingSeatList == null || bookingSeatList.isEmpty()) {
			System.out.println("No seats available!");
			return;
		}

		bookingSeatList.sort((s1, s2) -> {
			String seatNum1 = s1.getSeat().getSeatNumber();
			String seatNum2 = s2.getSeat().getSeatNumber();

			String row1 = seatNum1.replaceAll("[0-9]", "");
			String row2 = seatNum2.replaceAll("[0-9]", "");

			int rowCompare = row1.compareTo(row2);
			if (rowCompare != 0)
				return rowCompare;

			int num1 = Integer.parseInt(seatNum1.replaceAll("[^0-9]", ""));
			int num2 = Integer.parseInt(seatNum2.replaceAll("[^0-9]", ""));
			return Integer.compare(num1, num2);
		});

		String currentRow = "";
		double oldPrice=0,newPrice=0;
		System.out.println("\n--------- SEAT LAYOUT ---------");
		int count=0;
		for (BookingSeat bookingSeat : bookingSeatList) {
			String seatNumber = bookingSeat.getSeat().getSeatNumber();
			String row = seatNumber.replaceAll("[0-9]", "");
			newPrice=bookingSeat.getSeat().getPrice();
			if (!row.equals(currentRow)) {
				currentRow = row;
				if(count!=0)
				System.out.println("<------ : "+oldPrice);
				System.out.print(row + " ");
				oldPrice=newPrice;
			}

			if (bookingSeat.isBooked()) {
				System.out.print(" [XX] ");
			} else {
				System.out.print(" [" + bookingSeat.getSeat().getSeatNumber() + "] ");
			}
			count=1;
		}
		System.out.println("<------ : "+oldPrice);
		System.out.println("\n-------------------------------");
	}

}
