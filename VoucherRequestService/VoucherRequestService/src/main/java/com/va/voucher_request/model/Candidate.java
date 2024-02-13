package com.va.voucher_request.model;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor

@Document(collection = "candidates")
public class Candidate {
	@MongoId
	private ObjectId id;
	private String email;
	private String practice;
	private String status;
	

}
