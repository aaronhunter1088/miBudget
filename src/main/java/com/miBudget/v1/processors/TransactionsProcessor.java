package com.miBudget.v1.processors;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

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
	
	public java.sql.Date getEndDate() { return endDate; }
	public java.sql.Date getStartDate() { return startDate; }

	public TransactionsProcessor() {};
	
	public Response<TransactionsGetResponse> getTransactions(String accessToken, String accountId, int transactionsCount) throws IOException {
		
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		Date end = new Date();
		calendar.setTime(end);
		String monthStr = null, dateStr = null;
		int month = calendar.get(Calendar.MONTH) + 1; // could be 1-9
		int date = calendar.get(Calendar.DAY_OF_MONTH); // could be 1-9
		int year = calendar.get(Calendar.YEAR);
		if (month <= 9) monthStr = "0" + month;
		else monthStr = String.valueOf(month);
		if (date <= 9) dateStr = "0" + date;
		else dateStr = String.valueOf(date);
		String endDateStr = year+"-"+monthStr+"-"+dateStr;
		System.out.println("endDateStr: " + endDateStr);
		java.sql.Date endDate = java.sql.Date.valueOf(endDateStr);
		
		calendar.add(Calendar.MONTH, -1); // go back one month
		month = calendar.get(Calendar.MONTH) + 1; // could be 1-9
		date = calendar.get(Calendar.DAY_OF_MONTH); // could be 1-9
		year = calendar.get(Calendar.YEAR);
		if (month <= 9) monthStr = "0" + month;
		else monthStr = String.valueOf(month);
		if (date <= 9) dateStr = "0" + date;
		else dateStr = String.valueOf(date);
		String startDateStr = year+"-"+monthStr+"-"+dateStr;
		System.out.println("startDateStr: " + startDateStr);
		java.sql.Date startDate = java.sql.Date.valueOf(startDateStr);
		
		System.out.println("end_date: " + endDate);
		System.out.println("start_date: " + startDate);
		
		TransactionsGetRequest getReq = new TransactionsGetRequest(accessToken, startDate, endDate);
		getReq.withAccountIds(Arrays.asList(accountId)).withCount(transactionsCount);
		Response<TransactionsGetResponse> getRes = client().service().transactionsGet(getReq).execute();
		
		if (getRes.isSuccessful()) {
			System.out.println("successful");
			System.out.println(getRes.body().getTotalTransactions());
			System.out.println(getRes.body().getTransactions());
			setEndDate(endDate);
			setStartDate(startDate);
			return getRes;
		} else {
			System.out.println("raw: " + getRes.raw());
			System.out.println("error body: " + getRes.errorBody());
			System.out.println("code: " + getRes.code());
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

}
