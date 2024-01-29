package com.voucherservice;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.voucherservice.exception.ResourceNotFoundException;

 class ResourceNotFoundExceptionTest {

    @Test
     void testDefaultConstructor() {
        // When
        ResourceNotFoundException exception = new ResourceNotFoundException(null);

        // Then
        assertEquals(null, exception.getMessage());
    }

    @Test
     void testConstructorWithMessage() {
        // Given
        String errorMessage = "Resource not found";

        // When
        ResourceNotFoundException exception = new ResourceNotFoundException(errorMessage);

        // Then
        assertEquals(errorMessage, exception.getMessage());
    }

}
