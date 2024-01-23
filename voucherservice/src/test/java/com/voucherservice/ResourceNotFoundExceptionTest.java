package com.voucherservice;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.voucherservice.exception.ResourceNotFoundException;

public class ResourceNotFoundExceptionTest {

    @Test
    public void testDefaultConstructor() {
        // When
        ResourceNotFoundException exception = new ResourceNotFoundException(null);

        // Then
        assertEquals(null, exception.getMessage());
    }

    @Test
    public void testConstructorWithMessage() {
        // Given
        String errorMessage = "Resource not found";

        // When
        ResourceNotFoundException exception = new ResourceNotFoundException(errorMessage);

        // Then
        assertEquals(errorMessage, exception.getMessage());
    }

    // Add more test cases as needed
}
