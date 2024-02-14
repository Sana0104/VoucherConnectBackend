package com.va.voucher_request.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.va.voucher_request.exceptions.NoCandidatePresentException;
import com.va.voucher_request.exceptions.NoCandidateToUpDateException;
import com.va.voucher_request.model.Candidate;

public interface CandidateService {
	
int saveAllCandidate(MultipartFile file) throws IOException, NoCandidateToUpDateException;
	
	List<Candidate> getAllCandidate() throws NoCandidatePresentException;

}
