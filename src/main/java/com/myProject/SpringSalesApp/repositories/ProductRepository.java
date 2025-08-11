package com.myProject.SpringSalesApp.repositories;

import com.myProject.SpringSalesApp.entities.Category;
import com.myProject.SpringSalesApp.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long> { //long é o tipo da chave da entidade
}
