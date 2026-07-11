package duoc.cl.ms_classes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class MsClassesApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsClassesApplication.class, args);
	}

}
