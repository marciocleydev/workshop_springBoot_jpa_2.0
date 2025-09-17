package com.myProject.SpringSalesApp.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class FileStorageException extends RuntimeException{

    public FileStorageException(String msg){
        super(msg);
    }
    public FileStorageException(String msg, Throwable cause){
        super(msg, cause);
    }
}
