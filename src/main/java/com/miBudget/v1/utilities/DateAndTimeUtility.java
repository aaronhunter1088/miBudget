package com.miBudget.v1.utilities;

import java.util.Calendar;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DateAndTimeUtility {

	private static Logger LOGGER = null;
	static {
		System.setProperty("appName", "miBudget");
		LOGGER = LogManager.getLogger(DateAndTimeUtility.class);
	}
	
	public static String getDateAndTimeAsStr(Calendar cal) {
		String dateAndTimeStr = "";
		String day = DateAndTimeUtility.convertDayToStr(cal.get(Calendar.DAY_OF_WEEK));
		String month = DateAndTimeUtility.convertMonthToStr(cal.get(Calendar.MONTH) + 1);
		int date = cal.get(Calendar.DAY_OF_MONTH);
		int year = cal.get(Calendar.YEAR);
		int hour = cal.get(Calendar.HOUR);
		int minute = cal.get(Calendar.MINUTE);
		int second = cal.get(Calendar.SECOND);
		int time = cal.get(Calendar.AM_PM);
		String dateStr = date <= 9 ? "0" + Integer.toString(date) : Integer.toString(date);
		String hourStr = hour <= 9 ? "0" + Integer.toString(hour) : Integer.toString(hour);
		String minuteStr = minute <= 9 ? "0" + Integer.toString(minute) : Integer.toString(minute); 
		String secondStr = second <= 9 ? "00" + Integer.toString(second) : Integer.toString(second);
		String timeStr = time == 0 ? "AM" : "PM";
		dateAndTimeStr = 
				day + " " + month + " " + dateStr + ", " + Integer.toString(year) + " " + 
				hourStr + ":" + minuteStr + ":" + secondStr + " " + timeStr;
		LOGGER.info("dateAndTime: " + dateAndTimeStr);
		return dateAndTimeStr;
	}
	
	/** This method will take the month an dconver it to its String equivalent.
	 * @param thisDay
	 * @return */
	public static String convertDayToStr(int thisDay) {
		LOGGER.info("Inside convertDayToString()");
		String day = "";
		switch(thisDay) {
			case 1: day = "Sunday"; break; 
			case 2: day = "Monday"; break;
			case 3: day = "Tuesday"; break;
			case 4: day = "Wednesday"; break;
			case 5: day = "Thursday"; break;
			case 6: day = "Friday"; break;
			case 7: day = "Saturday"; break;
			default: day = "Unknown day"; break;
		}
		return day;
	}
	
	/** This method will take the month and convert it to its integer equivalent.
	 *  @param thisMonth
	 *  @return
	 *  @throws Exception */
	public static int convertMonthToInt(String thisMonth) throws Exception {
		LOGGER.info("Inside convertMonthToInt()");
		int month = 0;
		switch (thisMonth) {
			case "January" : month = 1; break;
			case "February" : month = 2; break;
			case "March" : month = 3; break;
			case "April" : month = 4; break;
			case "May" : month = 5; break;
			case "June" :month = 6; break;
			case "July" : month = 7; break;
			case "August" : month = 8; break;
			case "September" : month = 9; break;
			case "October" : month = 10; break;
			case "November" : month = 11; break;
			case "December" : month = 12; break;
			default : throw new Exception("This month '" + thisMonth + "' is invalid.");
		}
		LOGGER.info(thisMonth + " is equal to " + month);
		LOGGER.info("End convertMonthToInt()");
		return month;
	}
	/** This method will take the month and convert it to its String equivalent.
	 *  @param thisMonth
	 *  @return
	 *  @throws Exception */
	public static String convertMonthToStr(int thisMonth) {
		LOGGER.info("Inside convertMonthToStr()");
		String month = "";
		switch (thisMonth) {
			case 1 : month = "January"; break;
			case 2 : month = "February"; break;
			case 3 : month = "March"; break;
			case 4 : month = "April"; break;
			case 5 : month = "May"; break;
			case 6 :month = "June"; break;
			case 7 : month = "July"; break;
			case 8 : month = "August"; break;
			case 9 : month = "September"; break;
			case 10 : month = "October"; break;
			case 11 : month = "November"; break;
			case 12 : month = "December"; break;
			default : LOGGER.error("The month '" + thisMonth + "' is invalid.");
		}
		LOGGER.info(thisMonth + " is equivalent to " + month);
		LOGGER.info("End convertMonthToStr()");
		return month;
	}
}
