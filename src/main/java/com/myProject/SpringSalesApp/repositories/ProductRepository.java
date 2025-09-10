package com.myProject.SpringSalesApp.repositories;

import com.myProject.SpringSalesApp.entities.Category;
import com.myProject.SpringSalesApp.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product,Long> { //long é o tipo da chave da entidade


    @Modifying
    @Query("UPDATE Product p SET p.enabled = false WHERE p.id = :id")
    void disableProduct(@Param("id") Long id);
}
