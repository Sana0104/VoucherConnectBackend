package com.voucher.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class LoginDTO {
	@Email(message = "Please Provide Correct Email",regexp = "^[a-zA-Z0-9._%+-]+@capgemini\\.com$"
			+ "")
	private String userEmail;
	@NotBlank
	private String password;
	
	
}
