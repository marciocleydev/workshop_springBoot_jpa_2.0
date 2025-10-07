package com.myProject.SpringSalesApp.mail;

import com.myProject.SpringSalesApp.config.EmailConfig;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.StringTokenizer;

@Component
public class EmailSender implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    Logger logger = LoggerFactory.getLogger(EmailSender.class);

    private final JavaMailSender mailSender;
    private String to;
    private String subject;
    private String body;
    private ArrayList<InternetAddress> recipients = new ArrayList<>();
    private File attachment;


    public EmailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public EmailSender withMessage(String body) {
        this.body = body;
        return this;
    }

    public EmailSender withSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public EmailSender to(String to) {
        this.to = to;
        this.recipients = getRecipients(to);
        return this;
    }

    public EmailSender attach(String fileDir) {
        this.attachment = new File(fileDir);
        return this;
    }
    public void send(EmailConfig config){
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(config.getUsername());
            helper.setTo(recipients.toArray(new InternetAddress[recipients.size()]));
            helper.setSubject(subject);
            helper.setText(body, true);
            if (attachment != null) {
                helper.addAttachment(attachment.getName(), attachment);
            }
            mailSender.send(message);
            logger.info("Email sent to %s with subject '%s'%n!", to, subject);
            reset();
        } catch (MessagingException e) {
            throw new RuntimeException("Error sending the email ", e);
        }

    }

    private void reset() {
        this.to = null;
        this.subject = null;
        this.body = null;
        this.recipients = new ArrayList<>();
        this.attachment = null;
    }

    //email1@gmail.com; email2@gmail.com; email3@gmail.com; metodo retorna essa lista
    private ArrayList<InternetAddress> getRecipients(String to) {
        String  toWithoutSpaces = to.replaceAll("\\s", "");// elimina espaços em branco , ( "   " = \\s)
        StringTokenizer tok = new StringTokenizer(toWithoutSpaces, ";"); //separa os emails
        ArrayList<InternetAddress> recipientList = new ArrayList<>() ;
        while (tok.hasMoreElements()) {
            try {
                recipientList.add(new InternetAddress(tok.nextElement().toString()));
            } catch (AddressException e) {
                throw new RuntimeException(e);
            }
        }
        return recipientList;
    }
}
