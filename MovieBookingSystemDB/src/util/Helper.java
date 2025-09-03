package util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dao.ShowDAO;
import dao.UserDAO;
import model.Movies;
import model.Show;
import model.Theatre;
import model.Ticket;
import model.User;

public class Helper {

	public static List<Movies> getMovieList(Connection conn) {
		List<Movies> movies = new ArrayList<>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			String query = """
					    SELECT DISTINCT m.movieId, m.movieName, m.genre, m.duration, m.language ,m.releaseDate FROM shows s
					    INNER JOIN movies m ON s.movieId = m.movieId
					    WHERE s.status = 'active'
					""";

			stmt = conn.prepareStatement(query);
			rs = stmt.executeQuery();

			while (rs.next()) {
				Movies movie = new Movies(rs.getLong("movieId"), rs.getString("movieName"), rs.getInt("duration"),
						rs.getString("genre"), rs.getString("language"), rs.getLong("releaseDate"));
				movies.add(movie);
			}
		} catch (SQLException e) {
			System.err.println("Error while fetching active show movies: " + e.getMessage());
			e.printStackTrace();
		}
		return movies;
	}
	public static List<Movies> getActiveMovie(Connection conn,long active) {
		List<Movies> movies = new ArrayList<>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			String query = """
					   SELECT * FROM movies WHERE isActive = ? ;
					""";

			stmt = conn.prepareStatement(query);
			stmt.setLong(1, active);
			rs = stmt.executeQuery();
			while (rs.next()) {
				Movies movie = new Movies(rs.getLong("movieId"), rs.getString("movieName"), rs.getInt("duration"),
						rs.getString("genre"), rs.getString("language"), rs.getLong("releaseDate"));
				movies.add(movie);
			}
		} catch (SQLException e) {
			System.err.println("Error while fetching active show movies: " + e.getMessage());
			e.printStackTrace();
		}
		return movies;
	}

	public static Ticket getTicketById(Connection conn, Long ticketId) {
		Ticket ticket = null;
		String query = "SELECT ticketId, userId, showId, amount, ticketBookedDate,status FROM ticket WHERE ticketId = ?";
		try {
			ResultSet rs = DBHelper.executeQuery(conn, query, ticketId);
			if (rs.next()) {
				Long userId = rs.getLong("userId");
				Long showId = rs.getLong("showId");
				ticket = new Ticket(rs.getLong("ticketId"), rs.getDouble("amount"), rs.getString("status"),
						rs.getLong("ticketBookedDate"));
				User user = UserDAO.getUserById(conn, userId);
				Show show = ShowDAO.getShowById(conn, showId);
				ticket.setUser(user);
				ticket.setShow(show);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return ticket;
	}

	public static List<Ticket> getAllTicket(Connection conn, User currentUser) {
		List<Ticket> userTickets = new ArrayList<>();
		List<Long> userIdList = new ArrayList<>();
		List<Long> showIdList = new ArrayList<>();
		String query = "SELECT * FROM ticket WHERE userId = ?";
		try {
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setLong(1, currentUser.getId());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				userIdList.add(rs.getLong("userId"));
				showIdList.add(rs.getLong("showId"));
				Ticket ticket = new Ticket();
				ticket.setTicketId(rs.getLong("ticketId"));
				ticket.setStatus(rs.getString("status"));
				ticket.setAmount(rs.getDouble("amount"));
				ticket.setTicketBookedDate(rs.getLong("ticketBookedDate"));
				userTickets.add(ticket);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		int index = 0;
		for (Ticket t : userTickets) {
			t.setUser(UserDAO.getUserById(conn, userIdList.get(index)));
			index++;
		}
		index = 0;
		for (Ticket t : userTickets) {
			t.setShow(ShowDAO.getShowById(conn, showIdList.get(index)));
			index++;
		}
		return userTickets;
	}

	public static boolean updateTicketStatus(Connection conn,long ticketId, String status) {
		String query = "UPDATE ticket SET status = ? WHERE ticketId = ?";
		try {
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, status);
			ps.setLong(2, ticketId);					
			int rowsAffected = ps.executeUpdate();
			return rowsAffected > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}



}
