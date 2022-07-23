package com.miBudget.utilities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DateAndTimeUtility {
	private static Logger LOGGER = LogManager.getLogger(DateAndTimeUtility.class);

	public static String getDateAndTimeAsStr() {
		//LocalDateTime localDateTime = LocalDateTime.now(); // localDateTime: 2022-07-22T12:40:42.024946
		LocalTime time = LocalTime.now();
		LocalDate date = LocalDate.now();
		String day = date.getDayOfWeek().toString();
		String month = date.getMonth().toString();
		int numberDate = date.getDayOfMonth();
		int year = date.getYear();
		int hour = time.getHour() > 12 ? time.getHour() - 12 : time.getHour();
		int minute = time.getMinute();
		//int second = time.getSecond();
		int ampm = time.getHour() < 12 ? 0 : 1;
		String dateStr = numberDate <= 9 ? "0" + numberDate : Integer.toString(numberDate);
		String hourStr = hour <= 9 ? "0" + hour : Integer.toString(hour);
		String minuteStr = minute <= 9 ? "0" + minute : Integer.toString(minute);
		//String secondStr = second <= 9 ? "00" + second : Integer.toString(second);
		String timeStr = ampm == 0 ? "AM" : "PM";
		String localDateTime =
				day + " " + month + " " + dateStr + ", " + year + " " +
				hourStr + ":" + minuteStr + " " + timeStr;
		LOGGER.info("localDateTime: " + localDateTime); // localDateTime: FRIDAY JULY 22, 2022 12:50:000 PM
		return localDateTime.toString();
	}
}
