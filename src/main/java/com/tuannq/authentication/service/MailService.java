package com.tuannq.authentication.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String senderEmail;
    @Value("${spring.application.name}")
    private String applicationName;


    public void sendOTP(String toEmail, String otp) throws MessagingException, UnsupportedEncodingException {


        final MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        final MimeMessageHelper message =
                new MimeMessageHelper(mimeMessage, true, "UTF-8"); // true = multipart
        message.setSubject("Quên mật khẩu");

        message.setFrom(senderEmail, applicationName);
        message.setTo(toEmail);
        message.setText("Chúng tôi đã nhận được yêu cầu đặt lại mật khẩu của bạn.\n"
                .concat("Nhập mã đặt lại mật khẩu sau đây:\t")
                .concat(otp)
                .concat("\nMã có hiệu lực trong 5 phút."));

        // Send mail
        javaMailSender.send(mimeMessage);
    }


    public void sendPassword(String toEmail, String password) throws MessagingException, UnsupportedEncodingException {

        final MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        final MimeMessageHelper message =
                new MimeMessageHelper(mimeMessage, true, "UTF-8"); // true = multipart
        message.setSubject("Tạo mới tài khoản");
        message.setFrom(senderEmail, applicationName);
        message.setTo(toEmail);
        message.setText("username: ".concat(toEmail)
                .concat("\nPassword: ").concat(password));

        // Send mail
        javaMailSender.send(mimeMessage);
    }

}
