package site.autoever.verifyservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class VerifyServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(VerifyServiceApplication.class, args);
    }

}
