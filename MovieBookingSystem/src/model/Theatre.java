package model;

import java.util.List;


public class Theatre {
	Long theatreId;
	String theatreName;
	String theatreLocation;
	List<Screen> listOfScreen;
	List<Show> listOfShow;
	
	public Theatre(Long theatreId, String theatreName, String theatreLocation, List<Screen> listOfScreen,
			List<Show> listOfShow) {
		super();
		this.theatreId = theatreId;
		this.theatreName = theatreName;
		this.theatreLocation = theatreLocation;
		this.listOfScreen = listOfScreen;
		this.listOfShow = listOfShow;
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
		theatreName = theatreName;
	}
	public String getTheatreLocation() {
		return theatreLocation;
	}
	public void setTheatreLocation(String theatreLocation) {
		theatreLocation = theatreLocation;
	}
	public List<Screen> getListOfScreen() {
		return listOfScreen;
	}
	public void addScreen(int row,int col) {
		listOfScreen.add(new Screen(listOfScreen.size()+1,null,row,col));
	}
	public void setListOfScreen(List<Screen> listOfScreen) {
		this.listOfScreen = listOfScreen;
	}
	
	public void addShow(Show show) {
		listOfShow.add(show);
		
	}
	public List<Show> getListOfShow() {
		return listOfShow;
	}
	public void setListOfShow(List<Show> listOfShow) {
		this.listOfShow = listOfShow;
	}
}
