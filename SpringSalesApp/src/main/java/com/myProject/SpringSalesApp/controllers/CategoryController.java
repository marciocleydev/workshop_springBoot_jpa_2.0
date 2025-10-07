package com.myProject.SpringSalesApp.controllers;

import com.myProject.SpringSalesApp.entities.Category;
import com.myProject.SpringSalesApp.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/categories")
public class CategoryController {

    @Autowired
    private CategoryService service;

    @GetMapping
    public ResponseEntity<List<Category>> findAll(){
        List<Category> Category = service.findAll();
        return ResponseEntity.ok().body(Category);
    }
    @GetMapping(value = "/{id}")
    public ResponseEntity<Category> findById(@PathVariable Long id){
        Category category = service.findById(id);
        return ResponseEntity.ok().body(category);
    }
    @PostMapping
    public ResponseEntity<Category> insert(@RequestBody Category category){
        Category category1 = service.insert(category);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(category1.getId()).toUri();
        return ResponseEntity.created(uri).body(category1);
    }
    @PutMapping(value = "/{id}")
    public ResponseEntity<Category> updateById(@RequestBody Category category,@PathVariable Long id){
        Category category1 = service.updateById(category,id);
        return ResponseEntity.ok().body(category1);
    }
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
