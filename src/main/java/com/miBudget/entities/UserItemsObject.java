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

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Long id;
	long item__id;
	long userId;

	public UserItemsObject() {}
	
	public UserItemsObject(Long id, Long item__id, Long userId) {
		this.id = id;
		this.item__id = item__id;
		this.userId = userId;
	}
	
	public UserItemsObject(Long item__id, Long userId) {
		this.item__id = item__id;
		this.userId = userId;
	}
}
