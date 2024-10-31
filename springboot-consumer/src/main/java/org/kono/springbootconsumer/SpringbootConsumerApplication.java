package org.kono.springbootconsumer;

import org.konoechoda.rpc.springboot.starter.annotation.EnableRpc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableRpc(needServer = false)
@SpringBootApplication
public class SpringbootConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootConsumerApplication.class, args);
	}

}
