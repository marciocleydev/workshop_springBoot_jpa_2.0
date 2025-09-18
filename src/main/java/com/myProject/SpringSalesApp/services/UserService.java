package com.myProject.SpringSalesApp.services;

import com.myProject.SpringSalesApp.entities.User;
import com.myProject.SpringSalesApp.repositories.UserRepository;
import com.myProject.SpringSalesApp.exceptions.DataIntegrityException;
import com.myProject.SpringSalesApp.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepository repository;

    public List<User> findAll(){
        return repository.findAll();
    }
    public User findById(Long id){
        try {
            Optional<User> user = repository.findById(id);
            return user.get();
        }
        catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(id);
        }
    }
    public User insert(User user){
       return repository.save(user);
    }
    public User updateById(User newUser, Long id){
        User oldUser = repository.getReferenceById(id);
        updateGeneration(oldUser,newUser);
        return repository.save(oldUser);
    }
    private void updateGeneration(User oldUser, User newUser){
        oldUser.setName(newUser.getName());
        oldUser.setEmail(newUser.getEmail());
        oldUser.setPhone(newUser.getPhone());
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
