package com.myProject.SpringSalesApp.services;

import com.myProject.SpringSalesApp.entities.Category;
import com.myProject.SpringSalesApp.entities.Category;
import com.myProject.SpringSalesApp.repositories.CategoryRepository;
import com.myProject.SpringSalesApp.services.exceptions.DataIntegrityException;
import com.myProject.SpringSalesApp.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    CategoryRepository repository;

    public List<Category> findAll(){
        return repository.findAll();
    }
    public Category findById(Long id){
        try {
            Optional<Category> category = repository.findById(id);
            return category.get();
        }
        catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(id);
        }
    }
    public Category insert(Category category){
        return repository.save(category);
    }
    public Category updateById(Category newCategory, Long id){
        Category oldCategory = repository.getReferenceById(id);
        updateGeneration(oldCategory,newCategory);
        return repository.save(oldCategory);
    }
    private void updateGeneration(Category oldCategory, Category newCategory){
        oldCategory.setName(newCategory.getName());
    }
    public void deleteById(Long id) {
        try {
            repository.deleteById(id);
        }
        catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(id);
        }
        catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException(e.getMessage());
        }
    }
}
