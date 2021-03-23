package com.miBudget.v1.utilities;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DateAndTimeUtility {

	private static Logger LOGGER = null;
	static {
		System.setProperty("appName", "miBudget");
		LOGGER = LogManager.getLogger(DateAndTimeUtility.class);
	}

	public static String getDateAndTimeAsStr() {
		LocalTime time = LocalTime.now();
		LocalDate date = LocalDate.now();
		String day = date.getDayOfWeek().toString();
		String month = date.getMonth().toString();
		int numberDate = date.getDayOfMonth();
		int year = date.getYear();
		int hour = time.getHour() > 12 ? time.getHour() - 12 : time.getHour();
		int minute = time.getMinute();
		int second = time.getSecond();
		int ampm = hour < 12 ? 0 : 1;
		String dateStr = numberDate <= 9 ? "0" + numberDate : Integer.toString(numberDate);
		String hourStr = hour <= 9 ? "0" + hour : Integer.toString(hour);
		String minuteStr = minute <= 9 ? "0" + minute : Integer.toString(minute);
		String secondStr = second <= 9 ? "00" + second : Integer.toString(second);
		String timeStr = ampm == 0 ? "AM" : "PM";
		String dateAndTimeStr =
				day + " " + month + " " + dateStr + ", " + year + " " +
				hourStr + ":" + minuteStr + ":" + secondStr + " " + timeStr;
		LOGGER.info("dateAndTime: " + dateAndTimeStr);
		return dateAndTimeStr;
	}
	
	public static void main(String[] args)
	{
		System.out.println(getDateAndTimeAsStr());
	}

}
