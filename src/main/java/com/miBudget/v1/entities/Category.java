package com.miBudget.v1.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="users_categories")
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
	
	@Column(name="currency")
	private String currency;
	
	@Column(name="amount")
	private double budgetedAmt;
	
	@Column(name="rules")
	private ArrayList<Rule> rules;
	
	@Column(name="user_id")
	private int userId;
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	public String getCurrency() {
		return currency;
	}

	public double getBudgetedAmt() {
		return budgetedAmt;
	}

	public ArrayList<Rule> getRules() {
		return rules;
	}
	
	public int getUserId() {
		return userId;
	}
	
	private void setId(int id) {
		this.id = id;
	}

	private void setName(String name) {
		this.name = name;
	}
	
	private void setCurrency(String currency) {
		this.currency = currency;
	}

	private void setBudgetedAmt(double budgetedAmt) {
		this.budgetedAmt = budgetedAmt;
	}

	private void setRules(ArrayList<Rule> rules) {
		this.rules = rules;
	}
	
	private void setUserId(int userId) {
		this.userId = userId;
	}
	
	private void addRule(Rule rule) {
		if (this.rules == null) {
			rules = new ArrayList<Rule>();
		}
		rules.add(rule);
	}

	@Override
	public String toString() {
		return "Category [id=" + id + ", name=" + name + ", budgetedAmt=" + budgetedAmt + ", rules=" + rules + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(rules, budgetedAmt, name);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Category other = (Category) obj;
		return Objects.equals(rules, other.rules)
				&& Double.doubleToLongBits(budgetedAmt) == Double.doubleToLongBits(other.budgetedAmt)
				&& Objects.equals(name, other.name) && Objects.equals(id,  other.id);
	}
	
	public void createRules() {
		this.rules = new ArrayList<Rule>();
	}
	
}
