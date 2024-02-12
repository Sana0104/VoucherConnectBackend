package com.voucher.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.voucher.entity.Candidate;
import com.voucher.exceptions.NoCandidatePresentException;
import com.voucher.exceptions.NoCandidateToUpDateException;
import com.voucher.helper.ExcelHelper;
import com.voucher.repository.CandidateRepository;

@Service
public class CandidateServiceImpl implements CandidateService{
	
	@Autowired
	private CandidateRepository candidateRepo;

	@Override
	public int saveAllCandidate(MultipartFile file) throws IOException, NoCandidateToUpDateException {
		
		List<Candidate> convertedCandidateList = ExcelHelper.convertExcelToListOfCandidate(file.getInputStream());
		List<Candidate> listToBeUpload = new ArrayList<Candidate>();
		
		List<Candidate> listOfCandidate= candidateRepo.findAll();
		
		List<String> listOfCandidateEmail= new ArrayList<String>();
		
		for(Candidate c:listOfCandidate)
		{
			listOfCandidateEmail.add(c.getEmail());
		}
		
		int count=0;
		
		for(Candidate c:convertedCandidateList)
		{
			if(!listOfCandidateEmail.contains(c.getEmail()))
			{
				listToBeUpload.add(c);
			}
			else if(listOfCandidateEmail.contains(c.getEmail()))
			{
				Candidate cand = candidateRepo.findByEmail(c.getEmail());
				if(!cand.getStatus().equalsIgnoreCase(c.getStatus()))
				{
					cand.setStatus(c.getStatus());
					candidateRepo.save(cand);
					count++;
				}
			}
		}
		
		if(listToBeUpload.isEmpty()  && count==0) 
		{
			throw new NoCandidateToUpDateException();
		}
		
		List<Candidate> list  = candidateRepo.saveAll(listToBeUpload);
		
		if(listToBeUpload.isEmpty())
		{
			return count;
		}
		
		return list.size()+count;
	}

	@Override
	public List<Candidate> getAllCandidate() throws NoCandidatePresentException {
		
		List<Candidate> list = candidateRepo.findAll();
		
		if(list.isEmpty())
		{
			throw new NoCandidatePresentException();
		}
		return list;
	}

}
