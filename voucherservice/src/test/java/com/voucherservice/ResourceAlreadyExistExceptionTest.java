package com.voucherservice;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.voucherservice.exception.ResourceAlreadyExistException;

public class ResourceAlreadyExistExceptionTest {

    @Test
    public void testConstructorWithMessage() {
        // Given
        String errorMessage = "Resource already exists";

        // When
        ResourceAlreadyExistException exception = new ResourceAlreadyExistException(errorMessage);

        // Then
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    public void testDefaultConstructor() {
        // When
        ResourceAlreadyExistException exception = new ResourceAlreadyExistException(null);

        // Then
        assertEquals(null, exception.getMessage());
    }

    // Add more test cases as needed
}

