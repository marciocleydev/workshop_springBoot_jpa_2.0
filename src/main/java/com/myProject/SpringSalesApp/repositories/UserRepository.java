package com.myProject.SpringSalesApp.repositories;

import com.myProject.SpringSalesApp.entities.ApiUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<ApiUser,Long> { //long é o tipo da chave da entidade
}
