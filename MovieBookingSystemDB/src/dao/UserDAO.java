package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Admin;
import model.Theatre;
import model.TheatreAdmin;
import model.User;
import util.DBHelper;
import util.Input;

public class UserDAO {

	public static User getUserById(Connection conn, Long userId) {
		User user = null;
		String query = "SELECT * FROM users WHERE id = ?";
		try {
			ResultSet rs = DBHelper.executeQuery(conn, query, userId);
			if (rs.next()) {
				user = new User(rs.getLong("id"), rs.getString("emailId"), rs.getString("name"),
						rs.getLong("phoneNumber"), rs.getString("password"), rs.getString("role"),
						rs.getDouble("balance"), rs.getString("timeZone"), rs.getBoolean("active"));
			}
		} catch (SQLException e) {
			System.out.println("Error fetching user: " + e.getMessage());
			e.printStackTrace();
		}
		return user;
	}

	public static List<User> getAllUser(Connection conn) {
		User user = null;
		String query = "SELECT * FROM users";
		List<User> userList = new ArrayList<>();
		try {
			ResultSet rs = DBHelper.executeQuery(conn, query);
			while (rs.next()) {
				user = new User(rs.getLong("id"), rs.getString("emailId"), rs.getString("name"),
						rs.getLong("phoneNumber"), rs.getString("password"), rs.getString("role"),
						rs.getDouble("balance"), rs.getString("timeZone"), rs.getBoolean("active"));
				userList.add(user);
			}
		} catch (SQLException e) {
			System.out.println("Error fetching user: " + e.getMessage());
			e.printStackTrace();
		}
		return userList;
	}

	public static boolean updateUserBalance(Connection conn, Long userId, double newBalance) {
		String query = "UPDATE user SET balance = ? WHERE id = ?";
		return DBHelper.executeUpdate(conn, query, newBalance, userId) > 0 ? true : false;
	}

	public static void updateUserTimeZone(Connection conn,Long id, String timeZone) {
		String query = "UPDATE users SET timeZone = ? WHERE id = ?";
		DBHelper.executeUpdate(conn, query, timeZone, id);
	}

	public static TheatreAdmin getTheatreAdminById(Connection conn, Long theatreAdminId) {
		String sql = "SELECT * FROM users WHERE id = ?";
		TheatreAdmin theatreAdmin = null;
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setLong(1, theatreAdminId);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					theatreAdmin = new TheatreAdmin(rs.getLong("id"), rs.getString("name"), rs.getString("emailId"),
							rs.getLong("phoneNumber"), rs.getString("password"), null, rs.getString("timeZone"));
				} else {
					System.out.println("No TheatreAdmin found with ID: " + theatreAdminId);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return theatreAdmin;
	}

	public static Admin getAdminById(Connection conn, Long adminId) {
		String sql = "SELECT * FROM users WHERE id = ?";
		Admin admin = null;
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setLong(1, adminId);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					admin = new Admin(rs.getLong("id"), rs.getString("name"), rs.getString("emailId"),
							rs.getLong("phoneNumber"), rs.getString("password"), rs.getString("timeZone"));
				} else {
					System.out.println("No Admin found with ID: " + adminId);
					return null;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return admin;
	}

}
