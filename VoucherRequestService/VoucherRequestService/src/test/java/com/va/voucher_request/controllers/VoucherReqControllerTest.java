package com.va.voucher_request.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.va.voucher_request.client.VoucherClient;
import com.va.voucher_request.contoller.VoucherReqController;
import com.va.voucher_request.exceptions.NoCompletedVoucherRequestException;
import com.va.voucher_request.exceptions.NoVoucherPresentException;
import com.va.voucher_request.exceptions.NotFoundException;
import com.va.voucher_request.exceptions.VoucherNotFoundException;
import com.va.voucher_request.model.VoucherRequest;
import com.va.voucher_request.repo.CandidateRepository;
import com.va.voucher_request.service.EmailRequestImpl;
import com.va.voucher_request.service.VoucherReqServiceImpl;

@ExtendWith(MockitoExtension.class)
 class VoucherReqControllerTest {
	
	@Mock
    private VoucherReqServiceImpl voucherReqService;

    @Mock
    private EmailRequestImpl emailRequestService;

    @Mock
    private VoucherClient voucherClient;
    
    @InjectMocks
    private VoucherReqController voucherReqController;
    
    @Mock
    private CandidateRepository candidateRepo;
    
    @Test
    void testGetAllAssignedVoucher() throws NoVoucherPresentException {
        when(voucherReqService.getAllAssignedVoucherRequest()).thenReturn(Arrays.asList(new VoucherRequest()));

        ResponseEntity<List<VoucherRequest>> response = voucherReqController.getAllAssignedVoucher();
        verify(voucherReqService, times(1)).getAllAssignedVoucherRequest();
        assertEquals(HttpStatus.OK, response.getStatusCode());
       
    }
    
    @Test
    void testUpdateExamDate() throws NotFoundException {
        // Prepare input data
        String voucherCode = "V123";
        LocalDate newExamDate = LocalDate.now();
        VoucherRequest voucherRequest=new VoucherRequest();
        // Mock service behavior
        when(voucherReqService.updateExamDate(voucherCode, newExamDate)).thenReturn(voucherRequest);

        // Perform the request and assert the response
        ResponseEntity<VoucherRequest> response = voucherReqController.updateExamDate(voucherCode, newExamDate);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(voucherRequest, response.getBody());
    }
    
    @Test
    void testInvalidUpdateExamDate() throws NotFoundException {
        // Prepare input data
        String voucherCode = "InvalidCode";
        LocalDate newExamDate = LocalDate.now();

        // Mock service behavior
        when(voucherReqService.updateExamDate(voucherCode, newExamDate)).thenThrow(new NotFoundException("Voucher not found"));

        // Perform the request and assert the exception
        
       
        Assertions.assertThrows(NotFoundException.class, () -> {
            voucherReqController.updateExamDate(voucherCode, newExamDate);
        });
    }
    
    @Test
    void testUpdateExamResult() throws NotFoundException {
        // Prepare input data
        String voucherCode = "V123";
        String newExamResult = "Pass";  // Assuming "Pass" is a valid result
        VoucherRequest voucherRequest=new VoucherRequest();

        // Mock service behavior
        when(voucherReqService.updateExamResult(voucherCode, newExamResult)).thenReturn(voucherRequest);

        // Perform the request and assert the response
        ResponseEntity<VoucherRequest> response = voucherReqController.updateResultStatus(voucherCode, newExamResult);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(voucherRequest, response.getBody());
    }
    
   
    
    @Test
    void testGetAllVouchers() throws VoucherNotFoundException {
    	
        // Mock service behavior
        List<VoucherRequest> voucherRequests = new ArrayList<>();
        when(voucherReqService.getAllVoucherRequest()).thenReturn(voucherRequests);

        // Perform the request and assert the response
        ResponseEntity<List<VoucherRequest>> response = voucherReqController.getAllVouchers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(voucherRequests, response.getBody());
    }
    
    
    @Test
    void testGetAllAssignedVoucher1() throws NoVoucherPresentException {
        // Mock service behavior
        List<VoucherRequest> assignedVoucherRequests = new ArrayList<>();
        when(voucherReqService.getAllAssignedVoucherRequest()).thenReturn(assignedVoucherRequests);

        // Perform the request and assert the response
        ResponseEntity<List<VoucherRequest>> response = voucherReqController.getAllAssignedVoucher();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(assignedVoucherRequests, response.getBody());
    }
    
    
    @Test
    void testGetAllUnAssignedVoucher() throws NoVoucherPresentException {
        // Mock service behavior
        List<VoucherRequest> unassignedVoucherRequests =new ArrayList<>();
        when(voucherReqService.getAllNotAssignedVoucherRequest()).thenReturn(unassignedVoucherRequests);

        // Perform the request and assert the response
        ResponseEntity<List<VoucherRequest>> response = voucherReqController.getAllUnAssignedVoucher();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(unassignedVoucherRequests, response.getBody());
    }
    
    
    @Test
    void testGetAllCompletedVoucherRequests() throws NoCompletedVoucherRequestException {
        // Mock service behavior
        List<VoucherRequest> completedVoucherRequests = new ArrayList<>();
        when(voucherReqService.getAllCompletedVoucherRequest()).thenReturn(completedVoucherRequests);

        // Perform the request and assert the response
        ResponseEntity<List<VoucherRequest>> response = voucherReqController.getAllCompletedVoucherRequests();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(completedVoucherRequests, response.getBody());
    }
    
   


    @Test
    public void testGetAllVouchersByCandidateEmail() throws Exception {
        // Arrange
        String candidateEmail = "test@example.com";
        List<VoucherRequest> voucherRequests = new ArrayList<>();
        // populate voucherRequests with some test data

        when(voucherReqService.getAllVouchersByCandidateEmail(candidateEmail)).thenReturn(voucherRequests);

        // Act
        ResponseEntity<List<VoucherRequest>> responseEntity = voucherReqController.getAllVouchersByCandidateEmail(candidateEmail);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(voucherRequests, responseEntity.getBody());
    }
    private String path;
    
    @BeforeEach
    void setUp() {
        path = "/some/test/path";
    }

    @Test
    public void testPendingEmails() {
        // Mock the service method
        List<String> emails = new ArrayList<>();
        emails.add("email1@example.com");
        emails.add("email2@example.com");
        when(voucherReqService.pendingEmails()).thenReturn(emails);

        // Call the controller method
        ResponseEntity<List<String>> response = voucherReqController.pendingEmails();

        // Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(emails, response.getBody());
    }

}

