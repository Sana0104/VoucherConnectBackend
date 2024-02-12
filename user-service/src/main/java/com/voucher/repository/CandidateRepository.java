package com.voucher.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.voucher.entity.Candidate;

@Repository
public interface CandidateRepository extends MongoRepository<Candidate, String>{

	
	Candidate findByEmail(String email);
}
