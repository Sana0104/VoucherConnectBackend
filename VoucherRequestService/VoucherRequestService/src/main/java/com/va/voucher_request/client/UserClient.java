package com.va.voucher_request.client;



import java.util.Optional;
 
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
 
import com.va.voucher_request.dto.User;
import com.va.voucher_request.errordecoder.CustomErrorDecoder;
 
@FeignClient(url = "http://localhost:9092/user",name = "user-service",configuration = CustomErrorDecoder.class)
public interface UserClient {
 
	 @GetMapping("/getUserByName/{name}")
	    public ResponseEntity<Optional<User>> getUserByName(@PathVariable String name);
}
