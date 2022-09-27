package com.tuannq.authentication.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String senderEmail;


    public void sendOTP(String toEmail, String otp) throws MessagingException {


        final MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        final MimeMessageHelper message =
                new MimeMessageHelper(mimeMessage, true, "UTF-8"); // true = multipart
        message.setSubject("Quên mật khẩu");

        message.setFrom(senderEmail);
        message.setTo(toEmail);
        message.setText("Chúng tôi đã nhận được yêu cầu đặt lại mật khẩu của bạn.\n"
                .concat("Nhập mã đặt lại mật khẩu sau đây:\t")
                .concat(otp)
                .concat("\nMã có hiệu lực trong 5 phút."));

        // Send mail
        javaMailSender.send(mimeMessage);
    }

}
