package com.myProject.SpringSalesApp.services;

import com.myProject.SpringSalesApp.DTO.AddressDTO;
import com.myProject.SpringSalesApp.controllers.AddressController;
import com.myProject.SpringSalesApp.entities.Address;
import com.myProject.SpringSalesApp.mapper.AddressMapper;
import com.myProject.SpringSalesApp.repositories.AddressRepository;
import com.myProject.SpringSalesApp.services.exceptions.DataIntegrityException;
import com.myProject.SpringSalesApp.services.exceptions.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

@Service
public class AddressService {
    @Autowired
    AddressRepository repository;
    @Autowired
    AddressMapper mapper;
    private static final Logger logger = LoggerFactory.getLogger(AddressService.class);

    public List<AddressDTO> findAll(){
         List<Address> addresses = repository.findAll();
         var listAddressDTO = addresses.stream().map(this::toDTO).toList();
         listAddressDTO.forEach(this::addHateoasLinks);
        return listAddressDTO;
    }
    public AddressDTO findById(Long id){
        logger.info("Finding one Address!");
         Address address = repository.findById(id).orElseThrow(()-> new ResourceNotFoundException(id));
          var addressDTO = toDTO(address);
          addHateoasLinks(addressDTO);
        return addressDTO;
    }
    public AddressDTO insert(AddressDTO address){
        logger.info("Creating a new Address!");
       var addressDTO = toDTO(repository.save(mapper.toEntity(address)));
       addHateoasLinks(addressDTO);
        return addressDTO;
    }
    public AddressDTO updateById(AddressDTO newAddress, Long id){
        logger.info("Updating one Address!");
        Address oldAddress = repository.findById(id).orElseThrow(()-> new ResourceNotFoundException(id));
        updateGeneration(oldAddress,newAddress);
         var addressDTO = toDTO(repository.save(oldAddress));
         addHateoasLinks(addressDTO);
        return addressDTO;
    }
    private void updateGeneration(Address oldAddress, AddressDTO newAddress){
        oldAddress.setCity(newAddress.getCity());
        oldAddress.setComplement(newAddress.getComplement());
        oldAddress.setCountry(newAddress.getCountry());
        oldAddress.setNumber(newAddress.getNumber());
        oldAddress.setState(newAddress.getState());
        oldAddress.setStreet(newAddress.getStreet());
        oldAddress.setZipCode(newAddress.getZipCode());
    }
    public void deleteById(Long id) {
        logger.info("Deleting one Address!");
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

    private AddressDTO toDTO(Address address){
        var addressDTO = mapper.toDTO(address);
        if (address.getClient() != null){
            addressDTO.setIdClient(address.getClient().getId());
            addressDTO.setNameClient(address.getClient().getName());
        }
        return addressDTO;
    }

    private void addHateoasLinks(AddressDTO dto) {
        dto.add(linkTo(methodOn(AddressController.class).findById(dto.getId())).withSelfRel().withType("GET"));
        dto.add(linkTo(methodOn(AddressController.class).deleteById(dto.getId())).withRel("delete").withType("DELETE"));
        dto.add(linkTo(methodOn(AddressController.class).findAll()).withRel("findAll").withType("GET"));
        dto.add(linkTo(methodOn(AddressController.class).insert(dto)).withRel("create").withType("POST"));
        dto.add(linkTo(methodOn(AddressController.class).updateById(dto,dto.getId())).withRel("update").withType("PUT"));
    }

}
