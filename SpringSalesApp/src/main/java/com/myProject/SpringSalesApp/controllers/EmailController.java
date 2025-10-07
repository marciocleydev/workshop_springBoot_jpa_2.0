package com.myProject.SpringSalesApp.controllers;

import com.myProject.SpringSalesApp.DTO.request.EmailRequestDTO;
import com.myProject.SpringSalesApp.controllers.docs.EmailControllerDocs;
import com.myProject.SpringSalesApp.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/email")
public class EmailController implements EmailControllerDocs {

    @Autowired
    private EmailService service;

    @PostMapping
    @Override
    public ResponseEntity<String> sendEmail(@RequestBody EmailRequestDTO emailRequest) {
        service.sendSimpleEmail(emailRequest);
        return ResponseEntity.ok("E-Mail sent successfully!");
    }

    @PostMapping(value = "/withAttachment")
    @Override
    public ResponseEntity<String> sendEmailWithAttachment(
            @RequestParam("emailRequest") String emailRequest,
            @RequestParam("attachment") MultipartFile attachment) {
        service.sendEmailWithAttachment(emailRequest, attachment);
        return ResponseEntity.ok("E-Mail with attachment sent successfully!");
    }
}
