package com.va.voucher_request.contoller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import com.va.voucher_request.client.VoucherClient;
import com.va.voucher_request.dto.Voucher;
import com.va.voucher_request.exceptions.ExamNotPassedException;
import com.va.voucher_request.exceptions.NoCompletedVoucherRequestException;
import com.va.voucher_request.exceptions.NoVoucherPresentException;
import com.va.voucher_request.exceptions.NotAnImageFileException;
import com.va.voucher_request.exceptions.NotFoundException;
import com.va.voucher_request.exceptions.ParticularVoucherIsAlreadyAssignedException;
import com.va.voucher_request.exceptions.ResourceAlreadyExistException;
import com.va.voucher_request.exceptions.ScoreNotValidException;
import com.va.voucher_request.exceptions.VoucherIsAlreadyAssignedException;
import com.va.voucher_request.exceptions.VoucherNotFoundException;
import com.va.voucher_request.model.VoucherRequest;
import com.va.voucher_request.model.VoucherRequestDto;
import com.va.voucher_request.service.EmailRequestImpl;
import com.va.voucher_request.service.VoucherReqServiceImpl;

@RestController
@RequestMapping("/requests")
@CrossOrigin("*")
public class VoucherReqController {
	
	@Autowired
	private VoucherReqServiceImpl vservice;
	@Autowired
	
	EmailRequestImpl impl;
 
	@Autowired
	VoucherClient voucherClient;
	
	@Value("${project.image}")
	private String path;
	
	@PostMapping(value = "/voucher", consumes = {"application/json", "multipart/form-data"}) //post request to request for the voucher
    public ResponseEntity<VoucherRequest> requestVoucher(@RequestPart("data") VoucherRequestDto request,@RequestPart("image") MultipartFile file) throws ScoreNotValidException, ResourceAlreadyExistException, NotAnImageFileException, IOException {
		VoucherRequest req = vservice.requestVoucher(request,file,path);
		return new ResponseEntity<>(req,HttpStatus.OK);
    }

    @GetMapping("/{candidateEmail}") //get request to view all the vouchers by candidate's email
    public ResponseEntity<List<VoucherRequest>> getAllVouchersByCandidateEmail(@PathVariable String candidateEmail) throws NotFoundException {
    	List<VoucherRequest> list = vservice.getAllVouchersByCandidateEmail(candidateEmail);
    	return new ResponseEntity<>(list,HttpStatus.OK);
    }
    
    @PutMapping("/updateExamDate/{voucherCode}/{newExamDate}") //put request to update the exam date 
	public ResponseEntity<VoucherRequest> updateExamDate(@PathVariable String voucherCode,@PathVariable LocalDate newExamDate) throws NotFoundException {
    	VoucherRequest req = vservice.updateExamDate(voucherCode, newExamDate);
    	return new ResponseEntity<>(req, HttpStatus.OK);
    }
		

    @PutMapping("/{voucherCode}/{newExamResult}") //put request to update the exam result
    public ResponseEntity<VoucherRequest> updateResultStatus( @PathVariable String voucherCode, @PathVariable String newExamResult) throws NotFoundException {
       
            VoucherRequest updatedVoucherRequest = vservice.updateExamResult(voucherCode, newExamResult);
          return new ResponseEntity<>(updatedVoucherRequest,HttpStatus.OK);

    }
    
@GetMapping("/assignvoucher/{voucherId}/{emailId}/{voucherrequestId}")
	
    public ResponseEntity<VoucherRequest> assignVoucher(@PathVariable String voucherId,@PathVariable String emailId,@PathVariable String voucherrequestId) throws NotFoundException, VoucherNotFoundException, VoucherIsAlreadyAssignedException, ParticularVoucherIsAlreadyAssignedException {
    	Voucher voucher = voucherClient.getVoucherById(voucherId).getBody();
    	VoucherRequest request = vservice.assignVoucher(voucherId,emailId,voucherrequestId);
    	String msg = "Hi " +request.getCandidateName() + ",\r\n"
    	        + "\r\n"
    	        + "Please find the Pearson Exam voucher code for " + voucher.getExamName() + ": " + voucher.getVoucherCode() + " [Please schedule the exam directly without converting.]\r\n"
    	        + "\r\n"
    	        + "EXAM TO BE SCHEDULED AT THE NEAREST PEARSON VUE CENTRE ONLY. \r\n"
    	        + "\r\n"
    	        + "YOUR NAME IN AWS ACCOUNT AND ID PROOF SHOULD MATCH, ANY DISCREPANCY IN THE NAME ON ID PROOF WILL RESULT IN CANCELLATION OF EXAM AND VOUCHER.\r\n"
    	        + " " + "\r\n All the best 😊" +
    	        "\r\n Thanks and Regards,"
    	        + "\r\nTulsi Rao"
;
    	impl.sendEmail(emailId, voucher.getCloudPlatform()+ " Voucher ", msg);
    	return new ResponseEntity<>(request, HttpStatus.OK);
    			
    }

    
    @GetMapping("/getAllVouchers") //get request to view all the vouchers
    public ResponseEntity<List<VoucherRequest>> getAllVouchers() throws VoucherNotFoundException{
        	List<VoucherRequest> list =	vservice.getAllVoucherRequest();
        	
        	return new ResponseEntity<>(list,HttpStatus.OK);
    }
    
    @GetMapping("/allAssignedVoucher") //get request to view all assigned vouchers
    public ResponseEntity<List<VoucherRequest>> getAllAssignedVoucher() throws NoVoucherPresentException
    {
    	List<VoucherRequest> list = vservice.getAllAssignedVoucherRequest();
    	return new ResponseEntity<>(list,HttpStatus.OK);
    }
    
    @GetMapping("/allUnAssignedVoucher") //get request to view all unassigned vouchers
    public ResponseEntity<List<VoucherRequest>> getAllUnAssignedVoucher() throws NoVoucherPresentException
    {
    	List<VoucherRequest> list = vservice.getAllNotAssignedVoucherRequest();
    	return new ResponseEntity<>(list,HttpStatus.OK);
    }

    @GetMapping("/getAllCompletedVoucherRequests")
    public ResponseEntity<List<VoucherRequest>> getAllCompletedVoucherRequests() throws NoCompletedVoucherRequestException
    {
    	List<VoucherRequest> list = vservice.getAllCompletedVoucherRequest();
    	return new ResponseEntity<>(list,HttpStatus.OK);
    }
    
    @GetMapping("/sendPendingEmails")
    public ResponseEntity<List<String>> pendingEmails() {
    	List<String> pendingEmails = vservice.pendingEmails();
    	return new ResponseEntity<>(pendingEmails, HttpStatus.OK);
    }
    @GetMapping("/pendingResultRequests")
    public ResponseEntity<List<VoucherRequest>> pendingRequests() {
    	List<VoucherRequest> pendingRequests = vservice.pendingRequests();
    	return new ResponseEntity<>(pendingRequests, HttpStatus.OK);
    }

    @PostMapping(value = "/uploadCertificate", consumes = {"application/json", "multipart/form-data"}) //post request to request for the voucher
    public ResponseEntity<VoucherRequest> uploadCertificate(@RequestPart("coupon") String vouchercode,@RequestPart("image") MultipartFile file) throws NotFoundException, ExamNotPassedException, NotAnImageFileException, IOException {
		VoucherRequest req = vservice.uploadCertificate(vouchercode,file,path);
		return new ResponseEntity<>(req,HttpStatus.OK);
    }

    @GetMapping(value = "/getCertificate/{id}")
    public ResponseEntity<Resource> getCertificate(@PathVariable("id") String id) throws NotFoundException, IOException {
        Optional<VoucherRequest> voucherRequest = vservice.findByRequestId(id);
        
        if (voucherRequest.isPresent() && voucherRequest.get().getCertificateFileImage() != null) {
            // Load the certificate file
            Path certificatePath = Paths.get(path + File.separator + voucherRequest.get().getCertificateFileImage());
            ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(certificatePath));
            
            // Determine the file extension
            String fileExtension = voucherRequest.get().getCertificateFileImage().substring(voucherRequest.get().getCertificateFileImage().lastIndexOf('.'));
            
            // Set the appropriate content type based on the file extension
            MediaType mediaType;
            if (fileExtension.equalsIgnoreCase(".pdf")) {
                mediaType = MediaType.APPLICATION_PDF;
            } else if (fileExtension.equalsIgnoreCase(".png") || fileExtension.equalsIgnoreCase(".jpg")|| fileExtension.equalsIgnoreCase(".jpeg")) {
                mediaType = MediaType.IMAGE_JPEG;
            } else {
                throw new NotFoundException("Unsupported file format");
            }

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + voucherRequest.get().getCertificateFileImage());
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(resource.contentLength())
                    .contentType(mediaType)
                    .body(resource);
        } else {
            throw new NotFoundException("Certificate not found for request ID: " + id);
        }
    }




    @GetMapping("/getDoSelectImage/{id}")
    public ResponseEntity<byte[]> getVoucherRequestImage(@PathVariable String id) throws IOException {
        // Find the voucher request by ID
        Optional<VoucherRequest> optionalVoucherRequest = vservice.findByRequestId(id);

        if (optionalVoucherRequest.isPresent()) {
            VoucherRequest voucherRequest = optionalVoucherRequest.get();
            String imagePathString = voucherRequest.getDoSelectScoreImage();
            Path imagePath = Paths.get(imagePathString);

            // Check if the file exists
            if (Files.exists(imagePath)) {
                byte[] imageBytes = Files.readAllBytes(imagePath);
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.IMAGE_JPEG); // Adjust the media type based on your image type
                return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
   
    @GetMapping("/denyRequest/{requestId}")
    public ResponseEntity<VoucherRequest> denyRequest(@PathVariable String requestId) throws NoVoucherPresentException {
    	VoucherRequest denyRequest = vservice.denyRequest(requestId);
		return new ResponseEntity<VoucherRequest>(denyRequest, HttpStatus.OK);
    	
    }
}