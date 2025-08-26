package model;

import java.util.ArrayList;
import java.util.List;

public class Screen {
	private int screenNumber;
	private Theatre theatre;
	private List<List<Seat>> seatsLayout;
	private boolean isActive;

	public Screen(int screenId, Theatre theatre, List<Integer> seatsPerRow, List<Double> price) {
		this.screenNumber = screenId;
		this.theatre = theatre;
		isActive = true;
		this.seatsLayout = new ArrayList<>();
		createSeats(seatsPerRow, price);
	}

	public Theatre getTheatre() {
		return theatre;
	}

	private void createSeats(List<Integer> seatsPerRow, List<Double> price) {
		for (int row = 0; row < seatsPerRow.size(); row++) {
			int seatCount = seatsPerRow.get(row);
			List<Seat> rowSeats = new ArrayList<>();

			char rowLabel = (char) ('A' + row);
			double pricePerRow = price.get(row);
			for (int seatNum = 1; seatNum <= seatCount; seatNum++) {
				String seatId = rowLabel + String.valueOf(seatNum);

				rowSeats.add(new Seat(seatId, pricePerRow));
			}
			seatsLayout.add(rowSeats);
		}
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public List<List<Seat>> getSeatsLayout() {
		return seatsLayout;
	}

	public int getScreenNumber() {
		return screenNumber;
	}

	public void displayLayout() {
		System.out.println("\n--- Seat Layout for Screen: " + screenNumber + " ---");

		for (int i = 0; i < seatsLayout.size(); i++) {
			char rowLabel = (char) ('A' + i);
			System.out.print(rowLabel + "  ");

			for (Seat seat : seatsLayout.get(i)) {
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

}
