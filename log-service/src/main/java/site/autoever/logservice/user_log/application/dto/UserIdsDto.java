package site.autoever.logservice.user_log.application.dto;

import java.util.List;

public record UserIdsDto(
        List<Long> userIds
) {
    public static UserIdsDto of(List<Long> userIds) {
        return new UserIdsDto(userIds);
    }
}
