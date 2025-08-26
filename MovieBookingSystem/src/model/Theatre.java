package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import repository.InMemoryDatabase;


public class Theatre {
	private Long theatreId;
	private String theatreName;
	private String theatreLocation;
	private List<Screen> listOfScreen;
	private boolean isActive;
	
	public Theatre(Long theatreId, String theatreName, String theatreLocation, List<Screen> listOfScreen,
			List<Show> listOfShow, List<Movies> listOfMovies, boolean isActive) {
		this.theatreId = theatreId;
		this.theatreName = theatreName;
		this.theatreLocation = theatreLocation;
		this.listOfScreen = listOfScreen;
		//this.listOfShow = listOfShow;
		this.isActive = isActive;
		//this.listOfMovies = listOfMovies;
	}
	

	
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	public Long getTheatreId() {
		return theatreId;
	}
	
	public String getTheatreName() {
		return theatreName;
	}
	
	public String getTheatreLocation() {
		return theatreLocation;
	}
	
	public List<Screen> getListOfScreen() {
		return listOfScreen;
	}
//	public void addScreen(List<Seat> seats,List<Integer> listOfRow,List<Double> pricePerRow) {
//		listOfScreen.add(new Screen(listOfScreen.size()+1,listOfRow,pricePerRow));
//	}
	public void setListOfScreen(List<Screen> listOfScreen) {
		this.listOfScreen = listOfScreen;
	}
	
	public void addShow(Show show) {
		//listOfShow.add(show);
		
	}
	public List<Show> getListOfShow() {
		HashMap<Long,Show> showDB =InMemoryDatabase.getShowDB();
		List<Show>listOfShow=new ArrayList<>();
		for(Show show : showDB.values()) {
			
			if(show.getTheatre().getTheatreId()==theatreId && show.isActive().equalsIgnoreCase("upcoming")) {
				listOfShow.add(show);
			}
		}
		listOfShow=listOfShow.stream().distinct().collect(Collectors.toList());
		return listOfShow;
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
	}
	public List<Movies> getListOfMovies() {
		HashMap<Long,Show> showDB =InMemoryDatabase.getShowDB();
		List<Movies>listOfMovies=new ArrayList<>();
		for(Show show : showDB.values()) {
			
			if(show.getTheatre().getTheatreId()==theatreId && show.isActive().equalsIgnoreCase("upcoming")) {
				listOfMovies.add(show.getMovie());
			}
		}
		listOfMovies=listOfMovies.stream().distinct().collect(Collectors.toList());
		return listOfMovies;
	}
	public void setListOfMovies(List<Movies> listOfMovies) {
//		this.listOfMovies = listOfMovies;
	}
	public void addMovieInTheatre(Movies movie) {
//		if(!listOfMovies.contains(movie)) {
//			listOfMovies.add(movie);
//		}
	}

	public void removeMovieFromList(Movies movie) {
//		int index=0;
//		for(Movies mov:listOfMovies) {
//			if(mov.getMovieId()==movie.getMovieId()) {
//				listOfMovies.remove(index);
//				System.out.println("removed");
//				break;
//			}
//			index++;
//		}
	
	}
}
