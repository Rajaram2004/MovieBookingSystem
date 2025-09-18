package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.UserDAO;
import model.User;

public class LoginServlet extends HttpServlet {
	

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String redirect = request.getParameter("redirect");

		boolean isPhoneNumber = username.matches("\\d{10}");
		User user = UserDAO.getUser(username, isPhoneNumber);

		if (user != null) {
			boolean valid = isPhoneNumber ? password.equals(user.getPassword())
					: username.equals(user.getEmailId()) && password.equals(user.getPassword());
			if (valid) {
				HttpSession session = request.getSession(true);
				session.setAttribute("phoneNumber", String.valueOf(user.getPhoneNumber()));
				if (redirect != null && !redirect.isEmpty() && redirect.startsWith("/")) {
					response.sendRedirect(request.getContextPath() + redirect);
					return;
				} else {
					response.setContentType("text/html");
					response.getWriter().write("Login Successful");
					return;
				}
			}
		}

		response.setContentType("text/html");
		response.getWriter().write("Invalid username or password");
	}

}
