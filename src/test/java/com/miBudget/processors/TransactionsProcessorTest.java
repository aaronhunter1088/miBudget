package com.miBudget.processors;

import com.miBudget.core.MiBudgetError;
import com.miBudget.entities.Location;
import com.miBudget.entities.Transaction;
import com.plaid.client.response.TransactionsGetResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import retrofit2.Response;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RunWith(JUnit4.class)
public class TransactionsProcessorTest {
    private static Logger LOGGER = LogManager.getLogger(TransactionsProcessorTest.class);

    @Test
    public void testTransactionsProcessorReturnsTransactions() throws Exception
    {
        TransactionsProcessor transactionsProcessor = new TransactionsProcessor();
        try {
            Calendar c = Calendar.getInstance();
            java.sql.Date startDate = new java.sql.Date(new Date().getTime());
            c.setTime(startDate);
            c.add(Calendar.MONTH, 1);
            java.sql.Date endDate = new java.sql.Date(c.getTime().getTime());
            Response<TransactionsGetResponse> response = transactionsProcessor.getTransactions("access-development-7e2ed5ce-8d7e-471d-bc24-af1a4156774f",
                    "zJAKN9ak3jsKYPqew3nwuZPgaeNVgzFO6LxkR", 5, startDate, endDate);
            if (response.isSuccessful()) {
                LOGGER.info("transactionsProcessor was successful");
                LOGGER.info("raw: {}", (response.body().toString()));
                LOGGER.info("count: {}", (response.body().getTransactions().size()));
                LOGGER.info("transactions: {}", (response.body().getTransactions().toString()));
                List<Transaction> transactions = convertTransactions(response);
                for(com.miBudget.entities.Transaction transaction : transactions) {
                    LOGGER.info("transactions after conversion: {}", transaction);
                }
            }
        } catch (IOException | MiBudgetError e) {
            LOGGER.error(e);
        }
    }

    public List<com.miBudget.entities.Transaction> convertTransactions(Response<TransactionsGetResponse> response) throws ParseException
    {
        List<com.miBudget.entities.Transaction> listOfConvertedTransactions = new ArrayList<Transaction>();
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
                            LocalDate.parse(transaction.getDate())
                    );
            listOfConvertedTransactions.add(newTransaction);
        }
        return listOfConvertedTransactions;
    }

    public Location convertLocation(TransactionsGetResponse.Transaction.Location location)
    {
        Location loc = new Location(
                location.getAddress(),
                location.getCity(),
                location.getState(),
                location.getZip()
        );
        return loc;
    }

}
