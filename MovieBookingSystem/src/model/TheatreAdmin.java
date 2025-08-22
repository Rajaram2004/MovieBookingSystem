package model;

import java.util.ArrayList;
import java.util.List;

public class TheatreAdmin {
	private Long theatreAdminId;
	private String theatreAdminName;
	private String theatreAdminEmailId;
	private Long theatreAdminPhoneNumber;
	private String theatreAdminPassword;
	private List<Theatre> theatre;
	private String timeZone;
	
	public TheatreAdmin(Long theatreAdminId, String theatreAdminName, String theatreAdminEmailId,
			Long theatreAdminPhoneNumber, String theatreAdminPassword, List<Theatre> theatre, String timeZone) {
		super();
		this.theatreAdminId = theatreAdminId;
		this.theatreAdminName = theatreAdminName;
		this.theatreAdminEmailId = theatreAdminEmailId;
		this.theatreAdminPhoneNumber = theatreAdminPhoneNumber;
		this.theatreAdminPassword = theatreAdminPassword;
		this.theatre = theatre;
		this.timeZone = timeZone;
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
	public List<Theatre> getTheatre() {
		return theatre;
	}
	public void addTheatre(Theatre newTheatre) {
		if(theatre==null) {
			theatre=new ArrayList<>();
			theatre.add(newTheatre);
		}else if(newTheatre==null){
			
		}else {
			theatre.add(newTheatre);
		}
		
	}
	public void setTheatre(List<Theatre> theatre) {
		this.theatre = theatre;
	}
	public void removeTheatre(Theatre Retheatre){
		int count =0;
		boolean flag=false;
		for(Theatre t:theatre) {
			if(t==Retheatre) {
				flag=true;
				break;
			}
			count++;
		}
		if(flag==true) {
			theatre.remove(count);
		}
	}
}
