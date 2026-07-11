package cl.Duoc.MS_Entrenadores;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class MsEntrenadoresApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsEntrenadoresApplication.class, args);
	}

}
