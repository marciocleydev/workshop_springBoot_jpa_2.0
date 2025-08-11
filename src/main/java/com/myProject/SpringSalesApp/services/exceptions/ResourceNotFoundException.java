package com.myProject.SpringSalesApp.services.exceptions;

public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException(Long id){
        super("Not Found id: " + id);
    }
}
