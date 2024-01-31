package com.voucher.controller;
 
import java.time.LocalDate;
import java.util.List;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.voucher.client.VoucherRequestClient;
import com.voucher.dto.VoucherRequest;
import com.voucher.dto.VoucherRequestDto;
 
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
 
@RestController
@RequestMapping("/requests")
@EnableFeignClients(basePackages = "com.*")
@CrossOrigin("*")
public class VoucherRequestClientController {
	@Autowired
	VoucherRequestClient voucherReqClient;
	@PostMapping("/voucher")
	@PreAuthorize("hasAnyRole('CANDIDATE')")
	@SecurityRequirement(name = "api")
    public ResponseEntity<VoucherRequest> requestVoucher(@RequestBody VoucherRequestDto request){
		return voucherReqClient.requestVoucher(request);
	}
    @GetMapping("/{candidateEmail}")
    @PreAuthorize("hasAnyRole('CANDIDATE')")
    @SecurityRequirement(name = "api")
    public ResponseEntity<List<VoucherRequest>> getAllVouchersByCandidateEmail(@PathVariable String candidateEmail){
    	return voucherReqClient.getAllVouchersByCandidateEmail(candidateEmail);
    }
    @PutMapping("/updateExamDate/{voucherCode}/{newExamDate}")
    @PreAuthorize("hasAnyRole('CANDIDATE')")
    @SecurityRequirement(name = "api")
	public ResponseEntity<VoucherRequest> updateExamDate(@PathVariable String voucherCode,@PathVariable LocalDate newExamDate){
    	return voucherReqClient.updateExamDate(voucherCode, newExamDate);
    }

 
    @PutMapping("/{voucherCode}/{newExamResult}")
    @PreAuthorize("hasAnyRole('CANDIDATE')")
    @SecurityRequirement(name = "api")
    public ResponseEntity<VoucherRequest> updateResultStatus( @PathVariable String voucherCode, @PathVariable String newExamResult){
    	return voucherReqClient.updateResultStatus(voucherCode, newExamResult);
    }
    @GetMapping("/assignvoucher/{voucherId}/{emailId}/{voucherrequestId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @SecurityRequirement(name = "api")
    public ResponseEntity<VoucherRequest> assignVoucher(@PathVariable String voucherId,@PathVariable String emailId,@PathVariable String voucherrequestId){
    	return voucherReqClient.assignVoucher(voucherId, emailId, voucherrequestId);
    }
    @GetMapping("/getAllVouchers")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @SecurityRequirement(name = "api")
    public ResponseEntity<List<VoucherRequest>> getAllVouchers(){
    	return voucherReqClient.getAllVouchers();
    }
    @GetMapping("/allAssignedVoucher")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @SecurityRequirement(name = "api")
    public ResponseEntity<List<VoucherRequest>> getAllAssignedVoucher(){
    	return voucherReqClient.getAllAssignedVoucher();
    }
    @GetMapping("/allUnAssignedVoucher")
    @PreAuthorize("hasAnyRole('ADMIN')")
     @SecurityRequirement(name = "api")
    public ResponseEntity<List<VoucherRequest>> getAllUnAssignedVoucher(){
    	return voucherReqClient.getAllUnAssignedVoucher();
    }
    @GetMapping("/getAllCompletedVoucherRequests")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @SecurityRequirement(name = "api")
    public ResponseEntity<List<VoucherRequest>> getAllCompletedVoucherRequests(){
    	return voucherReqClient.getAllCompletedVoucherRequests();
    }

	@GetMapping("/sendPendingEmails")
	@PreAuthorize("hasAnyRole('ADMIN')")
	@SecurityRequirement(name = "api")
	public ResponseEntity<List<String>> pendingEmails() {
		return  voucherReqClient.pendingEmails();
	}

	@GetMapping("/pendingResultRequests")
	@PreAuthorize("hasAnyRole('ADMIN')")
	@SecurityRequirement(name = "api")
    public ResponseEntity<List<VoucherRequest>> pendingRequests() {
		return voucherReqClient.pendingRequests();
	}
	
	@GetMapping("/getDoSelectImage/{id}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	@SecurityRequirement(name = "api")
    public ResponseEntity<byte[]> getVoucherRequestImage(@PathVariable String id){
    	return voucherReqClient.getVoucherRequestImage(id);
    }
    
    @PostMapping(value = "/uploadCertificate", consumes = {"application/json", "multipart/form-data"}) //post request to request for the voucher
    @PreAuthorize("hasAnyRole('CANDIDATE')")
	@SecurityRequirement(name = "api")
    public ResponseEntity<VoucherRequest> uploadCertificate(@RequestPart("coupon") String vouchercode,@RequestPart("image") MultipartFile file){
    	return voucherReqClient.uploadCertificate(vouchercode, file);
    }
    
    @GetMapping(value = "/getCertificate/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
	@SecurityRequirement(name = "api")
    public ResponseEntity<Resource> getCertificate(@PathVariable("id") String id){
    	return voucherReqClient.getCertificate(id);
    }
 
	@SecurityRequirement(name = "api")
	@PreAuthorize("hasAnyRole('ADMIN')")
	@GetMapping("/denyRequest/{requestId}")
	public ResponseEntity<VoucherRequest> denyRequest(@PathVariable String requestId) {
		return voucherReqClient.denyRequest(requestId);
 
	}
}