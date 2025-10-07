package com.myProject.SpringSalesApp.services;

import com.myProject.SpringSalesApp.entities.Order;
import com.myProject.SpringSalesApp.repositories.OrderRepository;
import com.myProject.SpringSalesApp.exceptions.DataIntegrityException;
import com.myProject.SpringSalesApp.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    OrderRepository repository;

    public List<Order> findAll(){
        return repository.findAll();
    }
    public Order findById(Long id){
        try {
            Optional<Order> order = repository.findById(id);
            return order.get();
        }
        catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(id);
        }
    }
    public Order insert(Order order){
        return repository.save(order);
    }
    public Order updateById(Order newOrder, Long id){
        Order oldOrder = repository.getReferenceById(id);
        updateGeneration(oldOrder,newOrder);
        return repository.save(oldOrder);
    }
    private void updateGeneration(Order oldOrder, Order newOrder){
        oldOrder.setClient(newOrder.getClient());
        oldOrder.setPayment(newOrder.getPayment());
        oldOrder.setMoment(newOrder.getMoment());
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
