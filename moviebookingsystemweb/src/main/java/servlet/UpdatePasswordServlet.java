package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.UserDAO;

public class UpdatePasswordServlet extends HttpServlet {
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String PhoneNumber = request.getParameter("phoneNumber");
		String newPassword = request.getParameter("newPassword");
		System.out.println("--------------------------------------------------------");
		if(PhoneNumber==null)System.out.println("phone number null");
		if(newPassword==null)System.out.println("newPassword null");
		response.setContentType("text/plain");
		if(UserDAO.updateUserData(PhoneNumber,newPassword,true)) {
			response.getWriter().write("updated");
		}else {
			response.getWriter().write("No Update");
		}
		
	}

}
