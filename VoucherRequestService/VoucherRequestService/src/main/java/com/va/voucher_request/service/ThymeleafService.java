package com.va.voucher_request.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.va.voucher_request.model.VoucherRequest;
@Service
public class ThymeleafService {
     
	@Autowired
	TemplateEngine templateEngine;
	
	public String loadHTMLTemplate(String htmlTemplate,Context context) {
		
		return templateEngine.process(htmlTemplate, context);
		
	}
}