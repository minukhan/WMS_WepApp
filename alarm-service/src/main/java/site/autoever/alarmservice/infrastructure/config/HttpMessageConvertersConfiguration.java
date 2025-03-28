package site.autoever.alarmservice.infrastructure.config;

import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Configuration
public class HttpMessageConvertersConfiguration {

    @Bean
    public HttpMessageConverters messageConverters() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        return new HttpMessageConverters(converter);
    }
}
