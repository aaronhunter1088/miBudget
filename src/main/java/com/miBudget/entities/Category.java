package com.miBudget.entities;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

import javax.persistence.*;

@Data
@Entity
@Table(name="categories")
public class Category implements Serializable {

	@Id
	@SequenceGenerator(name="categories_sequence", sequenceName="categories_sequence", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="categories_sequence")
	private Long id;
	private String name;
	private String currency;
	private double budgetedAmt;
	private Long userId;
	private Long budgetId;

	public Category() {}
	
	public Category(String name, double amount, Long userId) {
		this.name = name;
		this.budgetedAmt = amount;
		this.userId = userId;
	}
	
	public Category(String name, String currency, double amount, Long userId, Long budgetId) {
		this.name = name;
		this.budgetedAmt = amount;
		this.currency = currency;
		this.userId = userId;
		this.budgetId = budgetId;
	}

}
