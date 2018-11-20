package com.v1.miBudget.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="users_accounts")
public class UserAccountObject implements Serializable {
	
	public UserAccountObject() {}
	
	public UserAccountObject(int user_id, String account_id, int itemTableId, double availableBalance, double currentBalance,
			double limit, String currencyCode, String nameOfAcct, String officialNameOfAcct,
			String mask, String type, String subtype) {
		this.user_id = user_id;
		this.account_id = account_id;
		this.itemTableId = itemTableId;
		this.availableBalance = availableBalance;
		this.currentBalance = currentBalance;
		this.limit = limit;
		this.currencyCode = currencyCode;
		this.nameOfAccount = nameOfAcct;
		this.officialName = officialNameOfAcct;
		this.mask = mask;
		this.type = type;
		this.subType = subtype;
	}
	
	/**
	 * Default generated
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
	private int id;
	
	@Column(name="user_id")
	private int user_id;
	
	@Column(name="account_id")
	private String account_id;
	
	@Column(name="available_balance", precision=10, scale=2)
	private double availableBalance;
	
	@Column(name="iso_currency_code")
	private String currencyCode;
	
	@Column(name="current_balance", precision=10, scale=2)
	private double currentBalance;
	
	@Column(name="_limit", precision=10, scale=2)
	private double limit;
	
	@Column(name="item_table_id")
	private int itemTableId;
	
	@Column(name="mask")
	private String mask;
	
	@Column(name="account_name")
	private String nameOfAccount;
	
	@Column(name="account_official_name")
	private String officialName;
	
	@Column(name="_type")
	private String type;
	
	@Column(name="subtype")
	private String subType;
	
	

	/**
	 * @return the user_id
	 */
	public int getUser_id() {
		return user_id;
	}

	/**
	 * @param user_id the user_id to set
	 */
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	/**
	 * @return the account_id
	 */
	public String getAccount_id() {
		return account_id;
	}

	/**
	 * @param account_id the account_id to set
	 */
	public void setAccount_id(String account_id) {
		this.account_id = account_id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getAvailableBalance() {
		return availableBalance;
	}

	public void setAvailableBalance(double availableBalance) {
		this.availableBalance = availableBalance;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public double getCurrentBalance() {
		return currentBalance;
	}

	public void setCurrentBalance(double currentBalance) {
		this.currentBalance = currentBalance;
	}

	public int getItemTableId() {
		return itemTableId;
	}

	public void setItemTableId(int itemTableId) {
		this.itemTableId = itemTableId;
	}

	public String getMask() {
		return mask;
	}

	public void setMask(String mask) {
		this.mask = mask;
	}

	public String getNameOfAccount() {
		return nameOfAccount;
	}

	public void setNameOfAccount(String nameOfAccount) {
		this.nameOfAccount = nameOfAccount;
	}

	public String getOfficialName() {
		return officialName;
	}

	public void setOfficialName(String officialName) {
		this.officialName = officialName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSubType() {
		return subType;
	}

	public void setSubType(String subType) {
		this.subType = subType;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
