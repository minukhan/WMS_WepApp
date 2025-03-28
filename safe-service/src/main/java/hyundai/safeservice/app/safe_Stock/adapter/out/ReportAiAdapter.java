package hyundai.safeservice.app.safe_Stock.adapter.out;

import hyundai.safeservice.app.safe_Stock.adapter.dto.AiResponse;
import hyundai.safeservice.app.safe_Stock.application.port.out.ReportAiPort;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class ReportAiAdapter implements ReportAiPort {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String aiApiUrl = "https://ai.wherehouse.site/predict";  // AI API URL

    @Override
    public AiResponse getPredictedStock(String partId, int currentSales) {
        // 요청 본문 데이터
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("part_id", partId);
        requestBody.put("current_sales", currentSales);

        // 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // HTTP 요청 생성
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            // AI 서버로 POST 요청 보내기
            ResponseEntity<AiResponse> response = restTemplate.postForEntity(aiApiUrl, requestEntity, AiResponse.class);

            // 응답이 성공적이면 AiResponse 반환
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody();
            } else {
                System.err.println("⚠️ AI 예측 실패: 상태 코드 " + response.getStatusCode());
                return null;
            }
        } catch (Exception e) {
            System.err.println("❌ AI 예측 요청 실패: " + e.getMessage());
            return null;
        }
    }
}
