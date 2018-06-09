package com.bu.util;

import org.apache.commons.lang.StringEscapeUtils;

public class Util {

	public static String escapeSql(String value) {
		return StringEscapeUtils.escapeSql(value);
	}
	
}
