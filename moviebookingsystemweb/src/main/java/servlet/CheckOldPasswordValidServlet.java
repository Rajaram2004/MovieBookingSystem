package servlet;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.UserDAO;
import model.User;

public class CheckOldPasswordValidServlet extends HttpServlet {
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/plain");
		String password = req.getParameter("oldPassword");
		String phoneNumber = req.getParameter("phoneNumber");
		
		System.out.println("-----------------"+phoneNumber);
		User user= UserDAO.getUser(phoneNumber,true);
		if(user==null)System.out.println("user nulll-------------------------");
		System.out.println("-----------------"+user.getPassword());
		System.out.println("-----------------"+password);
		if(user.getPassword().equals(password)) {
			resp.getWriter().write("Valid Password");
		}else {
			resp.getWriter().write("Invalid Password");
		}
	}
}
