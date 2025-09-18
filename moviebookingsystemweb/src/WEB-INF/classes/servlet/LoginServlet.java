package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.UserDAO;
import model.Users;


public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String username = request.getParameter("username");
		String password = request.getParameter("password");
		System.out.println("username ======= " + username);
		response.setContentType("text/plain");

		
		Users emp = UserDAO.getUser(username, password);
		System.out.println("------------------------------------------------"+emp);
		
		if (emp != null && emp.getUserId() == Integer.parseInt(username) && password.equalsIgnoreCase(emp.getPassword())) {
		response.getWriter().write("Login Successful");
		} else {
			System.out.println("user id & password mismatch---------------");
			response.getWriter().write("Invalid Credentials");
		}
	}
}
