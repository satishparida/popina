package com.popina.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.popina.product_management.Menu;
import com.popina.product_management.ProductAction;
import com.popina.product_management.ProductContainer;

@WebServlet("/inventorymanager")
public class InventoryManagerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int typeOfUser = 0;
		HttpSession session = request.getSession();
		if (null == session.getAttribute("typeOfUser")){
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
		else {
			typeOfUser = (int) session.getAttribute("typeOfUser");
			if (typeOfUser==2) {
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

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int typeOfUser = 0;
		HttpSession session = request.getSession();
		if (null == session.getAttribute("typeOfUser")){
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
		else {
			typeOfUser = (int) session.getAttribute("typeOfUser");
			if (typeOfUser==2) {
				int flag = 0;
				BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
				String jsonProductUpdate = "";
				if(br != null){
					jsonProductUpdate = br.readLine();
				}
				JsonObject jsonProductUpdateObject = new JsonParser().parse(jsonProductUpdate).getAsJsonObject();
				String action = jsonProductUpdateObject.get("action").getAsString();
				ProductAction product = new ProductAction();
				
				if ( action.equals("update") ) {
					flag = product.updateProduct(jsonProductUpdate);
				}
				else if ( action.equals("remove") ) {
					flag = product.removeProduct(jsonProductUpdate);
				}
				else if ( action.equals("add") ) {
					flag = product.addProduct(jsonProductUpdate);
				}
				
				response.setContentType("text/plain;charset=UTF-8");
				PrintWriter writer=response.getWriter();
				writer.append(String.valueOf(flag));
			}
			else {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
			}
		}
	}

}
