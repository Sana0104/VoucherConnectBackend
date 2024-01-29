package com.va.voucher_request;
 
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.va.voucher_request.client.VoucherClient;
import com.va.voucher_request.dto.Voucher;
import com.va.voucher_request.exceptions.NoCompletedVoucherRequestException;
import com.va.voucher_request.exceptions.NoVoucherPresentException;
import com.va.voucher_request.exceptions.NotAnImageFileException;
import com.va.voucher_request.exceptions.NotFoundException;
import com.va.voucher_request.exceptions.ParticularVoucherIsAlreadyAssignedException;
import com.va.voucher_request.exceptions.ResourceAlreadyExistException;
import com.va.voucher_request.exceptions.ScoreNotValidException;
import com.va.voucher_request.exceptions.VoucherIsAlreadyAssignedException;
import com.va.voucher_request.exceptions.VoucherNotFoundException;
import com.va.voucher_request.model.VoucherRequest;
import com.va.voucher_request.model.VoucherRequestDto;
import com.va.voucher_request.repo.VoucherRequestRepository;
import com.va.voucher_request.service.VoucherReqServiceImpl;
 

 
@SpringBootTest
class VoucherServiceTests {
 
    @Mock
    private VoucherRequestRepository voucherRepository;
 
    @InjectMocks
    private VoucherReqServiceImpl voucherService;
    
    @Mock
    private VoucherClient voucherClient;
 
 
    @Test
    void testRequestVoucherWithInvalidScore() {
        VoucherRequestDto requestDto = new VoucherRequestDto();
        requestDto.setDoSelectScore(75);
        assertThrows(ScoreNotValidException.class, () -> voucherService.requestVoucher(requestDto, null, null));
        verify(voucherRepository, never()).save(any(VoucherRequest.class));
    }
 
    @Test
    void testRequestVoucherSuccessful() throws ScoreNotValidException, ResourceAlreadyExistException, NotAnImageFileException, IOException {
        // Arrange
        VoucherRequestDto requestDto = new VoucherRequestDto();
        requestDto.setDoSelectScore(85);
        requestDto.setCandidateEmail("s.k@example.com");
        requestDto.setCloudExam("AWS Certified Solutions Architect");

        // Mock MultipartFile
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getOriginalFilename()).thenReturn("example.jpg"); // Set the desired filename
        when(mockFile.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[0])); // Provide a valid InputStream

        when(voucherRepository.existsByCloudExamAndCandidateEmail(anyString(), anyString())).thenReturn(false);

        // Act
        VoucherRequest result = voucherService.requestVoucher(requestDto, mockFile, "Images/");

        // Assert
        assertNotNull(result);
        assertEquals("Pending", result.getExamResult());

        //assertions
        assertNotNull(result.getDoSelectScoreImage());
        assertEquals(requestDto.getCandidateEmail(), result.getCandidateEmail());
        assertEquals(requestDto.getCloudExam(), result.getCloudExam());
    }

    @Test
    void testRequestVoucher_ResourceAlreadyExistException() {
        // Create a mock VoucherRequestDto, MultipartFile, and path
        VoucherRequestDto requestDto = new VoucherRequestDto();
        // Set properties on requestDto as needed
        requestDto.setDoSelectScore(85);
        requestDto.setCandidateEmail("s.k@example.com");
        requestDto.setCloudExam("AWS Certified Solutions Architect");

        MockMultipartFile mockFile = new MockMultipartFile(
            "file", "filename.txt", "text/plain", "Mock file content".getBytes()
        );

        String mockPath = "/mock/path"; // Provide a mock path

        // Mocking the existsByCloudExamAndCandidateEmail method to return true
        when(voucherRepository.existsByCloudExamAndCandidateEmail(anyString(), anyString())).thenReturn(true);

        // Define the executable code that is expected to throw ResourceAlreadyExistException
        Executable executable = () -> voucherService.requestVoucher(requestDto, mockFile, mockPath);

        // Verify that the exception is thrown
        assertThrows(ResourceAlreadyExistException.class, executable);
    }

    @Test
    void testRequestVoucher_NotAnImageFileException() {
        // Create a mock VoucherRequestDto, MultipartFile, and path
        VoucherRequestDto requestDto = new VoucherRequestDto();
        // Set properties on requestDto as needed
        requestDto.setDoSelectScore(85);
        requestDto.setCandidateEmail("s.k@example.com");
        requestDto.setCloudExam("AWS Certified Solutions Architect");
        MockMultipartFile mockFile = new MockMultipartFile(
            "file", "filename.txt", "text/plain", "Mock file content".getBytes()
        );

        String mockPath = "/mock/path"; // Provide a mock path

        // Mocking the existsByCloudExamAndCandidateEmail method to return false
        when(voucherRepository.existsByCloudExamAndCandidateEmail(anyString(), anyString())).thenReturn(false);

        // Define the executable code that is expected to throw NotAnImageFileException
        Executable executable = () -> voucherService.requestVoucher(requestDto, mockFile, mockPath);

        // Verify that the exception is thrown
        assertThrows(NotAnImageFileException.class, executable);
    }


 
    @Test
    void testGetAllVouchersByNonexistentCandidateEmail() {
        String nonexistentEmail = "nonexistent@example.com";
        when(voucherRepository.findByCandidateEmail(nonexistentEmail)).thenReturn(Collections.emptyList());
 
        assertThrows(NotFoundException.class, () -> voucherService.getAllVouchersByCandidateEmail(nonexistentEmail));
        verify(voucherRepository, times(1)).findByCandidateEmail(nonexistentEmail);
    }
    
    @Test
    void testRequestVoucherScoreNotValid() {
        
        VoucherRequestDto requestDto = new VoucherRequestDto();
        requestDto.setDoSelectScore(75); // Score less than 80

       
        assertThrows(ScoreNotValidException.class, () -> voucherService.requestVoucher(requestDto, null, null));
        verify(voucherRepository, never()).save(any());
    }
    
    
    @Test
    void testUpdateExamDate() {
        String voucherCode = "ABC123";
        LocalDate newExamDate = LocalDate.now().plusDays(7);
 
        VoucherRequest existingVoucherRequest = new VoucherRequest();
        existingVoucherRequest.setVoucherCode(voucherCode);
        
 
        when(voucherRepository.findByVoucherCode(voucherCode)).thenReturn(existingVoucherRequest);
 
        VoucherRequest updatedVoucherRequest = null;
        try {
            updatedVoucherRequest = voucherService.updateExamDate(voucherCode, newExamDate);
        } catch (NotFoundException e) {
            fail("NotFoundException not expected for a valid voucher code");
        }
 
        verify(voucherRepository, times(1)).save(existingVoucherRequest);
        assertNotNull(updatedVoucherRequest);
        assertEquals(newExamDate, updatedVoucherRequest.getPlannedExamDate());
    }
 
    @Test
    void testUpdateExamResult() {
        String voucherCode = "ABC123";
        String newExamResult = "Pass";
 
        VoucherRequest existingVoucherRequest = new VoucherRequest();
        existingVoucherRequest.setVoucherCode(voucherCode);
 
        when(voucherRepository.findByVoucherCode(voucherCode)).thenReturn(existingVoucherRequest);
 
        VoucherRequest updatedVoucherRequest = null;
        try {
            updatedVoucherRequest = voucherService.updateExamResult(voucherCode, newExamResult);
        } catch (NotFoundException e) {
            fail("NotFoundException not expected for a valid voucher code");
        }
 
        verify(voucherRepository, times(1)).save(existingVoucherRequest);
        assertNotNull(updatedVoucherRequest);
        assertEquals(newExamResult, updatedVoucherRequest.getExamResult());
    }
    
    @Test
    void testGetAllVoucherRequestSuccessful() throws VoucherNotFoundException {
        List<VoucherRequest> voucherRequests = Collections.singletonList(new VoucherRequest());
        when(voucherRepository.findAll()).thenReturn(voucherRequests);

        List<VoucherRequest> result = voucherService.getAllVoucherRequest();

        assertNotNull(result);
        assertEquals(voucherRequests, result);
    }
    @Test
    void testGetAllAssignedVoucherRequestSuccessful() throws VoucherNotFoundException, NoVoucherPresentException {
      
        List<VoucherRequest> allRequests = Arrays.asList(
                new VoucherRequest("1", "John Doe", "john.doe@example.com", "AWS", "AWS Exam", 85, "image1","V001",  LocalDate.now(), null, null, "Pass"),
                new VoucherRequest("2", "Jane Doe", "jane.doe@example.com", "GCP", "GCP Exam", 90,"image2", "V002", LocalDate.now(), null, null, "Fail")
        );
        when(voucherRepository.findAll()).thenReturn(allRequests);

      
        List<VoucherRequest> result = voucherService.getAllAssignedVoucherRequest();

      
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testGetAllVoucherRequestEmptyList() {
        
        when(voucherRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(VoucherNotFoundException.class, () -> voucherService.getAllVoucherRequest());
    }
    
    @Test
    void testGetAllNotAssignedVoucherRequestNoVouchers() {
      
        when(voucherRepository.findAll()).thenReturn(Collections.emptyList());

        
        assertThrows(NoVoucherPresentException.class, () -> voucherService.getAllNotAssignedVoucherRequest());
    }
    

    @Test
    void testAssignVoucherSuccessful() throws VoucherNotFoundException, NotFoundException, VoucherIsAlreadyAssignedException, ParticularVoucherIsAlreadyAssignedException {
        
        String voucherId = "1";
        String emailId = "john.doe@example.com";
        String voucherRequestId = "123";

        Voucher voucher = new Voucher(voucherId,"AWS","AWS Exam",  "V001", LocalDate.now(), LocalDate.now().plusDays(7), null);

        VoucherRequest voucherRequest = new VoucherRequest("123", "John Doe", "john.doe@example.com", "AWS", "AWS Exam", 85, "image" ,null,  LocalDate.now(), null, null, "Pending");

        when(voucherClient.getVoucherById(voucherId)).thenReturn(ResponseEntity.ok(voucher));
        when(voucherRepository.findById(voucherRequestId)).thenReturn(Optional.of(voucherRequest));
        when(voucherRepository.save(any(VoucherRequest.class))).thenReturn(voucherRequest);

        
        VoucherRequest result = voucherService.assignVoucher(voucherId, emailId, voucherRequestId);

      
        assertNotNull(result);
        assertEquals("V001", result.getVoucherCode());
        assertNotNull(result.getVoucherExpiryLocalDate());
        assertNotNull(result.getVoucherIssueLocalDate());
        
       
    }
    
    @Test
    void testUpdateExamDateException() {
        // Arrange
        String voucherCode = "V001";
        LocalDate newExamDate = LocalDate.now();

        when(voucherRepository.findByVoucherCode(voucherCode)).thenReturn(new VoucherRequest());

        doThrow(RuntimeException.class).when(voucherRepository).save(any(VoucherRequest.class));

        assertThrows(RuntimeException.class, () -> {
            voucherService.updateExamDate(voucherCode, newExamDate);});
    }
    
    @Test
    void testGetAllCompletedVoucherRequest_NoCompletedRequest() {
        // Arrange
        VoucherRequestRepository vrepo = mock(VoucherRequestRepository.class);

        when(vrepo.findAll()).thenReturn(new ArrayList<>());

        // Act & Assert
        assertThrows(NoCompletedVoucherRequestException.class, () -> voucherService.getAllCompletedVoucherRequest());
    }

//    @Test
//    void testGetAllCompletedVoucherRequest_WithCompletedRequests() throws NoCompletedVoucherRequestException {
//        // Arrange
//        VoucherRequestRepository vrepo = mock(VoucherRequestRepository.class);
//
//        List<VoucherRequest> allRequests = new ArrayList<>();
//        allRequests.add(new VoucherRequest(/* initialize with completed request details */));
//
//        when(vrepo.findAll()).thenReturn(allRequests);
//
//        // Act
//        List<VoucherRequest> completedRequests = voucherService.getAllCompletedVoucherRequest();
//
//        // Assert
//        assertEquals(allRequests, completedRequests);
//    }

  

//    @Test
//    void testPendingRequests() {
//        // Arrange
//        VoucherRequestRepository vrepo = mock(VoucherRequestRepository.class);
//
//        List<VoucherRequest> allRequests = new ArrayList<>();
//        allRequests.add(new VoucherRequest(/* initialize with pending request details */));
//        // Add more pending requests as needed
//
//        when(vrepo.findAll()).thenReturn(allRequests);
//
//        // Act
//        List<VoucherRequest> pendingRequests = voucherService.pendingRequests();
//
//        // Assert
//        assertEquals(allRequests, pendingRequests);
//    }


  
}
