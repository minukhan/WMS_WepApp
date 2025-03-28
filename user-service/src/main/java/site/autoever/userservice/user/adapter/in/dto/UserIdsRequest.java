package site.autoever.userservice.user.adapter.in.dto;

import java.util.List;

public record UserIdsRequest(
        List<Long> userIds
) {
}
