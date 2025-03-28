package site.autoever.logservice.infrastructure.util;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LogProcessorInitializer {

    private final AsyncLogProcessor logProcessor;

    @PreDestroy
    public void destroy() {
        logProcessor.shutdown();
    }
}
