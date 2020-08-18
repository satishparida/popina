package com.popina.product_management;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.popina.properties.DataBaseProperty;

public class ProductAction {
	
	public int addProduct(String jsonProduct){
		DataBaseProperty restnt = new DataBaseProperty();
		int insertFlag = 0;
		try (Connection con = DriverManager.getConnection(restnt.dbJDBCDriver+"://"+restnt.dbConnectionUrl, 
				restnt.dbUserId, restnt.dbPassword)) {
			JsonObject jsonOrderUpdateObject = new JsonParser().parse(jsonProduct).getAsJsonObject();
			PreparedStatement stmt=con.prepareStatement("INSERT INTO public.products"
					+ "(productname, productcost, productavalibility, productpreparationtime) VALUES ( ?, ?, ?, ? )");
			
			stmt.setString(1,jsonOrderUpdateObject.get("productname").getAsString());
			stmt.setFloat(2,jsonOrderUpdateObject.get("productcost").getAsFloat());
			stmt.setInt(3,jsonOrderUpdateObject.get("productavalibility").getAsInt());
			stmt.setFloat(4,jsonOrderUpdateObject.get("productpreparationtime").getAsFloat());
		
			insertFlag=stmt.executeUpdate();  
			return insertFlag;
		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
            e.printStackTrace();
            return insertFlag;
        }
	}
	
	public int removeProduct(String jsonProduct){
		DataBaseProperty restnt = new DataBaseProperty();
		int updateFlag = 0;
		try (Connection con = DriverManager.getConnection(restnt.dbJDBCDriver+"://"+restnt.dbConnectionUrl, 
				restnt.dbUserId, restnt.dbPassword)) {	
			
			JsonObject jsonOrderUpdateObject = new JsonParser().parse(jsonProduct).getAsJsonObject();
			PreparedStatement stmt=con.prepareStatement("DELETE FROM public.products WHERE productid=?");
			stmt.setInt(1,jsonOrderUpdateObject.get("id").getAsInt());
		
			updateFlag=stmt.executeUpdate();  
			return updateFlag;
		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
            e.printStackTrace();
            return updateFlag;
        }
	}
	
	public int updateProduct(String jsonProduct){
		DataBaseProperty restnt = new DataBaseProperty();
		int updateFlag = 0;
		try (Connection con = DriverManager.getConnection(restnt.dbJDBCDriver+"://"+restnt.dbConnectionUrl, 
				restnt.dbUserId, restnt.dbPassword)) {	  
			
			JsonObject jsonOrderUpdateObject = new JsonParser().parse(jsonProduct).getAsJsonObject();
			PreparedStatement stmt=con.prepareStatement("UPDATE public.products SET productname=?, productcost=?, "
										+ "	productavalibility=?, productpreparationtime=? WHERE productid=?");
			
			stmt.setString(1,jsonOrderUpdateObject.get("productname").getAsString());
			stmt.setFloat(2,jsonOrderUpdateObject.get("productcost").getAsFloat());
			stmt.setInt(3,jsonOrderUpdateObject.get("productavalibility").getAsInt());
			stmt.setFloat(4,jsonOrderUpdateObject.get("productpreparationtime").getAsFloat());
			stmt.setInt(5,jsonOrderUpdateObject.get("id").getAsInt());
		
			updateFlag=stmt.executeUpdate();  
			return updateFlag;
		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
            e.printStackTrace();
            return updateFlag;
        }
	}
}
