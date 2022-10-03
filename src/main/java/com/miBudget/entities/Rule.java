package com.miBudget.entities;

import lombok.Data;

import java.io.Serializable;

import javax.persistence.*;

@Data
@Entity
@Table(name="rules")
public class Rule implements Serializable {

	@Id
	@SequenceGenerator(name="rules_sequence", sequenceName="rules_sequence", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="rules_sequence")
	private int id;
	private double ruleAmount;
	private String ruleName;

	public Rule() {}

}
