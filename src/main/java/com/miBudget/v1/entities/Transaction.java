package com.miBudget.v1.entities;

import java.io.Serializable;
import java.util.List;

public class Transaction implements Serializable {

	private String accountId;
	
	private String name;
	
	private double amount;
	
	private Location location;
	
	private List<String> categories;
	private static final long serialVersionUID = 1L;
	public Transaction() {}
	
	/**
	 * 
	 * @param accountId
	 * @param name
	 * @param amount
	 * @param location
	 * @param categories
	 */
	public Transaction(String accountId, String name, double amount, Location location, List<String> categories) {
		setAccountId(accountId);
		setName(name);
		setAmount(amount);
		setLocation(location);
		setCategories(categories);
	}
	
	/**
	 * 
	 * @param accountId
	 * @param name
	 * @param amount
	 * @param location
	 */
	public Transaction(String accountId, String name, double amount, Location location) {
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
	 * @param categories
	 */
	public Transaction(String accountId, String name, double amount, List<String> categories) {
		setAccountId(accountId);
		setName(name);
		setAmount(amount);
		setLocation(null);
		setCategories(categories);
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
	public List<String> getCategories() {
		return categories;
	}

	/**
	 * @return the serialversionuid
	 */
	private static long getSerialversionuid() {
		return serialVersionUID;
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
	 * @param categories the categories to set
	 */
	private void setCategories(List<String> categories) {
		this.categories = categories;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
