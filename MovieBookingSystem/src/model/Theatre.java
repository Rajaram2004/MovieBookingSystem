package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import repository.InMemoryDatabase;


public class Theatre {
	Long theatreId;
	String theatreName;
	String theatreLocation;
	List<Screen> listOfScreen;
	List<Show> listOfShow;
	boolean isActive;
	List<Movies> listOfMovies;
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	public Theatre(Long theatreId, String theatreName, String theatreLocation, List<Screen> listOfScreen,
			List<Show> listOfShow, boolean isActive) {
		super();
		this.theatreId = theatreId;
		this.theatreName = theatreName;
		this.theatreLocation = theatreLocation;
		this.listOfScreen = listOfScreen;
		this.listOfShow = listOfShow;
		this.isActive = isActive;
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
	public void addScreen(List<Seat> seats,int row,int col, int numberPer,int numberRegular,boolean isactive) {
		listOfScreen.add(new Screen(listOfScreen.size()+1,seats,row,col,numberPer,numberRegular,isactive));
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
	public void removeScreen(Screen screen) {
		int count =0;
		for(Screen s: listOfScreen) {
			if(s.getScreenNumber()==screen.getScreenNumber()) {
				listOfScreen.remove(count);
				break;
			}count++;
		}
	}
	
	public void addMovieToTheatre(){
		listOfMovies=new ArrayList<>();
		HashMap<Long,Show> showDB = InMemoryDatabase.getShowDB();
		for(Show show:showDB.values()) {
			if(show.getTheatre().getTheatreId()==theatreId) {
				listOfMovies.add(show.getMovie());
			}
		}
		listOfMovies = listOfMovies.stream().distinct().collect(Collectors.toList());
	}
	public List<Movies> getListOfMovies() {
		return listOfMovies;
	}
	public void setListOfMovies(List<Movies> listOfMovies) {
		this.listOfMovies = listOfMovies;
	}
}
