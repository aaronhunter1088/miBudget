package com.miBudget.dao;

import com.miBudget.entities.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemDAO extends JpaRepository<Item, Long> {
    @Query("SELECT i from Item i where i.userId = :userId")
    List<Item> findItemByUserId(@Param("userId") Long userId);
}
