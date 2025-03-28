package hyundai.safeservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients // FeignClient 활성화
public class SafeServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SafeServiceApplication.class, args);
    }

}
