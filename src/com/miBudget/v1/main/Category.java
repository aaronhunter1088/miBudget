package com.miBudget.v1.main;

import java.io.Serializable;

public class Category implements Serializable {

	public Category() {}
	
	public Category(String name, Double amount) {
		this.name = name;
		this.amount = amount;
	}
	/**
	 * default 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name;
	private Double amount;
	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
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
	public Double getAmount() {
		return amount;
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
	private void setAmount(Double amount) {
		this.amount = amount;
	}
	
	
}
