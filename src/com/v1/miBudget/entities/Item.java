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
			this.institution_id = institutionId;
			this.access_token = accessToken;
			this.item_id = itemID;
		} else {
			throw new NullPointerException("item_id: " + itemID + "\naccess_token: " + accessToken);
			// Fail here
		}
		
	}
	
	public Item(int id, String item_id, String access_token, String institution_id) {
		this.id = id;
		this.institution_id = institution_id;
		this.item_id = item_id;
		this.access_token = access_token;
	}
	
	private static final long serialVersionUID = -8735910955797101041L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="item_table_id")
	public int id;
	
	@Column(name="institution_id")
	public String institution_id;
	
	@Column(name="item_id")
	public String item_id;
	
	@Column(name="access_token")
	public String access_token;

	public int getItemTableId() {
		return id;
	}

	public void setItemTableId(int id) {
		this.id = id;
	}
	
	public String getInsitutionId() {
		return institution_id;
	}
	
	public void setInsitutionId(String id) {
		this.institution_id = id;
	}

	public String getItemId() {
		return item_id;
	}

	public void setItemId(String item_id) {
		this.item_id = item_id;
	}

	public String getAccessToken() {
		return access_token;
	}

	public void setAccessToken(String access_token) {
		this.access_token = access_token;
	}

	@Override
	public String toString() {
		return "Item [id=" + id + ", institution_id=" + institution_id + ", item_id=" + item_id + ", access_token="
				+ access_token + "]";
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
