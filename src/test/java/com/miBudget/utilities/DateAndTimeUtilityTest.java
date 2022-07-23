package com.miBudget.utilities;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(JUnit4.class)
public class DateAndTimeUtilityTest {
    private static Logger LOGGER = LoggerFactory.getLogger(DateAndTimeUtilityTest.class);

    @Test
    public void testDateAndTimeReturnsDateAndTimeInANiceFormat() {
        String dateAndTimeAsStr = DateAndTimeUtility.getDateAndTimeAsStr();
        LOGGER.info(dateAndTimeAsStr);
    }

}