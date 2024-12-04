package com.example.lbook.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Value("${spring.mail.from}")
    private String emailFrom;

    @Value("${endpoint.confirmUser}")
    private String apiConfirmUser;

    @KafkaListener(topics = "confirm-account-topic", groupId = "confirm-account-group")
    public void sendConfirmLinkByKafka(String message) throws MessagingException, UnsupportedEncodingException {
        log.info("Processing Kafka message for account confirmation: {}", message);

        String[] arr = message.split(",");
        String emailTo = arr[0].substring(arr[0].indexOf('=') + 1);
        String userId = arr[1].substring(arr[1].indexOf('=') + 1);
        String otpCode = arr[2].substring(arr[2].indexOf('=') + 1);

        // Construct the confirmation link
        String linkConfirm = String.format("%s/%s?otpCode=%s", apiConfirmUser, userId, otpCode);
        log.info("Generated confirmation link: {}", linkConfirm);

        // Set up email content and properties
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
        Context context = new Context();
        Map<String, Object> properties = new HashMap<>();
        properties.put("linkConfirm", linkConfirm);
        properties.put("otpCode", otpCode);
        context.setVariables(properties);

        helper.setFrom(emailFrom, "Lê Minh Phúc CN22G");
        helper.setTo(emailTo);
        helper.setSubject("Your OTP Code for Account Confirmation");
        String html = templateEngine.process("confirm-email.html", context);
        helper.setText(html, true);

        // Send email and log the result
        mailSender.send(mimeMessage);
        log.info("Confirmation email sent to user at email={}, with OTP code={}", emailTo, otpCode);
    }

//    /**
//     * Send email by Google SMTP
//     *
//     * @param recipients
//     * @param subject
//     * @param content
//     * @param files
//     * @return
//     * @throws UnsupportedEncodingException
//     * @throws MessagingException
//     */
//    public String sendEmail(String recipients, String subject, String content, MultipartFile[] files) throws UnsupportedEncodingException, MessagingException {
//        log.info("Email is sending ...");
//
//        MimeMessage message = mailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
//        helper.setFrom(emailFrom, "Lê Minh Phúc");
//
//        if (recipients.contains(",")) { // send to multiple users
//            helper.setTo(InternetAddress.parse(recipients));
//        } else { // send to single user
//            helper.setTo(recipients);
//        }
//
//        // Send attach files
//        if (files != null) {
//            for (MultipartFile file : files) {
//                helper.addAttachment(Objects.requireNonNull(file.getOriginalFilename()), file);
//            }
//        }
//
//        helper.setSubject(subject);
//        helper.setText(content, true);
//        mailSender.send(message);
//
//        log.info("Email has sent to successfully, recipients: {}", recipients);
//
//        return "Sent";
//    }
//
//    /**
//     * Send link confirm to email register.
//     *
//     * @param emailTo
//     * @param userId
//     * @param verifyCode
//     * @throws MessagingException
//     * @throws UnsupportedEncodingException
//     */
//    public void sendConfirmLink(String emailTo, long userId, String verifyCode) throws MessagingException, UnsupportedEncodingException {
//        log.info("Sending confirming link to user, email={}", emailTo);
//
//        MimeMessage message = mailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
//        Context context = new Context();
//
//        // http://localhost:80/user/confirm/{userId}?verifyCode={verifyCode}
//        String linkConfirm = String.format("%s/%s?verifyCode=%s", apiConfirmUser, userId, verifyCode);
//
//        Map<String, Object> properties = new HashMap<>();
//        properties.put("linkConfirm", linkConfirm);
//        context.setVariables(properties);
//
//        helper.setFrom(emailFrom, " Java");
//        helper.setTo(emailTo);
//        helper.setSubject("Please confirm your account");
//        String html = templateEngine.process("confirm-email.html", context);
//        helper.setText(html, true);
//
//        mailSender.send(message);
//        log.info("Confirming link has sent to user, email={}, linkConfirm={}", emailTo, linkConfirm);
//    }


}