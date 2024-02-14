package com.voucher.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.bson.types.ObjectId;

import com.voucher.entity.Candidate;

@Repository
public interface CandidateRepository extends MongoRepository<Candidate, ObjectId>{

	
	Candidate findByEmail(String email);
}