package com.voucherservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import com.voucherservice.exception.DataIsNotInsertedException;
import com.voucherservice.exception.GivenFileIsNotExcelFileException;
import com.voucherservice.exception.NoVoucherPresentException;
import com.voucherservice.exception.ResourceAlreadyExistException;
import com.voucherservice.exception.ResourceNotFoundException;
import com.voucherservice.exception.TheseDataIsAlreadyPresentException;
import com.voucherservice.exceptionhandler.ExceptionResponse;
import com.voucherservice.exceptionhandler.GlobalExceptionHandler;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();

    @Test
    void handleDataIsNotInsertedException() {
        // Given
        DataIsNotInsertedException exception = new DataIsNotInsertedException();
        WebRequest webRequest = mock(WebRequest.class);

        // When
        ResponseEntity<ExceptionResponse> response = exceptionHandler.handleDataIsNotInsertedException(exception, webRequest);

        // Then
        assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
        assertEquals("Data is not inserted from Excel Sheet", response.getBody().getMessage());
    }

    @Test
    void handleGivenFileIsNotExcelException() {
        // Given
        GivenFileIsNotExcelFileException exception = new GivenFileIsNotExcelFileException();
        WebRequest webRequest = mock(WebRequest.class);

        // When
        ResponseEntity<ExceptionResponse> response = exceptionHandler.handleGivenFileIsNotExcelException(exception, webRequest);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Uploaded File is not an excel file", response.getBody().getMessage());
    }

//    @Test
//    void handleNoVoucherPresentException() {
//        // Given
//        NoVoucherPresentException exception = new NoVoucherPresentException();
//        WebRequest webRequest = mock(WebRequest.class);
//
//        // When
//        ResponseEntity<ExceptionResponse> response = exceptionHandler.handleNoVoucherPresentException(exception, webRequest);
//
//        // Then
//        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//        assertEquals("No Voucher is Present", response.getBody().getMessage());
//    }

    @Test
    void handleTheseDataIsAlreadyPresentException() {
        // Given
        TheseDataIsAlreadyPresentException exception = new TheseDataIsAlreadyPresentException();
        WebRequest webRequest = mock(WebRequest.class);

        // When
        ResponseEntity<ExceptionResponse> response = exceptionHandler.handleTheseDataIsAlreadyPresentException(exception, webRequest);

        // Then
        assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
        assertEquals("These Data is already present in the database", response.getBody().getMessage());
    }

    @Test
    void handleOtherException() {
        // Given
        Exception exception = new Exception("Some Error");
        WebRequest webRequest = mock(WebRequest.class);

        // When
        ResponseEntity<ExceptionResponse> response = exceptionHandler.handleOtherException(exception, webRequest);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Some Error Occured", response.getBody().getMessage());
    }

//    @Test
//    void handleMethodArgumentNotValid() {
//        // Given
//        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
//        WebRequest webRequest = mock(WebRequest.class);
//        when(exception.getBindingResult()).thenReturn(TestUtils.createMockBindingResult());
//
//        // When
//        ResponseEntity<Object> response = exceptionHandler.handleMethodArgumentNotValid(exception, null, null, webRequest);
//
//        // Then
//        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//        assertEquals("Validation fails", ((MethodArgumentNotValidException) response.getBody()).getMessage());
//    }

    @Test
    void handleResourceNotFoundException() {
        // Given
        ResourceNotFoundException exception = new ResourceNotFoundException("Resource not found");
        WebRequest webRequest = mock(WebRequest.class);

        // When
        ResponseEntity<ExceptionResponse> response = exceptionHandler.handleResourceNotFoundException(exception, webRequest);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Resource not found", response.getBody().getMessage());
    }

    @Test
    void handleResourceAlreadyExistException() {
        // Given
        ResourceAlreadyExistException exception = new ResourceAlreadyExistException("Resource already exists");
        WebRequest webRequest = mock(WebRequest.class);

        // When
        ResponseEntity<ExceptionResponse> response = exceptionHandler.handleResourceAlreadyExistException(exception, webRequest);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Resource already exists", response.getBody().getMessage());
    }
}
