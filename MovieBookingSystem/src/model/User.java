package model;

public class User extends UserDetails{
	
	private String PreferredLocation;
	private double balance;
    public User() {
		
	}
	public User(Long userId, String userName, String userEmailId, Long userPhoneNumber, String userPreferredLocation,
			String userPassword, double balance, String timeZone) {
		super();
		this.Id = userId;
		this.Name = userName;
		this.EmailId = userEmailId;
		this.PhoneNumber = userPhoneNumber;
		this.PreferredLocation = userPreferredLocation;
		this.Password = userPassword;
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
		return Id;
	}
	public void setUserId(Long userId) {
		this.Id = userId;
	}
	public String getUserName() {
		return Name;
	}
	public void setUserName(String userName) {
		this.Name = userName;
	}
	public String getUserEmailId() {
		return EmailId;
	}
	public void setUserEmailId(String userEmailId) {
		this.EmailId = userEmailId;
	}
	public Long getUserPhoneNumber() {
		return PhoneNumber;
	}
	public void setUserPhoneNumber(Long userPhoneNumber) {
		this.PhoneNumber = userPhoneNumber;
	}
	public String getUserPreferredLocation() {
		return PreferredLocation;
	}
	public void setUserPreferredLocation(String userPreferredLocation) {
		this.PreferredLocation = userPreferredLocation;
	}
	public String getUserPassword() {
		return Password;
	}
	public void setUserPassword(String userPassword) {
		this.Password = userPassword;
	}	
}
