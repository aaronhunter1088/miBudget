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
@Table(name="user_items")
public class UserItemsObject implements Serializable {

	public UserItemsObject() {}
	
	public UserItemsObject(int id, int item__id, int userId) {
		this.id = id;
		this.item__id = item__id;
		this.userId = userId;
	}
	
	public UserItemsObject(int item__id, int userId) {
		this.item__id = item__id;
		this.userId = userId;
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
	int id;
	
	@Column(name="item__id")
	int item__id;
	
	@Column(name="user_id")
	int userId;

	private static final long serialVersionUID = 1L;

}
