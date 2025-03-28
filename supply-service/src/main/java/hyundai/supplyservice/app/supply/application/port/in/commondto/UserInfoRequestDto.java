package hyundai.supplyservice.app.supply.application.port.in.commondto;

import java.util.List;

public record UserInfoRequestDto(
        List<Long> userIds
) {
}
