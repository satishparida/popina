package com.popina.driver;

import com.popina.properties.DataBaseProperty;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Login {
	
	//Returns 1 when user login, returns 2 when admin login, return 0 on failed login
	public int employeeLogin(String userName,String Password){
		DataBaseProperty restnt = new DataBaseProperty();
		int loginType =0;
		try (Connection con = DriverManager.getConnection(restnt.dbJDBCDriver+"://"+restnt.dbConnectionUrl, 
					restnt.dbUserId, restnt.dbPassword)){
			ResultSet resultSet= null;
			Statement stmt=con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			String query = "SELECT * FROM public.Users where loginid='"+userName+"' and pwd='"+Password +"'";
			resultSet=stmt.executeQuery(query);
			
			int i=0;
			while (resultSet.next()) {
				String typeOfUser=resultSet.getString("typeOfUser");
				if (typeOfUser.equals("user")) loginType=1;
				else if (typeOfUser.equals("admin")) loginType=2;
				else loginType=0;
				i=i+1;
				if (i > 1) loginType=0; //sql injection check
			}
			con.close();
			return loginType;
		}
		catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return loginType;
        }	
		
	}

}
