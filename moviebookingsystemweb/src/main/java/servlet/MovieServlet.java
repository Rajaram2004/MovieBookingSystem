package servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.MovieDAO;
import model.Movies;

public class MovieServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Movies> movieList = MovieDAO.getActiveMovies();
        boolean flag=false;
        for(Movies movie : movieList) {
        	System.out.println(movie);
        	flag=true;
        }
        if(flag==false) {
        	System.out.println("---------------------------------------no movies");
        }
        request.setAttribute("movies", movieList);
        request.getRequestDispatcher("movies.jsp").forward(request, response);
    }
}
