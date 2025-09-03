package model;

public class Admin extends User {
	public Admin() {
	}

	public Admin(Long adminId, String adminName, String adminEmailId, Long adminPhoneNumber, String adminPassword,
			String timeZone) {
		this.id = adminId;
		this.name = adminName;
		this.emailId = adminEmailId;
		this.phoneNumber = adminPhoneNumber;
		this.password = adminPassword;
		this.timeZone = timeZone;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public Long getAdminId() {
		return id;
	}

	public void setAdminId(Long adminId) {
		this.id = adminId;
	}

	public String getAdminName() {
		return name;
	}

	public void setAdminName(String adminName) {
		this.name = adminName;
	}

	public String getAdminEmailId() {
		return emailId;
	}

	public void setAdminEmailId(String adminEmailId) {
		this.emailId = adminEmailId;
	}

	public Long getAdminPhoneNumber() {
		return phoneNumber;
	}

	public void setAdminPhoneNumber(Long adminPhoneNumber) {
		this.phoneNumber = adminPhoneNumber;
	}

	public String getAdminPassword() {
		return password;
	}

	public void setAdminPassword(String adminPassword) {
		this.password = adminPassword;
	}

}
