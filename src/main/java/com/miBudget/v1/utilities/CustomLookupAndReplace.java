package com.miBudget.v1.utilities;

import java.util.Calendar;
import java.util.Date;
import java.io.File;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.lookup.StrLookup;

@Plugin(name="customReplace", category="Lookup")
public class CustomLookupAndReplace implements StrLookup {

	private Date date;
	private Calendar cal = Calendar.getInstance();
	private int month = 0, dateOfMonth = 0, year = 0;
	private static Logger LOGGER = null;
	
	static {
		System.setProperty("appName", "CustomLookupAndReplace");
		LOGGER = LogManager.getLogger(CustomLookupAndReplace.class);
	}

	@Override
	public String lookup(LogEvent event, String key) {
		// TODO Auto-generated method stub
		return lookup(key);
	}
	
	@Override
	public String lookup(String key) {
		String retStr = "";
		String path = "logs" + File.separator;
		path += getDate();
		if (key == null) return "";
		if (!key.contains(";")) return key;
		String[] params = key.split(";");
		String value1 = params[0];
		
		int numOfLogs = 0;
		
		if (StringUtils.isNumeric(value1)) numOfLogs = Integer.valueOf(value1);
		else {
			try {
				numOfLogs = new File(path).list().length;
 			} catch (NullPointerException e) {
 				LOGGER.error("There was an exception: couldn't find the file using the path: " + path);
 				printException(e);
 			}
		}
		if (numOfLogs == 0) {
			retStr = "001";
		} else if (numOfLogs > 0 && numOfLogs <= 8) {
			retStr = "00" + Integer.toString(numOfLogs+1);
		} else if (numOfLogs >=9 && numOfLogs <= 98) {
			retStr = "0" + Integer.toString(numOfLogs+1);
		}
		return retStr;
	}
	
	public String getDate() {
		date = new Date();
		cal.setTime(date);;
		String monthStr = "", dateOfMonthStr = "";
		try {
			month = cal.get(Calendar.MONTH) + 1;
			dateOfMonth = cal.get(Calendar.DAY_OF_MONTH);
			year = cal.get(Calendar.YEAR);
			if (month <= 9) monthStr = "0" + month;
			if (dateOfMonth <= 9) dateOfMonthStr = "0" + month;
		} catch (Exception e) {
			printException(e);
		}
		if (month <= 9) {
			return monthStr + "-"+Integer.toString(dateOfMonth)+"-"+Integer.toString(year);
		} else if (dateOfMonth <= 9) {
			return Integer.toString(month)+"-"+ dateOfMonthStr+"-"+Integer.toString(year);
		} else if (month <= 9 && dateOfMonth <= 9 ) {
			return monthStr + "-" + dateOfMonthStr + "-" + Integer.toString(year);
		} else {
			return Integer.toString(month) + "-" + Integer.toString(dateOfMonth) + "-" + Integer.toString(year);
		}
	}
	
	public void printException(Exception e) {
		StackTraceElement[] steArr = e.getStackTrace();
		for (StackTraceElement ste : steArr) {
			LOGGER.error(ste);
		}
	}
}
