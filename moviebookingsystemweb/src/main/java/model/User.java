package model;

public class User {
	protected Long id;
	protected String emailId;
	protected String name;
	protected String gender;
	protected Long phoneNumber;
	protected String password;
	protected String userType;
	protected double balance;
	protected String timeZone;
	protected boolean active;

	public User() {
	}

	public User(Long id, String emailId, String name, Long phoneNumber, String password, String userType,
			double balance, String timeZone, boolean active) {
		this.id = id;
		this.emailId = emailId;
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.password = password;
		this.userType = userType;
		this.balance = balance;
		this.timeZone = timeZone;
		this.active = active;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(Long phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public double getBalance() {
		return balance;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public boolean getIsActive() {
		return active;
	}

	public void setIsActive(boolean b) {
		this.active = b;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	


}
