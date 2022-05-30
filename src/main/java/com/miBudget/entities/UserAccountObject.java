package com.miBudget.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="usersaccounts")
public class UserAccountObject implements Serializable {
	
	public UserAccountObject() {}
	
	public UserAccountObject(int userId, String accountId, int itemTableId, double availableBalance, double currentBalance,
			double limit, String currencyCode, String nameOfAcct, String officialNameOfAcct,
			String mask, String type, String subtype) {
		this.userId = userId;
		this.accountId = accountId;
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
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="usersaccountsid")
	private int usersaccountsid;
	
	@Column(name="userid")
	private int userId;
	
	@Column(name="accountid")
	private String accountId;
	
	@Column(name="availablebalance", precision=10, scale=2)
	private double availableBalance;
	
	@Column(name="currencycode")
	private String currencyCode;
	
	@Column(name="currentbalance", precision=10, scale=2)
	private double currentBalance;
	
	@Column(name="_limit", precision=10, scale=2)
	private double limit;
	
	@Column(name="itemtableid")
	private int itemTableId;
	
	@Column(name="mask")
	private String mask;
	
	@Column(name="nameofaccount")
	private String nameOfAccount;
	
	@Column(name="officialname")
	private String officialName;
	
	@Column(name="_type")
	private String type;
	
	@Column(name="subtype")
	private String subType;
	
	/**
	 * Default generated
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * @return the user_id
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * @param userId the user_id to set
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}

	/**
	 * @return the account_id
	 */
	public String getAccountId() {
		return accountId;
	}

	/**
	 * @param accountId the account_id to set
	 */
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public int getId() {
		return usersaccountsid;
	}

	public void setId(int usersaccountsid) {
		this.usersaccountsid = usersaccountsid;
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

	@Override
	public String toString() {
		return "UserAccountObject{" +
				"usersaccountsid=" + usersaccountsid +
				", userId=" + userId +
				", accountId='" + accountId + '\'' +
				", availableBalance=" + availableBalance +
				", currencyCode='" + currencyCode + '\'' +
				", currentBalance=" + currentBalance +
				", limit=" + limit +
				", itemTableId=" + itemTableId +
				", mask='" + mask + '\'' +
				", nameOfAccount='" + nameOfAccount + '\'' +
				", officialName='" + officialName + '\'' +
				", type='" + type + '\'' +
				", subType='" + subType + '\'' +
				'}';
	}
}
