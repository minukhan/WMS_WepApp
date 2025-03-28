package site.autoever.verifyservice.verify.application.port.out.dto;

import site.autoever.verifyservice.verify.adapter.in.dto.OrderPartRequest;
import site.autoever.verifyservice.verify.adapter.in.dto.VerifyOrderRequest;

import java.time.LocalDate;
import java.util.List;

public record PurchaseRequestDto(
        LocalDate due,
        List<String> partIds
) {
    public static PurchaseRequestDto from(VerifyOrderRequest request) {
        return new PurchaseRequestDto(
                request.dueDate(),
                request.parts()
                        .stream()
                        .map(OrderPartRequest::partId)
                        .toList()
        );
    }
}
