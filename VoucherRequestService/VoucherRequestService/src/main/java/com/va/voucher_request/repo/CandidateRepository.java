package com.va.voucher_request.repo;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.bson.types.ObjectId;

import com.va.voucher_request.model.Candidate;

@Repository
public interface CandidateRepository extends MongoRepository<Candidate, ObjectId>{

	Candidate findByEmail(String email);
}