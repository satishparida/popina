package com.popina.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.popina.order_management.OrderAction;

@WebServlet("/kitchenmanager")
public class KitchenManagerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int typeOfUser = 0;
		HttpSession session = request.getSession();
		if (null == session.getAttribute("typeOfUser")){
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
		else {
			typeOfUser = (int) session.getAttribute("typeOfUser");
			if (typeOfUser==3) {
				OrderAction orders = new OrderAction();
				String pendingOrders = orders.fetchPendingOrders();
				response.setContentType("application/json");
			    response.setCharacterEncoding("UTF-8");
			    response.getWriter().write(pendingOrders);
			}
			else {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int typeOfUser = 0;
		HttpSession session = request.getSession();
		if (null == session.getAttribute("typeOfUser")){
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
		else {
			typeOfUser = (int) session.getAttribute("typeOfUser");
			if (typeOfUser==3) {
				BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
				String updateOrderJSON = "";
				
				if(br != null){
					updateOrderJSON = br.readLine();
				}

				OrderAction orderaction = new OrderAction();
				int updateFlag = orderaction.updateOrderStatus(updateOrderJSON);
				if (updateFlag == 1) {
					response.setContentType("text/plain;charset=UTF-8");
					PrintWriter writer=response.getWriter();
					writer.append(String.valueOf(updateFlag));
				}
			}
			else {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
			}
		}
		
	}

}
