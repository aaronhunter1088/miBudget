package com.miBudget.daos;

import com.miBudget.entities.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemDAO extends JpaRepository<Item, Long> {
    @Query("SELECT i FROM Item i WHERE i.userId = :userId")
    List<Item> findItemByUserId(@Param("userId") Long userId);
    @Query("SELECT i.itemId FROM Item i WHERE i.userId = :userId")
    List<String> findItemIdByUserId(Long userId);
    @Query("SELECT i.institutionId FROM Item i WHERE i.userId = :userId")
    List<String> getInstitutionIdsFromUser(Long userId);
    @Query("SELECT i.accessToken FROM Item i WHERE i.institutionId = :institutionId")
    String getAccessToken(@Param("institutionId") String institutionId);
    @Query("SELECT i.id FROM Item i WHERE i.userId = :userId")
    List<Long> findIdsByUserId(Long userId);
    @Query("SELECT i.id FROM Item i WHERE i.itemId = :itemId")
    Long findIdByItemId(String itemId);
    @Query("SELECT i FROM Item i WHERE i.institutionId = :institutionId")
    Item findItemByInstitutionId(String institutionId);
    @Query("SELECT i FROM Item i WHERE i.itemId = :itemId")
    Item findItemByItemId(String itemId);


    // DELETE
    @Modifying
    @Query("DELETE FROM Item i WHERE i.itemId = :itemId")
    Item deleteByItemId(String itemId);
}
