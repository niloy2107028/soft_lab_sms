package com.student_management_system.niloy;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

/**
 * Basic test to verify Spring Boot application starts correctly.
 * Uses H2 in-memory database for testing.
 */
@SpringBootTest
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
class NiloyApplicationTests {

	@Test
	void contextLoads() {
		// This test passes if the Spring application context loads successfully
	}

}
