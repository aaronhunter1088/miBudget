package com.miBudget.daos;

import com.miBudget.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountDAO extends JpaRepository<Account, Long> {
    @Query("SELECT a.id FROM Account a WHERE a.userId = :userId")
    List<Long> findIdByUserId(@Param("userId") Long userId);
    @Query("SELECT a FROM Account a WHERE a.userId = :userId")
    List<Account> findAccountsByUserId(@Param("userId") Long userId);
    @Query("SELECT a.accountId FROM Account a WHERE a.userId = :userId")
    List<String> findAccountIdByUserId(@Param("userId") Long userId);
    @Query("SELECT a FROM Account a WHERE a.id = :id")
    List<Account> findAccountsByItem_Id(@Param("id") Long id);
    @Query("SELECT a FROM Account a WHERE a.itemId = :itemId")
    List<Account> findAccountsByItemId(String itemId);
    @Query("SELECT a FROM Account a WHERE a.accountId = :accountId")
    Account findByAccountId(String accountId);
}
