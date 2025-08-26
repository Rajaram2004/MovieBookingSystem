package model;

import java.util.List;

public class Ticket {
	private Long ticketId;
	private Customer user;
	private Show show;
	private List<Seat> seats;
	private double totalAmount;
	private String status;
	
	 
	public Ticket(Long ticketId, Customer user, Show show, List<Seat> seats
		, double totalAmount, String status ) {
		this.ticketId = ticketId;
		this.user = user;
		this.show = show;
		this.seats = seats;
		this.totalAmount = totalAmount;
		this.status=status;
	}

	public Show getShow() {
		return show;
	}
	
	public List<Seat> getSeats() {
		return seats;
	}
	
	public Long getTicketId() {
		return ticketId;
	}
	public Customer getUser() {
		return user;
	}
	public Movies getMovie() {
		return show.getMovie();
	}
	public Theatre getTheatre() {
		return show.getTheatre();
	}
	public long getBookingDateEpoch() {
		return show.getDateTimeEpoch();
	}
	public double getTotalAmount() {
		return totalAmount;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}

