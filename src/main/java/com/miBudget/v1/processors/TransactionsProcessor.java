package com.miBudget.v1.processors;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.miBudget.v1.servlets.CAT;
import com.plaid.client.*;
import com.plaid.client.request.TransactionsGetRequest;
import com.plaid.client.response.TransactionsGetResponse;

import retrofit2.Response;
public class TransactionsProcessor {
	
	private static final String clientId = "5ae66fb478f5440010e414ae";
	private static final String secret = "0e580ef72b47a2e4a7723e8abc7df5"; 
	private static final String secretD = "c7d7ddb79d5b92aec57170440f7304";
	
	private java.sql.Date endDate, startDate;
	
	private void setEndDate(java.sql.Date endDate) { this.endDate = endDate; }
	private void setStartDate(java.sql.Date startDate) { this.startDate = startDate; }
	
	public java.sql.Date getEndDate(String endDate) throws ParseException {
		//return endDate;
		LOGGER.info("method: getEndDate");
		DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		Calendar calendar = Calendar.getInstance();
		Date end = sdf.parse(endDate);
		calendar.setTime(end);
		String monthStr = null, dateStr = null;
		int month = calendar.get(Calendar.MONTH) + 1;
		int date = calendar.get(Calendar.DAY_OF_MONTH);
		int year = calendar.get(Calendar.YEAR);
		if (month <= 9) monthStr = "0" + month;
		else monthStr = String.valueOf(month);
		if (date <= 9) dateStr = "0" + date;
		else dateStr = String.valueOf(date);
		String endDateStr = year+"-"+monthStr+"-"+dateStr;
		LOGGER.info("endDateStr: " + endDateStr);
		return java.sql.Date.valueOf(endDateStr);
	}
	public java.sql.Date getStartDate(String fromDate) throws ParseException {
		//return startDate;
		LOGGER.info("method: getStartDate");
		DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		Calendar calendar = Calendar.getInstance();
		Date start = sdf.parse(fromDate);
		calendar.setTime(start);
		//calendar.add(Calendar.MONTH, -1); // go back one month
		String monthStr = null, dateStr = null;
		int month = calendar.get(Calendar.MONTH) + 1;
		int date = calendar.get(Calendar.DAY_OF_MONTH);
		int year = calendar.get(Calendar.YEAR);
		if (month <= 9) monthStr = "0" + month;
		else monthStr = String.valueOf(month);
		if (date <= 9) dateStr = "0" + date;
		else dateStr = String.valueOf(date);
		String startDateStr = year+"-"+monthStr+"-"+dateStr;
		LOGGER.info("startDateStr: " + startDateStr);
		return java.sql.Date.valueOf(startDateStr);
	}

	public TransactionsProcessor() {};
	
	private static Logger LOGGER = null;
	static  {
		System.setProperty("appName", "miBudget");
		LOGGER = LogManager.getLogger(TransactionsProcessor.class);
	}
	
	public Response<TransactionsGetResponse> getTransactions(String accessToken, String accountId, int transactionsCount, java.sql.Date startDate, java.sql.Date endDate) throws ParseException, IOException {
		
		Response<TransactionsGetResponse> response = tryWithSqlDate(accessToken, accountId, transactionsCount, startDate, endDate);
		//Response<TransactionsGetResponse> response = tryWithDate(accessToken, accountId, transactionsCount);
		// i prefer sql date because the date is much easier to read and it still works
		
		return response;
	}
	
	public Response<TransactionsGetResponse> tryWithDate(String accessToken, String accountId, int transactionsCount) throws ParseException, IOException {
		LOGGER.info("method: tryWithDate");
		Date endDate = createEndDate();
		Date startDate = createStartDate();
		LOGGER.info("startDate: " + startDate);
		LOGGER.info("endDate: " + endDate);
		
		TransactionsGetRequest getReq = new TransactionsGetRequest(accessToken, startDate, endDate)
				.withAccountIds(Arrays.asList(accountId)).withCount(transactionsCount);
		LOGGER.info("getReq is asking for {} transactions", transactionsCount);
		Response<TransactionsGetResponse> getRes = client().service().transactionsGet(getReq).execute();
		
		if (getRes.isSuccessful()) {
			LOGGER.info("successful");
			LOGGER.info(getRes.body().getTransactions());
			//setEndDate(endDate);
			//setStartDate(startDate);
			return getRes;
		} else {
			LOGGER.error("raw: " + getRes.raw());
			LOGGER.error("error body: " + getRes.errorBody());
			LOGGER.error("code: " + getRes.code());
			return null;
		}
	}
	
	public Response<TransactionsGetResponse> tryWithSqlDate(String accessToken, String accountId, int transactionsCount, java.sql.Date startDate, java.sql.Date endDate) throws IOException {
		LOGGER.info("method: tryWithSqlDate");
		//java.sql.Date endDate = createSqlEndDate();
		//java.sql.Date startDate = createSqlStartDate();
		LOGGER.info("startDate: " + startDate);
		LOGGER.info("endDate: " + endDate);
		
		TransactionsGetRequest getReq = new TransactionsGetRequest(accessToken, startDate, endDate)
				.withAccountIds(Arrays.asList(accountId)).withCount(transactionsCount);
		LOGGER.info("getReq is asking for {} transactions", transactionsCount);
		Response<TransactionsGetResponse> getRes = client().service().transactionsGet(getReq).execute();
		
		if (getRes.isSuccessful()) {
			LOGGER.info("successful");
			LOGGER.info(getRes.body().getTransactions());
			setEndDate(endDate);
			setStartDate(startDate);
			return getRes;
		} else {
			LOGGER.error("raw: " + getRes.raw());
			LOGGER.error("error body: " + getRes.errorBody());
			LOGGER.error("code: " + getRes.code());
			return null;
		}
	}
	
	public static final PlaidClient client() {
		// Use builder to create a client
		PlaidClient client = PlaidClient.newBuilder()
				  .clientIdAndSecret(clientId, secretD)
				  .publicKey("") // optional. only needed to call endpoints that require a public key
				  .developmentBaseUrl() // or equivalent, depending on which environment you're calling into
				  .build();
		return client;
	}
	
	public Date createEndDate() throws ParseException {
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		Date end = new Date();
		calendar.setTime(end);
		String monthStr = null, dateStr = null;
		int month = calendar.get(Calendar.MONTH) + 1;
		int date = calendar.get(Calendar.DAY_OF_MONTH);
		int year = calendar.get(Calendar.YEAR);
		if (month <= 9) monthStr = "0" + month;
		else monthStr = String.valueOf(month);
		if (date <= 9) dateStr = "0" + date;
		else dateStr = String.valueOf(date);
		String endDateStr = year+"-"+monthStr+"-"+dateStr;
		calendar.setTime(sdf.parse(endDateStr));
		//System.out.println("endDateStr: " + endDateStr);
		return calendar.getTime();
	}
	
	public Date createStartDate() throws ParseException {
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		Date start = new Date();
		calendar.setTime(start);
		calendar.add(Calendar.MONTH, -1); // go back one month
		String monthStr = null, dateStr = null;
		int month = calendar.get(Calendar.MONTH) + 1;
		int date = calendar.get(Calendar.DAY_OF_MONTH);
		int year = calendar.get(Calendar.YEAR);
		if (month <= 9) monthStr = "0" + month;
		else monthStr = String.valueOf(month);
		if (date <= 9) dateStr = "0" + date;
		else dateStr = String.valueOf(date);
		String startDateStr = year+"-"+monthStr+"-"+dateStr;
		calendar.setTime(sdf.parse(startDateStr));
		//System.out.println("endDateStr: " + endDateStr);
		return calendar.getTime();
	}
	
	public java.sql.Date createSqlEndDate() {
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		Date end = new Date();
		calendar.setTime(end);
		String monthStr = null, dateStr = null;
		int month = calendar.get(Calendar.MONTH) + 1;
		int date = calendar.get(Calendar.DAY_OF_MONTH);
		int year = calendar.get(Calendar.YEAR);
		if (month <= 9) monthStr = "0" + month;
		else monthStr = String.valueOf(month);
		if (date <= 9) dateStr = "0" + date;
		else dateStr = String.valueOf(date);
		String endDateStr = year+"-"+monthStr+"-"+dateStr;
		//System.out.println("endDateStr: " + endDateStr);
		return java.sql.Date.valueOf(endDateStr);
	}

	public java.sql.Date createSqlStartDate() {
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		Date start = new Date();
		calendar.setTime(start);
		calendar.add(Calendar.MONTH, -1); // go back one month
		String monthStr = null, dateStr = null;
		int month = calendar.get(Calendar.MONTH) + 1; 
		int date = calendar.get(Calendar.DAY_OF_MONTH);
		int year = calendar.get(Calendar.YEAR);
		if (month <= 9) monthStr = "0" + month;
		else monthStr = String.valueOf(month);
		if (date <= 9) dateStr = "0" + date;
		else dateStr = String.valueOf(date);
		String startDateStr = year+"-"+monthStr+"-"+dateStr;
		//System.out.println("startDateStr: " + startDateStr);
		return java.sql.Date.valueOf(startDateStr);
	}
}
