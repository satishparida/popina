package com.popina.product_management;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.popina.properties.DataBaseProperty;

public class Menu {
	public ArrayList<ProductContainer> fetchMenu(){
		ArrayList<ProductContainer> menu = new ArrayList<ProductContainer>();
		DataBaseProperty restnt = new DataBaseProperty();
		try (Connection con = DriverManager.getConnection(restnt.dbJDBCDriver+"://"+restnt.dbConnectionUrl, 
				restnt.dbUserId, restnt.dbPassword)) {
			
			Statement stmt=con.createStatement();
			String queryString = "SELECT productid, productname, productcost, "
					+ "productavalibility, productpreparationtime "
					+ "FROM public.products";
			ResultSet resultSet = stmt.executeQuery(queryString);
	        while (resultSet.next()) {
	        	ProductContainer product = new ProductContainer();
	        	product.productId=resultSet.getInt("productId");
	        	product.productName=resultSet.getString("productName");
	        	product.productCost=resultSet.getDouble("productCost");
	        	product.productAvalibility=resultSet.getInt("productAvalibility");
	        	product.productPreparationTime=resultSet.getDouble("productPreparationTime");
	        	menu.add(product);
	        }
		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
            e.printStackTrace();
            return null;
        }
		return menu;
	}
}
