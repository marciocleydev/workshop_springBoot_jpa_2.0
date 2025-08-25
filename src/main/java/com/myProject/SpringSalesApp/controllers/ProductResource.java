package com.myProject.SpringSalesApp.controllers;

import com.myProject.SpringSalesApp.entities.Product;
import com.myProject.SpringSalesApp.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/products")
public class ProductResource {

    @Autowired
    private ProductService service;

    @GetMapping
    public ResponseEntity<List<Product>> findAll(){
        List<Product> products = service.findAll();
        return ResponseEntity.ok().body(products);
    }
    @GetMapping(value = "/{id}")
    public ResponseEntity<Product> findById(@PathVariable Long id){
        Product product = service.findById(id);
        return ResponseEntity.ok().body(product);
    }
    @PostMapping
    public ResponseEntity<Product> insert(@RequestBody Product product){
        Product product1 = service.insert(product);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(product1.getId()).toUri();
        return ResponseEntity.created(uri).body(product1);
    }
    @PutMapping(value = "/{id}")
    public ResponseEntity<Product> updateById(@RequestBody Product product,@PathVariable Long id){
        Product product1 = service.updateById(product,id);
        return ResponseEntity.ok().body(product1);
    }
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
