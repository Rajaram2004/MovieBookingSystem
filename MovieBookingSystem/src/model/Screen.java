package model;

import java.util.ArrayList;
import java.util.List;

public class Screen {
	private int screenNumber;
	private List<Seat> seats ;
	public Screen(int screenNumber, List<Seat> seats,int rows,int cols) {
		this.screenNumber = screenNumber;
		this.seats = seats != null ? seats : new ArrayList<>();;
		createSeatLayout(rows,cols);
	}
	
	private void createSeatLayout(int rows, int cols) {
        for (int row = 1; row <= rows; row++) {
            for (int col = 1; col <= cols; col++) {
                String seatNumber = (char) ('A' + row - 1) + String.valueOf(col);

                String seatType;
                double seatPrice;
                if (row <= 3) {
                    seatType = "Premium";
                    seatPrice = 300.0;
                } else if (row <= 6) {
                    seatType = "Regular";
                    seatPrice = 200.0;
                } else {
                    seatType = "Economy";
                    seatPrice = 100.0;
                }

                seats.add(new Seat(seatNumber, row, col, seatType, seatPrice, false));
            }
        }
    }
	
	
	public int getScreenNumber() {
		return screenNumber;
	}
	public void setScreenNumber(int screenNumber) {
		this.screenNumber = screenNumber;
	}
	public List<Seat> getSeats() {
		return seats;
	}
	public void setSeats(List<Seat> seats) {
		this.seats = seats;
	} 
	
	
	
	
	
}
