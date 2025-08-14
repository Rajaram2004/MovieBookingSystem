package model;

import java.util.List;

public class Movies {
	
	private Long movieId;
	private String movieTitle;
	private int duration;
	private String genre;
	private String language;
	private int releaseYear;
	private List<Theatre> listOfTheatre;
	
	public Movies(Long movieId, String movieTitle, int duration, String genre, String language, int releaseYear,
			List<Theatre> listOfTheatre) {
		
		this.movieId = movieId;
		this.movieTitle = movieTitle;
		this.duration = duration;
		this.genre = genre;
		this.language = language;
		this.releaseYear = releaseYear;
		this.listOfTheatre = listOfTheatre;
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
	public List<Theatre> getListOfTheatre() {
		return listOfTheatre;
	}
	public void setListOfTheatre(List<Theatre> listOfTheatre) {
		this.listOfTheatre = listOfTheatre;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
}



