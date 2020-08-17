package com.popina.servlet;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.popina.product_management.Menu;
import com.popina.product_management.ProductContainer;

@WebServlet("/menu")
public class MenuServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int typeOfUser = 0;
		HttpSession session = request.getSession();
		if (null == session.getAttribute("typeOfUser")){
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
		else {
			typeOfUser = (int) session.getAttribute("typeOfUser");
			if (typeOfUser==1 || typeOfUser==2) {
				Menu menuExtract = new Menu();
				ArrayList<ProductContainer> menu = menuExtract.fetchMenu();
				String json = new Gson().toJson(menu);
				response.setContentType("application/json");
			    response.setCharacterEncoding("UTF-8");
			    response.getWriter().write(json);
			}
			else {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
			}
		}
	}
}
