package model;

public class BookingSeat {
	private long bookingSeatId;
	private Seat seat;
	private Show show;
	private boolean isBooked;

	public BookingSeat(long bookingSeatId, Seat seat, Show show, boolean isBooked) {
		this.bookingSeatId = bookingSeatId;
		this.seat = seat;
		this.show = show;
		this.isBooked = isBooked;
	}

	public BookingSeat() {
	}

	public long getBookingSeatId() {
		return bookingSeatId;
	}

	public void setBookingSeatId(long bookingSeatId) {
		this.bookingSeatId = bookingSeatId;
	}

	public Seat getSeat() {
		return seat;
	}

	public void setSeat(Seat seat) {
		this.seat = seat;
	}

	public Show getShow() {
		return show;
	}

	public void setShow(Show show) {
		this.show = show;
	}

	public boolean isBooked() {
		return isBooked;
	}

	public void setBooked(boolean isBooked) {
		this.isBooked = isBooked;
	}
}
