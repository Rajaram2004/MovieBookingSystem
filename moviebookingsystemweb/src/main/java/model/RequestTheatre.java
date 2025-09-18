package model;

public class RequestTheatre {
	private Long requestId;
	private Theatre theatre;
	private User theatreAdmin;
	private boolean isApproved;
	private User approvedAdmin;
public RequestTheatre() {}
	public RequestTheatre(Long requestId, Theatre theatre, User theatreAdmin, boolean isApproved, User approvedAdmin) {
		
		this.requestId = requestId;
		this.theatre = theatre;
		this.theatreAdmin = theatreAdmin;
		this.isApproved = isApproved;
		this.approvedAdmin = approvedAdmin;
	}

	public Long getRequestId() {
		return requestId;
	}

	public void setRequestId(Long requestId) {
		this.requestId = requestId;
	}

	public Theatre getTheatre() {
		return theatre;
	}

	public void setTheatre(Theatre theatre) {
		this.theatre = theatre;
	}

	public User getTheatreAdmin() {
		return theatreAdmin;
	}

	public void setTheatreAdmin(User theatreAdmin) {
		this.theatreAdmin = theatreAdmin;
	}

	public boolean isApproved() {
		return isApproved;
	}

	public void setApproved(boolean isApproved) {
		this.isApproved = isApproved;
	}

	public User getApprovedAdmin() {
		return approvedAdmin;
	}

	public void setApprovedAdmin(User approvedAdmin) {
		this.approvedAdmin = approvedAdmin;
	}

}
