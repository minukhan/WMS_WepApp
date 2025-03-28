package hyundai.supplyservice.app.infrastructure.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@Service
public class Ai {
    private final RestTemplate restTemplate = new RestTemplate();  // 직접 초기화 방식
    private final String AI_SERVICE_URL = "https://ai.wherehouse.site/predict";


    public Integer predictNextMonthQuantity(String partId, int currentQuantity) {
        try {
            // 요청 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // 요청 본문
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("part_id", partId);
            requestBody.put("current_sales", currentQuantity);

            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<AiPredictResponse> response = restTemplate.postForEntity(
                    AI_SERVICE_URL,
                    requestEntity,
                    AiPredictResponse.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                //log.info("📩 AI Service Response: {}", response.getBody());
                return response.getBody().predicted_next_month_sales();
            } else {
                //log.error("❌ AI Service Failed with Status Code: {}", response.getStatusCode());
                return Integer.MIN_VALUE;
            }


        } catch (Exception e) {
            //log.error("❌ AI Service Exception: {}", e.getMessage());
            return Integer.MIN_VALUE;

        }
    }


}
