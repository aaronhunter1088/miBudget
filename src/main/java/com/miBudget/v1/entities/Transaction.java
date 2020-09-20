package com.miBudget.v1.entities;

import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.Serializable;
import java.util.*;

/**
 * Notes
 * @author michaelball
 * 
 * This is what a Plaid Transaction is/has:
 * {
      "account_id": "zJAKN9ak3jsKYPqew3nwuZPgaeNVgzFO6LxkR", check
      "account_owner": null,   not needed
      "amount": 483.48, check   
      "authorized_date": null,  not needed
      "category": [   check
        "Service",
        "Financial"
      ],
      "category_id": "18020000",   not needed
      "date": "2020-03-12",   check
      "location": {  check
        "address": null,
        "city": null,
        "lat": null,
        "lon": null,
        "state": null,
        "store_number": null,
        "zip": null
      },
      "name": "TALLY TECH TALLY WEB ID: 2473932324", check 
      "payment_channel": "online",    not needed
      "payment_meta": {   not needed
        "by_order_of": null,
        "payee": null,
        "payer": null,
        "payment_method": null,
        "payment_processor": null,
        "ppd_id": null,
        "reason": null,
        "reference_number": null
      },
      "pending": false,    not needed
      "pending_transaction_id": "yxkyNg4KBoSyYPjgdB7XfYdBzY389JtO8zJD7",    not needed
      "transaction_id": "kPXYBzyJV0s8nDgAZ6EbFddoXXjgx5fRQAwz0",      check
      "transaction_type": "place"   not needed
    }
 *
 */

public class Transaction implements Serializable {

	private String transactionId;
	
	private String accountId;
	
	/**
	 * the name of where the transaction took place
	 */
	private String name;
	
	private double amount;
	
	private Location location;
	
	private List<String> defaultCategories; // TODO: rename defaultCategories
	
	private Date date;
	
	private static final long serialVersionUID = 1L;
	
	public Transaction() {}
	
	public Transaction(String transactionId, String accountId, String name, double amount, com.miBudget.v1.entities.Location location, List<String> defaultCategories, Date date) {
		setTransactionId(transactionId);
		setAccountId(accountId);
		setName(name);
		setAmount(amount);
		setLocation(location);
		setCategories(defaultCategories);
		setDate(date);
	}
	
	/**
	 * 
	 * @param accountId
	 * @param name, where the transaction took place
	 * @param amount
	 * @param location
	 * @param defaultCategories
	 */
	public Transaction(String accountId, String name, double amount, com.miBudget.v1.entities.Location location, List<String> defaultCategories) {
		setAccountId(accountId);
		setName(name);
		setAmount(amount);
		setLocation(location);
		setCategories(defaultCategories);
	}
	
	/**
	 * 
	 * @param accountId
	 * @param name
	 * @param amount
	 * @param location
	 */
	public Transaction(String accountId, String name, double amount, com.miBudget.v1.entities.Location location) {
		setAccountId(accountId);
		setName(name);
		setAmount(amount);
		setLocation(location);
		setCategories(null);
	}
	
	/**
	 * 
	 * @param accountId
	 * @param name
	 * @param amount
	 * @param defaultCategories
	 */
	public Transaction(String accountId, String name, double amount, List<String> defaultCategories) {
		setAccountId(accountId);
		setName(name);
		setAmount(amount);
		setLocation(null);
		setCategories(defaultCategories);
	}
	
	/**
	 * 
	 * @param accountId
	 * @param name
	 * @param amount
	 */
	public Transaction(String accountId, String name, double amount) {
		setAccountId(accountId);
		setName(name);
		setAmount(amount);
		setLocation(null);
		setCategories(null);
	}

    public Transaction(JSONObject jsonObject) {
		this(jsonObject.get("transactionId").toString(),
			jsonObject.get("accountId").toString(),
			jsonObject.get("name").toString(),
			Double.parseDouble(jsonObject.get("amount").toString()),
			(Location)jsonObject.get("location"),
			(ArrayList<String>)jsonObject.get("defaultCategories"),
			(Date)jsonObject.get("date"));
    }

	/**
	 * return the transactionId
	 */
	public String getTransactionId() {
		return transactionId;
	}

	/**
	 * @return the accountId
	 */
	public String getAccountId() {
		return accountId;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the amount
	 */
	public double getAmount() {
		return amount;
	}

	/**
	 * @return the location
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * @return the categories
	 */
	public List<String> getDefaultCategories() {
		return defaultCategories;
	}

	/**
	 * @return the serialversionuid
	 */
	private static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	/**
	 * @return the date of the transaction
	 */
	private Date getDate() {
		return date;
	}
	
	/* ***************** SETTERS ******************** */

	/**
	 * @param transactionId the transactionId to set
	 */
	private void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	
	/**
	 * @param accountId the accountId to set
	 */
	private void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	/**
	 * @param name the name to set
	 */
	private void setName(String name) {
		this.name = name;
	}

	/**
	 * @param amount the amount to set
	 */
	private void setAmount(double amount) {
		this.amount = amount;
	}

	/**
	 * @param location the location to set
	 */
	private void setLocation(Location location) {
		this.location = location;
	}

	/**
	 * @param defaultCategories the categories to set
	 */
	private void setCategories(List<String> defaultCategories) {
		this.defaultCategories = defaultCategories;
	}

	/**
	 * @param date the Date to set
	 */
	private void setDate(Date date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "Transaction{" +
				"transactionId='" + transactionId + '\'' +
				", accountId='" + accountId + '\'' +
				", name='" + name + '\'' +
				", amount=" + amount +
				", location=" + location +
				", defaultCategories=" + defaultCategories +
				", date=" + date +
				'}';
	}

	public String toJsonString() {
		return new StringBuilder().append("Transaction{")
				.append("\"transactionId\":").append(transactionId).append(",")
				.append("\"accountId\"").append(accountId).append(",")
				.append("\"name\"").append(name).append(",")
				.append("\"amount\"").append(amount).append(",")
				.append("\"location\"").append(location).append(",")
				.append("\"defaultCategories\"").append(defaultCategories).append(",")
				.append("\"date\"").append(date).append("}").toString();
	}
}
