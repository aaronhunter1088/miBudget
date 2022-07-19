package com.miBudget.utilities;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.miBudget.daoimplementations.AccountDAOImpl;
import com.miBudget.daoimplementations.ItemDAOImpl;
import com.miBudget.daoimplementations.MiBudgetDAOImpl;
import com.miBudget.entities.MiBudgetError;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.miBudget.entities.Location;
import com.miBudget.processors.TransactionsProcessor;
import com.plaid.client.response.TransactionsGetResponse;

import retrofit2.Response;


public class ClientTest {
	
	private static Logger LOGGER = LogManager.getLogger(ClientTest.class);

	public static void main(String[] args) throws ParseException {
		// Test MySql connection
		//testMySql();
		
		// Test TransactionsProcessor
		testTransactionsProcessor();
	}
	
	public static void testTransactionsProcessor() throws ParseException {
		TransactionsProcessor transactionsProcessor = new TransactionsProcessor();
		try {
			Response<TransactionsGetResponse> response = transactionsProcessor.getTransactions("access-development-7e2ed5ce-8d7e-471d-bc24-af1a4156774f",
					"zJAKN9ak3jsKYPqew3nwuZPgaeNVgzFO6LxkR", 5, null, null);
			if (response.isSuccessful()) {
				LOGGER.info("transactionsProcessor was successful");
				LOGGER.info("raw: {}", (response.body().toString()));
				LOGGER.info("count: {}", (response.body().getTransactions().size()));
				LOGGER.info("transactions: {}", (response.body().getTransactions().toString()));
				List<com.miBudget.entities.Transaction> transactions = convertTransactions(response);
				for(com.miBudget.entities.Transaction transaction : transactions) {
					LOGGER.info("transactions after conversion: {}", transaction);
				}
			}
		} catch (IOException | MiBudgetError e) {
			LOGGER.error(e);
		}
	}
	
	public static List<com.miBudget.entities.Transaction> convertTransactions(Response<TransactionsGetResponse> response) throws ParseException {
		List<com.miBudget.entities.Transaction> listOfConvertedTransactions = new ArrayList<com.miBudget.entities.Transaction>();
		int i = 1;
		for(TransactionsGetResponse.Transaction transaction : response.body().getTransactions()) {
//			LOGGER.info("\ntransaction number {}", i++);
//			LOGGER.info("transaction accountId: {}", transaction.getAccountId());
//			LOGGER.info("transaction transactionId: {}", transaction.getTransactionId());
//			LOGGER.info("transaction name: {}", transaction.getName());
//			LOGGER.info("transaction amount: {}", transaction.getAmount());
//			LOGGER.info("transaction location: {}", convertLocation(transaction.getLocation()));
//			LOGGER.info("transaction categories: {}", transaction.getCategory());
//			LOGGER.info("transaction date: {}", transaction.getDate());
			com.miBudget.entities.Transaction newTransaction =
				new com.miBudget.entities.Transaction(
					transaction.getTransactionId(), 
					transaction.getAccountId(),
					transaction.getName(), 
					transaction.getAmount(), 
					convertLocation(transaction.getLocation()), 
					transaction.getCategory(), 
					new SimpleDateFormat("yyyy-dd-MM").parse(transaction.getDate())
				);
			listOfConvertedTransactions.add(newTransaction);
		}
		return listOfConvertedTransactions;
	}
	
	public static Location convertLocation(TransactionsGetResponse.Transaction.Location location) {
		Location loc = new Location(
			location.getAddress(),
			location.getCity(),
			location.getState(),
			location.getZip()
		);
		return loc;
	}
	
	public static void testMySql() {
		try {
			HibernateUtilities hibernate = new HibernateUtilities();
			SessionFactory factory = hibernate.getSessionFactory();
			Session hibernateSession = factory.openSession();
			Transaction t = hibernateSession.beginTransaction();
			String SQL = "SELECT version()";
			String result = hibernateSession.createNativeQuery(SQL).getResultList().get(0).toString();
			LOGGER.info("MySQL version is {}\n", result);
			//t.commit();
			hibernateSession.close();
			LOGGER.info("End client test.");
			//t = hibernateSession.beginTransaction();
			//User me = new User(20);
			//@SuppressWarnings("unchecked")
			//ArrayList<?> ids = miBudgetDAOImpl.getAllInstitutionIdsFromUser(me);
			//t.commit();
			//t = hibernateSession.beginTransaction();
			//ArrayList<Item> items = new ArrayList<>();
			//Item item = itemDAOImpl.getItemFromUser(ids.get(0).toString());
			//System.out.println(item);//
			//for(int i = 0; i < ids.size(); i++) {
				//String itemTableId = (String) itemsTableIdList.get(i);
			//	Item item = itemDAOImpl.getItemFromUser(ids.get(i).toString());
			//	System.out.println(item);
				//t.commit();
				//t = hibernateSession.beginTransaction();
			//}
			//t.commit();
			
			//HibernateUtilities.shutdown();
		} catch (NullPointerException | HibernateException e) {
			LOGGER.error("Error making a connection to the database");
			StackTraceElement[] ste = e.getStackTrace();
			StringBuilder err = new StringBuilder();
			for (int i = 0; i < ste.length; i++) {
				err.append(ste[i] + "\n");
			}
			LOGGER.error(err);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
	}

}
