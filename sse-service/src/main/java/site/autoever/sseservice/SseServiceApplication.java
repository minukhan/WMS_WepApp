package site.autoever.sseservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class SseServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SseServiceApplication.class, args);
    }

}
