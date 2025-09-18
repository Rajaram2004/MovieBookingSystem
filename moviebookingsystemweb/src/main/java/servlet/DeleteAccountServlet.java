package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.UserDAO;

public class DeleteAccountServlet extends HttpServlet {
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String PhoneNumber = request.getParameter("phoneNumber");
		
		
		response.setContentType("text/plain");
		if(UserDAO.deleteAccount(PhoneNumber)) {
			response.getWriter().write("deleted");
		}else {
			response.getWriter().write("Not deleted");
		}
		
	}
}
