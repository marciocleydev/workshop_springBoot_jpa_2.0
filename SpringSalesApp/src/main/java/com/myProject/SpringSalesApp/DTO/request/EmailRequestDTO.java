package com.myProject.SpringSalesApp.DTO.request;

public class EmailRequestDTO {

    private String to;
    private String subject;
    private String body;

    public EmailRequestDTO() {
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
