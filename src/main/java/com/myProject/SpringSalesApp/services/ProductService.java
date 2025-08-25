package com.myProject.SpringSalesApp.services;

import com.myProject.SpringSalesApp.DTO.ProductDTO;
import com.myProject.SpringSalesApp.entities.Product;
import static com.myProject.SpringSalesApp.mapper.ObjectMapper.parseObject;
import static com.myProject.SpringSalesApp.mapper.ObjectMapper.parseListObjects;
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

    public List<ProductDTO> findAll(){
        return parseListObjects(repository.findAll(),ProductDTO.class); //no import lá em cima eu coloquei static para importar apenas o método da minha classe ObjectMapper.
    }
    public ProductDTO findById(Long id) {
        logger.info("Finding one Product!");
        return  parseObject(repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id)),ProductDTO.class);
    }

    public ProductDTO insert(ProductDTO product){
        logger.info("Creating a new product!");
        return parseObject(repository.save(parseObject(product,Product.class)),ProductDTO.class);
    }
    public ProductDTO updateById(ProductDTO newProductDTO, Long id){
        logger.info("Updating one product!");
        Product oldProduct = repository.getReferenceById(id);
        updateGeneration(oldProduct,newProductDTO);
        return parseObject(repository.save(parseObject(oldProduct,Product.class)),ProductDTO.class);
    }
    private void updateGeneration(Product oldProduct, ProductDTO newProduct){
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
