package com.myProject.SpringSalesApp.services;

import com.myProject.SpringSalesApp.entities.Product;
import com.myProject.SpringSalesApp.repositories.ProductRepository;
import com.myProject.SpringSalesApp.services.exceptions.DataIntegrityException;
import com.myProject.SpringSalesApp.services.exceptions.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProductService {
    @Autowired
    ProductRepository repository;
    private Logger logger = LoggerFactory.getLogger(ProductService.class.getName());

    public List<Product> findAll(){
        return repository.findAll();
    }
    public Product findById(Long id) {
        logger.info("Finding one Product!");
        logger.debug("DEBUG MODE: tentando buscar produto com id {}", id);
        return  repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public Product insert(Product product){
        logger.info("Creating a new product!");
        return repository.save(product);
    }
    public Product updateById(Product newProduct, Long id){
        logger.info("Updating one product!");
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
        logger.info("Deleting one product!");
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
