package com.miBudget.entities;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

import javax.persistence.*;

@Data
@Entity
@Table(name="user_categories")
public class Category implements Serializable {

	public Category() {}
	
	public Category(String name, double amount) {
		this.name = name;
		this.budgetedAmt = amount;
		createRules();
	}
	
	public Category(String name, String currency, double amount) {
		this.name = name;
		this.budgetedAmt = amount;
		this.currency = currency;
		createRules();
	}
	
	public Category(String name, double amount, ArrayList<Rule> rules) {
		this.name = name;
		this.budgetedAmt = amount;
		this.rules = rules;
	}
	
	public Category(String name, String currency, double amount, ArrayList<Rule> rules) {
		this.name = name;
		this.currency = currency;
		this.budgetedAmt = amount;
		this.rules = rules;
	}
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id", updatable=false, nullable=false)
	private int id;
	
	@Column(name="name")
	private String name;
	
	@Column(name="currency_type")
	private String currency;
	
	@Column(name="budgeted_amt")
	private double budgetedAmt;

	@SuppressWarnings("JpaAttributeTypeInspection")
	@Column(name = "rules")
	private ArrayList<Rule> rules;
	
	@Column(name="user_id")
	private int userId;
	
	public void createRules() {
		this.rules = new ArrayList<Rule>();
	}
	
}
