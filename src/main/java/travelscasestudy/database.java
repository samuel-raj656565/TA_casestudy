package travelscasestudy;

import java.sql.*;
public class database {
	
	public static Connection establishConnection() {
		Connection connection=null;
		try
		{
		Class.forName("org.postgresql.Driver");
		 connection=DriverManager.getConnection("jdbc:postgresql://localhost:5432/TravelsDB","postgres","sammy");
		
		
		
		}catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		
		return connection;
	}

}

