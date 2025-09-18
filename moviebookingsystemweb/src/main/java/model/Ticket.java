package model;

public class Ticket {
	private Long ticketId;
	private User user;
	private Show show;
	private double amount;
	private String status;
	private long ticketBookedDate;

	public Ticket() {
	}

	public Ticket(Long ticketId, User user, Show show, double amount, String status, long ticketBookedDate) {
		super();
		this.ticketId = ticketId;
		this.user = user;
		this.show = show;
		this.amount = amount;
		this.status = status;
		this.ticketBookedDate = ticketBookedDate;
	}
	public Ticket(Long ticketId, double amount, String status, long ticketBookedDate) {
		super();
		this.ticketId = ticketId;
		this.amount = amount;
		this.status = status;
		this.ticketBookedDate = ticketBookedDate;
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

	public Show getShow() {
		return show;
	}

	public void setShow(Show show) {
		this.show = show;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public long getTicketBookedDate() {
		return ticketBookedDate;
	}

	public void setTicketBookedDate(long ticketBookedDate) {
		this.ticketBookedDate = ticketBookedDate;
	}

}
