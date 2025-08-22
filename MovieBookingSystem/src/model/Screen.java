package model;

import java.util.ArrayList;
import java.util.List;

public class Screen {
	private int screenNumber;
	private List<Seat> seats ;
	private int cols;
	private int rows;
	int premiumRows; 
	
	int regularRows ;
	boolean isActive;
	public Screen(int screenNumber, List<Seat> seats, int cols, int rows) {
		this.screenNumber = screenNumber;
		this.premiumRows=3;
		this.regularRows=3;
		this.cols = cols;
		this.rows = rows;
		this.isActive=true;
		if(seats==null) {
			this.seats=new ArrayList<>();
			createSeatLayout(rows,cols);
		}else {
			this.seats = seats;
		}
	}
	
	public Screen(int screenNumber, List<Seat> seats, int cols, int rows, int premiumRows, int regularRows,
			boolean isActive) {
		super();
		this.screenNumber = screenNumber;
		this.seats = seats;
		this.cols = cols;
		this.rows = rows;
		this.premiumRows = premiumRows;
		this.regularRows = regularRows;
		this.isActive = isActive;
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
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	public int getCols() {
		return cols;
	}

	public void setCols(int cols) {
		this.cols = cols;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
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
	public int getPremiumRows() {
		return premiumRows;
	}
	public void setPremiumRows(int premiumRows) {
		this.premiumRows = premiumRows;
	}
	public int getRegularRows() {
		return regularRows;
	}
	public void setRegularRows(int regularRows) {
		this.regularRows = regularRows;
	}
	
	
	
	
}
