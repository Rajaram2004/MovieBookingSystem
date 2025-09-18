package model;


public class Show {
	@Override
	public String toString() {
		return "Show [showId=" + showId + ", movie=" + movie + ", screen=" + screen + ", theatre=" + theatre
				+ ", showDateTime=" + showDateTime + ", status=" + status + "]";
	}

	private Long showId;
	private Movies movie;
	private Screen screen;
	private Theatre theatre;
	private long showDateTime;
	private String status;

	public Show() {
	}

	public Show(Long showId, Movies movie, Screen screen, Theatre theatre, long showDateTime, String status) {
		this.showId = showId;
		this.movie = movie;
		this.screen = screen;
		this.theatre = theatre;
		this.showDateTime = showDateTime;
		this.status = status;
	}

	public Long getShowId() {
		return showId;
	}

	public void setShowId(Long showId) {
		this.showId = showId;
	}

	public Movies getMovie() {
		return movie;
	}

	public void setMovie(Movies movie) {
		this.movie = movie;
	}

	public void setScreen(Screen screen) {
		this.screen = screen;
	}

	public void setTheatre(Theatre theatre) {
		this.theatre = theatre;
	}

	public Theatre getTheatre() {
		return theatre;
	}

	public long getShowDateTime() {
		return showDateTime;
	}

	public void setShowDateTime(long showDateTime) {
		this.showDateTime = showDateTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Screen getScreen() {
		return screen;
	}

	

}
