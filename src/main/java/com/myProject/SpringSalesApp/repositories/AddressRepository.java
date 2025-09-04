package com.myProject.SpringSalesApp.repositories;

import com.myProject.SpringSalesApp.entities.Address;
import com.myProject.SpringSalesApp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address,Long> { //long é o tipo da chave da entidade
}
