package com.voucherservice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.voucherservice.service.VoucherService;

import lombok.Value;

@SpringBootTest
class VoucherserviceApplicationTests {

    @Autowired
    private VoucherService yourService;

    @Test
    void contextLoads() {
        assertThat(yourService).isNotNull();
    }
    
    @Test
    void mainMethodExecution() {
        VoucherserviceApplication.main(new String[] {});
        // If the main method executes without throwing an exception, the test passes
    }
    
}
