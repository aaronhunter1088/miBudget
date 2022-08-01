package com.miBudget.daos;

import com.miBudget.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface CategoryDAO extends JpaRepository<Category, Long> {
    @Query("SELECT c FROM Category c WHERE c.budgetId = :budgetId")
    List<Category> findAllByBudgetId(Long budgetId);
    @Query("SELECT c FROM Category c WHERE c.userId = :userId")
    List<Category> findAllByUserId(Long userId);
}
