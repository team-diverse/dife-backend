package com.dife.api;

import com.dife.api.config.AWSConfig;
import com.dife.api.config.PasswordEncoderConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

class DifeApplicationTests {

	@MockBean AWSConfig awsConfig;
	@MockBean PasswordEncoderConfig passwordEncoderConfig;

	@Test
	void contextLoads() {}
}
