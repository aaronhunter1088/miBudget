package com.v1.miBudget.entities;

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
	
	public Item(String itemID, String accessToken, String institutionId) {
		if (StringUtils.isNotEmpty(accessToken) && StringUtils.isNotEmpty(itemID)) {
			this.institutionId = institutionId;
			this.accessToken = accessToken;
			this.itemId = itemID;
		} else {
			throw new NullPointerException("item_id: " + itemID + "\naccess_token: " + accessToken);
			// Fail here
		}
		
	}
	
	public Item(int itemTableId, String itemId, String accessToken, String institutionId) {
		this.itemTableId = itemTableId;
		this.institutionId = institutionId;
		this.itemId = itemId;
		this.accessToken = accessToken;
	}
	
	private static final long serialVersionUID = -8735910955797101041L;
	
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
	
	public String getInsitutionId() {
		return institutionId;
	}
	
	public void setInsitutionId(String institutionId) {
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

	public void setAccessToken(String access_Tken) {
		this.accessToken = accessToken;
	}

	

	
}

/**
 * Example
 * {
  "access_token": "access-sandbox-de3ce8ef-33f8-452c-a685-8671031fc0f6",
  "item_id": "M5eVJqLnv3tbzdngLDp9FL5OlDNxlNhlE55op"
}

We are leaving out request_id because this is a session related object, not necessarily 
related to the Item we just created, just when the Item was created. Therefore, it is 
left out of the creation of this Item.
 * 
 */
