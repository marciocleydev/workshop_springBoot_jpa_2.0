package com.myProject.SpringSalesApp.repositories;

import com.myProject.SpringSalesApp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    @Query("select u from User u where u.userName =:userName")
    User findByUsername(@Param("userName") String userName);
}
