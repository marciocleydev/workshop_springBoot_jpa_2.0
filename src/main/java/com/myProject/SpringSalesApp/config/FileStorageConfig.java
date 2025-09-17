package com.myProject.SpringSalesApp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "file") // prefix é o mesmo que esta configurado no application.yml
public class FileStorageConfig {

    private String uploadDir; // mesmo que esta configurado no application.yml porém tira "-" e coloca D maiusc

    public FileStorageConfig() {
    }
    public String getUploadDir() {
        return uploadDir;
    }
    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }
}
