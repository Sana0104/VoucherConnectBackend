package com.va.voucher_request.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.va.voucher_request.client.UserClient;
import com.va.voucher_request.client.VoucherClient;
import com.va.voucher_request.dto.User;
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
import com.va.voucher_request.repo.VoucherRequestRepository;

@Service
@EnableFeignClients(basePackages = "com.*")
public class VoucherReqServiceImpl implements VoucherReqService {

	@Autowired
	private VoucherRequestRepository vrepo;
	
	
	@Autowired
	VoucherClient voucherClient;
	
	@Autowired
	EmailRequestImpl impl;
	
	@Autowired
	UserClient userClient;
//image get 
	@Override
	public VoucherRequest requestVoucher(VoucherRequestDto request, MultipartFile file, String path)
	        throws ScoreNotValidException, ResourceAlreadyExistException, NotAnImageFileException, IOException {
	    String userEmail = request.getCandidateEmail();
	    String examName = request.getCloudExam();

	    // Check if a voucher for the same exam and candidate already exists
	    boolean examExists = vrepo.existsByCloudExamAndCandidateEmail(examName, userEmail);

	    // If candidate has requested for the same exam again, throw an exception
	    if (examExists) {
	        throw new ResourceAlreadyExistException("You have already requested a voucher for this particular exam");
	    }

	    VoucherRequest vreq = new VoucherRequest();
	    if (request.getDoSelectScore() >= 80) {
	        String requestID = UUID.randomUUID().toString();
	        vreq.setId(requestID);
	        vreq.setCandidateName(request.getCandidateName());
	        vreq.setCandidateEmail(request.getCandidateEmail());
	        vreq.setCloudExam(request.getCloudExam());
	        vreq.setCloudPlatform(request.getCloudPlatform());
	        vreq.setDoSelectScore(request.getDoSelectScore());

	        // Create a random name for the file
	        String random = UUID.randomUUID().toString();

	        // Set the file name by appending the random string to the original filename
	        String name = random + file.getOriginalFilename();

	        // Checking if the file is an image (case-insensitive check)
	        String extension = name.substring(name.lastIndexOf('.')).toLowerCase();
	        if (!extension.equals(".png") && !extension.equals(".jpeg") && !extension.equals(".jpg")) {
	            throw new NotAnImageFileException("Invalid image file format. Supported formats: .png, .jpeg, .jpg");
	        }

	        // Fetching the full path where to store
	        String filePath = path + File.separator  + name;

	        // Creating and checking if the path exists or not
	        File f = new File(path);
	        if (!f.exists()) {
	            // If not exist, then make this directory
	            f.mkdir();
	        }

	        // File copy
	        Files.copy(file.getInputStream(), Paths.get(filePath));

	        // Updating the path into the database
	        vreq.setDoSelectScoreImage(filePath);
	        vreq.setPlannedExamDate(request.getPlannedExamDate());
	        vreq.setExamResult("Pending");
	        vrepo.save(vreq);
	        return vreq;
	    } else {
	        throw new ScoreNotValidException("doSelectScore should be >= 80 to issue a voucher.");
	    }
	}


	@Override  //get all vouchers using candidate email
	public List<VoucherRequest> getAllVouchersByCandidateEmail(String candidateEmail) throws NotFoundException {
		List<VoucherRequest> vouchersByCandidate = vrepo.findByCandidateEmail(candidateEmail);

		if (vouchersByCandidate.isEmpty()) {
			throw new NotFoundException("No vouchers found for candidate email: " + candidateEmail);
		}

		return vouchersByCandidate;
	}

	@Override   //to update the exam date by the candidate
	public VoucherRequest updateExamDate(String voucherCode, LocalDate newExamDate) throws NotFoundException {
		VoucherRequest voucherRequest = vrepo.findByVoucherCode(voucherCode);
		if (voucherRequest != null) {
			voucherRequest.setPlannedExamDate(newExamDate);
			try {
				vrepo.save(voucherRequest);
				return voucherRequest;
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("Error saving VoucherRequest");
			}
 
		} else {

			throw new NotFoundException("No voucher found for voucher code: " + voucherCode);

		}

	}

	@Override  //to update the exam exam by the candidate
	public VoucherRequest updateExamResult(String voucherCode, String newExamResult) throws NotFoundException {
		VoucherRequest voucherRequest = vrepo.findByVoucherCode(voucherCode);
		if (voucherRequest != null) {
			voucherRequest.setExamResult(newExamResult);
			try {
				vrepo.save(voucherRequest);
				//return statement
				return voucherRequest;
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("Error saving VoucherRequest");
			}

		} else {
			throw new NotFoundException("No voucher found for voucher code: " + voucherCode);

		}

	}
	//method to upload the certificate once the exam is passed
	//method2
	@Override
	public VoucherRequest uploadCertificate(String voucherCode, MultipartFile certificateFile, String path) throws ExamNotPassedException, IOException, NotAnImageFileException, NotFoundException {
	    VoucherRequest voucherRequest = vrepo.findByVoucherCode(voucherCode);

	    if (voucherRequest != null) {
	        // Check if the exam result is "pass"
	        if (!voucherRequest.getExamResult().equalsIgnoreCase("Pass")) {
	            throw new ExamNotPassedException("Certificate can only be uploaded for vouchers with exam result 'pass'.");
	        }
	        
	        String certificateFileName = certificateFile.getOriginalFilename();

	        // Validate the certificate file is an image
	        String extension = certificateFileName.substring(certificateFileName.lastIndexOf('.')).toLowerCase();
	        if (!extension.equals(".png") && !extension.equals(".jpeg") && !extension.equals(".jpg") && !extension.equals(".pdf")) {
	            throw new NotAnImageFileException("Invalid image file format. Supported formats: .png, .jpeg, .jpg");
	        }
	        

	        
	     // logic to save the certificate file to a specific location
	        String certificateFilePath = path + File.separator  + certificateFileName;

	        // Creating and checking if the path exists or not
	        File f = new File(path);
	        if (!f.exists()) {
	            // If not exist, then make this directory
	            f.mkdir();
	        }

	     // Save the certificate file and update the certificate path
	        Files.copy(certificateFile.getInputStream(), Paths.get(certificateFilePath));
	        voucherRequest.setCertificateFileImage(certificateFileName);

	        try {
	            vrepo.save(voucherRequest);
	            return voucherRequest;
	        } catch (Exception e) {
	            e.printStackTrace();
	            throw new RuntimeException("Error saving VoucherRequest");
	        }
	    } else {
	        throw new NotFoundException("No voucher found for voucher code: " + voucherCode);
	    }
	}
	

	@Override //assign the voucher to the respective candidate 
	public VoucherRequest assignVoucher(String voucherId, String emailId,String voucherrequestId) throws VoucherNotFoundException, NotFoundException, VoucherIsAlreadyAssignedException, ParticularVoucherIsAlreadyAssignedException {
		
		Voucher voucher = voucherClient.getVoucherById(voucherId).getBody();
		
		if(voucher==null)
		{
			throw new VoucherNotFoundException();
		}
		
		if(voucher.getIssuedTo()!=null) //check if the voucher is already assigned 
		{
			throw new ParticularVoucherIsAlreadyAssignedException();
		}
		
		Optional<VoucherRequest> voucherRequest = vrepo.findById(voucherrequestId);
		if(voucherRequest.isEmpty())
		{
			throw new NotFoundException("Voucher Request is Not Found");
		}
		
		
		VoucherRequest request = voucherRequest.get();
		
		if(request.getVoucherCode()!=null)
		{
			throw new VoucherIsAlreadyAssignedException();
		}
		request.setVoucherCode(voucher.getVoucherCode());
		request.setVoucherExpiryLocalDate(voucher.getExpiryDate());
		request.setVoucherIssueLocalDate(LocalDate.now());
		
		voucherClient.assignUserInVoucher(voucherId, emailId);
		return vrepo.save(request);

	}

	@Override //to view all the vouchers 
	public List<VoucherRequest> getAllVoucherRequest() throws VoucherNotFoundException {
		
		List<VoucherRequest> allRequest = vrepo.findAll();
		
		if(allRequest.isEmpty())
		{
			throw new VoucherNotFoundException();
		}
		
		return allRequest;
	}

	@Override //to view all the assigned vouchers 
	public List<VoucherRequest> getAllAssignedVoucherRequest() throws NoVoucherPresentException {
		List<VoucherRequest> allrequest = vrepo.findAll();
		
		List<VoucherRequest> assignedvouchers = new ArrayList<>();
		
		if(allrequest.isEmpty())
		{
			throw new NoVoucherPresentException();
		}
		for(VoucherRequest v:allrequest)
		{
			if(v.getVoucherCode()!=null)
			{
				assignedvouchers.add(v);
			}
		}
		
		return assignedvouchers;
	}

	@Override // to view all the unused vouchers 
	public List<VoucherRequest> getAllNotAssignedVoucherRequest() throws NoVoucherPresentException {
		List<VoucherRequest> allrequest = vrepo.findAll();
		
		List<VoucherRequest> notassignedvouchers = new ArrayList<>();
		
		if(allrequest.isEmpty())
		{
			throw new NoVoucherPresentException();
		}
		for(VoucherRequest v:allrequest)
		{
			if(v.getVoucherCode()==null)
			{
				notassignedvouchers.add(v);
			}
		}
		
		return notassignedvouchers;
	}

	@Override
	public List<VoucherRequest> getAllCompletedVoucherRequest() throws NoCompletedVoucherRequestException {
		List<VoucherRequest> allrequest = vrepo.findAll();
		
		List<VoucherRequest> filteredList = new ArrayList<>();
		for(VoucherRequest v: allrequest) {
			if(!v.getExamResult().equalsIgnoreCase("pending")) {
				filteredList.add(v);
			}
		}
		if(filteredList.isEmpty()) {
			throw new NoCompletedVoucherRequestException();
		}
		return filteredList;
	}
	
	
	@Override
	public List<String> pendingEmails() {
		List<String> pending = new ArrayList<>();
		List<VoucherRequest> allrequest = vrepo.findAll();
		LocalDate today = LocalDate.now();
		
		for(VoucherRequest v: allrequest) {
			if(v.getExamResult().equalsIgnoreCase("pending")&&v.getPlannedExamDate().isBefore(today)&&v.getVoucherCode()!=null) {
				
				pending.add(v.getCandidateEmail());
				
				Optional<User> userByName = userClient.getUserByName(v.getCandidateName()).getBody();
				User user = userByName.get();
				String mentorEmail = user.getMentorEmail();
 
		    	for (String s:pending) {
		    		String msg = "Hi "+v.getCandidateName().toUpperCase()+","+"\r\n"+
			    			 "We hope this message finds you well. It has come to our attention that your exam status for the "+v.getCloudPlatform() + " certification is currently marked as pending. " +
			    		        "To ensure a smooth process and timely updates, we kindly request you to log in to your account and update your exam status as soon as possible.\n\n" +
			    		        "Your prompt attention to this matter is greatly appreciated.\n\n" +
			    		        "Best regards,\n" +
			    		        "VOUCHER DASHBOARD TEAM";
		    		impl.sendPendingEmail(s,mentorEmail,"Urgent: Update Your Exam Status", msg);
		    		
		    		
		    	}
			}
	}
		return pending;
 
}
 
	@Override
	public List<VoucherRequest> pendingRequests() {
		List<VoucherRequest> pendingRequests = new ArrayList<>();
		List<VoucherRequest> allrequest = vrepo.findAll();
		LocalDate today = LocalDate.now();
		for(VoucherRequest v: allrequest) {
			if(v.getExamResult().equalsIgnoreCase("pending")&&v.getPlannedExamDate().isBefore(today)&&v.getVoucherCode()!=null) {
				pendingRequests.add(v);
			}
		
	}
		return pendingRequests;
}

    //method of getting message
	public Optional<VoucherRequest> findByRequestId(String id) {
		return vrepo.findById(id);
	}
	
	@Override
	public VoucherRequest denyRequest(String requestId) throws NoVoucherPresentException {
			Optional<VoucherRequest> findReqById = vrepo.findById(requestId);
			if(findReqById.isPresent()) {
				VoucherRequest re = findReqById.get();
				
				String adminMail = "boyinapalli.ravi-chandra@capgemini.com";
				 String msg = "Dear "+re.getCandidateName().toUpperCase() +
			                "\n\n" +
			                "We hope this message finds you well. We regret to inform you that your voucher request for the "+re.getCloudExam().toUpperCase()+" has been denied due to the following reasons:\n\n" +
			                "1. **Image Format Issue:**\n" +
			                "   The image you uploaded for the "+re.getCloudPlatform().toUpperCase()+" exam is not in the proper format. Please ensure that the image belongs to the "+re.getCloudExam()+" you have requested for.\n\n" +
			                "2. **Missing Image:**\n" +
			                "   You have not provided the latest DoSelect image required for the voucher issuance process. Kindly ensure that you upload the latest image before submitting the voucher request.\n\n" +
			                "3. **Minimum Score Requirement:**\n" +
			                "   Unfortunately, your DoSelect score does not meet the minimum requirement to avail a voucher for the "+re.getCloudExam().toUpperCase()+". To be eligible, candidates must achieve a minimum score of 80.\n\n" +
			                "We understand that this may be disappointing, and we encourage you to review and address the mentioned issues before attempting to request a voucher again. If you have any questions or concerns, feel free to reach out to our support team at "+adminMail+".\n\n" +
			                "Thank you for your understanding.\n\n" +
			                "Best regards,\n\n" +
			                "VOUCHER-CONNECT \n" +
			                "VOUCHER DASHBOARD TEAM";
				
				
				impl.sendEmail(re.getCandidateEmail(), "VOUCHER CONNECT - Voucher Request Denied", msg);
				vrepo.delete(re);
				return re;
			}else {
				throw new NoVoucherPresentException();
			}
	}

	
}
