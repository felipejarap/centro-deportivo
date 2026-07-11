package duoc.cl.ms_plans;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class MsPlansApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsPlansApplication.class, args);
	}

}
