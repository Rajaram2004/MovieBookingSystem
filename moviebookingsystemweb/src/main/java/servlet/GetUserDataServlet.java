package servlet;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.UserDAO;
import model.User;

public class GetUserDataServlet extends HttpServlet {
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");
		String phoneNumber = req.getParameter("phoneNumber");
		//String password = request.getParameter("password");

		User user = UserDAO.getUser(phoneNumber,true);
		String memberSince = "September 2025";

		String json = "{" + "\"profileName\":\"" + user.getName() + "\"," + "\"role\":\"" + user.getUserType() + "\","
				+ "\"username\":\"" + user.getName() + "\"," + "\"emailId\":\"" + user.getEmailId() + "\"," + "\"phoneNumber\":\""
				+ phoneNumber + "\"," + "\"gender\":\"" + user.getGender() + "\"," + "\"timezone\":\"" + user.getTimeZone() + "\","
				+ "\"balance\":\"" + user.getBalance() + "\"," + "\"memberSince\":\"" + memberSince + "\"" + "}";

		resp.getWriter().write(json);
	}
}
