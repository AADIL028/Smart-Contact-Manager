package com.scm;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.scm.services.EmailService;

@SpringBootTest
class ScmApplicationTests {
	@Autowired
	private EmailService emailService;
	@Test
	void contextLoads() {
	}

	@Disabled
	@Test
	void emailServiceTest(){
		emailService.sendEmail("paadil820@gmail.com", "Testing", "Testing email from scm");
	}
}
