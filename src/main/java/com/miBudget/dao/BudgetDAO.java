package com.miBudget.dao;

import com.miBudget.entities.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BudgetDAO extends JpaRepository<Budget, Long> {
    @Query("SELECT b FROM Budget b WHERE userId = :userId")
    Budget findBudgetByUserId(Long userId);
}
