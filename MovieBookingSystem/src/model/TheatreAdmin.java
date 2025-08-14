package model;

public class TheatreAdmin {
	private Long theatreAdminId;
	private String theatreAdminName;
	private String theatreAdminEmailId;
	private Long theatreAdminPhoneNumber;
	private String theatreAdminPassword;
	private Theatre theatre;
	
	public TheatreAdmin(Long theatreAdminId, String theatreAdminName, String theatreAdminEmailId,
			Long theatreAdminPhoneNumber, String theatreAdminPassword, Theatre theatre) {
		super();
		this.theatreAdminId = theatreAdminId;
		this.theatreAdminName = theatreAdminName;
		this.theatreAdminEmailId = theatreAdminEmailId;
		this.theatreAdminPhoneNumber = theatreAdminPhoneNumber;
		this.theatreAdminPassword = theatreAdminPassword;
		this.theatre = theatre;
	}
	public String getTheatreAdminName() {
		return theatreAdminName;
	}
	public void setTheatreAdminName(String theatreAdminName) {
		this.theatreAdminName = theatreAdminName;
	}
	public Long getTheatreAdminId() {
		return theatreAdminId;
	}
	public void setTheatreAdminId(Long theatreAdminId) {
		this.theatreAdminId = theatreAdminId;
	}
	public String getTheatreAdminEmailId() {
		return theatreAdminEmailId;
	}
	public void setTheatreAdminEmailId(String theatreAdminEmailId) {
		this.theatreAdminEmailId = theatreAdminEmailId;
	}
	public Long getTheatreAdminPhoneNumber() {
		return theatreAdminPhoneNumber;
	}
	public void setTheatreAdminPhoneNumber(Long theatreAdminPhoneNumber) {
		this.theatreAdminPhoneNumber = theatreAdminPhoneNumber;
	}
	public String getTheatreAdminPassword() {
		return theatreAdminPassword;
	}
	public void setTheatreAdminPassword(String theatreAdminPassword) {
		this.theatreAdminPassword = theatreAdminPassword;
	}
	public Theatre getTheatre() {
		return theatre;
	}
	public void setTheatre(Theatre theatre) {
		this.theatre = theatre;
	}
}
