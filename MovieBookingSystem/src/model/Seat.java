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

	public String getSeatNumber() {
		return seatNumber;
	}



	public double getPrice() {
		return price;
	}

	public boolean isBooked() {
		return isBooked;
	}

	public void setBooked(boolean isBooked) {
		this.isBooked = isBooked;
	}

}
