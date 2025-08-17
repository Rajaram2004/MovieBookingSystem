package model;

public class User {
	private Long userId;
	private String userName;
	private String userEmailId;
	private Long userPhoneNumber;
	private String userPreferredLocation;
	private String userPassword;
    public User() {
		
	}
	public User(Long userId, String userName, String userEmailId, Long userPhoneNumber, String userPreferredLocation,
			String userPassword) {
	
		this.userId = userId;
		this.userName = userName;
		this.userEmailId = userEmailId;
		this.userPhoneNumber = userPhoneNumber;
		this.userPreferredLocation = userPreferredLocation;
		this.userPassword = userPassword;
	}
	
	
	

	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserEmailId() {
		return userEmailId;
	}
	public void setUserEmailId(String userEmailId) {
		this.userEmailId = userEmailId;
	}
	public Long getUserPhoneNumber() {
		return userPhoneNumber;
	}
	public void setUserPhoneNumber(Long userPhoneNumber) {
		this.userPhoneNumber = userPhoneNumber;
	}
	public String getUserPreferredLocation() {
		return userPreferredLocation;
	}
	public void setUserPreferredLocation(String userPreferredLocation) {
		this.userPreferredLocation = userPreferredLocation;
	}
	public String getUserPassword() {
		return userPassword;
	}
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}	
	
}
