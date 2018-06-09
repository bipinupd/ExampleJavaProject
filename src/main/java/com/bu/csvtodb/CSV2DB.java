package com.bu.csvtodb;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.bu.util.DateUtil;
import com.bu.util.Util;
import com.typesafe.config.Config;

public class CSV2DB {
	final static Logger logger = Logger.getLogger(CSV2DB.class);
	private final FileInputStream fis;
	private final Scanner scanner;
	private final LinkedHashMap<String, String> fields;
	private String seperator;
	private String tableName;
	private String sqlStatement;
	private int preDefinedBatch;
	private Config config;

	public CSV2DB(Config config) throws FileNotFoundException {
		this.config = config;
		fis = new FileInputStream(config.getString("csv.fileLocation"));
		scanner = new Scanner(fis);
		seperator = config.getString("csv.fieldSeperator");
		tableName = config.getString("csv.tableName");
		preDefinedBatch = config.getInt("csv.batchSize");
		fields = getFields(config.getConfig("csv"));
		logger.info(tableName + " batch Size " + preDefinedBatch);
	}

	public void loadDBTable(Connection connection) throws SQLException, ParseException, ClassNotFoundException {
		createTable(connection);
		try (PreparedStatement ps = connection.prepareStatement(sqlStatement)) {
			int batchSize = 0;
			while (scanner.hasNextLine()) {
				String[] data = scanner.nextLine().split(this.seperator);
				Object[] tableFields = fields.keySet().toArray();
				int count = 0;
				for (String valueToInsert : data) {
					addFields(ps, count + 1, fields.get(tableFields[count]), valueToInsert);
					count++;
				}
				ps.addBatch();
				if (++batchSize % preDefinedBatch == 0) {
					ps.executeBatch();
				}
			}
			ps.executeBatch();
		}
	}

	private void addFields(PreparedStatement ps, int index, String type, String value)
			throws SQLException, ParseException {
		if (type.contains("("))
			type = type.substring(0, type.indexOf("("));
		switch (type) {
		case "integer":
			ps.setInt(index, Integer.parseInt(value));
			break;
		case "varchar":
			ps.setString(index, value.toString());
			break;
		case "long":
			ps.setLong(index, Long.parseLong(value));
			break;
		case "double":
			ps.setDouble(index, Long.parseLong(value));
			break;
		case "date":
			ps.setDate(index, new java.sql.Date(DateUtil.convertToDate(value).getTime()));
			break;
		default:
			throw new IllegalArgumentException("Invalid field");
		}
	}

	private LinkedHashMap<String, String> getFields(Config config) {
		List<? extends Config> configFields = config.getConfigList("fields");
		StringBuffer insertSql = new StringBuffer("insert into ");
		insertSql.append(tableName).append(" (");
		StringBuffer outputfeilds = new StringBuffer(" values (");
		LinkedHashMap<String, String> sFields = new LinkedHashMap<String, String>();
		configFields.forEach(configField -> {
			sFields.put(configField.getString("name"), configField.getString("dbtype"));
			insertSql.append(configField.getString("name") + ",");
			outputfeilds.append("?,");
		});
		insertSql.delete(insertSql.length() - 1, insertSql.length()).append(") ");
		outputfeilds.delete(outputfeilds.length() - 1, outputfeilds.length()).append(")");
		sqlStatement = insertSql.append(outputfeilds).toString();

		return sFields;
	}

	private void createTable(Connection connection) throws SQLException, ClassNotFoundException {
		connection.createStatement().execute(getTableCreateSQLStatement());
	}

	private String getTableCreateSQLStatement() {
		StringBuffer sbuilder = new StringBuffer("create table ");
		Config tableConfig = config.getConfig("csv");
		sbuilder.append(Util.escapeSql(tableConfig.getString("tableName").trim()) + " ( ");
		List<? extends Config> fields = tableConfig.getConfigList("fields");
		fields.forEach(configField -> {
			sbuilder.append(Util.escapeSql(configField.getString("name")) + " "
					+ Util.escapeSql(configField.getString("dbtype"))).append(",");
		});
		sbuilder.delete(sbuilder.length() - 1, sbuilder.length());
		sbuilder.append(")");
		logger.info(sbuilder.toString());
		return sbuilder.toString();
	}

	public String getTableName() {
		return tableName;
	}

}
