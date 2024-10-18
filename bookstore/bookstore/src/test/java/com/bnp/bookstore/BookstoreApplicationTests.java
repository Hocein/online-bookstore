package com.bnp.bookstore;

import com.bnp.bookstore.security.JwtRequestFilter;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class BookstoreApplicationTests {

	@MockBean
	private JwtRequestFilter jwtRequestFilter;

	@Test
	void contextLoads() {
		// Checks that the Spring Boot application context loads without issues
	}
}