package com.myProject.SpringSalesApp.services.exceptions;

public class DataIntegrityException extends RuntimeException{
    public DataIntegrityException(String msg){
        super(msg);
    }
}
