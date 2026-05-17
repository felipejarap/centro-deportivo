package duoc.cl.ms_medicalRecord;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsMedicalRecordApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsMedicalRecordApplication.class, args);
	}

}
