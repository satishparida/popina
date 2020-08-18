package com.popina.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.popina.driver.Login;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String userName=request.getParameter("username");
		String passWord=request.getParameter("password");
		
		Login login = new Login();
		
		int typeOfUser=login.employeeLogin(userName,passWord);
		
		if (typeOfUser==0) {
			response.sendRedirect("failed-login.html");
		}
		else if (typeOfUser==1) {
			//user login
			HttpSession session = request.getSession();
			session.setAttribute("typeOfUser", typeOfUser);
			response.sendRedirect("menu.html");
		}
		else if (typeOfUser==2) {
			//admin login
			HttpSession session = request.getSession();
			session.setAttribute("typeOfUser", typeOfUser);
			response.sendRedirect("inventory-manager.html");
		}
		else if (typeOfUser==3) {
			//kitchen manager login
			HttpSession session = request.getSession();
			session.setAttribute("typeOfUser", typeOfUser);
			response.sendRedirect("kitchen-manager.html");
		}
		else {
			response.sendRedirect("failed-login.html");
		}
	}

}
