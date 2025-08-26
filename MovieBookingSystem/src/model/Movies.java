package model;

public class Movies {

	private Long movieId;
	private String movieTitle;
	private int duration;
	private String genre;
	private String language;
	private int releaseYear;

	public Movies(Long movieId, String movieTitle, int duration, String genre, String language, int releaseYear) {
		this.movieId = movieId;
		this.movieTitle = movieTitle;
		this.duration = duration;
		this.genre = genre;
		this.language = language;
		this.releaseYear = releaseYear;
	}

	public int getReleaseYear() {
		return releaseYear;
	}

	public void setReleaseYear(int releaseYear) {
		this.releaseYear = releaseYear;
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
}
