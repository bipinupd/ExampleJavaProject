package com.bu.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DateUtil {

	private static List<SimpleDateFormat> dateFormats = new ArrayList<SimpleDateFormat>() {
		{
			add(new SimpleDateFormat("M/dd/yyyy"));
			add(new SimpleDateFormat("dd.M.yyyy"));
			add(new SimpleDateFormat("M/dd/yyyy hh:mm:ss a"));
			add(new SimpleDateFormat("dd.M.yyyy hh:mm:ss a"));
			add(new SimpleDateFormat("dd.MMM.yyyy"));
			add(new SimpleDateFormat("dd-MMM-yyyy"));
		}
	};

	/**
	 * Convert String with various formats into java.util.Date
	 * 
	 * @param input
	 *            Date as a string
	 * @return java.util.Date object if input string is parsed successfully else
	 *         returns null
	 * @throws ParseException
	 */
	public static Date convertToDate(String input) throws ParseException {
		Date date = null;
		if (null == input) {
			return null;
		}
		for (SimpleDateFormat format : dateFormats) {
			format.setLenient(false);
			date = format.parse(input);
			if (date != null) {
				break;
			}
		}

		return date;
	}
}