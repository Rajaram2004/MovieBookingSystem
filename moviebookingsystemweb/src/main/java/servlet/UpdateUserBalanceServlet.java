package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.UserDAO;

public class UpdateUserBalanceServlet extends HttpServlet {
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String PhoneNumber = request.getParameter("phoneNumber");
		String balance= request.getParameter("Balance");
		System.out.println("---------------------------------------- UpdateUserBalanceServlet");
		if(PhoneNumber==null)System.out.println("phone number null");
		
		response.setContentType("text/plain");
		if(UserDAO.updateUserData(PhoneNumber,balance,false)) {
			response.getWriter().write("updated");
		}else {
			response.getWriter().write("No Update");
		}
		
	}
}
