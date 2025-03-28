package hyundai.supplyservice.app.supply.application.port.in.commondto;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserInfoDto(
        @Schema(example = "1")
        Long userId,
        @Schema(example = "금천구 가산동")
        String address,
        @Schema(example = "abc@gmail.com")
        String email,
        @Schema(example = "홍길동")
        String name,
        @Schema(example = "010-1223-1232")
        String phoneNumber,
        @Schema(example = "ROLE_VERIFIED_USER")
        String role
) {
}
