package site.autoever.verifyservice.verify.application.port.out.dto;

public record PurchaseInfoDto(
        String partId,
        long quantity
) {
}
