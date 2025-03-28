package site.autoever.reportservice.infrastructure.config;

import feign.Request;
import feign.Retryer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Bean
    public Request.Options requestOptions() {
        return new Request.Options(300000, 300000); // connectTimeout, readTimeout (ms)
    }

    @Bean
    public Retryer feignRetryer() {
        return new Retryer.Default(1000, 5000, 3); // 1000ms 간격으로 최대 3번 재시도
    }
}
