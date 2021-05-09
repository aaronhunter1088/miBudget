package com.miBudget.v1.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;

import com.miBudget.v1.daoimplementations.AccountDAOImpl;


@Entity
@Table(name="users")
public class User implements Serializable {

	public User() {}
	
	public User(int userid) {
		this.userid = userid;
	}
	
	/**
	 * If you need to validate a user, you can use this constructor.
	 * @param cellphone
	 * @param password
	 */
	public User(String cellphone, String password) {
		this.cellphone = cellphone;
		this.password = password;
		createCategories();
		this.ignoredTransactions = new ArrayList<>();
	}
	
	/**
	 * To create a user, please provide in the following order:
	 * @param firstname
	 * @param lastname
	 * @param cellphone
	 * @param password
	 */
	public User(String firstname, String lastname, String cellphone, String password) {
		this.firstname = firstname;
		this.lastname = lastname;
		this.cellphone = cellphone;
		this.password = password;
		createAccounts();
		createCategories();
	}
	
	/**
	 * To create a user, please provide in the following order:
	 * @param firstname
	 * @param lastname
	 * @param cellphone
	 * @param password
	 * @param email
	 */
	public User(String firstname, String lastname, String cellphone, String password, String email) {
		this.firstname = firstname;
		this.lastname = lastname;
		this.cellphone = cellphone;
		this.password = password;
		this.email = email;
		this.setupMode = true;
		createAccounts();
		createCategories();
		setIgnoredTransactions(new ArrayList<Transaction>());
	}
	
	/**
	 * To create a user with no accounts, please provide in the following order:
	 * @param userid
	 * @param firstname
	 * @param lastname
	 * @param cellphone
	 * @param password
	 */
	public User(int userid, String firstname, String lastname, String cellphone, String password, String email) {
		this.userid = userid;
		this.firstname = firstname;
		this.lastname = lastname;
		this.cellphone = cellphone;
		this.password = password;
		this.email = email;
		createAccounts();
		createCategories();
	}
	
	/**
	 * To create a user with accounts, please provide in the following order:
	 * @param userid
	 * @param firstname
	 * @param lastname
	 * @param cellphone
	 * @param password
	 * @param email
	 * @param accountIds
	 */
	public User(int userid, String firstname, String lastname, String cellphone, String password, String email, ArrayList<String> accountIds) {
		this.userid = userid;
		this.firstname = firstname;
		this.lastname = lastname;
		this.cellphone = cellphone;
		this.password = password;
		this.email = email;
		this.accountIds = accountIds;
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="userId", updatable=false, nullable=false)
	private int userid;
	
	@Column(name="firstname")
	private String firstname;
	
	@Column(name="lastname")
	private String lastname;
	
	@Column(name="cellphone")
	private String cellphone;
	
	@Column(name="password")
	private String password;
	
	// New
	@Column(name="email")
	private String email;

	// New
	@Column(name="setupmode")
	private boolean setupMode;
	
	@Transient
	private ArrayList<String> accountIds; // will become budget_ids ...

	@Transient
	private ArrayList<Transaction> transactions = new ArrayList<>();

	@Transient // will need to persist
	private ArrayList<Transaction> ignoredTransactions = new ArrayList<>();

	@Transient // will need to persist
	private ArrayList<Transaction> bills;

	@Transient // will need to persist
	private ArrayList<Category> categories;

	private static final long serialVersionUID = 1L;

	public int getId() {
		return userid;
	}

	public void setId(int userid) {
		this.userid = userid;
	}

	public String getFirstName() {
		return firstname;
	}

	private void setFirstName(String firstname) {
		this.firstname = firstname;
	}

	public String getLastName() {
		return lastname;
	}

	private void setLastName(String lastname) {
		this.lastname = lastname;
	}

	public String getCellphone() {
		return cellphone;
	}

	private void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}

	public String getPassword() {
		return password;
	}

	private void setPassword(String password) {
		this.password = password;
	}
	
	private String getEmail() {
		return email;
	}
	
	private void setEmail(String email) {
		this.email = email;
	}

	public ArrayList<Transaction> getTransactions() { return transactions; }
	public ArrayList<Transaction> getIgnoredTransactions() { return ignoredTransactions; }

	public void setTransactions(ArrayList<Transaction> transactions) { this.transactions = transactions; }
	public void setIgnoredTransactions(ArrayList<Transaction> ignoredTransactions) { this.ignoredTransactions = ignoredTransactions; }

	public ArrayList<Transaction> getBills() { return bills; }

	public void setBills(ArrayList<Transaction> bills) {
		this.bills = bills;
	}

	public ArrayList<Category> getCategories() {
		return categories;
	}

	public void setCategories(ArrayList<Category> categories) {
		if (categories == null) {
			createCategories();
			return;
		}
		this.categories = categories;
	}

	public boolean isSetupMode() {
		return setupMode;
	}

	public User setSetupMode(boolean setupMode) {
		this.setupMode = setupMode;
		return this;
	}

	@Override
	public String toString() {
		return "User [userid=" + userid + ", firstname=" + firstname + ", lastname=" + lastname + ", cellphone=" + cellphone
				+ ", password=" + password + ", email=" + email + ", accountIds=" + accountIds 
				+ ", categories=" + categories + "]";
	}

	public static List<String> getAccountIds(User user) {
		System.out.println("Getting account_ids from user...");
		AccountDAOImpl accountDAOImpl = new AccountDAOImpl();
		// Get list of item_ids from user in DB
//		List<String> idsFromUserInDB = MiBudgetDAOImpl.getAllItemIdsFromUser(this);
		List<String> accountIdsFromUserInDB = accountDAOImpl.getAccountIdsFromUser(user);
		return accountIdsFromUserInDB;
		
	}
	
	public ArrayList<String> getAccountIds() {
		return this.accountIds;
	}
	
	/**
	 * Sets the item for the user
	 * itemSepcifics is : access token, item id, request id
	 *
	 */
	public void setAccountIds(ArrayList<String> accountIds) {
		if (accountIds == null) {
			createAccounts();
			return;
		}
		this.accountIds = accountIds;
	}
		
	public void createAccounts() {
		
		System.out.println("accountIds is null for this user.");
		this.accountIds = new ArrayList<>();
		System.out.println("AccountsList has been created for this user.");
		
		
	}
	
	public void createCategories() {
		
		System.out.println("categories is null for " + this.firstname + " " + this.lastname);
		ArrayList<Category> categoriesList = new ArrayList<>();
		Category morgtageCat = new Category("Mortgage", "USD", 1500.00);
		categoriesList.add(morgtageCat);
		Category utilitiesCat = new Category("Utilities", "USD", 500.00);
		categoriesList.add(utilitiesCat);
		Category transportCat = new Category("Transportation", "USD", 1000.00);
		categoriesList.add(transportCat);
		Category insuranceCat = new Category("Insurance", "USD", 200.00);
		categoriesList.add(insuranceCat);
		Category foodCat = new Category("Food", "USD", 500.00);
		categoriesList.add(foodCat);
		Category subscriptionsCat = new Category("Subscriptions", "USD", 500.00);
		categoriesList.add(subscriptionsCat);
		Category billsCat = new Category("Bill", "USD", 1000.00);
		categoriesList.add(billsCat);
		System.out.println("default categories list created for " + this.firstname + " " + this.lastname);
		this.categories = categoriesList;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hash(accountIds, cellphone, email, firstname, userid, lastname, password);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return StringUtils.equals(other.getCellphone(), this.getCellphone()) && StringUtils.equals(other.getPassword(), this.getPassword());
	}
}
