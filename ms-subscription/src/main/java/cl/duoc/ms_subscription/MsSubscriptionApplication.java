package cl.duoc.ms_subscription;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients
public class MsSubscriptionApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsSubscriptionApplication.class, args);
	}

}
