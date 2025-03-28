package site.autoever.authservice.auth.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

public record KeycloakUserInfoDto(
        String sub,                         // 사용자 ID (Subject)
        String upn,                         // 사용자 이름
        @JsonProperty("email_verified")     // 이메일 검증 여부 (JSON 속성 매핑)
        boolean emailVerified,
        Set<String> groups,                // 그룹 리스트 (권한 정보 등)
        @JsonProperty("phone_number")       // JSON 속성 매핑
        String phoneNumber,
        @JsonProperty("preferred_username") // 선호 사용자명
        String preferredUsername,
        String email,                       // 이메일 주소
        @JsonProperty("customAddress")      // 사용자 정의 주소 (JSON 속성 매핑)
        String customAddress
) {
}
