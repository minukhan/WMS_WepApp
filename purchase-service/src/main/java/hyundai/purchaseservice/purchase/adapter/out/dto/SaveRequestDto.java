package hyundai.purchaseservice.purchase.adapter.out.dto;

import hyundai.purchaseservice.purchase.adapter.in.dto.RegisterRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class SaveRequestDto{
    private Long id;
    private String partId;
    private Integer quantity;
    private String requester;
    private LocalDate orderedAt;
    private LocalDate expectedAt;
    private Long totalPrice;
    private String status;

    public static SaveRequestDto of(RegisterRequest registerRequest, Long partPrice, LocalDate orderedAt, LocalDate expectedAt) {
        Long totalPrice = registerRequest.quantity() * partPrice;

        return new SaveRequestDto(
                null,
                registerRequest.partId(),
                registerRequest.quantity(),
                registerRequest.name(),
                orderedAt,
                expectedAt,
                totalPrice,
                "요청 중"
        );
    }


    public static SaveRequestDto of(PartIdAndQuantityResponse partQuantity, Long partPrice, LocalDate orderedAt, LocalDate expectedAt) {
        Long totalPrice = partQuantity.quantity() * partPrice;

        return new SaveRequestDto(
                null,
                partQuantity.partId(),
                partQuantity.quantity(),
                "자동 발주",
                orderedAt,
                expectedAt,
                totalPrice,
                "요청 중"
        );
    }
}
