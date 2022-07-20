package com.miBudget.entities;

import lombok.Data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name="user_accounts")
public class UserAccountObject implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private Long userId;
	private String accountId;
	@Column(name="availableBalance", precision=10, scale=2)
	private double availableBalance;
	private String currencyCode;
	@Column(name="currentBalance", precision=10, scale=2)
	private double currentBalance;
	@Column(name="_limit", precision=10, scale=2)
	private double _limit;
	private Long itemId;
	private String accountName;
	private String officialName;
	private String mask;
	private String _type;
	private String subType;

	public UserAccountObject() {}
	
	public UserAccountObject(Long userId, String accountId, long itemId, double availableBalance, double currentBalance,
							 double _limit, String currencyCode, String nameOfAcct, String officialNameOfAcct,
							 String mask, String _type, String subtype) {
		this.userId = userId;
		this.accountId = accountId;
		this.itemId = itemId;
		this.availableBalance = availableBalance;
		this.currentBalance = currentBalance;
		this._limit = _limit;
		this.currencyCode = currencyCode;
		this.accountName = nameOfAcct;
		this.officialName = officialNameOfAcct;
		this.mask = mask;
		this._type = _type;
		this.subType = subtype;
	}
}
