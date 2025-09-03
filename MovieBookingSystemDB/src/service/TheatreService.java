package service;

import java.sql.Connection;
import java.util.List;

import dao.TheatreDAO;
import model.Theatre;

public class TheatreService {
	public TheatreService(){
		
	}
	public void printAllTheatres(Connection conn) {
		List<Theatre> thetareList = TheatreDAO.getTheatreList(conn);
	    System.out.println("+----+---------------------+------------+");
	    System.out.println("| ID | Theatre Name        | Location   |");
	    System.out.println("+----+---------------------+------------+");

	    for (Theatre theatre : thetareList) {
	    	
	    		System.out.printf("| %-2d | %-19s | %-10s |\n",
		                theatre.getTheatreId(),
		                theatre.getTheatreName(),
		                theatre.getTheatreLocation());
	    }
	    System.out.println("+----+---------------------+------------+");
	}

}
