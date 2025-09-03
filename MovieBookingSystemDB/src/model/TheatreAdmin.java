package model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import util.DBHelper;

public class TheatreAdmin extends User {

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
	public List<Theatre> returnListOfTheatre(){
		return theatre;
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

	public List<Theatre> getTheatre(Connection conn) {
		List<Theatre> theatres = new ArrayList<>();
		String query = "SELECT t.theatreId, t.theatreName, t.theatreLocation,t.status FROM theatre t "
				+ "JOIN TheatreAdmin ta ON t.theatreId = ta.theatreId " + "WHERE ta.id = ?";
		try {
			ResultSet rs = DBHelper.executeQuery(conn,query, id);
			while (rs.next()) {
				Theatre theatre = new Theatre(rs.getLong("theatreId"), rs.getString("theatreName"),
						rs.getString("theatreLocation"), rs.getString("status"));
				theatres.add(theatre);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return theatres;
	}

	public void addTheatre(Theatre newTheatre) {
		if (theatre == null) {
			theatre = new ArrayList<>();
			theatre.add(newTheatre);
		} else if (newTheatre == null) {

		} else {
			theatre.add(newTheatre);
		}

	}

	public void setTheatre(List<Theatre> theatre) {
		this.theatre = theatre;
	}

	public void removeTheatre(Theatre Retheatre) {
		int count = 0;
		boolean flag = false;
		for (Theatre t : theatre) {
			if (t == Retheatre) {
				flag = true;
				break;
			}
			count++;
		}
		if (flag == true) {
			theatre.remove(count);
		}
	}
}
