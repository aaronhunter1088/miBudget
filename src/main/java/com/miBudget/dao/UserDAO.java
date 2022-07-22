package com.miBudget.dao;

import com.miBudget.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public interface UserDAO extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.firstName = :firstName")
    public User findUserByFirstName(@Param("firstName") String firstName);
    @Query("SELECT u FROM User u WHERE u.lastName = :lastName")
    public List<User> findUserByLastName(@Param("lastName") String lastName);
    @Query("SELECT cellphone FROM User")
    List<String> findAllCellphones();
    @Query("SELECT u FROM User u WHERE u.cellphone = :cellphone")
    User findUserByCellphone(String cellphone);
}
