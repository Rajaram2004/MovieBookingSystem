package service;

import java.util.HashMap;
import java.util.Scanner;

import model.Movies;
import model.Theatre;
import repository.InMemoryDatabase;
import util.Input;

public class MovieService {
	HashMap<Long, Movies> movieDB = InMemoryDatabase.getMovieDB();

	public MovieService() {

	}

	public void searchByMovieName() {
		Scanner sc = Input.getScanner();
		System.out.print("Enter Movie Name : ");
		String movieName = sc.nextLine();
		boolean found = false;
		for (Movies m : movieDB.values()) {
			if (m.getMovieTitle().equalsIgnoreCase(movieName)) {
				found = true;
				printSearchMovie(m);
				break;
			}
		}

		if (!found)
		{
			System.err.println("Movie not found or invalid. Please check the name and try again.");
			printAllMovies();
			searchByMovieName();
		}
	}
	public void printSearchMovie(Movies m) {
		// Table format
		String format = "| %-4s | %-27s | %-10s | %-10s | %-10s | %-6s | %-32s |%n";

		System.out.println(
				"+------+-----------------------------+------------+------------+------------+--------+--------------------------------+");
		System.out.format(format, "ID", "Title", "Duration", "Genre", "Language", "Year", "Theatres");
		System.out.println(
				"+------+-----------------------------+------------+------------+------------+--------+--------------------------------+");
		
			String theatres = m.getListOfTheatre().stream().map(Theatre::getTheatreName).reduce((a, b) -> a + ", " + b)
					.orElse("No Theatre");

			int hours = m.getDuration() / 60;
			int minutes = m.getDuration() % 60;
			String durationFormatted = String.format("%d hr %02d min", hours, minutes);

			if (theatres.length() > 30) {
				theatres = theatres.substring(0, 27) + "...";
			}

			System.out.format(format, m.getMovieId(), m.getMovieTitle(), durationFormatted, m.getGenre(),
					m.getLanguage(), m.getReleaseYear(), theatres);
		
		System.out.println(
				"+------+-----------------------------+------------+------------+------------+--------+--------------------------------+");
	}
	
	
	public void printAllMovies() {
		// Table format
		String format = "| %-4s | %-27s | %-10s | %-10s | %-10s | %-6s | %-32s |%n";

		System.out.println(
				"+------+-----------------------------+------------+------------+------------+--------+--------------------------------+");
		System.out.format(format, "ID", "Title", "Duration", "Genre", "Language", "Year", "Theatres");
		System.out.println(
				"+------+-----------------------------+------------+------------+------------+--------+--------------------------------+");

		for (Movies m : movieDB.values()) {
			String theatres = m.getListOfTheatre().stream().map(Theatre::getTheatreName).reduce((a, b) -> a + ", " + b)
					.orElse("No Theatre");

			int hours = m.getDuration() / 60;
			int minutes = m.getDuration() % 60;
			String durationFormatted = String.format("%d hr %02d min", hours, minutes);

			if (theatres.length() > 30) {
				theatres = theatres.substring(0, 27) + "...";
			}

			System.out.format(format, m.getMovieId(), m.getMovieTitle(), durationFormatted, m.getGenre(),
					m.getLanguage(), m.getReleaseYear(), theatres);
		}
		System.out.println(
				"+------+-----------------------------+------------+------------+------------+--------+--------------------------------+");
	}
}
