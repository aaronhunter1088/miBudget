package com.miBudget.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
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

@Data
@Entity
@Table(name="transactions")
public class Transaction implements Serializable {

	@Id
	@SequenceGenerator(name="transaction_sequence", sequenceName="transaction_sequence", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="transaction_sequence")
	private Long id;
	private String transactionId;
	private String accountId;
	private String name; /* the name is where the transaction took place */
	private double amount;
	private Location location;
	private List<String> defaultCategories;
	private LocalDate date;
	
	public Transaction() {}
	
	public Transaction(String transactionId, String accountId, String name, double amount, Location location, List<String> defaultCategories, LocalDate date) {
		this.transactionId = transactionId;
		this.accountId = accountId;
		this.name = name;
		this.amount = amount;
		this.location = location;
		this.defaultCategories = defaultCategories;
		this.date = date;
	}
	
	/**
	 * 
	 * @param accountId
	 * @param name, where the transaction took place
	 * @param amount
	 * @param location
	 * @param defaultCategories
	 */
	public Transaction(String accountId, String name, double amount, Location location, List<String> defaultCategories) {
		this.accountId = accountId;
		this.name = name;
		this.amount = amount;
		this.location = location;
		this.defaultCategories = defaultCategories;
	}
	
	/**
	 * 
	 * @param accountId
	 * @param name
	 * @param amount
	 * @param location
	 */
	public Transaction(String accountId, String name, double amount, Location location) {
		this.accountId = accountId;
		this.name = name;
		this.amount = amount;
		this.location = location;
	}

}
