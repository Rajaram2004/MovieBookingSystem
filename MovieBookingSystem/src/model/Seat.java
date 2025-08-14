package model;

public class Seat {
	private String seatNumber;     
	private int rowNumber;
	private int colNumber;
	private String seatType;    
	private double seatPrice;
	private boolean seatStatus;
	
	public Seat(String seatNumber, int rowNumber, int colNumber, String seatType, double seatPrice,
			boolean seatStatus) {
		this.seatNumber = seatNumber;
		this.rowNumber = rowNumber;
		this.colNumber = colNumber;
		this.seatType = seatType;
		this.seatPrice = seatPrice;
		this.seatStatus = seatStatus;
	}
	public double getSeatPrice() {
		return seatPrice;
	}
	public void setSeatPrice(double seatPrice) {
		this.seatPrice = seatPrice;
	}
	public String getSeatNumber() {
		return seatNumber;
	}
	public void setSeatNumber(String seatNumber) {
		this.seatNumber = seatNumber;
	}
	public int getRowNumber() {
		return rowNumber;
	}
	public void setRowNumber(int rowNumber) {
		this.rowNumber = rowNumber;
	}
	public int getColNumber() {
		return colNumber;
	}
	public void setColNumber(int colNumber) {
		this.colNumber = colNumber;
	}
	public String getSeatType() {
		return seatType;
	}
	public void setSeatType(String seatType) {
		this.seatType = seatType;
	}
	public boolean isSeatStatus() {
		return seatStatus;
	}
	public void setSeatStatus(boolean seatStatus) {
		this.seatStatus = seatStatus;
	} 
}
