package com.myProject.SpringSalesApp.repositories;

import com.myProject.SpringSalesApp.entities.Category;
import com.myProject.SpringSalesApp.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> { //long é o tipo da chave da entidade
}
