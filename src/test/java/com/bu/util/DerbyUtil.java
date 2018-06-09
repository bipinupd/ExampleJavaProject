
package com.bu.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DerbyUtil {
	private static final String DB_URL = "jdbc:derby:memory:target/testDB;";
	
	private static String dbDriverName = "org.apache.derby.jdbc.EmbeddedDriver";

	public static void createDb() throws SQLException, ClassNotFoundException  {
		String dbUrl =  DB_URL + "create=true";
        Class.forName(dbDriverName);
		Connection connection =  DriverManager.getConnection(dbUrl);
		connection.close();
	}
	
	public static Connection getConnection() throws SQLException, ClassNotFoundException  {
		String dbUrl =  DB_URL;
        Class.forName(dbDriverName);
		return  DriverManager.getConnection(dbUrl);
		}
	

	
	public static int countRecords(Connection connection, String tableName) throws SQLException {
		int count=0;
		try (ResultSet rs = connection.createStatement().executeQuery("select COUNT(*) from " + tableName)){
		    while(rs.next())
		        count=rs.getInt(1);
		} 
		return count;
		
	}
	
}
