package com.voucherservice;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.voucherservice.exception.DataIsNotInsertedException;

public class DataIsNotInsertedExceptionTest {

    @Test
    public void testDefaultConstructor() {
        // When
        DataIsNotInsertedException exception = new DataIsNotInsertedException();

        // Then
        assertEquals(null, exception.getMessage());
    }

    @Test
    public void testConstructorWithMessage() {
        // Given
        String errorMessage = "Error while inserting data";

        // When
        DataIsNotInsertedException exception = new DataIsNotInsertedException();

        // Then
        assertEquals(errorMessage, exception.getMessage());
    }

    // Add more test cases as needed
}
