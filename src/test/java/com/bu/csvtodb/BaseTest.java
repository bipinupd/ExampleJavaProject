package com.bu.csvtodb;

import static org.junit.Assert.assertNotNull;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;

import org.junit.BeforeClass;
import org.junit.Test;

import com.bu.util.DerbyUtil;


public class BaseTest {

	static String fileName;
	static Configuration configuration;

	@BeforeClass
	public static void setupTest() throws ClassNotFoundException, SQLException {
		fileName = "./src/test/resouces/test-resources.conf";
		configuration = new Configuration(fileName);
		DerbyUtil.createDb();
	}

	@Test
	public void getConnection_verifyConnection() throws ClassNotFoundException, SQLException {
		Connection connection = DerbyUtil.getConnection();
		assertNotNull(connection);
		connection.close();
	}

	@Test
	public void canLoadFile() throws FileNotFoundException, ClassNotFoundException, SQLException, ParseException {
		Connection connection = DerbyUtil.getConnection();
		CSV2DB csv2db = new CSV2DB(configuration.getConfig());
		csv2db.loadDBTable(connection);
		assert (3 == DerbyUtil.countRecords(DerbyUtil.getConnection(), "tableName"));
	}

}
