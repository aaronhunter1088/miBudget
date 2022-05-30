package com.miBudget.entities;

import java.io.Serializable;
import java.util.ArrayList;

import javax.persistence.*;

import com.miBudget.enums.AppType;
import lombok.Data;

import static com.miBudget.enums.AppType.FREE;

@Data
@Entity
@Table(name="users")
public class User implements Serializable {

	public User() {}
	
	public User(int id) {
		this.id = id;
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
	 * @param firstName
	 * @param lastName
	 * @param cellphone
	 * @param password
	 */
	public User(String firstName, String lastName, String cellphone, String password) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.cellphone = cellphone;
		this.password = password;
		createAccounts();
		createCategories();
	}
	
	/**
	 * To create a user, please provide in the following order:
	 * @param firstName
	 * @param lastName
	 * @param cellphone
	 * @param password
	 * @param email
	 */
	public User(String firstName, String lastName, String cellphone, String password, String email) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.cellphone = cellphone;
		this.password = password;
		this.email = email;
		this.appType = FREE;
		createAccounts();
		createCategories();
		setIgnoredTransactions(new ArrayList<Transaction>());
	}
	
	/**
	 * To create a user with no accounts, please provide in the following order:
	 * @param id
	 * @param firstName
	 * @param lastName
	 * @param cellphone
	 * @param password
	 */
	public User(int id, String firstName, String lastName, String cellphone, String password, String email) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.cellphone = cellphone;
		this.password = password;
		this.email = email;
		createAccounts();
		createCategories();
	}
	
	/**
	 * To create a user with accounts, please provide in the following order:
	 * @param id
	 * @param firstName
	 * @param lastName
	 * @param cellphone
	 * @param password
	 * @param email
	 * @param accountIds
	 */
	public User(int id, String firstName, String lastName, String cellphone, String password, String email, ArrayList<String> accountIds) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.cellphone = cellphone;
		this.password = password;
		this.email = email;
		this.accountIds = accountIds;
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id", updatable=false, nullable=false)
	private int id;
	
	@Column(name="first_name")
	private String firstName;
	
	@Column(name="last_name")
	private String lastName;
	
	@Column(name="cellphone")
	private String cellphone;
	
	@Column(name="password")
	private String password;

	@Column(name="email")
	private String email;

	@Column(name="app_type")
	private AppType appType; // free or paid

	@ElementCollection
	@Column(name="account_ids")
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

	public ArrayList<String> getAccountIds() {
		return this.accountIds;
	}
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

		System.out.println("categories is null for " + this.firstName + " " + this.lastName);
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
		System.out.println("default categories list created for " + this.firstName + " " + this.lastName);
		this.categories = categoriesList;
	}
}
