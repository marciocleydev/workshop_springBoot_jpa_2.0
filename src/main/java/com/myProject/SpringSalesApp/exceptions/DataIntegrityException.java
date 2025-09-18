package com.myProject.SpringSalesApp.exceptions;

public class DataIntegrityException extends RuntimeException{
    public DataIntegrityException(String msg){
        super(msg);
    }
}
