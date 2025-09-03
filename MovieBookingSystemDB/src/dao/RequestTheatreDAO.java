package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Admin;
import model.RequestTheatre;

public class RequestTheatreDAO {

	public static long insertRequestTheatre(Connection conn, long theatreId, long theatreAdminId) {
		String sql = "INSERT INTO requestTheatre (theatreId, theatreAdminId) VALUES (?, ?)";

		try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			ps.setLong(1, theatreId);
			ps.setLong(2, theatreAdminId);

			int rows = ps.executeUpdate();
			if (rows == 0) {
				throw new SQLException("Inserting request failed, no rows affected.");
			}
			try (ResultSet rs = ps.getGeneratedKeys()) {
				if (rs.next()) {
					return rs.getLong(1);
				} else {
					System.out.println("Inserting request failed, no ID obtained.");

				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return -1;
	}

	public static List<RequestTheatre> getRequestTheatre(Connection conn, long theatreAdminId) {
		String sql = "SELECT * FROM requestTheatre WHERE theatreAdminId = ?";
		List<RequestTheatre> requestList = new ArrayList<>();

		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setLong(1, theatreAdminId);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					RequestTheatre request = new RequestTheatre();
					request.setRequestId(rs.getLong("requestId"));
					request.setTheatre(TheatreDAO.getTheatreById(conn, rs.getLong("theatreId")));
					request.setTheatreAdmin(UserDAO.getUserById(conn, rs.getLong("theatreAdminId")));
					request.setApproved(rs.getBoolean("isApproved"));
					Admin admin = UserDAO.getAdminById(conn, rs.getLong("approvedAdminId"));
					request.setApprovedAdmin(admin);
					requestList.add(request);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return requestList;
	}

	public static List<RequestTheatre> getAllRequestTheatre(Connection conn) {
		String sql = "SELECT * FROM requestTheatre";
		List<RequestTheatre> requestList = new ArrayList<>();
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					RequestTheatre request = new RequestTheatre();
					request.setRequestId(rs.getLong("requestId"));
					request.setTheatre(TheatreDAO.getTheatreById(conn, rs.getLong("theatreId")));
					request.setTheatreAdmin(UserDAO.getUserById(conn, rs.getLong("theatreAdminId")));
					request.setApproved(rs.getBoolean("isApproved"));
					Admin admin = UserDAO.getAdminById(conn, rs.getLong("approvedAdminId"));
					request.setApprovedAdmin(admin);
					requestList.add(request);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return requestList;
	}

	public static RequestTheatre getRequestById(Connection conn, long requestId) {
		RequestTheatre request = null;
		String query = "SELECT * FROM requestTheatre WHERE requestId = ?";
		ResultSet rs = null;

		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setLong(1, requestId);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				request = new RequestTheatre(rs.getLong("requestId"),
						TheatreDAO.getTheatreById(conn, rs.getLong("theatreId")),
						UserDAO.getTheatreAdminById(conn, rs.getLong("theatreAdminId")), rs.getBoolean("isApproved"),
						UserDAO.getAdminById(conn, rs.getLong("approvedAdminId"))
				);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return request;
	}
	public static boolean approveRequest(Connection conn, Long requestId, Long adminId) {
	    String query = "UPDATE requestTheatre SET isApproved = ?, approvedAdminId = ? WHERE requestId = ?";

	    try (PreparedStatement pstmt = conn.prepareStatement(query)) {
	        pstmt.setBoolean(1, true);
	        pstmt.setLong(2, adminId);      
	        pstmt.setLong(3, requestId); 

	        int rowsAffected = pstmt.executeUpdate();
	        return rowsAffected > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return false;
	}

}
