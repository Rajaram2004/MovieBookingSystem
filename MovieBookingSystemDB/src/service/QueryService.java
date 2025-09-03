package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import dao.ScreenDAO;
import model.Screen;
import model.Seat;
import model.Show;
import model.Theatre;

public class QueryService {
	
	public void cloneSeatsForShow(Connection conn, long showId, long screenId) {
		String sql = "INSERT INTO bookingSeat (seatId, showId, isBooked) "
				+ "SELECT seatId, ?, FALSE FROM seat WHERE screenId = ?";
		try {
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setLong(1, showId);
			stmt.setLong(2, screenId);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public long addShowToDB(Connection conn, Show show) {
		long generatedShowId = -1;
		String insertQuery = "INSERT INTO shows (theatreId, movieId, screenId, showDateTime, status) VALUES (?, ?, ?, ?, ?)";
		try {
			PreparedStatement pstmt = conn.prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS);
			pstmt.setLong(1, show.getTheatre().getTheatreId());
			pstmt.setLong(2, show.getMovie().getMovieId());
			pstmt.setLong(3, show.getScreen().getScreenId());
			pstmt.setLong(4, show.getShowDateTime());
			pstmt.setString(5, show.getStatus());
			int rowsAffected = pstmt.executeUpdate();
			if (rowsAffected > 0) {
				try (ResultSet rs = pstmt.getGeneratedKeys()) {
					if (rs.next()) {
						generatedShowId = rs.getLong(1);
					}
				}
			}
		} catch (SQLException e) {
			System.err.println("Error in show adding");
			e.printStackTrace();
		}
		return generatedShowId;
	}
	
	 int insertScreen(Connection conn, Screen screen, Theatre theatre) {
		String query = "INSERT INTO screen (screenName, theatreId, status) VALUES (?, ?, ?)";
		try (PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
			ps.setString(1, screen.getScreenName());
			ps.setLong(2, theatre.getTheatreId());
			ps.setString(3, screen.getStatus());
			ps.executeUpdate();

			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	void insertSeats(Connection conn, long screenId, int totalRows, List<Integer> seatsPerRow,
			List<Double> pricesPerRow) {
		TheatreAdminService theatreAdminServiceObj = new TheatreAdminService();
		
		String sql = "INSERT INTO seat (screenId, seatNumber, seatType, price) VALUES (?, ?, ?, ?)";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			for (int row = 1; row <= totalRows; row++) {
				String rowLabel = theatreAdminServiceObj.toRowLabel(row);
				int seatCount = seatsPerRow.get(row - 1);
				double price = pricesPerRow.get(row - 1);
				String seatType = "Regular";
				for (int seatNo = 1; seatNo <= seatCount; seatNo++) {
					String seatNumber = rowLabel + seatNo;
					ps.setLong(1, screenId);
					ps.setString(2, seatNumber);
					ps.setString(3, seatType);
					ps.setDouble(4, price);
					ps.addBatch();
				}
			}
			ps.executeBatch();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public List<Seat> getSeatListByScreen(Connection conn, long screenId) {
		List<Seat> seatList = new ArrayList<>();
		List<Long> screenIds = new ArrayList<>();
		String query = "SELECT * FROM seat WHERE screenId = ?";
		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setLong(1, screenId);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Seat seat = new Seat(rs.getLong("seatId"), rs.getString("seatNumber"), rs.getString("seatType"),
							rs.getDouble("price"));
					screenIds.add(rs.getLong("screenId"));
					seatList.add(seat);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		int index = 0;
		for (Seat seat : seatList) {
			long id = screenIds.get(index);
			seat.setScreenId(ScreenDAO.getScreenById(conn, id));
			index++;
		}

		return seatList;
	}
	
}
