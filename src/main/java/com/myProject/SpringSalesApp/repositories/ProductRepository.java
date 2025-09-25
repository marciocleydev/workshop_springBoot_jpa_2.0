package com.myProject.SpringSalesApp.repositories;

import com.myProject.SpringSalesApp.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product,Long> { // Long is the entity's primary key type

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Product p SET p.enabled = false WHERE p.id = :id")
    void disableProduct(@Param("id") Long id);

    // No @Modifying needed as this is a read-only operation
    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Product> findProductByName(@Param("name") String name, Pageable pageable);
}
