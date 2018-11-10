package com.v1.miBudget.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.v1.miBudget.daoimplementations.AccountDAOImpl;
import com.v1.miBudget.daoimplementations.MiBudgetDAOImpl;


@Entity
@Table(name="users")
public class User implements Serializable {
	
	private static final long serialVersionUID = -6056201749901818847L;
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
	private List<String> account_ids; // will become budget_ids ...
	
	public User(int id, String firstname, String lastname, String cellphone, String password, String email, List<String> accountIds) {
		this.id = id;
		this.firstname = firstname;
		this.lastname = lastname;
		this.cellphone = cellphone;
		this.password = password;
		this.email = email;
		this.account_ids = accountIds;
	}
	
	/**
	 * To create a user, please provide in the following order:
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
		this.account_ids = createAccounts();
	}
	
	/**
	 * To create a user, please provide in the following order:
	 * @param firstname
	 * @param lastname
	 * @param cellphone
	 * @param password
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
	 * If you need to validate a user, you can use this constructor.
	 * @param cellphone
	 * @param password
	 */
	public User(String cellphone, String password) {
		this.cellphone = cellphone;
		this.password = password;
		createAccounts();
	}
	
	public User() {}

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
	
	
	
	public static List<String> getAccountIds(User user) {
		System.out.println("Getting account_ids from user...");
		// Get list of item_ids from user in DB
//		List<String> idsFromUserInDB = MiBudgetDAOImpl.getAllItemIdsFromUser(this);
		List<String> accountIdsFromUserInDB = AccountDAOImpl.getAccountIdsFromUser(user);
		return accountIdsFromUserInDB;
		
	}
	
	public List<String> getAccountIds() {
		return this.account_ids;
	}
	
	/**
	 * Sets the item for the user
	 * itemSepcifics is : access token, item id, request id
	 * @param itemSpecifics
	 */
	public void setAccountIds(List<String> account_ids) {
		if (account_ids.size() == 0) {
			createAccounts();
		}
		this.account_ids = account_ids;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJsonArray() {
		JSONObject userJsonObj = new JSONObject();
		userJsonObj.put("id", id);
		userJsonObj.put("firstname", firstname);
		userJsonObj.put("lastname", lastname);
		userJsonObj.put("cellphone", cellphone);
		userJsonObj.put("email", email);
		userJsonObj.put("password", password);
		userJsonObj.put("accountIds", account_ids);
		//System.out.println(userJsonObj);
		return userJsonObj;
//		return "User [id=" + id + ", firstname=" + firstname + ", lastname=" + lastname + ", cellphone=" + cellphone
//				+ ", password=" + password + ", account_ids=" + account_ids + "]";
	}
	
	
	
	public List<String> createAccounts() {
		System.out.println("account_ids is null");
		account_ids = new ArrayList<String>();
		System.out.println("AccountsList has been created for " + this.firstname);
		return account_ids;
		
	}
}
