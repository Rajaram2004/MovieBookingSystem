package model;

import java.util.ArrayList;
import java.util.List;

public class TheatreAdmin extends UserDetails{
	
	private List<Theatre> theatre;
	
	
	public TheatreAdmin(Long theatreAdminId, String theatreAdminName, String theatreAdminEmailId,
			Long theatreAdminPhoneNumber, String theatreAdminPassword, List<Theatre> theatre, String timeZone) {
		super();
		this.Id = theatreAdminId;
		this.Name = theatreAdminName;
		this.EmailId = theatreAdminEmailId;
		this.PhoneNumber = theatreAdminPhoneNumber;
		this.Password = theatreAdminPassword;
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
		return Name;
	}
	public void setTheatreAdminName(String theatreAdminName) {
		this.Name = theatreAdminName;
	}
	public Long getTheatreAdminId() {
		return Id;
	}
	public void setTheatreAdminId(Long theatreAdminId) {
		this.Id = theatreAdminId;
	}
	public String getTheatreAdminEmailId() {
		return EmailId;
	}
	public void setTheatreAdminEmailId(String theatreAdminEmailId) {
		this.EmailId = theatreAdminEmailId;
	}
	public Long getTheatreAdminPhoneNumber() {
		return PhoneNumber;
	}
	public void setTheatreAdminPhoneNumber(Long theatreAdminPhoneNumber) {
		this.PhoneNumber = theatreAdminPhoneNumber;
	}
	public String getTheatreAdminPassword() {
		return Password;
	}
	public void setTheatreAdminPassword(String theatreAdminPassword) {
		this.Password = theatreAdminPassword;
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
