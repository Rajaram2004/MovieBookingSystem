package service;

import java.util.HashMap;

import model.Theatre;
import repository.InMemoryDatabase;

public class TheatreService {
	HashMap<Long,Theatre> theatreDB = InMemoryDatabase.getTheatreDB();
	public TheatreService(){
		
	}
	public void printAllTheatres() {
	    System.out.println("+----+---------------------+------------+");
	    System.out.println("| ID | Theatre Name        | Location   |");
	    System.out.println("+----+---------------------+------------+");

	    for (Theatre theatre : theatreDB.values()) {
	    	if(theatre.isActive()==true) {
	    		System.out.printf("| %-2d | %-19s | %-10s |\n",
		                theatre.getTheatreId(),
		                theatre.getTheatreName(),
		                theatre.getTheatreLocation());
	    	}
	        
	                
	    }
	    System.out.println("+----+---------------------+------------+");
	}

}
