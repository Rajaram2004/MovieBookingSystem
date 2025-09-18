package model;

import java.time.Instant;
import java.time.ZoneId;

public class Movies {

	private Long movieId;
	private String movieTitle;
	private int duration;
	private String genre;
	private String language;
	private long releaseDate;

	
	public Movies(Long movieId, String movieTitle, int duration, String genre, String language, long releaseDate) {
		super();
		this.movieId = movieId;
		this.movieTitle = movieTitle;
		this.duration = duration;
		this.genre = genre;
		this.language = language;
		this.releaseDate = releaseDate;
	}

	public Long getMovieId() {
		return movieId;
	}

	public void setMovieId(Long movieId) {
		this.movieId = movieId;
	}

	public String getMovieTitle() {
		return movieTitle;
	}

	public void setMovieTitle(String movieTitle) {
		this.movieTitle = movieTitle;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public long getReleaseYear() {
		return Instant.ofEpochSecond(releaseDate).atZone(ZoneId.systemDefault()).getYear();
	}

	public void setReleaseYear(long releaseYear) {
		this.releaseDate = releaseYear;
	}

	@Override
	public String toString() {
		return "Movies [movieId=" + movieId + ", movieTitle=" + movieTitle + ", duration=" + duration + ", genre="
				+ genre + ", language=" + language + ", releaseDate=" + releaseDate + "]";
	}

}
