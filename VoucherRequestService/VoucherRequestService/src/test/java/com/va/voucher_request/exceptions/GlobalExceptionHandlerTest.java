package com.va.voucher_request.exceptions;
 
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.ServletWebRequest;
 
class GlobalExceptionHandlerTest {
	 private final GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();
 
	    @Test
	    void testHandleNotFoundException() {
	        NotFoundException exception = new NotFoundException("Not Found");
 
	        ResponseEntity<String> responseEntity = globalExceptionHandler.handleNotFoundException(exception);
 
	        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	        assertEquals("Not Found", responseEntity.getBody());
	    }
 
	    
	    @Test
	    void testHandleResourceNotFoundException() {
	        ResourceNotFoundException exception = new ResourceNotFoundException("Resource Not Found");
	        MockHttpServletRequest request = new MockHttpServletRequest();
	        ServletWebRequest webRequest = new ServletWebRequest(request);
 
	        ResponseEntity<ExceptionResponse> responseEntity = globalExceptionHandler.handleResourceNotFoundException(exception, webRequest);
 
	        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	        assertEquals("Resource Not Found", responseEntity.getBody().getMessage());
	    }
	    @Test
	    void testHandleResourceAlreadyExistException() {
	        ResourceAlreadyExistException exception = new ResourceAlreadyExistException("Resource Already Exists");
	        MockHttpServletRequest request = new MockHttpServletRequest();
	        ServletWebRequest webRequest = new ServletWebRequest(request);
 
	        ResponseEntity<ExceptionResponse> responseEntity = globalExceptionHandler.handleResourceAlreadyExistException(exception, webRequest);
 
	        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	        assertEquals("Resource Already Exists", responseEntity.getBody().getMessage());
	    }
	    @Test
	    void testHandleScoreNotValidException() {
	        ScoreNotValidException exception = new ScoreNotValidException("Score is not valid");
 
	        ResponseEntity<String> responseEntity = globalExceptionHandler.handleScoreNotValidException(exception);
 
	        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
	        assertEquals("Score is not valid", responseEntity.getBody());
	    }
	    @Test
	    void testHandleVoucherNotFoundException() {
	        // Create a VoucherNotFoundException
	        VoucherNotFoundException exception = new VoucherNotFoundException("Voucher not found");

	        // Call the exception handler method
	        ResponseEntity<Object> responseEntity = globalExceptionHandler.handleVoucherNotFoundException(exception);

	        // Assert the status code
	        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

	        // Assert the response body
	        String responseBody = (String) responseEntity.getBody();
	        assertNotNull(responseBody);
	        assertEquals("Voucher Not Found With This Id: Voucher not found", responseBody);
	    }

	    @Test
	    void testHandleParticularVoucherIsAlreadyAssignedException() {
	        ParticularVoucherIsAlreadyAssignedException exception = new ParticularVoucherIsAlreadyAssignedException("Already assigned");
 
	        ResponseEntity<String> responseEntity = globalExceptionHandler.handleParticularVoucherIsAlreadyAssignedException(exception);
 
	        assertEquals(HttpStatus.ALREADY_REPORTED, responseEntity.getStatusCode());
	        assertEquals("Particular Voucher Is Already Assigned to Other,Can't Use it", responseEntity.getBody());
	    }

 
	    @Test
	    void testHandleVoucherIsAlreadyAssignedException() {
	        VoucherIsAlreadyAssignedException exception = new VoucherIsAlreadyAssignedException("Voucher is already assigned");
 
	        ResponseEntity<String> responseEntity = globalExceptionHandler.handleVoucherIsAlreadyAssignedException(exception);
 
	        assertEquals(HttpStatus.ALREADY_REPORTED, responseEntity.getStatusCode());
	        assertEquals("Voucher is Already Assigned", responseEntity.getBody());
	    }
 
	    
	    
	    @Test
	    void testHandleNoVoucherPresentException() {
	        // Create a NoVoucherPresentException
	        NoVoucherPresentException exception = new NoVoucherPresentException("No Voucher Present");

	        // Call the exception handler method
	        ResponseEntity<Object> responseEntity = globalExceptionHandler.handleNoVoucherPresentException(exception);

	        // Assert the status code
	        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

	        // Assert the response body
	        String responseBody = (String) responseEntity.getBody();
	        assertNotNull(responseBody);
	        assertEquals("No Voucher Present: No Voucher Present", responseBody);
	    }


 

//	    
}