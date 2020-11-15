package com.example.gmail;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
class GmailApplicationTests {

	@Test
	void contextLoads() {
		UUID uuid = UUID.randomUUID();
		String uuidString = uuid.toString();
	}

}
