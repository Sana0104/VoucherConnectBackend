package com.voucherservice;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import com.voucherservice.exceptionhandler.ExceptionResponse;

public class ExceptionResponseTest {

    @Test
    public void testCreateExceptionResponse() {
        LocalDate timestamp = LocalDate.now();
        String message = "Test Message";
        String details = "Test Details";
        String httpCodeMessage = "Test HTTP Code Message";

        ExceptionResponse exceptionResponse = new ExceptionResponse(timestamp, message, details, httpCodeMessage);

        assertThat(exceptionResponse).isNotNull();
        assertThat(exceptionResponse.getTimestamp()).isEqualTo(timestamp);
        assertThat(exceptionResponse.getMessage()).isEqualTo(message);
        assertThat(exceptionResponse.getDetails()).isEqualTo(details);
        assertThat(exceptionResponse.getHttpCodeMessage()).isEqualTo(httpCodeMessage);
    }

    @Test
    public void testDefaultConstructor() {
        ExceptionResponse exceptionResponse = new ExceptionResponse();

        assertThat(exceptionResponse).isNotNull();
        assertThat(exceptionResponse.getTimestamp()).isNull();
        assertThat(exceptionResponse.getMessage()).isNull();
        assertThat(exceptionResponse.getDetails()).isNull();
        assertThat(exceptionResponse.getHttpCodeMessage()).isNull();
    }

    @Test
    public void testSetters() {
        ExceptionResponse exceptionResponse = new ExceptionResponse();

        LocalDate timestamp = LocalDate.now();
        String message = "Test Message";
        String details = "Test Details";
        String httpCodeMessage = "Test HTTP Code Message";

        exceptionResponse.setTimestamp(timestamp);
        exceptionResponse.setMessage(message);
        exceptionResponse.setDetails(details);
        exceptionResponse.setHttpCodeMessage(httpCodeMessage);

        assertThat(exceptionResponse.getTimestamp()).isEqualTo(timestamp);
        assertThat(exceptionResponse.getMessage()).isEqualTo(message);
        assertThat(exceptionResponse.getDetails()).isEqualTo(details);
        assertThat(exceptionResponse.getHttpCodeMessage()).isEqualTo(httpCodeMessage);
    }

    // Add more test cases as needed
}

