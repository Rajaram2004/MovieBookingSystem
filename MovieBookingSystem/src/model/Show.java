package model;

import java.util.ArrayList;
import java.util.List;

public class Show {
    private Long showId;
    private Theatre theatre;
    private Movies movie;
    private Screen screen;
    private int dateTimeEpoch;
    private List<Seat> seats;

    public Show(Long showId, Theatre theatre, Movies movie, Screen screen, int dateTimeEpoch, List<Seat> seats) {
        this.showId = showId;
        this.theatre = theatre;
        this.movie = movie;
        this.screen = screen;
        this.dateTimeEpoch = dateTimeEpoch;

        this.seats = new ArrayList<>();
        for (Seat s : screen.getSeats()) {
            Seat clonedSeat = new Seat(
                s.getSeatNumber(),
                s.getRowNumber(),
                s.getColNumber(),
                s.getSeatType(),
                s.getSeatPrice(),
                false
            );
            this.seats.add(clonedSeat);
        }
    }

    @Override
	public String toString() {
		return "Show [showId=" + showId + ", theatre=" + theatre + ", movie=" + movie + ", screen=" + screen
				+ ", dateTimeEpoch=" + dateTimeEpoch + ", seats=" + seats + "]";
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
    public int getDateTimeEpoch() {
        return dateTimeEpoch;
    }
    public void setDateTimeEpoch(int dateTimeEpoch) {
        this.dateTimeEpoch = dateTimeEpoch;
    }
    public List<Seat> getSeats() {
        return seats;
    }
    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }
}



//package model;
//
//import java.util.List;
//
//public class Show {
//	private Long showId;
//	private Theatre theatre;
//	private Movies movie;
//	private Screen screen;
//	private int dateTimeEpoch;
//	private List<Seat> seats;
//	public Show(Long showId, Theatre theatre, Movies movie, Screen screen, int dateTimeEpoch, List<Seat> seats) {
//		this.showId = showId;
//		this.theatre = theatre;
//		this.movie = movie;
//		this.screen = screen;
//		this.dateTimeEpoch = dateTimeEpoch;
//		this.seats = seats;
//	}
//	public Long getShowId() {
//		return showId;
//	}
//	public void setShowId(Long showId) {
//		this.showId = showId;
//	}
//	public Theatre getTheatre() {
//		return theatre;
//	}
//	public void setTheatre(Theatre theatre) {
//		this.theatre = theatre;
//	}
//	public Movies getMovie() {
//		return movie;
//	}
//	public void setMovie(Movies movie) {
//		this.movie = movie;
//	}
//	public Screen getScreen() {
//		return screen;
//	}
//	public void setScreen(Screen screen) {
//		this.screen = screen;
//	}
//	public int getDateTimeEpoch() {
//		return dateTimeEpoch;
//	}
//	public void setDateTimeEpoch(int dateTimeEpoch) {
//		this.dateTimeEpoch = dateTimeEpoch;
//	}
//	public List<Seat> getSeats() {
//		return seats;
//	}
//	public void setSeats(List<Seat> seats) {
//		this.seats = seats;
//	}
//}
// 
