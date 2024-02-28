package com.va.voucher_request.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import com.va.voucher_request.exceptions.NoCandidatePresentException;
import com.va.voucher_request.exceptions.NoCandidateToUpDateException;
import com.va.voucher_request.model.Candidate;
import com.va.voucher_request.repo.CandidateRepository;

@ExtendWith(MockitoExtension.class)
public class CandidateServiceImplTest {

    @Mock
    private CandidateRepository candidateRepository;

    @InjectMocks
    private CandidateServiceImpl candidateService;

    @Test
    public void testSaveAllCandidate_NoNewCandidatesAndNoUpdates() throws IOException, NoCandidateToUpDateException {
        // Arrange
        List<Candidate> existingCandidates = new ArrayList<>();
        when(candidateRepository.findAll()).thenReturn(existingCandidates);

        MockMultipartFile file = new MockMultipartFile("file", "test.xlsx", "application/vnd.ms-excel", new byte[0]);

        // Act & Assert
        assertThrows(NoCandidateToUpDateException.class, () -> candidateService.saveAllCandidate(file));
    }


    @Test
    public void testGetAllCandidate_NoCandidatesPresent() throws NoCandidatePresentException {
        // Arrange
        List<Candidate> existingCandidates = new ArrayList<>();
        when(candidateRepository.findAll()).thenReturn(existingCandidates);

        // Act & Assert
        assertThrows(NoCandidatePresentException.class, () -> candidateService.getAllCandidate());
    }

    @Test
    public void testGetAllCandidate_CandidatesPresent() throws NoCandidatePresentException {
        // Arrange
        List<Candidate> existingCandidates = new ArrayList<>();
        Candidate candidate = new Candidate();
        existingCandidates.add(candidate);
        when(candidateRepository.findAll()).thenReturn(existingCandidates);

        // Act
        List<Candidate> result = candidateService.getAllCandidate();

        // Assert
        assertEquals(existingCandidates, result);
    }
    
    @Test
    public void testSaveAllCandidate_EmptyExcelFile() throws IOException, NoCandidateToUpDateException {
        // Arrange
        MockMultipartFile file = new MockMultipartFile("file", "empty.xlsx", "application/vnd.ms-excel", new byte[0]);

        // Act & Assert
        assertThrows(NoCandidateToUpDateException.class, () -> candidateService.saveAllCandidate(file));
        
    }

}
