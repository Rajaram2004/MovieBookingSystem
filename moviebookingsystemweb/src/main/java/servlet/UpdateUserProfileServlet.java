package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.UserDAO;

public class UpdateUserProfileServlet  extends HttpServlet {
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String oldPhoneNumber = request.getParameter("oldPhoneNumber");
		String newPhoneNumber = request.getParameter("newPhoneNumber");
		String name = request.getParameter("name");
		String timeZone = request.getParameter("timezone");
		response.setContentType("text/plain");
		if(UserDAO.updateUserData(oldPhoneNumber,newPhoneNumber,name,timeZone)) {
			HttpSession session = request.getSession();
	        session.setAttribute("phoneNumber", newPhoneNumber);
			response.getWriter().write("updated");
		}else {
			response.getWriter().write("No Update");
		}
	}
}
