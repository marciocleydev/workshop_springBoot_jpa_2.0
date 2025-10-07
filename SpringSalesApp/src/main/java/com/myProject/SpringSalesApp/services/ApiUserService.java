package com.myProject.SpringSalesApp.services;

import com.myProject.SpringSalesApp.entities.ApiUser;
import com.myProject.SpringSalesApp.repositories.ApiUserRepository;
import com.myProject.SpringSalesApp.exceptions.DataIntegrityException;
import com.myProject.SpringSalesApp.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ApiUserService {
    @Autowired
    ApiUserRepository repository;

    public List<ApiUser> findAll(){
        return repository.findAll();
    }
    public ApiUser findById(Long id){
        try {
            Optional<ApiUser> user = repository.findById(id);
            return user.get();
        }
        catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(id);
        }
    }
    public ApiUser insert(ApiUser apiUser){
       return repository.save(apiUser);
    }
    public ApiUser updateById(ApiUser newApiUser, Long id){
        ApiUser oldApiUser = repository.getReferenceById(id);
        updateGeneration(oldApiUser, newApiUser);
        return repository.save(oldApiUser);
    }
    private void updateGeneration(ApiUser oldApiUser, ApiUser newApiUser){
        oldApiUser.setName(newApiUser.getName());
        oldApiUser.setEmail(newApiUser.getEmail());
        oldApiUser.setPhone(newApiUser.getPhone());
    }
    public void deleteById(Long id) {
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
