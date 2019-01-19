package com.miBudget.v1.entities;

import org.apache.commons.lang3.StringUtils;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serializable;

import javax.persistence.Column;

@Entity
@Table(name="items")
public class Item implements Serializable {

	public Item() {}
	
	public Item(String itemId, String accessToken) {
		this.itemId = itemId;
		this.accessToken = accessToken;
	}
	
	public Item(int itemTableId, String itemId, String accessToken) {
		this.itemTableId = itemTableId;
		this.itemId = itemId;
		this.accessToken = accessToken;
	}
	
	public Item(String itemId, String accessToken, String institutionId) {
		if (StringUtils.isNotEmpty(accessToken) && StringUtils.isNotEmpty(itemId) && StringUtils.isNotEmpty(institutionId)) {
			this.itemId = itemId;
			this.accessToken = accessToken;
			this.institutionId = institutionId;
		} else {
			throw new NullPointerException("itemId: " + itemId + "\naccessToken: " + accessToken + "\ninstituionId: " + institutionId);
			// Fail here
		}
		
	}
	
	public Item(int itemTableId, String itemId, String accessToken, String institutionId) {
		this.itemTableId = itemTableId;
		this.institutionId = institutionId;
		this.itemId = itemId;
		this.accessToken = accessToken;
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="item_table_id")
	public int itemTableId;
	
	@Column(name="institution_id")
	public String institutionId;
	
	@Column(name="item_id")
	public String itemId;
	
	@Column(name="access_token")
	public String accessToken;

	public int getItemTableId() {
		return itemTableId;
	}

	public void setItemTableId(int itemTableId) {
		this.itemTableId = itemTableId;
	}
	
	public String getInstitutionId() {
		return institutionId;
	}
	
	public void setInstitutionId(String institutionId) {
		this.institutionId = institutionId;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		return "Item [itemTableId=" + itemTableId + ", institutionId=" + institutionId + ", itemId=" + itemId
				+ ", accessToken=" + accessToken + "]";
	}
	
}

/**
 * We are leaving out request_id because this is a session related object, not necessarily 
 * related to the Item we just created, just when the Item was created. Therefore, it is 
 * left out of the creation of this Item.
 */
