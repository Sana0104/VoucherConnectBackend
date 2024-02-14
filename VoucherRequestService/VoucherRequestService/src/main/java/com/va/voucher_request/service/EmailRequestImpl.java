package com.va.voucher_request.service;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailRequestImpl {

	@Autowired
	JavaMailSender javaMailSender;

	@Value("VOUCHER CONNECT<${username}>")
	String username;

	public String sendEmail(String toMail, String subject, String body) {

		SimpleMailMessage message = new SimpleMailMessage();

		message.setFrom(username);
		message.setTo(toMail);
		message.setSubject(subject);
		message.setText(body);

		javaMailSender.send(message);

		return "mail send successfully";
	}

	public String sendPendingEmail(String toMail, String cc, String subject, String body) {

		SimpleMailMessage message = new SimpleMailMessage();

		message.setFrom(username);
		message.setTo(toMail);
		message.setCc(cc);
		message.setSubject(subject);
		message.setText(body);

		javaMailSender.send(message);

		return "mail send successfully";

	}

	public String sendHtmlEmail(String toMail, String subject, String htmlContent, List<String> paths)
			throws MessagingException {
		try {
			MimeMessage mimeMessage = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,
					MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

			helper.setFrom(username);
			helper.setTo(toMail);
			helper.setSubject(subject);
			helper.setText(htmlContent, true);

//			for (String s : paths) {
//				FileSystemResource fs = new FileSystemResource(new File(s));
//				helper.addAttachment(fs.getFilename(), fs);
//			}
			javaMailSender.send(mimeMessage);

			return "HTML mail send successfully";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

}
