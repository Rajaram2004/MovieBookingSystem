package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Movies;
import util.DBHelper;

public class MovieDAO {
	public static long insertMovie(Connection conn, Movies movie) {
		String sql = "INSERT INTO movies (movieName, genre, releaseDate, duration, language) VALUES (?, ?, ?, ?, ?)";

		try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			ps.setString(1, movie.getMovieTitle());
			ps.setString(2, movie.getGenre());
			ps.setLong(3, movie.getReleaseYear());
			ps.setInt(4, movie.getDuration());
			ps.setString(5, movie.getLanguage());

			int affectedRows = ps.executeUpdate();

			if (affectedRows == 0) {
				throw new SQLException("Inserting movie failed, no rows affected.");
			}
			try (ResultSet rs = ps.getGeneratedKeys()) {
				if (rs.next()) {
					return rs.getLong(1);
				} else {
					throw new SQLException("Inserting movie failed, no ID obtained.");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}

	public static Movies getMovieById(Connection conn, Long movieId) {
		Movies movie = null;
		String sql = "SELECT * FROM movies WHERE movieId = ?";
		ResultSet rs = null;

		try {
			rs = DBHelper.executeQuery(conn, sql, movieId);
			if (rs.next()) {
				movie = new Movies(rs.getLong("movieId"), rs.getString("movieName"), rs.getInt("duration"),
						rs.getString("genre"), rs.getString("language"), rs.getLong("releaseDate"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return movie;
	}

	public static List<Movies> getActiveMoviesByTheatreId(Connection conn, long theatreId) {
		List<Movies> moviesList = new ArrayList<>();
		String query = "SELECT DISTINCT m.movieId, m.movieName, m.language, m.genre, m.duration, m.releaseDate "
				+ "FROM movies m " + "JOIN shows s ON m.movieId = s.movieId "
				+ "WHERE s.theatreId = ? AND LOWER(s.status) = 'active'";
		ResultSet rs = null;

		try {
			rs = DBHelper.executeQuery(conn, query, theatreId);
			while (rs.next()) {
				Movies movie = new Movies(rs.getLong("movieId"), rs.getString("movieName"), rs.getInt("duration"),
						rs.getString("genre"), rs.getString("language"), rs.getLong("releaseDate"));
				moviesList.add(movie);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return moviesList;
	}

	public static void updateMoviesIsActive(Connection conn) {
		String query = """
				    UPDATE movies m
				    SET isActive = (
				        CASE
				            -- If there is at least one show in the last 1 year, we proceed
				            WHEN EXISTS (
				                SELECT 1 FROM shows s
				                WHERE s.movieId = m.movieId
				                AND s.showDateTime >= (UNIX_TIMESTAMP() * 1000 - (365 * 24 * 60 * 60 * 1000))
				            ) THEN
				                CASE
				                    -- If at least one active show exists → TRUE
				                    WHEN EXISTS (
				                        SELECT 1 FROM shows s
				                        WHERE s.movieId = m.movieId
				                        AND s.status = 'active'
				                    ) THEN TRUE

				                    -- If the last show's end time + 7 days < current time → FALSE
				                    WHEN NOT EXISTS (
				                        SELECT 1 FROM shows s
				                        WHERE s.movieId = m.movieId
				                        AND (s.showDateTime + (m.duration * 60000) + (7 * 24 * 60 * 60 * 1000)) > UNIX_TIMESTAMP() * 1000
				                    ) THEN FALSE

				                    -- Otherwise, keep the current value
				                    ELSE m.isActive
				                END

				            -- If no shows exist in the last 1 year → Don't change isActive
				            ELSE m.isActive
				        END
				    )
				""";

		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			int updatedRows = pstmt.executeUpdate();
			System.out.println(updatedRows + " movie(s) updated successfully.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void updateMoviesActive(Connection conn, Long movieId) {
		String query = """
				    UPDATE movies SET isActive = 1 where movieId = ? ;
				""";

		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setLong(1, movieId);
			int updatedRows = pstmt.executeUpdate();
			System.out.println(updatedRows + " movie(s) updated successfully.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//    private static Movies returnMovie(ResultSet rs) {
//        try {
//            return new Movies(
//                    rs.getLong("movieId"),
//                    rs.getString("movieName"),
//                    rs.getInt("duration"),
//                    rs.getString("genre"),
//                    rs.getString("language"),
//                    rs.getLong("releaseDate")
//            );
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
	public static List<Movies> getAllMovieList(Connection conn) {
		List<Movies> movies = new ArrayList<>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			String query = """
					    SELECT * FROM
					    movies m
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
}
