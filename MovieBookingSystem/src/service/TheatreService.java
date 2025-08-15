package service;

import java.util.HashMap;

import model.Theatre;
import repository.InMemoryDatabase;

public class TheatreService {
	HashMap<Long,Theatre> theatreDB = InMemoryDatabase.getTheatreDB();
	public TheatreService(){
		
	}
	
	public void printAllTheatres() {
	    System.out.println("+----+---------------------+------------+----------------+---------------------+");
	    System.out.println("| ID | Theatre Name        | Location   | Number of Screens   | Number of Shows|");
	    System.out.println("+----+---------------------+------------+----------------+---------------------+");

	    for (Theatre theatre : theatreDB.values()) {
	        System.out.printf("| %-2d | %-19s | %-10s | %-14d | %-14d     |\n",
	                theatre.getTheatreId(),
	                theatre.getTheatreName(),
	                theatre.getTheatreLocation(),
	                theatre.getListOfScreen().size(),
	                theatre.getListOfShow().size());
	    }

	    System.out.println("+----+---------------------+------------+----------------+---------------------+");
	}

}
