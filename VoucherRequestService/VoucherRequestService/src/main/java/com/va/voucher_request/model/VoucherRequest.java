package com.va.voucher_request.model;
import java.time.LocalDate;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document("requests")
public class VoucherRequest {
	
	@MongoId
	private String id;
	
	private String candidateName;
	
	private String candidateEmail;
    private String cloudPlatform;
    private String cloudExam;
    private int doSelectScore;
    private String doSelectScoreImage;
    private String voucherCode;
    private LocalDate voucherIssueLocalDate;
    private LocalDate voucherExpiryLocalDate;
    private LocalDate plannedExamDate;
    private String examResult;
    private String certificateFileImage;
    private String validationNumber;
    private String r2d2Screenshot;
    
    

    

}
