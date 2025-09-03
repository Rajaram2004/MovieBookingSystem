package service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

import model.Movies;
import util.Helper;
import util.Input;

public class MovieService {
	public MovieService() {

	}

	public List<Movies> getMoviesByName(String input) {
		Connection conn=Input.getConnection();
		List<Movies> movieList = Helper.getMovieList(conn);
		if (movieList == null) {
			return null;
		}
		List<Movies> movies = new ArrayList<>();
		movies = movieList.stream().filter(m -> m.getMovieTitle().toLowerCase().startsWith(input.toLowerCase()))
				.collect(Collectors.toList());
		return movies;
	}

	public void searchByMovieName() {
		Scanner sc = Input.getScanner();
		System.out.print("Enter Movie Name (or Type '0' to Exit): ");
		String movieName = sc.nextLine();
		if (movieName.equalsIgnoreCase("0")) {
			System.out.println("------Back------");
			return;
		}
		List<Movies> movieList = getMoviesByName(movieName);
		if (movieList.isEmpty() || movieList == null) {
			System.err.println("No Movies Available in this Name");
			printAllMovies();
			searchByMovieName();
		} else {
			printSearchMovie(movieList);
		}
	}

	public void printSearchMovie(List<Movies> movieList) {

		String format = "| %-4s | %-27s | %-10s | %-10s | %-10s | %-6s |%n";
		System.out.println("+------+-----------------------------+------------+------------+------------+--------+");
		System.out.format(format, "ID", "Title", "Duration", "Genre", "Language", "Year");
		System.out.println("+------+-----------------------------+------------+------------+------------+--------+");
		for (Movies m : movieList) {
			int hours = m.getDuration() / 60;
			int minutes = m.getDuration() % 60;
			String durationFormatted = String.format("%d hr %02d min", hours, minutes);
			System.out.format(format, m.getMovieId(), m.getMovieTitle(), durationFormatted, m.getGenre(),
					m.getLanguage(), m.getReleaseYear());
		}

		System.out.println("+------+-----------------------------+------------+------------+------------+--------+");
	}

	public void printAllMovies() {
		Connection conn=Input.getConnection();
		List<Movies> movieList = Helper.getMovieList(conn);
		String format = "| %-4s | %-27s | %-10s | %-10s | %-10s | %-6s |%n";
		System.out.println("+------+-----------------------------+------------+------------+------------+--------+");
		System.out.format(format, "ID", "Title", "Duration", "Genre", "Language", "Year");
		System.out.println("+------+-----------------------------+------------+------------+------------+--------+");
		for (Movies m : movieList) {
			int hours = m.getDuration() / 60;
			int minutes = m.getDuration() % 60;
			String durationFormatted = String.format("%d hr %02d min", hours, minutes);
			System.out.format(format, m.getMovieId(), m.getMovieTitle(), durationFormatted, m.getGenre(),
					m.getLanguage(), m.getReleaseYear());
		}
		System.out.println("+------+-----------------------------+------------+------------+------------+--------+");
	}

	public void viewMoviesByGenre() {
		Connection conn=Input.getConnection();
		List<Movies> movieList = Helper.getMovieList(conn);
		Set<String> genres = new HashSet<>();
		for (Movies movie : movieList) {
			genres.add(movie.getGenre());
		}
		System.out.println("Select a Genre (or press 0 to exit):");
		int count = 1;
		Map<Integer, String> genreMap = new HashMap<>();
		for (String g : genres) {
			System.out.println(count + ". " + g);
			genreMap.put(count, g);
			count++;
		}
		System.out.println("Enter the Genre Number : ");
		int selectedId = Input.getInteger(count - 1);
		if (selectedId == 0) {
			System.out.println("------Back------");
			return;
		}
		String selectedGenre = genreMap.get(selectedId);
		System.out.println("Movies in Genre: " + selectedGenre);
		System.out.println("+----+-----------------------------+--------+----------------+");
		System.out.println("| ID | Title                       | Year   | Language       |");
		System.out.println("+----+-----------------------------+--------+----------------+");
		for (Movies movie : movieList) {
			if (movie.getGenre().equalsIgnoreCase(selectedGenre)) {
				System.out.printf("| %-2d | %-27s | %-6d | %-14s |\n", movie.getMovieId(), movie.getMovieTitle(),
						movie.getReleaseYear(), movie.getLanguage());
			}
		}
		System.out.println("+----+-----------------------------+--------+----------------+");
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
