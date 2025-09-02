package com.myProject.SpringSalesApp.services;

import com.myProject.SpringSalesApp.DTO.ProductDTO;
import com.myProject.SpringSalesApp.controllers.ProductResource;
import com.myProject.SpringSalesApp.entities.Product;
import com.myProject.SpringSalesApp.mapper.ProductMapper;
import com.myProject.SpringSalesApp.repositories.ProductRepository;
import com.myProject.SpringSalesApp.services.exceptions.DataIntegrityException;
import com.myProject.SpringSalesApp.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class ProductService {
    @Autowired
    ProductRepository repository;
    @Autowired
    ProductMapper productMapper;
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    public List<ProductDTO> findAll(){
        var dtoList = productMapper.toDtoList(repository.findAll());
        dtoList.forEach(this::addHateoasLinks);
        return dtoList;
    }
    public ProductDTO findById(Long id) {
        logger.info("Finding one Product!");
        var dto = productMapper.toDTO(repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id)));
        addHateoasLinks(dto);
        return dto;
    }

    public ProductDTO insert(ProductDTO product){
        logger.info("Creating a new product!");
        var dto = productMapper.toDTO(repository.save(productMapper.toEntity(product)));
        addHateoasLinks(dto);
        return dto;
    }

    public ProductDTO updateById(ProductDTO newProductDTO, Long id){
        logger.info("Updating one product!");
        try {
            Product oldProduct = repository.getReferenceById(id);
            updateGeneration(oldProduct, newProductDTO);
            var dto = productMapper.toDTO(repository.save(oldProduct));
            addHateoasLinks(dto);
            return dto;
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(id);
        }
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
    private void addHateoasLinks(ProductDTO dto) {
        dto.add(linkTo(methodOn(ProductResource.class).findById(dto.getId())).withSelfRel().withType("GET"));
        dto.add(linkTo(methodOn(ProductResource.class).deleteById(dto.getId())).withRel("delete").withType("DELETE"));
        dto.add(linkTo(methodOn(ProductResource.class).findAll()).withRel("findAll").withType("GET"));
        dto.add(linkTo(methodOn(ProductResource.class).insert(dto)).withRel("create").withType("POST"));
        dto.add(linkTo(methodOn(ProductResource.class).updateById(dto,dto.getId())).withRel("update").withType("PUT"));
    }
}
