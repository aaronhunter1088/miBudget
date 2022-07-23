package com.miBudget.processors;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import com.miBudget.core.MiBudgetError;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.plaid.client.*;
import com.plaid.client.request.TransactionsGetRequest;
import com.plaid.client.response.TransactionsGetResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import retrofit2.Response;
public class TransactionsProcessor {
	
	private static final String clientId = "5ae66fb478f5440010e414ae";
	private static final String secret = "0e580ef72b47a2e4a7723e8abc7df5"; 
	private static final String secretD = "c7d7ddb79d5b92aec57170440f7304";
	private static final PlaidClient client() {
		// Use builder to create a client
		PlaidClient client = PlaidClient.newBuilder()
				.clientIdAndSecret(clientId, secretD)
				.publicKey("") // optional. only needed to call endpoints that require a public key
				.developmentBaseUrl() // or equivalent, depending on which environment you're calling into
				.build();
		return client;
	}
	private static Logger LOGGER = null;
	private LocalDate endDate, startDate;
	static  {
		System.setProperty("appName", "miBudget");
		LOGGER = LogManager.getLogger(TransactionsProcessor.class);
	}

	public LocalDate getEndDate() {
		return endDate;
	}
	public TransactionsProcessor setEndDate(LocalDate endDate) {
		this.endDate = endDate;
		return this;
	}

	public LocalDate getStartDate() {
		return startDate;
	}
	public TransactionsProcessor setStartDate(LocalDate startDate) {
		this.startDate = startDate;
		return this;
	}

	public TransactionsProcessor() {};
	
	public Response<TransactionsGetResponse> getTransactions(String accessToken, String accountId, int transactionsCount, java.sql.Date startDate, java.sql.Date endDate) throws ParseException, IOException, MiBudgetError
	{
		
		Response<TransactionsGetResponse> response = tryWithSqlDate(accessToken, accountId, transactionsCount, startDate, endDate);
		//Response<TransactionsGetResponse> response = tryWithDate(accessToken, accountId, transactionsCount);
		// i prefer sql date because the date is much easier to read and it still works
		
		return response;
	}
	
	public Response<TransactionsGetResponse> tryWithSqlDate(
			String accessToken, String accountId, int transactionsCount, java.sql.Date startDate, java.sql.Date endDate)
			throws IOException, MiBudgetError
	{
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
			setEndDate(LocalDate.parse(endDate.toString()));
			setStartDate(LocalDate.parse(endDate.toString()));
			return getRes;
		}
		else {
			MiBudgetError error = new MiBudgetError();
			JSONObject jsonObj = createJSONObject(getRes);
			error.setMessage((String)jsonObj.get("error_message"));
			error.setErrorCode(Integer.toString(getRes.code()));
			error.setErrorType((String)jsonObj.get("error_type"));
			LOGGER.error("raw: " + getRes.raw());
			LOGGER.error("code: " + getRes.code());
			LOGGER.error("error body: " + getRes.errorBody().string());
			throw error;
		}
	}

	@Deprecated
	public Date createEndDate(String endDate) throws ParseException {
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
	@Deprecated
	public Date createStartDate(String fromDate) throws ParseException {
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
	
	public java.sql.Date createSqlEndDate()
	{
		LOGGER.info("method: createSqlEndDate");
		LocalDate date = null;
		if (getStartDate() != null) date = getStartDate();
		else date = LocalDate.now();
		date = date.plusMonths(1);
		LOGGER.info("date: " + date);
		String dateAsStr = date.toString();
		dateAsStr = dateAsStr.replaceAll("-", "/");
		LOGGER.info("replaced: " + dateAsStr);
		dateAsStr = dateAsStr.substring(5) + "/" + dateAsStr.substring(0,4);
		LOGGER.info("swapped and done: " + dateAsStr);
		setEndDate(date);
		return java.sql.Date.valueOf(LocalDate.parse(dateAsStr, DateTimeFormatter.ofPattern("MM/dd/yyyy")));
	}
	public java.sql.Date createSqlStartDate()
	{
		LOGGER.info("method: createSqlStartDate");
		LocalDate date = LocalDate.now();
		LOGGER.info("date: " + date);
		String dateAsStr = date.toString();
		dateAsStr = dateAsStr.replaceAll("-", "/");
		LOGGER.info("replaced: " + dateAsStr);
		dateAsStr = dateAsStr.substring(5) + "/" + dateAsStr.substring(0,4);
		LOGGER.info("swapped and done: " + dateAsStr);
		setStartDate(date);
		return java.sql.Date.valueOf(LocalDate.parse(dateAsStr, DateTimeFormatter.ofPattern("MM/dd/yyyy")));
	}

	/* NEW */
	public java.sql.Date getSqlStartDateNew(String fromDate)
	{
		LOGGER.info("method: getSqlStartDateNew");
		LocalDate date = LocalDate.parse(fromDate, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
		LOGGER.info("date: " + date);
		String[] datePieces = (date.toString()).split("-");
		String dateAsStr = datePieces[1] + "/" + datePieces[2] + "/" + datePieces[0];
		LOGGER.info("done: " + dateAsStr);
		setStartDate(date);
		return java.sql.Date.valueOf(LocalDate.parse(dateAsStr, DateTimeFormatter.ofPattern("MM/dd/yyyy")));
	}
	public java.sql.Date getSqlEndDateNew(String endDate)
	{
		LOGGER.info("method: getSqlEndDateNew");
		LocalDate date = LocalDate.parse(endDate, DateTimeFormatter.ofPattern("MM/dd/yyyy")).plusMonths(1);
		LOGGER.info("date: " + date);
		String[] datePieces = (date.toString()).split("-");
		String dateAsStr = datePieces[2] + "/" + datePieces[1] + "/" + datePieces[0];
		LOGGER.info("done: " + dateAsStr);
		setEndDate(date);
		return java.sql.Date.valueOf(LocalDate.parse(dateAsStr, DateTimeFormatter.ofPattern("MM/dd/yyyy")));
	}
	/* NEW */

	public org.json.simple.JSONObject createJSONObject(Response<TransactionsGetResponse> tgr) throws IOException
	{
		String errBody = tgr.errorBody().string();
		LOGGER.debug("creating jsonObj from: " +errBody);
		org.json.simple.JSONObject thisJsonObject = null;
		try
		{
			JSONParser parser = new JSONParser();
			String formattedString = errBody.trim().replaceAll("\n", "");
			thisJsonObject = (JSONObject) parser.parse(formattedString);
			String errType = thisJsonObject.get("error_type").toString();
			LOGGER.debug("thisJSONObject: " + thisJsonObject);
			LOGGER.debug("errType: " + errType);
		}
		catch (org.json.simple.parser.ParseException pe)
		{

		}
		catch (Exception e)
		{
			LOGGER.error("Fails to create json object because {}", e.getMessage());
			LOGGER.debug("pause to read...");
		}

		return thisJsonObject;
	}
}
