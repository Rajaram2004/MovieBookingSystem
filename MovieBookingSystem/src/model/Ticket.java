package model;

import java.util.List;

public class Ticket {
	private Long ticketId;
	private User user;
	private Movies movie;
	private Theatre theatre;
	private Show show;
	private List<Seat> seats;
	private int bookingDateEpoch;
	private double totalAmount;
	private String status;
	
	 
	public Ticket(Long ticketId, User user, Movies movie, Theatre theatre, Show show, List<Seat> seats,
			int bookingDateEpoch, double totalAmount, String status ) {
		this.ticketId = ticketId;
		this.user = user;
		this.movie = movie;
		this.theatre = theatre;
		this.show = show;
		this.seats = seats;
		this.bookingDateEpoch = bookingDateEpoch;
		this.totalAmount = totalAmount;
		this.status=status;
	}
	public Show getShow() {
		return show;
	}
	public void setShow(Show show) {
		this.show = show;
	}
	public List<Seat> getSeats() {
		return seats;
	}
	public void setSeats(List<Seat> seats) {
		this.seats = seats;
	}
	public Long getTicketId() {
		return ticketId;
	}
	public void setTicketId(Long ticketId) {
		this.ticketId = ticketId;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Movies getMovie() {
		return movie;
	}
	public void setMovie(Movies movie) {
		this.movie = movie;
	}
	public Theatre getTheatre() {
		return theatre;
	}
	public void setTheatre(Theatre theatre) {
		this.theatre = theatre;
	}
	public int getBookingDateEpoch() {
		return bookingDateEpoch;
	}
	public void setBookingDateEpoch(int bookingDateEpoch) {
		bookingDateEpoch = bookingDateEpoch;
	}
	public double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}

