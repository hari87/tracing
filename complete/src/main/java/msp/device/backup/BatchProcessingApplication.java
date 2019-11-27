package msp.device.backup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "msp.device.*")
public class BatchProcessingApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(BatchProcessingApplication.class, args);
    }
}
