package com.dcl.practise.reactive.bankingapp.user.notification.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class AppConfig {
    private static final String HOST = "smtp.gmail.com";
    private static final short PORT = 587;
    private static final String USER = "gws.notification.api@gmail.com";
    private static final String PWD = "";

    @Bean
    public JavaMailSender getJavaMailSender(){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(HOST);
        mailSender.setPort(PORT);
        mailSender.setUsername(USER);
        mailSender.setPassword(PWD);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol","smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable","true");
        props.put("mail.debug","true");

        return mailSender;
    }
}
