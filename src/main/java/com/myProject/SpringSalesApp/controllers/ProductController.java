package com.myProject.SpringSalesApp.controllers;

import com.myProject.SpringSalesApp.DTO.ProductDTO;
import com.myProject.SpringSalesApp.controllers.docs.ProductResourceDocs;
import com.myProject.SpringSalesApp.services.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/products")
@Tag(name = "Product Resource", description = "Endpoints for product management")
public class ProductController implements ProductResourceDocs {

    @Autowired
    private ProductService service;

    @GetMapping
    @Override
    public ResponseEntity<List<ProductDTO>> findAll(){
        List<ProductDTO> products = service.findAll();
        return ResponseEntity.ok().body(products);
    }

    @GetMapping(value = "/{id}")
    @Override
    public ResponseEntity<ProductDTO> findById(@PathVariable Long id){
        ProductDTO product = service.findById(id);
        return ResponseEntity.ok().body(product);
    }

    @PostMapping
    @Override
    public ResponseEntity<ProductDTO> insert(@RequestBody ProductDTO product){
        ProductDTO product1 = service.insert(product);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(product1.getId()).toUri();
        return ResponseEntity.created(uri).body(product1);
    }

    @PutMapping(value = "/{id}")
    @Override
    public ResponseEntity<ProductDTO> updateById(@RequestBody ProductDTO product, @PathVariable Long id){
        ProductDTO product1 = service.updateById(product,id);
        return ResponseEntity.ok().body(product1);
    }

    @DeleteMapping(value = "/{id}")
    @Override
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
