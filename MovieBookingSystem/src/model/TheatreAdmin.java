package model;

import java.util.ArrayList;
import java.util.List;

public class TheatreAdmin extends User{
	
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
