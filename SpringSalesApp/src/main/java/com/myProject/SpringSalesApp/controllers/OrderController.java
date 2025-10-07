package com.myProject.SpringSalesApp.controllers;

import com.myProject.SpringSalesApp.entities.Order;
import com.myProject.SpringSalesApp.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/orders")
public class OrderController {

    @Autowired
    private OrderService service;

    @GetMapping
    public ResponseEntity<List<Order>> findAll(){
        List<Order> orders = service.findAll();
        return ResponseEntity.ok().body(orders);
    }
    @GetMapping(value = "/{id}")
    public ResponseEntity<Order> findById(@PathVariable Long id){
        Order order = service.findById(id);
        return ResponseEntity.ok().body(order);
    }
    @PostMapping
    public ResponseEntity<Order> insert(@RequestBody Order order){
        Order order1 = service.insert(order);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(order1.getId()).toUri();
        return ResponseEntity.created(uri).body(order1);
    }
    @PutMapping(value = "/{id}")
    public ResponseEntity<Order> updateById(@RequestBody Order order,@PathVariable Long id){
        Order order1 = service.updateById(order,id);
        return ResponseEntity.ok().body(order1);
    }
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
