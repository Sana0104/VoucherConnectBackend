package com.voucher.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.voucher.entity.Candidate;
import com.voucher.exceptions.NoCandidatePresentException;
import com.voucher.exceptions.NoCandidateToUpDateException;

public interface CandidateService {
	
	int saveAllCandidate(MultipartFile file) throws IOException, NoCandidateToUpDateException;
	
	List<Candidate> getAllCandidate() throws NoCandidatePresentException;
	

}
