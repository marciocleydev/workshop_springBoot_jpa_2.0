package com.myProject.SpringSalesApp.repositories;

import com.myProject.SpringSalesApp.entities.Category;
import com.myProject.SpringSalesApp.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product,Long> { //long é o tipo da chave da entidade

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Product p SET p.enabled = false WHERE p.id = :id")
    void disableProduct(@Param("id") Long id);

    //nao precisa @Modifying pq é apenas leitura nao envolve ACID
    @Query("select p from Product p WHERE p.name LIKE lower(concat('%',:name,'%') )")
    Page<Product> findProductByName(@Param("name") String name, Pageable pageable);
}
