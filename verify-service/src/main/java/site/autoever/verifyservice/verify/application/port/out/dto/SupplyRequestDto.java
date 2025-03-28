package site.autoever.verifyservice.verify.application.port.out.dto;

import site.autoever.verifyservice.verify.adapter.in.dto.OrderPartRequest;
import site.autoever.verifyservice.verify.adapter.in.dto.VerifyOrderRequest;

import java.time.LocalDate;
import java.util.List;

public record SupplyRequestDto(
        LocalDate dueDate,
        List<String> partIds
) {
    public static SupplyRequestDto from(VerifyOrderRequest request) {
        return new SupplyRequestDto(
                request.dueDate(),
                request.parts()
                        .stream()
                        .map(OrderPartRequest::partId)
                        .toList()
        );
    }
}
