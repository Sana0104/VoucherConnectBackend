package com.va.voucher_request.service;
 

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyString;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.va.voucher_request.client.UserClient;
import com.va.voucher_request.client.VoucherClient;
import com.va.voucher_request.dto.User;
import com.va.voucher_request.dto.Voucher;
import com.va.voucher_request.exceptions.ExamNotPassedException;
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

import jakarta.mail.MessagingException;

import static org.mockito.ArgumentMatchers.eq;


 
@SpringBootTest
class VoucherServiceTests {
 
    @Mock
    private VoucherRequestRepository voucherRepository;
 
    @InjectMocks
    private VoucherReqServiceImpl voucherService;
    
    @Mock
    private VoucherClient voucherClient;
    
    @Mock
    private UserClient userClient;

    @Mock
    private EmailRequestImpl emailService;
    
    @AfterEach
    public void tearDown() throws IOException {
        // Clean up the directory after each test
        Files.walk(Paths.get(VALID_PATH))
                .filter(Files::isRegularFile)
                .map(Path::toFile)
                .forEach(File::delete);
    }
 
 
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
                new VoucherRequest("1", "John Doe", "john.doe@example.com", "AWS", "AWS Exam", 85, "image1","V001",  LocalDate.now(), null, null, "Pass", "certificate" ,"ValidationID", "ss"),
                new VoucherRequest("2", "Jane Doe", "jane.doe@example.com", "GCP", "GCP Exam", 90,"image2", "V002", LocalDate.now(), null, null, "Fail", "certificate", "ValidationID", "N/A")
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




   

    private static final String VALID_CERTIFICATE_FILENAME = "valid_certificate.png";
    private static final String VALID_PATH = "Images/";
    private static final String VALID_VOUCHER_CODE = "V007";
    private static final String INVALID_IMAGE_FORMAT = "invalid_certificate.docx";

    @Test
    void testUploadCertificateSuccessfully() throws ExamNotPassedException, IOException, NotAnImageFileException, NotFoundException {
        // Arrange
        String voucherCode = "V001";

        VoucherRequest voucherRequest = new VoucherRequest();
        voucherRequest.setVoucherCode(voucherCode);
        voucherRequest.setExamResult("Pass");

        // Use MockMultipartFile to simulate file upload
        MockMultipartFile certificateFile = new MockMultipartFile(
                "file",
                VALID_CERTIFICATE_FILENAME,
                "application/jpeg",
                "certificate content".getBytes()
        );

        when(voucherRepository.findByVoucherCode(voucherCode)).thenReturn(voucherRequest);

        // Act
        VoucherRequest result = voucherService.uploadCertificate(voucherCode, certificateFile, VALID_PATH);

        
        // Assert
        assertNotNull(result);
        assertEquals(VALID_CERTIFICATE_FILENAME, result.getCertificateFileImage());
        verify(voucherRepository, times(1)).save(voucherRequest);
    }
    
    @Test
    void testUploadCertificateWithInvalidImageExtension() throws NotFoundException, ExamNotPassedException, IOException {
        // Arrange
        String voucherCode = VALID_VOUCHER_CODE;

        VoucherRequest voucherRequest = new VoucherRequest();
        voucherRequest.setVoucherCode(voucherCode);
        voucherRequest.setExamResult("Pass");

        // Use MockMultipartFile to simulate an invalid image file (e.g., ".docx")
        MockMultipartFile invalidImageFile = new MockMultipartFile(
                "file",
                INVALID_IMAGE_FORMAT,
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                "invalid image content".getBytes()
        );

        when(voucherRepository.findByVoucherCode(voucherCode)).thenReturn(voucherRequest);

        // Act and Assert
        assertThrows(NotAnImageFileException.class, () -> {
            voucherService.uploadCertificate(voucherCode, invalidImageFile, VALID_PATH);
        });
    }
    
    
    
    
    @Test
    void testProvideValidationNumberSuccessfully() throws NotFoundException {
        // Arrange
        String voucherRequestId = "VoucherID123";
        String validationNumber = "123456";

        VoucherRequest voucherRequest = new VoucherRequest();
        voucherRequest.setId(voucherRequestId);

        when(voucherRepository.findById(voucherRequestId)).thenReturn(java.util.Optional.of(voucherRequest));

        // Act
        voucherService.provideValidationNumber(voucherRequestId, validationNumber);

        // Assert
        verify(voucherRepository, times(1)).save(any(VoucherRequest.class));
        assertEquals(validationNumber, voucherRequest.getValidationNumber());
    }

    @Test
    void testProvideValidationNumberVoucherNotFound() {
        // Arrange
        String voucherRequestId = "NonExistentID";
        String validationNumber = "123456";

        when(voucherRepository.findById(voucherRequestId)).thenReturn(java.util.Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class,
                () -> voucherService.provideValidationNumber(voucherRequestId, validationNumber));

        // Ensure that save method is not called since the voucher is not found
        verify(voucherRepository, never()).save(any(VoucherRequest.class));
    }
    @Test
    void testGetValidationNumberSuccess() throws NotFoundException {
        // Arrange
        String voucherRequestId = "validVoucherRequestId";
        String expectedValidationNumber = "123456";

        VoucherRequest voucherRequest = new VoucherRequest();
        voucherRequest.setValidationNumber(expectedValidationNumber);

        when(voucherRepository.findById(voucherRequestId)).thenReturn(Optional.of(voucherRequest));

        // Act
        String actualValidationNumber = voucherService.getValidationNumber(voucherRequestId);

        // Assert
        assertEquals(expectedValidationNumber, actualValidationNumber);
    }
    
    @Test
    void testGetValidationNumberNotFound() {
        // Arrange
        String voucherRequestId = "nonexistentVoucherRequestId";

        when(voucherRepository.findById(voucherRequestId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(NotFoundException.class, () -> voucherService.getValidationNumber(voucherRequestId));
    }
    
    
    private static final String VALID_SCREENSHOT_FILENAME = "valid_screenshot.png";
    
    
    @Test
    void testUploadR2d2ScreenshotSuccess() throws Exception {
        // Arrange
        String voucherCode = VALID_VOUCHER_CODE;

        VoucherRequest voucherRequest = new VoucherRequest();
        voucherRequest.setVoucherCode(voucherCode);

        MockMultipartFile screenshotFile = new MockMultipartFile(
                "file",
                "valid_screenshot.png",
                "image/png",
                "screenshot file content".getBytes());
        

        when(voucherRepository.findByVoucherCode(voucherCode)).thenReturn(voucherRequest);

        // Act
        VoucherRequest result = voucherService.uploadR2d2Screenshot(voucherCode, screenshotFile, VALID_PATH);

        // Assert
        assertNotNull(result);
        assertEquals(VALID_PATH + File.separator + VALID_SCREENSHOT_FILENAME, result.getR2d2Screenshot());
        verify(voucherRepository, times(1)).save(voucherRequest);
    }
    
    @Test
    void testUploadR2d2ScreenshotWrongFileFormat() {
        // Arrange
        String voucherCode = VALID_VOUCHER_CODE;

        VoucherRequest voucherRequest = new VoucherRequest();
        voucherRequest.setVoucherCode(voucherCode);

        MockMultipartFile screenshotFile = new MockMultipartFile(
                "file",
                "invalid_screenshot.pdf",
                "application/pdf",
                "screenshot file content".getBytes());

        when(voucherRepository.findByVoucherCode(voucherCode)).thenReturn(voucherRequest);

        // Act and Assert
        assertThrows(NotAnImageFileException.class,
                () -> voucherService.uploadR2d2Screenshot(voucherCode, screenshotFile, VALID_PATH));

        // Verify that save method was not called
        verify(voucherRepository, never()).save(voucherRequest);
    }


    @Test
    void testUploadR2d2ScreenshotNotFound() {
        // Arrange
        String voucherCode = "nonexistentVoucherCode";

        when(voucherRepository.findByVoucherCode(voucherCode)).thenReturn(null);

        MockMultipartFile screenshotFile = new MockMultipartFile(
                "file",
                "valid_screenshot.png",
                "image/png",
                "screenshot file content".getBytes());
        
        // Act and Assert
        assertThrows(NotFoundException.class,
                () -> voucherService.uploadR2d2Screenshot(voucherCode, screenshotFile, VALID_PATH));

        // Verify that save method was not called
        verify(voucherRepository, never()).save(any());
    }
  
    @Test
    void testPendingEmails() {
        // Mock voucher requests
        List<VoucherRequest> voucherRequests = new ArrayList<>();
        VoucherRequest voucherRequest = new VoucherRequest();
        voucherRequest.setExamResult("pending");
        voucherRequest.setPlannedExamDate(LocalDate.now().minusDays(1));
        voucherRequest.setVoucherCode("ABC123");
        voucherRequest.setCandidateEmail("test@example.com"); // Set candidate email
        voucherRequest.setCandidateName("Test Candidate"); // Set candidate name
        voucherRequest.setCloudPlatform("AWS"); // Set cloud platform
        voucherRequest.setCloudExam("AWS Solutions Architect"); // Set cloud exam
        voucherRequests.add(voucherRequest);
        when(voucherRepository.findAll()).thenReturn(voucherRequests);
        
        // Mock userClient response
        User user = new User();
        user.setMentorEmail("mentor@example.com");
        ResponseEntity<Optional<User>> userResponse = ResponseEntity.ok(Optional.of(user));
        when(userClient.getUserByName(anyString())).thenReturn(userResponse);
        
                
        // Call the method
        List<String> pendingEmails = voucherService.pendingEmails();
        
        // Verify that the email service was called
        verify(emailService, times(1)).sendPendingEmail(eq("test@example.com"), eq("mentor@example.com"), anyString(), anyString());
        
        // Assert the result
        assertEquals(1, pendingEmails.size());
        assertEquals("test@example.com", pendingEmails.get(0));
    }
    
    @Test
    void testNoPendingEmails() {
        // Mock voucher requests to return an empty list
        when(voucherRepository.findAll()).thenReturn(new ArrayList<>());

        // Test
        List<String> pendingEmails = voucherService.pendingEmails();

        // Verify no emails are sent
        verify(emailService, never()).sendPendingEmail(anyString(), anyString(), anyString(), anyString());

        // Assert that the list of pending emails is empty
        assertTrue(pendingEmails.isEmpty());
    }
    

    @Test
    void testNoCandidatesWithPendingStatus() {
        // Mock voucher requests to return a list where exam result is not "pending"
        List<VoucherRequest> voucherRequests = new ArrayList<>();
        VoucherRequest voucherRequest1 = new VoucherRequest();
        voucherRequest1.setExamResult("pass");
        VoucherRequest voucherRequest2 = new VoucherRequest();
        voucherRequest2.setExamResult("fail");
        voucherRequests.add(voucherRequest1);
        voucherRequests.add(voucherRequest2);
        when(voucherRepository.findAll()).thenReturn(voucherRequests);

        // Test
        List<String> pendingEmails = voucherService.pendingEmails();

        // Verify no emails are sent
        verify(emailService, never()).sendPendingEmail(anyString(), anyString(), anyString(), anyString());

        // Assert that the list of pending emails is empty
        assertTrue(pendingEmails.isEmpty());
    }

    @Test
    void testPendingRequestsWithPendingRequests() {
        // Mock voucher requests
        List<VoucherRequest> allRequests = new ArrayList<>();
        VoucherRequest pendingRequest1 = new VoucherRequest();
        pendingRequest1.setExamResult("pending");
        pendingRequest1.setPlannedExamDate(LocalDate.now().minusDays(1));
        pendingRequest1.setVoucherCode("ABC123");
        VoucherRequest pendingRequest2 = new VoucherRequest();
        pendingRequest2.setExamResult("pending");
        pendingRequest2.setPlannedExamDate(LocalDate.now().minusDays(1));
        pendingRequest2.setVoucherCode("XYZ456");
        allRequests.add(pendingRequest1);
        allRequests.add(pendingRequest2);
        when(voucherRepository.findAll()).thenReturn(allRequests);

        // Test
        List<VoucherRequest> pendingRequests = voucherService.pendingRequests();

        // Verify
        assertEquals(2, pendingRequests.size());
    }

    @Test
    void testPendingRequestsWithNoPendingRequests() {
        // Mock voucher requests
        List<VoucherRequest> allRequests = new ArrayList<>();
        VoucherRequest completedRequest1 = new VoucherRequest();
        completedRequest1.setExamResult("completed");
        VoucherRequest completedRequest2 = new VoucherRequest();
        completedRequest2.setExamResult("completed");
        allRequests.add(completedRequest1);
        allRequests.add(completedRequest2);
        when(voucherRepository.findAll()).thenReturn(allRequests);

        // Test
        List<VoucherRequest> pendingRequests = voucherService.pendingRequests();

        // Verify
        assertTrue(pendingRequests.isEmpty());
    }
    @Test
    void testFindByRequestIdWhenVoucherExists() throws MessagingException {
        // Mock behavior of the repository to return a voucher request with the given ID
        String voucherId = "123";
        VoucherRequest voucherRequest = new VoucherRequest();
        when(voucherRepository.findById(voucherId)).thenReturn(Optional.of(voucherRequest));

        // Test
        Optional<VoucherRequest> result = voucherService.findByRequestId(voucherId);

        // Verify
        assertTrue(result.isPresent());
        assertSame(voucherRequest, result.get());
    }

    @Test
    void testFindByRequestIdWhenVoucherDoesNotExist() throws MessagingException {
        // Mock behavior of the repository to return an empty optional
        String voucherId = "456";
        when(voucherRepository.findById(voucherId)).thenReturn(Optional.empty());

        // Test
        Optional<VoucherRequest> result = voucherService.findByRequestId(voucherId);
        

        // Verify
        assertNull(result);


    }
    @Test
    void testDenyRequestWhenRequestExists() throws NoVoucherPresentException {
        // Mock voucher request
        String requestId = "123";
        VoucherRequest voucherRequest = new VoucherRequest();
        voucherRequest.setCandidateName("John Doe");
        voucherRequest.setCandidateEmail("john@example.com");
        voucherRequest.setCloudExam("AWS");
        Optional<VoucherRequest> optionalVoucherRequest = Optional.of(voucherRequest);
        when(voucherRepository.findById(requestId)).thenReturn(optionalVoucherRequest);

        // Test
        VoucherRequest result = voucherService.denyRequest(requestId, "Incomplete Application");

        // Verify email sent
        verify(emailService, times(1)).sendEmail(eq("john@example.com"), anyString(), anyString());

        // Verify that voucher request is deleted
        verify(voucherRepository, times(1)).delete(voucherRequest);

        // Assert result
        assertNotNull(result);
        assertEquals(voucherRequest, result);
    }
    
    @Test
    void testGetDenialReasonMessageLowScore() {
        // Arrange
    	VoucherReqServiceImpl voucherService = new VoucherReqServiceImpl();
        String reason = "lowScore";
        String examName = "AWS";

        // Act
        String message = voucherService.getDenialReasonMessage(reason, examName);

        // Assert
        String expectedMessage = "your DoSelect score is below the minimum requirement. \n\n" +
                "To be eligible for the voucher, candidates must achieve a minimum score of 80. \n" +
                "We encourage you to review your performance and consider revising your preparation strategy before attempting to request a voucher again.";
        assertEquals(expectedMessage, message);
    }

    // Add similar test methods for other denial reasons...

    @Test
    void testGetDenialReasonMessageDefault() {
        // Arrange
    	VoucherReqServiceImpl voucherService = new VoucherReqServiceImpl();
        String reason = "unknownReason";
        String examName = "Azure";

        // Act
        String message = voucherService.getDenialReasonMessage(reason, examName);

        // Assert
        String expectedMessage = "Your voucher request for the Azure has been denied. Please contact support for any queries.";
        assertEquals(expectedMessage, message);
    }
    @Test
    void testGetDenialReasonMessageOutdatedImage() {
        // Arrange
    	VoucherReqServiceImpl voucherService = new VoucherReqServiceImpl();
        String reason = "outdatedImage";
        String examName = "Azure";

        // Act
        String message = voucherService.getDenialReasonMessage(reason, examName);

        // Assert
        String expectedMessage = "an outdated DoSelect image. The image you uploaded for issuing of a voucher apperas to be an old doSelect image. \n\n"
				+ "Kindly ensure that you have recently taken the DoSelect exam for the cloud certification you are requesting for and upload the lastest score to facilitate the voucher request process.";
        assertEquals(expectedMessage, message);
    }
    
    @Test
    void testGetDenialReasonMessageIncorrectScreenshot() {
        // Arrange
    	VoucherReqServiceImpl voucherService = new VoucherReqServiceImpl();
        String reason = "incorrectScreenshot";
        String examName = "Azure";

        // Act
        String message = voucherService.getDenialReasonMessage(reason, examName);

        // Assert
        String expectedMessage = "an incorrect DoSelect screenshot. The image you uploaded apperas to be incorrect. \n\n"
				+ "Kindly ensure that you upload a proper DoSelect screenshot that corresponds to the cloud exam you are requesting for inorder to facilitate the voucher request process accurately.";
        		
        assertEquals(expectedMessage, message);
    }
    
    @Test
    void testGetDenialReasonMessageIncorrectImageFormat() {
        // Arrange
    	VoucherReqServiceImpl voucherService = new VoucherReqServiceImpl();
        String reason = "incorrectImageFormat";
        String examName = "Azure";

        // Act
        String message = voucherService.getDenialReasonMessage(reason, examName);

        // Assert
        String expectedMessage = "an incorrect format of the DoSelect image. The DoSelect image you uploaded does not correspond to the certification you have requested for. \n\n"
				+ "Kindly ensure that you upload the appropriate DoSelect screenshot specifically for the exam - "
				+ examName + ".";
        assertEquals(expectedMessage, message);
    }
    
   
    
}