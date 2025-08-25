package model;

import java.util.ArrayList;
import java.util.List;

public class Show {
	private Long showId;
	private Theatre theatre;
	private Movies movie;
	private Screen screen;
	private long dateTimeEpoch;
	private List<List<Seat>> showSeats;
	private String isActive;

	public Show(Long showId, Theatre theatre, Movies movie, Screen screen, long dateTimeEpoch, List<Seat> seats) {
		this.showId = showId;
		this.theatre = theatre;
		this.movie = movie;
		this.screen = screen;
		this.dateTimeEpoch = dateTimeEpoch;
		isActive = "Upcoming";
		showSeats = new ArrayList<>();
		for (List<Seat> row : screen.getSeatsLayout()) {
			List<Seat> copiedRow = new ArrayList<>();
			for (Seat seat : row) {
				copiedRow.add(new Seat(seat.getSeatNumber(), seat.getPrice()));

			}
			showSeats.add(copiedRow);
		}
	}

	public void displayLayout() {
		System.out.println("---------------------------------------");

		for (int i = 0; i < showSeats.size(); i++) {
			char rowLabel = (char) ('A' + i);
			System.out.print(rowLabel + "  ");

			for (Seat seat : showSeats.get(i)) {
				if (seat.isBooked()) {
					System.out.print("[X] ");
				} else {
					System.out.print(seat.getSeatNumber() + " ");
				}
			}
			System.out.println();
		}

		System.out.println("\nLegend: [Available]  [X = Booked]");
	}

	public List<List<Seat>> getShowSeats() {
		return showSeats;
	}

	public void setShowSeats(List<List<Seat>> showSeats) {
		this.showSeats = showSeats;
	}

	public boolean bookSeat(String seatNumber) {
		for (List<Seat> row : showSeats) {
			for (Seat seat : row) {
				if (seat.getSeatNumber().equalsIgnoreCase(seatNumber)) {
					if (!seat.isBooked()) {
						seat.bookSeat();
						return true;
					} else {
						return false; 
					}
				}
			}
		}
		return false; 
	}

	public String isActive() {
		return isActive;
	}

	public void setActive(String isActive) {
		this.isActive = isActive;
	}

	public Long getShowId() {
		return showId;
	}

	public void setShowId(Long showId) {
		this.showId = showId;
	}

	public Theatre getTheatre() {
		return theatre;
	}

	public void setTheatre(Theatre theatre) {
		this.theatre = theatre;
	}

	public Movies getMovie() {
		return movie;
	}

	public void setMovie(Movies movie) {
		this.movie = movie;
	}

	public Screen getScreen() {
		return screen;
	}

	public void setScreen(Screen screen) {
		this.screen = screen;
	}

	public long getDateTimeEpoch() {
		return dateTimeEpoch;
	}

	public void setDateTimeEpoch(int dateTimeEpoch) {
		this.dateTimeEpoch = dateTimeEpoch;
	}
}
