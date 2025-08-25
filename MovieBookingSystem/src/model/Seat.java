package model;

public class Seat {
	private String seatNumber;
	private double price;
	private boolean isBooked;

	public Seat(String seatNumber, double price) {
		this.seatNumber = seatNumber;
		this.price=price;
	}

	public Seat() {
		
	}
	  public String toString() {
	        return seatNumber + " (" + price + "₹)" + (isBooked ? " [X]" : "");
	    }

	public void bookSeat() {
		if (!isBooked) {
			this.isBooked = true;
			System.out.println("Seat " + seatNumber + " booked successfully!");
		} else {
			System.out.println("Seat " + seatNumber + " is already booked.");
		}
	}

	public void unBookSeat() {
		this.isBooked = false; 
	}

	public String getSeatNumber() {
		return seatNumber;
	}

	public void setSeatNumber(String seatNumber) {
		this.seatNumber = seatNumber;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public boolean isBooked() {
		return isBooked;
	}

	public void setBooked(boolean isBooked) {
		this.isBooked = isBooked;
	}

}
