package com.myProject.SpringSalesApp.repositories;

import com.myProject.SpringSalesApp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> { //long é o tipo da chave da entidade
}
