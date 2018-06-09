package com.bu.csvtodb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.typesafe.config.Config;

public class DBConnection {

	public Connection connection;
	public static DBConnection instance;

	private DBConnection() {
	}

	public static synchronized DBConnection getInstance() {
		instance = new DBConnection();
		return instance;
	}

	public Connection getConnection(Config properties) throws ClassNotFoundException, SQLException {
		if (connection == null || connection.isClosed()) {
			String driver = properties.getString("driver");
			String url = properties.getString("url").trim();
			String userName = properties.getString("username");
			String password = properties.getString("password");
			if (driver != null) {
				Class.forName(driver);
			}
			try (Connection connection = DriverManager.getConnection(url, userName, password)) {
				connection.createStatement()
						.execute("CREATE DATABASE IF NOT EXISTS " + properties.getString("database"));
			}
			connection = DriverManager.getConnection(url + properties.getString("database"),
					properties.getString("username"), properties.getString("password"));
			return connection;
		} else
			return connection;

	}

}
