package com.va.voucher_request.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

 class NotFoundExcepionTest {
	
	  @Test
	    void testConstructorWithMessage() {
	        String errorMessage = "Entity not found";

	        NotFoundException exception = new NotFoundException(errorMessage);

	        assertNotNull(exception);
	        assertEquals(errorMessage, exception.getMessage());
	    }

}

