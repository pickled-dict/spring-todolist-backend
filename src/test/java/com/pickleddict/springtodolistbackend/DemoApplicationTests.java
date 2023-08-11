package com.pickleddict.springtodolistbackend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(WebSecurityConfig.class)
class DemoApplicationTests {

	@Test
	void contextLoads() {
	}

}
