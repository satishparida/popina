package com.popina.product_management;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import com.popina.properties.DataBaseProperty;

public class ProductAction {
	
	public void addProduct(ProductContainer pr){
		DataBaseProperty restnt = new DataBaseProperty();
		try (Connection con = DriverManager.getConnection(restnt.dbJDBCDriver+"://"+restnt.dbConnectionUrl, 
				restnt.dbUserId, restnt.dbPassword)) {
			Statement stmt=con.createStatement();
			stmt.executeUpdate("insert into public.Products values("
					+ "DEFAULT, "
					+ pr.productName + "," 
					+ "'', " 
					+ pr.productCost + "," 
					+ pr.productPreparationTime + ","
					+ pr.productAvalibility + ")"
					); 
		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
            e.printStackTrace();
        }
	}
	
	//public void updateProductDetail(ProductContainer pr){}
	
	public void deleteProduct(int productId){
		DataBaseProperty restnt = new DataBaseProperty();
		try (Connection con = DriverManager.getConnection(restnt.dbJDBCDriver+"://"+restnt.dbConnectionUrl, 
				restnt.dbUserId, restnt.dbPassword)) {	        
			Statement stmt=con.createStatement();
			stmt.executeUpdate("delete from public.Products where productId = "
										+ productId + ")");  
		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
            e.printStackTrace();
        }
	}
}
