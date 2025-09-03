package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Show;
import util.DBHelper;
import util.Input;

public class ShowDAO {

	public static Show getShowById(Connection conn, Long showId) {
		Show show = null;
		String query = "SELECT * FROM shows WHERE showId = ?";
		Long movieId = null;
		Long theatreId = null;
		Long screenId = null;
		long showDateTime = 0;
		String status = null;

		try {
			ResultSet rs1 = DBHelper.executeQuery(conn, query, showId);
			if (rs1.next()) {
				movieId = rs1.getLong("movieId");
				theatreId = rs1.getLong("theatreId");
				screenId = rs1.getLong("screenId");
				showDateTime = rs1.getLong("showDateTime");
				status = rs1.getString("status");
			}

			if (movieId != null && theatreId != null && screenId != null) {
				show = new Show();
				show.setShowId(showId);
				show.setShowDateTime(showDateTime);
				show.setStatus(status);
				show.setMovie(MovieDAO.getMovieById(conn, movieId));
				show.setTheatre(TheatreDAO.getTheatreById(conn, theatreId));
				show.setScreen(ScreenDAO.getScreenById(conn, screenId));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return show;
	}

	public static List<Show> getActiveShowsByTheatreId(Connection conn, long theatreId) {
		List<Long> showIds = new ArrayList<>();
		List<Show> showList = new ArrayList<>();
		String query = "SELECT s.showId FROM shows s WHERE s.theatreId = ? AND LOWER(s.status) = 'active'";
		ResultSet rs = null;

		try {
			rs = DBHelper.executeQuery(conn, query, theatreId);
			while (rs.next()) {
				showIds.add(rs.getLong("showId"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		for (Long id : showIds) {
			Show show = getShowById(conn, id);
			showList.add(show);
		}
		return showList;
	}
	
	public static List<Show> getRunningShow(Connection conn) {

		List<Show> showList = new ArrayList<>();
		String query = "SELECT * FROM shows WHERE LOWER(status) = 'running'";

		ResultSet rs = null;

		try {
			rs = DBHelper.executeQuery(conn, query);
			while (rs.next()) {
				Show show = new Show(rs.getLong("showId"),MovieDAO.getMovieById(conn, rs.getLong("movieId")),ScreenDAO.getScreenById(conn, rs.getLong("screenId")),
						TheatreDAO.getTheatreById(conn, rs.getLong("theatreId")),rs.getLong("showDateTime"),rs.getString("status"));
				showList.add(show);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return showList;
	}

	public static List<Show> getAllShows(Connection conn) {

		List<Show> showList = new ArrayList<>();
		String query = "SELECT * FROM shows";
		ResultSet rs = null;

		try {
			rs = DBHelper.executeQuery(conn, query);
			while (rs.next()) {
				Show show = new Show(rs.getLong("showId"),MovieDAO.getMovieById(conn, rs.getLong("movieId")),ScreenDAO.getScreenById(conn, rs.getLong("screenId")),
						TheatreDAO.getTheatreById(conn, rs.getLong("theatreId")),rs.getLong("showDateTime"),rs.getString("status"));
				showList.add(show);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return showList;
	}

	public static List<Show> getAllShowsByTheatreId(Connection conn, long theatreId) {
		List<Long> showIds = new ArrayList<>();
		List<Show> showList = new ArrayList<>();
		String query = "SELECT s.showId FROM shows s WHERE s.theatreId = ? ";
		ResultSet rs = null;

		try {
			rs = DBHelper.executeQuery(conn, query, theatreId);
			while (rs.next()) {
				showIds.add(rs.getLong("showId"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		for (Long id : showIds) {
			Show show = getShowById(conn, id);
			showList.add(show);
		}
		return showList;
	}

	public static void changeShowStatus() {
		Connection conn = Input.getConnection();
		String query = """
				    UPDATE shows s
				    JOIN movies m ON s.movieId = m.movieId
				    SET s.status = CASE
				        WHEN UNIX_TIMESTAMP() * 1000 BETWEEN s.showDateTime AND (s.showDateTime + (m.duration * 60000)) THEN 'Running'
				        WHEN UNIX_TIMESTAMP() * 1000 > (s.showDateTime + (m.duration * 60000)) THEN 'Completed'
				        ELSE s.status
				    END;
				""";

		DBHelper.executeUpdate(conn, query);
	}

	public static boolean updateShowStatus(Connection conn, Long showId, String newStatus) {
		String query = "UPDATE shows SET status = ? WHERE showId = ?";
		int rowsUpdated = 0;
		rowsUpdated = DBHelper.executeUpdate(conn, query, newStatus, showId);
		return rowsUpdated > 0;
	}

}
