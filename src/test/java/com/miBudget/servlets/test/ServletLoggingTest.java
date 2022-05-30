package com.miBudget.servlets.test;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

@WebServlet("/ServletLoggingTest")
public class ServletLoggingTest {

	private static Logger LOGGER = null;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		System.setProperty("appName", "miBudget");
		LOGGER = LogManager.getLogger(ServletLoggingTest.class);
	}

	@Test
	public StringBuilder test(StringBuilder sb) {
		LOGGER.info("If you are reading me, I am found in ServletLoggingTest.test()");
		return sb.append("If you are reading me, I am found in ServletLoggingTest.test()");
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
		LOGGER.info("Inside doGet for ServletLoggingTest");
		StringBuilder sb = new StringBuilder();
		sb = test(sb);
		response.setStatus(HttpServletResponse.SC_OK);
		try {
			response.getWriter().append(sb);
		} catch (IOException e) {
			LOGGER.error("Couldn't write the response");
		}
	}
}
