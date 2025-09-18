package model;

import java.util.List;

public class TheatreAdmin extends User {

	private List<Theatre> theatre;

	public TheatreAdmin(Long theatreAdminId, String theatreAdminName, String theatreAdminEmailId,
			Long theatreAdminPhoneNumber, String theatreAdminPassword, List<Theatre> theatre, String timeZone) {

		this.id = theatreAdminId;
		this.name = theatreAdminName;
		this.emailId = theatreAdminEmailId;
		this.phoneNumber = theatreAdminPhoneNumber;
		this.password = theatreAdminPassword;
		this.theatre = theatre;
		this.timeZone = timeZone;
	}
	public List<Theatre> returnListOfTheatre(){
		return theatre;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public TheatreAdmin() {

	}

	public String getTheatreAdminName() {
		return name;
	}

	public void setTheatreAdminName(String theatreAdminName) {
		this.name = theatreAdminName;
	}

	public Long getTheatreAdminId() {
		return id;
	}

	public void setTheatreAdminId(Long theatreAdminId) {
		this.id = theatreAdminId;
	}

	public String getTheatreAdminEmailId() {
		return emailId;
	}

	public void setTheatreAdminEmailId(String theatreAdminEmailId) {
		this.emailId = theatreAdminEmailId;
	}

	public Long getTheatreAdminPhoneNumber() {
		return phoneNumber;
	}

	public void setTheatreAdminPhoneNumber(Long theatreAdminPhoneNumber) {
		this.phoneNumber = theatreAdminPhoneNumber;
	}

	public String getTheatreAdminPassword() {
		return password;
	}

	public void setTheatreAdminPassword(String theatreAdminPassword) {
		this.password = theatreAdminPassword;
	}


	
}
