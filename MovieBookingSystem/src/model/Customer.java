package model;

public class Customer extends User{
	
	private String PreferredLocation;
	private double balance;
    public Customer() {
		
	}
	public Customer(Long userId, String userName, String userEmailId, Long userPhoneNumber, String userPreferredLocation,
			String userPassword, double balance, String timeZone) {
		
		this.id = userId;
		this.name = userName;
		this.emailId = userEmailId;
		this.phoneNumber = userPhoneNumber;
		this.PreferredLocation = userPreferredLocation;
		this.password = userPassword;
		this.balance = balance;
		this.timeZone = timeZone;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	public String getTimeZone() {
		return timeZone;
	}
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}
	public Long getUserId() {
		return id;
	}
	public void setUserId(Long userId) {
		this.id = userId;
	}
	public String getUserName() {
		return name;
	}
	public void setUserName(String userName) {
		this.name = userName;
	}
	public String getUserEmailId() {
		return emailId;
	}
	public void setUserEmailId(String userEmailId) {
		this.emailId = userEmailId;
	}
	public Long getUserPhoneNumber() {
		return phoneNumber;
	}
	public void setUserPhoneNumber(Long userPhoneNumber) {
		this.phoneNumber = userPhoneNumber;
	}
	public String getUserPreferredLocation() {
		return PreferredLocation;
	}
	public void setUserPreferredLocation(String userPreferredLocation) {
		this.PreferredLocation = userPreferredLocation;
	}
	public String getUserPassword() {
		return password;
	}
	public void setUserPassword(String userPassword) {
		this.password = userPassword;
	}	
}
