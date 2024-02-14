package com.voucher.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.voucher.entity.Candidate;
import com.voucher.exceptions.GivenFileIsNotExcelFileException;
import com.voucher.exceptions.NoCandidatePresentException;
import com.voucher.exceptions.NoCandidateToUpDateException;
import com.voucher.helper.ExcelHelper;
import com.voucher.service.CandidateService;

@RestController
@RequestMapping("/candidate")
@CrossOrigin(origins = "http://localhost:3000")
public class CandidateController {
	
	@Autowired
	CandidateService candService;
	
	@PostMapping("/saveAllCandidate")
	public ResponseEntity<String> saveAllCandidate(@RequestParam("candidates") MultipartFile file) throws IOException, NoCandidateToUpDateException, GivenFileIsNotExcelFileException
	{
		if(ExcelHelper.checkExcelFormat(file))
		{
			int count = candService.saveAllCandidate(file);
		return new ResponseEntity<String>("Total "+count+" rows changed in the database",HttpStatus.OK);
		}else {
			throw new GivenFileIsNotExcelFileException();
		}
	
	}
	
	@GetMapping("/getAllCandidate")
	public ResponseEntity<List<Candidate>> getAllCandidate() throws NoCandidatePresentException
	{
		List<Candidate> candidates = candService.getAllCandidate();
		return new ResponseEntity<List<Candidate>>(candidates,HttpStatus.OK);
	}

}
