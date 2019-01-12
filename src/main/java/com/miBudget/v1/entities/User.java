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
import com.miBudget.v1.daoimplementations.AccountDAOImpl;


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
		createAccounts();
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
		createAccounts();
	}
	
	/**
	 * To create a user with no accounts, please provide in the following order:
	 * @param id
	 * @param firstname
	 * @param lastname
	 * @param cellphone
	 * @param password
	 */
	public User(int id, String firstname, String lastname, String cellphone, String password, String email) {
		this.id = id;
		this.firstname = firstname;
		this.lastname = lastname;
		this.cellphone = cellphone;
		this.password = password;
		this.email = email;
		createAccounts();
	}
	
	/**
	 * To create a user with accounts, please provide in the following order:
	 * @param id
	 * @param firstname
	 * @param lastname
	 * @param cellphone
	 * @param password
	 * @param email
	 * @param accountIds
	 */
	public User(int id, String firstname, String lastname, String cellphone, String password, String email, ArrayList<String> accountIds) {
		this.id = id;
		this.firstname = firstname;
		this.lastname = lastname;
		this.cellphone = cellphone;
		this.password = password;
		this.email = email;
		this.accountIds = accountIds;
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id", updatable = false, nullable = false)
	private int id;
	
	@Column(name="first_name")
	private String firstname;
	
	@Column(name="last_name")
	private String lastname;
	
	@Column(name="cellphone")
	private String cellphone;
	
	@Column(name="password")
	private String password;
	
	// New
	@Column(name="email")
	private String email;
	
	@Transient
	private ArrayList<String> accountIds; // will become budget_ids ...
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	@Override
	public String toString() {
		return "User [id=" + id + ", firstname=" + firstname + ", lastname=" + lastname + ", cellphone=" + cellphone
				+ ", password=" + password + ", email=" + email + ", accountIds=" + accountIds + "]";
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
	 * @param itemSpecifics
	 */
	public void setAccountIds(ArrayList<String> accountIds) {
		if (accountIds.size() == 0) {
			createAccounts();
		}
		this.accountIds = accountIds;
	}
		
	public ArrayList<String> createAccounts() {
		System.out.println("account_ids is null");
		accountIds = new ArrayList<String>();
		System.out.println("AccountsList has been created for " + this.firstname);
		return accountIds;
		
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hash(accountIds, cellphone, email, firstname, id, lastname, password);
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
		return Objects.equals(accountIds, other.accountIds) && Objects.equals(cellphone, other.cellphone)
				&& Objects.equals(email, other.email) && Objects.equals(firstname, other.firstname) && id == other.id
				&& Objects.equals(lastname, other.lastname) && Objects.equals(password, other.password);
	}
}
