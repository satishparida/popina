package com.popina.driver;

import java.sql.SQLException;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.popina.product_management.Menu;
import com.popina.product_management.ProductContainer;


public class Test_All {
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
//		Login l = new Login();
//		int t=l.employeeLogin("satish","satish");
//		System.out.println(t);
//		int t1=l.employeeLogin("admin","admin");
//		System.out.println(t1);
//		int t2=l.employeeLogin("test","test");
//		System.out.println(t2);;
		
		Menu menuExtract = new Menu();
		ArrayList<ProductContainer> menu = menuExtract.fetchMenu();
		
		String json = new Gson().toJson(menu);
		
		System.out.println(json);
		
	}
}
