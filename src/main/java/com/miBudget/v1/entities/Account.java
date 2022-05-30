package com.miBudget.v1.entities;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="accounts")
public class Account implements Serializable {
	
	public Account() {}
	
	public Account(String accountId, int itemTableId, double availableBalance, double currentBalance,
			double limit, String currencyCode, String nameOfAcct, String officialNameOfAcct,
			String mask, String type, String subtype) {
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
	@Column(name="accountid")
	private String accountId;
	
	@Column(name="itemtableid")
	private int itemTableId;
	
	@Column(name="availablebalance", precision=10, scale=2)
	private double availableBalance;
	
	@Column(name="currentbalance", precision=10, scale=2)
	private double currentBalance;
	
	@Column(name="_limit", precision=10, scale=2)
	private double limit;
	
	@Column(name="isocurrencycode")
	private String currencyCode;
	
	@Column(name="nameofaccount")
	private String nameOfAccount;
	
	@Column(name="officialname")
	private String officialName;
	
	@Column(name="mask")
	private String mask;
	
	@Column(name="_type")
	private String type;
	
	@Column(name="subtype")
	private String subType;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		return "Account [accountId=" + accountId + ", itemTableId=" + itemTableId + ", availableBalance=" + availableBalance
				+ ", currentBalance=" + currentBalance + ", limit=" + limit + ", currencyCode=" + currencyCode
				+ ", nameOfAccount=" + nameOfAccount + ", officialName=" + officialName + ", mask=" + mask + ", type="
				+ type + ", subType=" + subType + "]";
	}
	
	

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hash(accountId, availableBalance, currencyCode, currentBalance, itemTableId, limit, mask,
				nameOfAccount, officialName, subType, type);
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
		Account other = (Account) obj;
		return Objects.equals(accountId, other.accountId)
				&& Double.doubleToLongBits(availableBalance) == Double.doubleToLongBits(other.availableBalance)
				&& Objects.equals(currencyCode, other.currencyCode)
				&& Double.doubleToLongBits(currentBalance) == Double.doubleToLongBits(other.currentBalance)
				&& itemTableId == other.itemTableId
				&& Double.doubleToLongBits(limit) == Double.doubleToLongBits(other.limit)
				&& Objects.equals(mask, other.mask) && Objects.equals(nameOfAccount, other.nameOfAccount)
				&& Objects.equals(officialName, other.officialName) && Objects.equals(subType, other.subType)
				&& Objects.equals(type, other.type);
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

	/**
	 * @return the item_id
	 */
	public int getItemTableId() {
		return itemTableId;
	}

	/**
	 * @param itemTableId the item to set
	 */
	public void setItemTableId(int itemTableId) {
		this.itemTableId = itemTableId;
	}

	/**
	 * @return the availableBalance
	 */
	public double getAvailableBalance() {
		return availableBalance;
	}

	/**
	 * @param availableBalance the availableBalance to set
	 */
	public void setAvailableBalance(double availableBalance) {
		
		this.availableBalance = availableBalance;
	}

	/**
	 * @return the currentBalance
	 */
	public double getCurrentBalance() {
		return currentBalance;
	}

	/**
	 * @param currentBalance the currentBalance to set
	 */
	public void setCurrentBalance(double currentBalance) {
		this.currentBalance = currentBalance;
	}

	/**
	 * @return the limit
	 */
	public double getLimit() {
		return limit;
	}

	/**
	 * @param limit the limit to set
	 */
	public void setLimit(double limit) {
		this.limit = limit;
	}

	/**
	 * @return the currencyCode
	 */
	public String getCurrencyCode() {
		return currencyCode;
	}

	/**
	 * @param currencyCode the currencyCode to set
	 */
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	/**
	 * @return the nameOfAccount
	 */
	public String getNameOfAccount() {
		return nameOfAccount;
	}

	/**
	 * @param nameOfAccount the nameOfAccount to set
	 */
	public void setNameOfAccount(String nameOfAccount) {
		this.nameOfAccount = nameOfAccount;
	}

	/**
	 * @return the officialName
	 */
	public String getOfficialName() {
		return officialName;
	}

	/**
	 * @param officialName the officialName to set
	 */
	public void setOfficialName(String officialName) {
		this.officialName = officialName;
	}

	/**
	 * @return the mask
	 */
	public String getMask() {
		return mask;
	}

	/**
	 * @param mask the mask to set
	 */
	public void setMask(String mask) {
		this.mask = mask;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the subType
	 */
	public String getSubType() {
		return subType;
	}

	/**
	 * @param subType the subType to set
	 */
	public void setSubType(String subType) {
		this.subType = subType;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
