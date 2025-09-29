package com.myProject.SpringSalesApp.controllers;

import com.myProject.SpringSalesApp.entities.ApiUser;
import com.myProject.SpringSalesApp.services.ApiUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/apiUsers")
public class ApiUserController {

    @Autowired
    private ApiUserService service;

    @GetMapping
    public ResponseEntity<List<ApiUser>> findAll(){
        List<ApiUser> apiUsers = service.findAll();
        return ResponseEntity.ok().body(apiUsers);
    }
    @GetMapping(value = "/{id}")
    public ResponseEntity<ApiUser> findById(@PathVariable Long id){
        ApiUser apiUser = service.findById(id);
        return ResponseEntity.ok().body(apiUser);
    }
   @PostMapping
    public ResponseEntity<ApiUser> insert(@RequestBody ApiUser apiUser){
        ApiUser apiUser1 = service.insert(apiUser);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(apiUser1.getId()).toUri();
        return ResponseEntity.created(uri).body(apiUser1);
   }
   @PutMapping(value = "/{id}")
    public ResponseEntity<ApiUser> updateById(@RequestBody ApiUser apiUser, @PathVariable Long id){
        ApiUser apiUser1 = service.updateById(apiUser,id);
        return ResponseEntity.ok().body(apiUser1);
   }
   @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        service.deleteById(id);
        return ResponseEntity.noContent().build();
   }
}
