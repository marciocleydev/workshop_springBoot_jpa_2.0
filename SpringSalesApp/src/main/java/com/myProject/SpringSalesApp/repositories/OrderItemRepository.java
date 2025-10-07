package com.myProject.SpringSalesApp.repositories;

import com.myProject.SpringSalesApp.entities.OrderItem;
import com.myProject.SpringSalesApp.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem,Long> { //long é o tipo da chave da entidade
}
