package model;

public class Seat {
	private long seatId;
	private Screen screenId;
	private String seatNumber;
	private String seatType;
	private double price;

	public Seat(long seatId, Screen screenId, String seatNumber, String seatType, double price) {

		this.seatId = seatId;
		this.screenId = screenId;
		this.seatNumber = seatNumber;
		this.seatType = seatType;
		this.price = price;
	}

	public Seat(long seatId, String seatNumber, String seatType, double price) {
		this.seatId = seatId;
		this.seatNumber = seatNumber;
		this.seatType = seatType;
		this.price = price;
	}

	public Seat() {
		// TODO Auto-generated constructor stub
	}

	public long getSeatId() {
		return seatId;
	}

	public void setSeatId(long seatId) {
		this.seatId = seatId;
	}

	public Screen getScreenId() {
		return screenId;
	}

	public void setScreenId(Screen screenId) {
		this.screenId = screenId;
	}

	public String getSeatNumber() {
		return seatNumber;
	}

	public void setSeatNumber(String seatNumber) {
		this.seatNumber = seatNumber;
	}

	public String getSeatType() {
		return seatType;
	}

	public void setSeatType(String seatType) {
		this.seatType = seatType;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

}
