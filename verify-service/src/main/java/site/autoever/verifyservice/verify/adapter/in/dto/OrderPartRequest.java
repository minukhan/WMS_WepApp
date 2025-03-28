package site.autoever.verifyservice.verify.adapter.in.dto;

public record OrderPartRequest(
        String partId,
        long quantity
) {
}
