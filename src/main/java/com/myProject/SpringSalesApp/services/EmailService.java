package com.myProject.SpringSalesApp.services;

import com.myProject.SpringSalesApp.config.EmailConfig;
import com.myProject.SpringSalesApp.mail.EmailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    EmailSender emailSender;

    @Autowired
    EmailConfig emailConfig;

    public  void sendSimpleEmail(String to, String subject, String body){
        emailSender
                .to(to)
                .withSubject(subject)
                .withMessage(body)
                .send(emailConfig);
    }
}
