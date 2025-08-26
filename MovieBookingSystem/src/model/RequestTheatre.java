package model;

public class RequestTheatre {
	private Long requestId;
	private Theatre theatre;
	private TheatreAdmin theatreAdmin;
	private boolean isApproved;
	private Admin approvedAdmin;

	public RequestTheatre(Long requestId, Theatre theatre, TheatreAdmin theatreAdmin, boolean isApproved) {
		this.requestId = requestId;
		this.theatre = theatre;
		this.theatreAdmin = theatreAdmin;
		this.isApproved = isApproved;
		this.approvedAdmin = null;
	}

	RequestTheatre() {

	}

	public Admin getApprovedAdmin() {
		return approvedAdmin;
	}

	public void setApprovedAdmin(Admin approvedAdmin) {
		this.approvedAdmin = approvedAdmin;
	}

	public Long getRequestId() {
		return requestId;
	}

	public Theatre getTheatre() {
		return theatre;
	}

	public boolean isApproved() {
		return isApproved;
	}

	public void setApproved(boolean isApproved) {
		this.isApproved = isApproved;
	}

	public TheatreAdmin getTheatreAdmin() {
		return theatreAdmin;
	}

}
