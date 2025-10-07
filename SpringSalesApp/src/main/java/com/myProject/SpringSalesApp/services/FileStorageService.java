package com.myProject.SpringSalesApp.services;

import com.myProject.SpringSalesApp.config.FileStorageConfig;
import com.myProject.SpringSalesApp.exceptions.FileNotFoundException;
import com.myProject.SpringSalesApp.exceptions.FileStorageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageService {
    private static final Logger logger = LoggerFactory.getLogger(FileStorageService.class);
    private final Path fileStorageLocation;

    @Autowired
    public FileStorageService(FileStorageConfig fileStorageConfig) {
        Path path = Paths.get(fileStorageConfig.getUploadDir()).toAbsolutePath()
                .normalize();
        this.fileStorageLocation = path;
        try {
            logger.info("Creating directory: {}", this.fileStorageLocation);
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException e) {
            logger.error("Could not create the directory where the uploaded files will be stored.", e);
            throw new FileStorageException("could not create directory where files will be stored", e );
        }
    }
    public String storeFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            if (fileName.contains("..")) {
                logger.error("Sorry! Filename contains invalid path sequence " + fileName);
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }
            logger.info("Saving file in disk");
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        }catch ( IOException e){
            logger.error("Could not store file " + fileName + ". Please try again!", e);
            throw new FileStorageException("could not store file " + fileName + ". Please try again!", e);
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new FileNotFoundException("File not found " + fileName);
            }
        }catch ( IOException e){
            throw new FileNotFoundException("File not found " + fileName, e);
        }
    }
}
