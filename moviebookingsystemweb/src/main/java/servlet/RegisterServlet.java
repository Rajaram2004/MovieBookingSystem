package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.UserDAO;

public class RegisterServlet extends HttpServlet {
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String userName = request.getParameter("username");
		String emailId = request.getParameter("email");
		String phoneNumber = request.getParameter("phone");
		String gender = request.getParameter("gender");
		String role = request.getParameter("role");
		String timeZone = request.getParameter("timezone");
		String password = request.getParameter("password");
		response.setContentType("text/plain");

		boolean phone = UserDAO.checkPhoneNumberPresent(phoneNumber);
		boolean email = UserDAO.checkEmailIdPresent(emailId);

		if (phone && email) {
			response.getWriter().write("phone number");
			response.getWriter().write("email");
		} else if (phone) {
			response.getWriter().write("phone number");
		} else if (email) {
			response.getWriter().write("email");
		} else {
			if (UserDAO.createUser(userName, emailId, phoneNumber, gender, role, timeZone, password)) {
				response.getWriter().write("Registered Successfully");
			}
		}
	}
}
