package model;

public class Admin extends UserDetails{

	public Admin(Long adminId, String adminName, String adminEmailId, Long adminPhoneNumber, String adminPassword,
			String timeZone) {
		
		this.Id = adminId;
		this.Name = adminName;
		this.EmailId = adminEmailId;
		this.PhoneNumber = adminPhoneNumber;
		this.Password = adminPassword;
		this.timeZone = timeZone;
	}
	public String getTimeZone() {
		return timeZone;
	}
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}
	public Long getAdminId() {
		return Id;
	}
	public void setAdminId(Long adminId) {
		this.Id = adminId;
	}
	public String getAdminName() {
		return Name;
	}
	public void setAdminName(String adminName) {
		this.Name = adminName;
	}
	public String getAdminEmailId() {
		return EmailId;
	}
	public void setAdminEmailId(String adminEmailId) {
		this.EmailId = adminEmailId;
	}
	public Long getAdminPhoneNumber() {
		return PhoneNumber;
	}
	public void setAdminPhoneNumber(Long adminPhoneNumber) {
		this.PhoneNumber = adminPhoneNumber;
	}
	public String getAdminPassword() {
		return Password;
	}
	public void setAdminPassword(String adminPassword) {
		this.Password = adminPassword;
	}
	
}
