package com.voucherservice;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.voucherservice.exception.NoVoucherPresentException;

public class NoVoucherPresentExceptionTest {

    @Test
    public void testDefaultConstructor() {
        // When
        NoVoucherPresentException exception = new NoVoucherPresentException();

        // Then
        assertEquals(null, exception.getMessage());
    }

    // Add more test cases as needed
}
