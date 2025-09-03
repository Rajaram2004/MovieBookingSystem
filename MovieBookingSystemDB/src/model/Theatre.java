package model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import util.DBHelper;

public class Theatre {
	private Long theatreId;
	private String theatreName;
	private String theatreLocation;
	private String status;

	public Theatre(Long theatreId, String theatreName, String theatreLocation, String status) {
		this.theatreId = theatreId;
		this.theatreName = theatreName;
		this.theatreLocation = theatreLocation;
		this.status = status;
	}
	public Theatre( String theatreName, String theatreLocation, String status) {
		this.theatreName = theatreName;
		this.theatreLocation = theatreLocation;
		this.status = status;
	}

	public Theatre() {

	}

	public Long getTheatreId() {
		return theatreId;
	}

	public void setTheatreId(Long theatreId) {
		this.theatreId = theatreId;
	}

	public String getTheatreName() {
		return theatreName;
	}

	public void setTheatreName(String theatreName) {
		this.theatreName = theatreName;
	}

	public String getTheatreLocation() {
		return theatreLocation;
	}

	public void setTheatreLocation(String theatreLocation) {
		this.theatreLocation = theatreLocation;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<Screen> getListOfScreen(Connection conn ,Theatre theatre) {
	    List<Screen> listOfScreen = new ArrayList<>();
	    String query = "SELECT * FROM screen WHERE theatreId = ?";
	        try {
	        	ResultSet rs = DBHelper.executeQuery(conn,query,theatreId);
	            while (rs.next()) {
	                Screen screen = new Screen();
	                screen.setScreenId(rs.getLong("screenId"));
	                screen.setScreenName(rs.getString("screenName"));
	                screen.setTheatre(theatre);
	                screen.setStatus(rs.getString("status"));
	                listOfScreen.add(screen);
	            }
	        
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	        
	    return listOfScreen;
	}

}
