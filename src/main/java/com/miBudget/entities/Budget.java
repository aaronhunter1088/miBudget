package com.miBudget.entities;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Note at the bottom
 */

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
    private Long userId; // the id of the user
    @Transient
    private List<Category> categories;
    @Transient
    private List<Long> childBudgetIds;

    public Budget() { this(1L); }
    // Parent Budget
    public Budget(Long userId) {
        this.userId = userId; // tie to a user
        name = "Primary Budget";
        categories = createDefaultCategories();
        amount = BigDecimal.valueOf(categories.stream().mapToDouble(Category::getBudgetedAmt).sum());
        childBudgetIds = new ArrayList<>();
    }
    // Child Budget: Created by parent when parent is created. Child Budgets cannot have children
    public Budget(Budget budget) {
        this.userId = budget.getUserId();
        this.name = budget.getName();
        this.categories = budget.getCategories();
        this.amount = BigDecimal.valueOf(categories.stream().mapToDouble(Category::getBudgetedAmt).sum());
        this.childBudgetIds = null;
    }
    // Child Budget: Created by user at will.
    public Budget(Budget budget, String name, List<Category> categories) {
        this.name = name;
        this.categories = categories;
        this.amount = BigDecimal.valueOf(categories.stream().mapToDouble(Category::getBudgetedAmt).sum());
        this.childBudgetIds = null;
    }

    private List<Category> createDefaultCategories() {
        ArrayList<Category> categoriesList = new ArrayList<>();
        Category morgtageCat = new Category("Mortgage", "USD", 1500.00);
        categoriesList.add(morgtageCat);
        Category utilitiesCat = new Category("Utilities", "USD", 500.00);
        categoriesList.add(utilitiesCat);
        Category transportCat = new Category("Transportation", "USD", 1000.00);
        categoriesList.add(transportCat);
        Category insuranceCat = new Category("Insurance", "USD", 200.00);
        categoriesList.add(insuranceCat);
        Category foodCat = new Category("Food", "USD", 500.00);
        categoriesList.add(foodCat);
        Category subscriptionsCat = new Category("Subscriptions", "USD", 500.00);
        categoriesList.add(subscriptionsCat);
        Category billsCat = new Category("Bill", "USD", 1000.00);
        categoriesList.add(billsCat);
        return categoriesList;
    }
}

/**
 * Note:
 *
 * A budget can be one or more Budgets. Let me explain.
 *
 * We may all be used to a normal day-to-day budget.
 * Mortgage: x
 * Utilities: x
 * Car: x
 * ...
 *
 * Let's say you want to go on vacation, and you have a "budget"
 * of how much you need to save, so you can keep track of what
 * is spent against your budget.
 *
 * The user should be able to create a "sub" budget, which will
 * still be a part of their main budget. You should be able to
 * view any Child budget individually, and see the grand scope
 * of everything at a Main level.
 */