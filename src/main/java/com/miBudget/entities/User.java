package com.miBudget.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.*;

import com.miBudget.daos.BudgetDAO;
import com.miBudget.daos.CategoryDAO;
import com.miBudget.daos.UserDAO;
import com.miBudget.enums.AppType;
import lombok.Data;

import static com.miBudget.enums.AppType.FREE;

//@Data
@Entity
@Table(name="users")
public class User {

	@Id
	@SequenceGenerator(name="users_sequence", sequenceName="users_sequence", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="users_sequence")
	private Long id;
	private String firstName;
	private String lastName;
	private String cellphone;
	private String password;
	private String email;
	private Long mainBudgetId;
	@Enumerated(EnumType.ORDINAL)
	private AppType appType; // free or paid
	@Transient
	private Budget budget; // stored in Budgets but not saved as part of the user
	@Transient
	private List<Transaction> ignoredTransactions;
	@Transient
	private List<Transaction> transactions;
	@Transient
	private List<Transaction> bills;
	@Transient
	private List<Account> accounts;
	@Transient
	private List<Item> items;
	// Holds instances of DAOs to retrieve user specific information

	public User() {}
	
	/**
	 * Used when logging in
	 * @param cellphone
	 * @param password
	 */
	public User(String cellphone, String password) {
		this.cellphone = cellphone;
		this.password = password;
		this.ignoredTransactions = new ArrayList<>();
	}
	
	/**
	 * To create a user, please provide in the following order:
	 * @param firstName
	 * @param lastName
	 * @param cellphone
	 * @param password
	 * @param email
	 */
	public User(String firstName, String lastName, String cellphone, String password, String email, AppType appType) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.cellphone = cellphone;
		this.password = password;
		this.email = email;
		this.appType = appType;
		this.ignoredTransactions = new ArrayList<>();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getCellphone() {
		return cellphone;
	}

	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getMainBudgetId() {
		return mainBudgetId;
	}

	public void setMainBudgetId(Long mainBudgetId) {
		this.mainBudgetId = mainBudgetId;
	}

	public AppType getAppType() {
		return appType;
	}

	public void setAppType(AppType appType) {
		this.appType = appType;
	}

	public Budget getBudget() {
		return budget;
	}

	public void setBudget(Budget budget) {
		this.budget = budget;
	}

	public List<Transaction> getIgnoredTransactions() {
		return ignoredTransactions;
	}

	public void setIgnoredTransactions(List<Transaction> ignoredTransactions) {
		this.ignoredTransactions = ignoredTransactions;
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}

	public List<Transaction> getBills() {
		return bills;
	}

	public void setBills(List<Transaction> bills) {
		this.bills = bills;
	}

	public List<Account> getAccounts() {
		return accounts;
	}

	public void setAccounts(List<Account> accounts) {
		this.accounts = accounts;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		User user = (User) o;
		return getId() == user.getId() || (
				Objects.equals(getCellphone(), user.getCellphone()) &&
				Objects.equals(getPassword(), user.getPassword()) );
	}

}
