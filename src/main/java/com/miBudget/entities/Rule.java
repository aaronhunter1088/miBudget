package com.miBudget.entities;

import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name="rules")
public class Rule implements Serializable {

	public Rule() {}
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id", updatable=false, nullable=false)
	private int id;
	
	@Column(name="amount")
	private double amount;
	
	@Column(name="names")
	private String names;

}
