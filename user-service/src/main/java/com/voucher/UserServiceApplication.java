package com.voucher;
 
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
 
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
 
@SpringBootApplication
@EnableMongoRepositories
@OpenAPIDefinition(info=@Info(title="Voucher-Service API-DOCUMENTATION",
		version="1.0",
		description="Voucher service Dashboard.",
		contact  = @Contact(
        name = "Team voucher Dashboard",
        email = "sanatabasum693@gmail.com"
    )))
@SecurityScheme(name = "api", scheme = "bearer", type = SecuritySchemeType.HTTP,bearerFormat = "JWT",in = SecuritySchemeIn.HEADER)
public class UserServiceApplication {
 
	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}
 
}