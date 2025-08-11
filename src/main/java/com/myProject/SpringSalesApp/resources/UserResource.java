package com.myProject.SpringSalesApp.resources;

import com.myProject.SpringSalesApp.entities.User;
import com.myProject.SpringSalesApp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/users")
public class UserResource {

    @Autowired
    private UserService service;

    @GetMapping
    public ResponseEntity<List<User>> findAll(){
        List<User> users = service.findAll();
        return ResponseEntity.ok().body(users);
    }
    @GetMapping(value = "/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id){
        User user = service.findById(id);
        return ResponseEntity.ok().body(user);
    }
   @PostMapping
    public ResponseEntity<User> insert(@RequestBody User user){
        User user1 = service.insert(user);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user1.getId()).toUri();
        return ResponseEntity.created(uri).body(user1);
   }
   @PutMapping(value = "/{id}")
    public ResponseEntity<User> updateById(@RequestBody User user,@PathVariable Long id){
        User user1 = service.updateById(user,id);
        return ResponseEntity.ok().body(user1);
   }
   @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        service.deleteById(id);
        return ResponseEntity.noContent().build();
   }
}
