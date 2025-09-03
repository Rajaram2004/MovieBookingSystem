package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Screen;
import model.Theatre;
import util.DBHelper;

public class ScreenDAO {

	public static Screen getScreenById(Connection conn, Long screenId) {
		Screen screen = null;
		String query = "SELECT * FROM screen WHERE screenId = ?";

		try {
			ResultSet rs = DBHelper.executeQuery(conn, query, screenId);
			if (rs.next()) {
				long id = rs.getLong("theatreId");
				Theatre theatre = TheatreDAO.getTheatreById(conn, id);
				screen = new Screen(rs.getLong("screenId"), rs.getString("screenName"), theatre,
						rs.getString("status"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return screen;
	}

	public static List<String> getSeat(Connection conn, Long ticketId) {
		List<String> seatNames = new ArrayList<>();
		String query = "SELECT s.seatNumber " + "FROM bookedTicket bt "
				+ "JOIN bookingSeat bs ON bt.bookingSeatId = bs.bookingSeatId " + "JOIN seat s ON bs.seatId = s.seatId "
				+ "WHERE bt.ticketId = ?";

		try {
			ResultSet rs = DBHelper.executeQuery(conn, query, ticketId);

			while (rs.next()) {
				seatNames.add(rs.getString("seatNumber"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return seatNames;
	}
}
