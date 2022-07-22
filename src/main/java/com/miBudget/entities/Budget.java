package com.miBudget.entities;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "budgets")
public class Budget {

    @Id
    @SequenceGenerator(name="budgets_sequence", sequenceName="budgets_sequence", allocationSize = 1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="budgets_sequence")
    private Long id;
    private String name;
    private BigDecimal amount;
    private Long userId;


}
