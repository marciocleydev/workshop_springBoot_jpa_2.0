package com.myProject.SpringSalesApp.exceptions;

public class BadRequestException extends RuntimeException{
    public BadRequestException(String msg){
        super(msg);
    }
    public BadRequestException(){
        super("Unsupported file extension");
    }
}
