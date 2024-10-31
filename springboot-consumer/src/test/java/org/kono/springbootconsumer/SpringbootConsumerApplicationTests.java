package org.kono.springbootconsumer;

import org.junit.jupiter.api.Test;
import org.kono.springbootconsumer.controller.UserController;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class SpringbootConsumerApplicationTests {

	@Resource
	private UserController userController;

	@Test
	void test() {
		userController.testUserService();
	}

	@Test
	void contextLoads() {
	}

}
