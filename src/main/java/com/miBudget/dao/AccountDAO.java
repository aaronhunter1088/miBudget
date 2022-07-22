package com.miBudget.dao;

import com.miBudget.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountDAO extends JpaRepository<Account, Long> {
    @Query("SELECT a FROM Account a WHERE a.userId = :userId")
    List<String> findAccountByUserId(@Param("userId") Long userId);
    @Query("SELECT a.accountId FROM Account a WHERE a.userId = :userId")
    List<Long> findAccountIdByUserId(@Param("userId") Long userId);
    @Query("SELECT a FROM Account a WHERE a.itemId = :itemId")
    List<Account> findAccountsByItemId(@Param("itemId") String itemId);
}
