package site.autoever.verifyservice.verify.application.port.out.dto;

import site.autoever.verifyservice.verify.adapter.in.dto.OrderPartRequest;
import site.autoever.verifyservice.verify.adapter.in.dto.VerifyOrderRequest;

import java.util.List;

public record PartRequestDto(
        List<String> partIds
) {
    public static PartRequestDto from(VerifyOrderRequest request) {
        return new PartRequestDto(request.parts()
                .stream()
                .map(OrderPartRequest::partId)
                .toList());
    }
}
