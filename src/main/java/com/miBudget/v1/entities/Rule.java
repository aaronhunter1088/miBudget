package com.miBudget.v1.entities;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

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

	private static long getSerialversionuid() {
		return serialVersionUID;
	}

	public int getId() {
		return id;
	}

	public double getAmount() {
		return amount;
	}

	public String getNames() {
		return names;
	}

	private void setId(int id) {
		this.id = id;
	}

	private void setAmount(double amount) {
		this.amount = amount;
	}

	private void setNames(String names) {
		this.names = names;
	}

	@Override
	public String toString() {
		return "Rule [id=" + id + ", amount=" + amount + ", names=" + names + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(amount, id, names);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rule other = (Rule) obj;
		return Double.doubleToLongBits(amount) == Double.doubleToLongBits(other.amount) && id == other.id
				&& Objects.equals(names, other.names);
	}

}
