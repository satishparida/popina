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

@WebServlet("/placeOrder")
public class PlaceOrderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int typeOfUser = 0;
		HttpSession session = request.getSession();
		if (null == session.getAttribute("typeOfUser")){
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
		else {
			typeOfUser = (int) session.getAttribute("typeOfUser");
			if (typeOfUser==1 || typeOfUser==2) {
				BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
				String jsonOrder = "";
				if(br != null){
					jsonOrder = br.readLine();
					//System.out.println(jsonOrder);
				}
		
				OrderAction orderaction = new OrderAction();
				int orderId = orderaction.placeNewOrder(jsonOrder);
				//System.out.println(orderId);
				
				response.setContentType("text/plain;charset=UTF-8");
				PrintWriter writer=response.getWriter();
				writer.append(String.valueOf(orderId));
			}
			else {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
			}
		}
	}
       
//	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		Enumeration<String> en=request.getParameterNames();
//		ArrayList<ItemContainer> order = new ArrayList<ItemContainer>();
//
//		while(en.hasMoreElements())
//		{
//			Object objOri=en.nextElement();
//			String param=(String)objOri;
//			String value=request.getParameter(param);
//			param=param.substring(4);
//			
//			if (value != null && !value.isEmpty()) {
//				ItemContainer item = new ItemContainer(Integer.parseInt(param),Integer.parseInt(value));
//				order.add(item);
//			}
//		}
//		String jsonOrder = new Gson().toJson(order);
//		
//		OrderAction orderaction = new OrderAction();
//		int orderId = orderaction.placeNewOrder(jsonOrder);
//		System.out.println(orderId);
//	}
}
