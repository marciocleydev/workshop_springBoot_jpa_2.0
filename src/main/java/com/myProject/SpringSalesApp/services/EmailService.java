package com.myProject.SpringSalesApp.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myProject.SpringSalesApp.DTO.request.EmailRequestDTO;
import com.myProject.SpringSalesApp.config.EmailConfig;
import com.myProject.SpringSalesApp.mail.EmailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class EmailService {

    @Autowired
    EmailSender emailSender;

    @Autowired
    EmailConfig emailConfig;

    public  void sendSimpleEmail(EmailRequestDTO emailRequest){
        emailSender
                .to(emailRequest.getTo())
                .withSubject(emailRequest.getSubject())
                .withMessage(emailRequest.getBody())
                .send(emailConfig);
    }

    public void sendEmailWithAttachment(String emailRequestJson, MultipartFile attachment){
        File tempFile = null;
        try {
            // como o objeto vem por string e não dto, precisamos convertê-lo para o DTO, na função a cima não precisamos pq já é feito a conversão automatica de Json para DTO
            EmailRequestDTO emailRequest = new ObjectMapper().readValue(emailRequestJson, EmailRequestDTO.class);
            //cria arquivo temporario no disco para o arquivo anexado
            tempFile = File.createTempFile("attachment-", attachment.getOriginalFilename());
            attachment.transferTo(tempFile);
            emailSender
                    .to(emailRequest.getTo())
                    .withSubject(emailRequest.getSubject())
                    .withMessage(emailRequest.getBody())
                    .attach(tempFile.getAbsolutePath())
                    .send(emailConfig);

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error parsing email request JSON !", e);
        } catch (IOException e) {
            throw new RuntimeException("Error processing the attachment !", e);
        }finally {
            if(tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
        }

    }
}
