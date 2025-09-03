package model;

public class Screen {
	private long screenId;
	private String screenName;
	private Theatre theatre;
	private String status;

	public Screen(long screenId, String screenName, Theatre theatre, String status) {
		this.screenId = screenId;
		this.screenName = screenName;
		this.theatre = theatre;
		this.status = status;
	}
	public Screen( String screenName, Theatre theatre, String status) {
		this.screenName = screenName;
		this.theatre = theatre;
		this.status = status;
	}

	public Screen() {
	}

	public long getScreenId() {
		return screenId;
	}

	public void setScreenId(long screenId) {
		this.screenId = screenId;
	}

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public Theatre getTheatre() {
		return theatre;
	}

	public void setTheatre(Theatre theatre) {
		this.theatre = theatre;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
