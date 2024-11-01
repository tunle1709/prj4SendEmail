package com.prj4.springSendEmail.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prj4.springSendEmail.constants.JobQueue;
import com.prj4.springSendEmail.dto.EmailAuthenDTO;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailAuthenConsumer {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private Configuration freemarkerConfig;

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

    private void sendEmail(EmailAuthenDTO emailAuthenDTO) throws MessagingException, IOException, TemplateException {
        Template template = freemarkerConfig.getTemplate("email_verification_template.ftl");

        Map<String, Object> model = new HashMap<>();
        model.put("email", emailAuthenDTO.getEmail());
        model.put("url", emailAuthenDTO.getUrl());

        String htmlContent = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(emailAuthenDTO.getEmail());
        helper.setSubject("Email Authentication");
        helper.setText(htmlContent, true);

        mailSender.send(message);
        System.out.println("Email sent successfully to: " + emailAuthenDTO.getEmail());
    }
}
