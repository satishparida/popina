package com.popina.order_management;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.popina.properties.DataBaseProperty;

public class OrderAction {
	
	/*Returns the order ID to the requester*/
	public int placeNewOrder(String jsonOrder){
		DataBaseProperty restnt = new DataBaseProperty();
		String runFunction = "{ ? = call placeOrder( ?::json ) }";
		int orderId=0;
		try (Connection con = DriverManager.getConnection(restnt.dbJDBCDriver+"://"+restnt.dbConnectionUrl, 
				restnt.dbUserId, restnt.dbPassword);
			 CallableStatement callableStatement = con.prepareCall(runFunction)) {
			
			callableStatement.registerOutParameter(1, Types.INTEGER);
			callableStatement.setString(2, jsonOrder);
			callableStatement.executeUpdate();
			orderId = callableStatement.getInt(1);
			return orderId;
		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
            e.printStackTrace();
            return orderId;
        }
	}
	
	/*Returns JSON as in String*/
	public String fetchPendingOrders(){
		DataBaseProperty restnt = new DataBaseProperty();
		String runFunction = "{ ? = call fetchPendingOrders() }";
		String pendingOrders = "";
		try (Connection con = DriverManager.getConnection(restnt.dbJDBCDriver+"://"+restnt.dbConnectionUrl, 
				restnt.dbUserId, restnt.dbPassword);
			 CallableStatement callableStatement = con.prepareCall(runFunction)) {
			callableStatement.registerOutParameter(1, Types.LONGVARCHAR);
			callableStatement.executeUpdate();
			pendingOrders = callableStatement.getString(1);
			return pendingOrders;
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
            e.printStackTrace();
            return pendingOrders;
		}
	}
	
	public int updateOrderStatus(String jsonOrderUpdate){
		DataBaseProperty restnt = new DataBaseProperty();
		int updateFlag=0;
		try (Connection con = DriverManager.getConnection(restnt.dbJDBCDriver+"://"+restnt.dbConnectionUrl, 
					restnt.dbUserId, restnt.dbPassword)){
			
			JsonObject jsonOrderUpdateObject = new JsonParser().parse(jsonOrderUpdate).getAsJsonObject();
			
			
			PreparedStatement stmt=con.prepareStatement("update orders set orderstatus=? where orderid=?");
			stmt.setString(1,jsonOrderUpdateObject.get("status").getAsString());
			stmt.setInt(2,jsonOrderUpdateObject.get("id").getAsInt());
		
			updateFlag=stmt.executeUpdate();  
			return updateFlag;
		}
		catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return 0;
        }	
	}
}
