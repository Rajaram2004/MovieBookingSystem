package servlet;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.UserDAO;
public class CheckPhoneNumberServlet extends HttpServlet {
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");
		System.out.println("-----------------");
		String phoneNumber = req.getParameter("phoneNumber");
		//String password = request.getParameter("password");
		System.out.println("-----------------"+phoneNumber);
		if(UserDAO.checkPhoneNumberPresent(phoneNumber)) {
			resp.getWriter().write("Invalid Number");
		}else {
			resp.getWriter().write("Valid Number");
		}
	}
}
