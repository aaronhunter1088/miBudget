package com.miBudget.dao;

import com.miBudget.entities.Transaction;
import com.miBudget.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;

@Repository
public interface TransactionDAO extends JpaRepository<Transaction, Long> {

}
