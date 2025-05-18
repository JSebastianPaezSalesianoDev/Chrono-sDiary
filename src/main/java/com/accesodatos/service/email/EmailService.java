package com.accesodatos.service.email;

public interface EmailService {
    void sendEmail(String to, String subject, String body);
}
