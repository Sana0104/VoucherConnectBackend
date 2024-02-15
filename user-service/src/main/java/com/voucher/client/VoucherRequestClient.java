package com.voucher.client;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;


import com.voucher.dto.VoucherRequest;
import com.voucher.dto.VoucherRequestDto;
import com.voucher.errordecoder.CustomErrorDecoder;

@FeignClient(url = "http://localhost:8085/requests",name = "voucher-request",configuration = CustomErrorDecoder.class)
public interface VoucherRequestClient {
	
	@PostMapping(value = "/voucher", consumes = {"application/json", "multipart/form-data"}) //post request to request for the voucher
    public ResponseEntity<VoucherRequest> requestVoucher(@RequestPart("data") VoucherRequestDto request,@RequestPart("image") MultipartFile file);
	
    @GetMapping("/{candidateEmail}")
    public ResponseEntity<List<VoucherRequest>> getAllVouchersByCandidateEmail(@PathVariable String candidateEmail);
    
    @PutMapping("/updateExamDate/{voucherCode}/{newExamDate}")
	public ResponseEntity<VoucherRequest> updateExamDate(@PathVariable String voucherCode,@PathVariable LocalDate newExamDate);
		

    @PutMapping("/updateExamResult/{voucherCode}/{newExamResult}")
    public ResponseEntity<VoucherRequest> updateResultStatus( @PathVariable String voucherCode, @PathVariable String newExamResult);
    
    @GetMapping("/assignvoucher/{voucherId}/{emailId}/{voucherrequestId}")
    public ResponseEntity<VoucherRequest> assignVoucher(@PathVariable String voucherId,@PathVariable String emailId,@PathVariable String voucherrequestId);
    
    @GetMapping("/getAllVouchers")
    public ResponseEntity<List<VoucherRequest>> getAllVouchers();
    
    @GetMapping("/allAssignedVoucher")
    public ResponseEntity<List<VoucherRequest>> getAllAssignedVoucher();
    
    @GetMapping("/allUnAssignedVoucher")
    public ResponseEntity<List<VoucherRequest>> getAllUnAssignedVoucher();
    
    @GetMapping("/getAllCompletedVoucherRequests")
    public ResponseEntity<List<VoucherRequest>> getAllCompletedVoucherRequests();
    
    @GetMapping("/sendPendingEmails")
    public ResponseEntity<List<String>> pendingEmails();
    
    @GetMapping("/pendingResultRequests")
    public ResponseEntity<List<VoucherRequest>> pendingRequests() ;
    
    @GetMapping("/getDoSelectImage/{id}")
    public ResponseEntity<byte[]> getVoucherRequestImage(@PathVariable String id);
    
    @PostMapping(value = "/uploadCertificate", consumes = {"application/json", "multipart/form-data"}) //post request to request for the voucher
    public ResponseEntity<VoucherRequest> uploadCertificate(@RequestPart("coupon") String vouchercode,@RequestPart("image") MultipartFile file);
    
    @GetMapping(value = "/getCertificate/{id}")
    public ResponseEntity<Resource> getCertificate(@PathVariable("id") String id);
       
    @GetMapping("/denyRequest/{requestId}")
    public ResponseEntity<VoucherRequest> denyRequest(@PathVariable String requestId);
    
    @PutMapping("/provideValidationNumber/{voucherRequestId}")
    public ResponseEntity<String> provideValidationNumber(@PathVariable String voucherRequestId, @RequestParam String validationNumber);
    
    @GetMapping("/getValidationNumber/{voucherRequestId}")
    public ResponseEntity<String> getValidationNumber(@PathVariable String voucherRequestId);
    
    @PatchMapping("/updateField/{voucherRequestId}")
    public ResponseEntity<VoucherRequest> updateField(@PathVariable String voucherRequestId,
                                                     @RequestBody Map<String, Object> updates);
    
    @PostMapping(value = "/uploadR2d2Screenshot", consumes = {"application/json", "multipart/form-data"}) 
    public ResponseEntity<VoucherRequest> uploadR2d2Screenshot(@RequestPart("coupon") String vouchercode,@RequestPart("image") MultipartFile file);
    
    @GetMapping(value = "/getR2d2Screenshot/{id}")
    public ResponseEntity<byte[]> getR2d2Screenshot(@PathVariable("id") String id);
        
}
