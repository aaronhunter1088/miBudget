package com.miBudget.dao;

import com.miBudget.entities.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BudgetDAO extends JpaRepository<Budget, Long> {
    @Query("SELECT b FROM Budget b WHERE userId = :userId")
    Budget findBudgetByUserId(Long userId);
    @Query("SELECT b FROM Budget b WHERE id = :id")
    List<Budget> findBudgetById(Long id);
    @Query("SELECT b FROM Budget b WHERE id = :mainBudgetId")
    Budget findBudgetByMainBudgetId(Long mainBudgetId);
}
