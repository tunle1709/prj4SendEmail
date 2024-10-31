package com.prj4.springSendEmail.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prj4.springSendEmail.constants.JobQueue;
import com.prj4.springSendEmail.dto.EmailAuthenDTO;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailAuthenConsumer {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private JavaMailSender mailSender;

    @RabbitHandler
    @RabbitListener(queues = JobQueue.QUEUE_DEV)
    public void receiveMessage(String message) {
        try {
            EmailAuthenDTO emailAuthenDTO = objectMapper.readValue(message, EmailAuthenDTO.class);
            System.out.println("Received email for authentication: " + emailAuthenDTO.getEmail());
            System.out.println("Authentication URL: " + emailAuthenDTO.getUrl());

            sendEmail(emailAuthenDTO);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendEmail(EmailAuthenDTO emailAuthenDTO) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(emailAuthenDTO.getEmail());
        mailMessage.setSubject("Email Authentication");
        mailMessage.setText("Please click the following link to authenticate: " + emailAuthenDTO.getUrl());

        mailSender.send(mailMessage);
        System.out.println("Email sent successfully to: " + emailAuthenDTO.getEmail());
    }
}
