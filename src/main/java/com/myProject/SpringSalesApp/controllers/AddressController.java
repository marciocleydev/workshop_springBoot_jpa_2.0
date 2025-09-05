package com.myProject.SpringSalesApp.controllers;

import com.myProject.SpringSalesApp.DTO.AddressDTO;
import com.myProject.SpringSalesApp.services.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/addresses")
public class AddressController implements com.myProject.SpringSalesApp.controllers.docs.AddressControllerDocs {
    @Autowired
    private AddressService service;

    @GetMapping
    @Override
    public ResponseEntity<List<AddressDTO>> findAll() {
        List<AddressDTO> addresses = service.findAll();
        return ResponseEntity.ok().body(addresses);
    }

    @GetMapping(value = "/{id}")
    @Override
    public ResponseEntity<AddressDTO> findById(@PathVariable Long id) {
        AddressDTO address = service.findById(id);
        return ResponseEntity.ok().body(address);
    }

    @PostMapping
    @Override
    public ResponseEntity<AddressDTO> insert(@RequestBody AddressDTO address) {
        AddressDTO address1 = service.insert(address);
        return ResponseEntity.ok().body(address1);
    }

    @PutMapping(value = "/{id}")
    @Override
    public ResponseEntity<AddressDTO> updateById(@RequestBody AddressDTO address, @PathVariable Long id) {
        AddressDTO persistedAddress1 = service.updateById(address, id);
        return ResponseEntity.ok().body(persistedAddress1);
    }

    @DeleteMapping(value = "/{id}")
    @Override
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }


}
