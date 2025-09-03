package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import model.Seat;
import model.Ticket;
import util.DBHelper;
import util.Input;

public class TicketDAO {

	public static List<Ticket> getTicketsByShowId(Connection conn, long showId) {
		List<Ticket> tickets = new ArrayList<>();
		String query = "SELECT * FROM ticket WHERE showId = ?";
		try {
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setLong(1, showId);
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					Ticket ticket = new Ticket();
					ticket.setTicketId(rs.getLong("ticketId"));
					ticket.setShow(ShowDAO.getShowById(conn, rs.getLong("showId")));
					ticket.setAmount(rs.getDouble("amount"));
					ticket.setStatus(rs.getString("status"));
					ticket.setUser(UserDAO.getUserById(conn, rs.getLong("userId")));
					tickets.add(ticket);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tickets;
	}

	public static List<Ticket> getAllTicket(Connection conn) {
		List<Ticket> tickets = new ArrayList<>();
		String query = "SELECT * FROM ticket";
		try {
			PreparedStatement pstmt = conn.prepareStatement(query);
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					Ticket ticket = new Ticket();
					ticket.setTicketId(rs.getLong("ticketId"));
					ticket.setShow(ShowDAO.getShowById(conn, rs.getLong("showId")));
					ticket.setAmount(rs.getDouble("amount"));
					ticket.setStatus(rs.getString("status"));
					ticket.setUser(UserDAO.getUserById(conn, rs.getLong("userId")));
					tickets.add(ticket);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tickets;
	}

	public static List<Ticket> getTicketsByDate(Connection conn, LocalDate date) {
		List<Ticket> tickets = new ArrayList<>();
		long startOfDay = date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
		long endOfDay = date.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
		String query = """
				    SELECT t.* FROM ticket t
				    JOIN shows s ON t.showId = s.showId
				    WHERE s.showDateTime BETWEEN ? AND ?
				""";

		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setLong(1, startOfDay);
			pstmt.setLong(2, endOfDay);

			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					Ticket ticket = new Ticket();
					ticket.setTicketId(rs.getLong("ticketId"));
					ticket.setShow(ShowDAO.getShowById(conn, rs.getLong("showId")));
					ticket.setAmount(rs.getDouble("amount"));
					ticket.setStatus(rs.getString("status"));
					ticket.setUser(UserDAO.getUserById(conn, rs.getLong("userId")));
					tickets.add(ticket);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tickets;
	}

	public static List<Seat> getSeatsByTicketId(Connection conn, Long ticketId) {
		List<Seat> seats = new ArrayList<>();
		String query = "SELECT bt.*, bs.*, s.* FROM bookedTicket bt JOIN bookingSeat bs ON bt.bookingSeatId = bs.bookingSeatId JOIN seat s ON bs.seatId = s.seatId WHERE bt.ticketId = ?";
		try {
			ResultSet rs = DBHelper.executeQuery(conn, query, ticketId);
			while (rs.next()) {
				Seat seat = new Seat();
				seat.setSeatId(rs.getLong("seatId"));
				seat.setScreenId(ScreenDAO.getScreenById(conn, rs.getLong("screenId")));
				seat.setSeatNumber(rs.getString("seatNumber"));
				seat.setSeatType(rs.getString("seatType"));
				seat.setPrice(rs.getDouble("price"));
				seats.add(seat);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return seats;
	}

	public static void updateCompletedTickets() {
		Connection conn = Input.getConnection();
		String query = """
				    UPDATE ticket t
				    INNER JOIN `shows` s ON t.showId = s.showId
				    INNER JOIN movies m ON s.movieId = m.movieId
				    SET t.status = 'completed'
				    WHERE t.status = 'active'
				      AND (s.showDateTime + (m.duration * 60000)) < ?
				""";

		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			long currentEpochMillis = System.currentTimeMillis();
			pstmt.setLong(1, currentEpochMillis);

			int updated = pstmt.executeUpdate();
			System.out.println("Tickets marked as completed: " + updated);
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
