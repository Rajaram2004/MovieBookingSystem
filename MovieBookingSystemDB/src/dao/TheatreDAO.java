package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Theatre;
import util.DBHelper;

public class TheatreDAO {
	public static Theatre getTheatreById(Connection conn, Long theatreId) {
		Theatre theatre = null;
		String query = "SELECT * FROM theatre WHERE theatreId = ?";
		try {
			ResultSet rs = DBHelper.executeQuery(conn, query, theatreId);
			if (rs.next()) {
				theatre = new Theatre(rs.getLong("theatreId"), rs.getString("theatreName"),
						rs.getString("theatreLocation"), rs.getString("status"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return theatre;
	}

	public static List<Theatre> getTheatreList(Connection conn) {
		List<Theatre> listTheatre = new ArrayList<>();
		String query = "SELECT * FROM theatre " + "WHERE LOWER(status) = 'active'";
		try {
			ResultSet rs = DBHelper.executeQuery(conn, query);

			while (rs.next()) {
				Theatre theatre = new Theatre(rs.getLong("theatreId"), rs.getString("theatreName"),
						rs.getString("theatreLocation"), rs.getString("status"));
				listTheatre.add(theatre);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listTheatre;
	}

	public static long insertTheatreAndGetId(Connection conn, String theatreName, String theatreLocation,
			String status) {
		String sql = "INSERT INTO theatre (theatreName, theatreLocation,status) VALUES (?, ?, ?)";
		try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			ps.setString(1, theatreName);
			ps.setString(2, theatreLocation);
			ps.setString(3, status);

			int rows = ps.executeUpdate();
			if (rows == 0) {
				System.err.println("Inserting theatre failed, no rows affected.");
			}
			try (ResultSet rs = ps.getGeneratedKeys()) {
				if (rs.next()) {
					return rs.getLong(1);
				} else {
					System.out.println("Inserting theatre failed, no ID obtained.");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public static boolean activeTheatre(Connection conn, Theatre theatre) {
		String query = "UPDATE theatre SET status = 'active' WHERE theatreId = ?";

		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setLong(1, theatre.getTheatreId());

			int rowsUpdated = pstmt.executeUpdate();
			return rowsUpdated > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean assignTheatreToAdmin(Connection conn, Long theatreId, Long adminId) {
		String checkQuery = "SELECT COUNT(*) FROM TheatreAdmin WHERE id = ? AND theatreId = ?";
		String insertQuery = "INSERT INTO TheatreAdmin (id, theatreId) VALUES (?, ?)";

		try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
			checkStmt.setLong(1, adminId);
			checkStmt.setLong(2, theatreId);

			try (ResultSet rs = checkStmt.executeQuery()) {
				if (rs.next() && rs.getInt(1) > 0) {
					System.out.println("Theatre already present for this Theatre Admin!");
					return false;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
			insertStmt.setLong(1, adminId);
			insertStmt.setLong(2, theatreId);

			int rowsInserted = insertStmt.executeUpdate();
			if (rowsInserted > 0) {
				System.out.println("Theatre Added To Theatre Admin!");
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

//	public static boolean assignTheatreToAdmin(Connection conn, Long theatreId, Long adminId) {
//	    String query = "INSERT INTO TheatreAdmin (id, theatreId) VALUES (?, ?)";
//
//	    try (PreparedStatement pstmt = conn.prepareStatement(query)) {
//	        pstmt.setLong(1, adminId);
//	        pstmt.setLong(2, theatreId);
//
//	        int rowsInserted = pstmt.executeUpdate();
//	        return rowsInserted > 0;  
//	    } catch (SQLException e) {
//	        e.printStackTrace();
//	    }
//	    return false;
//	}

}
