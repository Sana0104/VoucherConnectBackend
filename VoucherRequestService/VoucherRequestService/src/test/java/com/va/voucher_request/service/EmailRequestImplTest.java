package com.va.voucher_request.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@ExtendWith(MockitoExtension.class)
public class EmailRequestImplTest {

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private EmailRequestImpl emailRequestImpl;

    @Test
    public void testSendEmail() {
        // Arrange
        String toMail = "recipient@example.com";
        String subject = "Test Subject";
        String body = "Test Body";

        // Act
        String result = emailRequestImpl.sendEmail(toMail, subject, body);

        // Assert
        assertEquals("mail send successfully", result);
        verify(javaMailSender).send(any(SimpleMailMessage.class));
    }
    
    @Test
    public void testSendPendingEmail() {
        // Arrange
        String toMail = "recipient@example.com";
        String cc = "cc@example.com";
        String subject = "Test Subject";
        String body = "Test Body";

        // Act
        String result = emailRequestImpl.sendPendingEmail(toMail, cc, subject, body);

        // Assert
        assertEquals("mail send successfully", result);
        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(javaMailSender).send(captor.capture());
        SimpleMailMessage sentMessage = captor.getValue();
        assertEquals(toMail, sentMessage.getTo()[0]);
        assertEquals(cc, sentMessage.getCc()[0]);
    }

    @Test
    public void testSendHtmlEmail() throws MessagingException {
        // Arrange
        String fromMail = "sender@example.com"; // Provide a valid sender email address
        String toMail = "recipient@example.com";
        String subject = "Test Subject";
        String htmlContent = "<html><body><h1>Hello</h1></body></html>";
        List<String> paths = Arrays.asList("/path/to/attachment1", "/path/to/attachment2");

        // Mock behavior for javaMailSender
        MimeMessage mimeMessage = mock(MimeMessage.class);
        doReturn(mimeMessage).when(javaMailSender).createMimeMessage();

        // Act
        try {
            String result = emailRequestImpl.sendHtmlEmail(fromMail, toMail, subject, paths);

            // Assert
            assertEquals("HTML mail send successfully", result);
            verify(javaMailSender, times(1)).createMimeMessage(); // Verify that createMimeMessage is called
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception occurred: " + e.getMessage());
        }
    }


}
