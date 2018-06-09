package com.bu.csvtodb;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;

import org.apache.log4j.Logger;

/**
 * Hello world!
 *
 */
public class App {
	final static Logger logger = Logger.getLogger(App.class);

	public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException, ParseException {
		if (args.length == 0) {
			printUsage();
			System.exit(1);
		}
		logger.info("Starting the DB Loader program ... Reading the configuration from " + args[0]);

		String file = args[0];// "./src/main/resources/resources.conf";
		Configuration configuration = new Configuration(file);
		CSV2DB csv2db = new CSV2DB(configuration.getConfig());
		try (Connection connection = DBConnection.getInstance()
				.getConnection(configuration.getConfig().getConfig("jdbc"))) {
			csv2db.loadDBTable(connection);
		}
		System.exit(0);

		logger.info("Starting loading the CSV file to DB");

	}

	public static void printUsage() {
		System.out.println("Missing the Configuration file");
	}
}
