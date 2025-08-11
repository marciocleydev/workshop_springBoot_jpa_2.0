package com.myProject.SpringSalesApp.services;

import com.myProject.SpringSalesApp.entities.Product;
import com.myProject.SpringSalesApp.entities.Product;
import com.myProject.SpringSalesApp.repositories.ProductRepository;
import com.myProject.SpringSalesApp.services.exceptions.DataIntegrityException;
import com.myProject.SpringSalesApp.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    ProductRepository repository;

    public List<Product> findAll(){
        return repository.findAll();
    }
    public Product findById(Long id){
        try {
            Optional<Product> product = repository.findById(id);
            return product.get();
        }
        catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(id);
        }
    }
    public Product insert(Product product){
        return repository.save(product);
    }
    public Product updateById(Product newProduct, Long id){
        Product oldProduct = repository.getReferenceById(id);
        updateGeneration(oldProduct,newProduct);
        return repository.save(oldProduct);
    }
    private void updateGeneration(Product oldProduct, Product newProduct){
        oldProduct.setName(newProduct.getName());
        oldProduct.setDescription(newProduct.getDescription());
        oldProduct.setImgUrl(newProduct.getImgUrl());
        oldProduct.setPrice(newProduct.getPrice());
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
