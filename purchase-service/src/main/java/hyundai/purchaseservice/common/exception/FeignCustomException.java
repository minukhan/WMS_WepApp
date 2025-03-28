package hyundai.purchaseservice.common.exception;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import hyundai.purchaseservice.purchase.application.exception.FeignResponseException;
import lombok.Data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class FeignCustomException implements ErrorDecoder {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Exception decode(String methodKey, Response response) {
        // 응답 본문(body)을 읽기
        String body = null;
        try {
            if (response.body() != null) {
                body = new BufferedReader(new InputStreamReader(response.body().asInputStream()))
                        .lines()
                        .collect(Collectors.joining("\n"));
            }
        } catch (IOException e) {
            return new RuntimeException("[Feign] Failed to read error response", e);
        }

        // JSON 파싱 및 에러 메시지 처리
        if (response.status() == 404) { // HTTP 404 상태 처리
            try {
                // JSON 응답을 매핑할 DTO 클래스
                ErrorResponse errorResponse = mapper.readValue(body, ErrorResponse.class);
                if ("PartNotFoundException".equals(errorResponse.getException())) {
                    return new FeignResponseException(errorResponse.getMessage());
                } else if ("SupplierNameNotFoundException".equals(errorResponse.getException())) {
                    return new FeignResponseException(errorResponse.getMessage());
                } else if ("SectionIdNotFoundException".equals(errorResponse.getException())) {
                    return new FeignResponseException(errorResponse.getMessage());
                } else if ("/parts/contian/name".equals(errorResponse.getAdditionalFields().get("path").toString())) {
                    return new FeignResponseException("해당하는 품목 명이 없습니다.");
                } else{
                    return new RuntimeException(errorResponse.getError());
                }
            } catch (Exception e) {
                return new RuntimeException("[Feign] Failed to parse error response: " + body, e);
            }
        }

        // 기타 기본 예외 처리
        return new RuntimeException("[Feign] 통신 오류: " + response);
    }

    @Data
    public static class ErrorResponse {
        private int status;
        private String error;
        private String message;
        private String exception;

        private Map<String, Object> additionalFields = new HashMap<>();
        @JsonAnySetter
        public void setAdditionalField(String key, Object value) {
            additionalFields.put(key, value);
        }
    }
}
