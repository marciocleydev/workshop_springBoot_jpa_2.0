package com.myProject.SpringSalesApp.repositories;

import com.myProject.SpringSalesApp.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> { //long é o tipo da chave da entidade
}
