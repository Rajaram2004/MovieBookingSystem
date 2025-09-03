package util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBHelper {
	public static ResultSet executeQuery(Connection conn ,String query, Object... params) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(query);
		for (int i = 0; i < params.length; i++) {
			ps.setObject(i + 1, params[i]);
		}
		return ps.executeQuery();
	}

	
	public static int executeUpdate(Connection conn,String sql, Object... params) {
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			for (int i = 0; i < params.length; i++) {
				pstmt.setObject(i + 1, params[i]);
			}
			return pstmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println("Error executing update: " + e.getMessage());
			e.printStackTrace();
		}
		return 0;
	}

	

}
