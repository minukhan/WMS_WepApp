package hyundai.purchaseservice.purchase.application.service;

import hyundai.purchaseservice.purchase.adapter.in.dto.ExpectedQuantityRequest;
import hyundai.purchaseservice.purchase.adapter.in.dto.ExpectedQuantityResponse;
import hyundai.purchaseservice.purchase.adapter.in.dto.ExpectedQuantityTomorrowResponse;
import hyundai.purchaseservice.purchase.adapter.out.dto.GetProgressPercentResponse;
import hyundai.purchaseservice.purchase.application.dto.ExpectedQuantityTomorrowDto;
import hyundai.purchaseservice.purchase.application.dto.PartQuantityInfoDto;
import hyundai.purchaseservice.purchase.adapter.out.dto.PartIdAndQuantityResponse;
import hyundai.purchaseservice.purchase.application.exception.LocalDateError;
import hyundai.purchaseservice.purchase.application.port.in.ExpectedQuantityUseCase;
import hyundai.purchaseservice.purchase.application.port.out.PurchaseSchedulePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class ExpectedQuantityService implements ExpectedQuantityUseCase {
    private final PurchaseSchedulePort purchaseSchedulePort;

    @Override
    public ExpectedQuantityResponse prefixSumStockAmount(ExpectedQuantityRequest request) {
        if(request.due().isBefore(LocalDate.now())) {
            throw new LocalDateError(request.due().toString());
        }
        if(request.partIds().isEmpty()) {return new ExpectedQuantityResponse(0L, List.of());}

        List<PartIdAndQuantityResponse> response = purchaseSchedulePort.getPartIds(request.partIds(), LocalDate.now(), request.due());

        AtomicReference<Long> total = new AtomicReference<>(0L);
        List<PartQuantityInfoDto> infoDtos = request.partIds().stream().map(partId -> {
            Long currentStock = response.stream()
                    .filter(res -> res.partId().equals(partId))
                    .mapToLong(PartIdAndQuantityResponse::quantity).sum();
            total.updateAndGet(v -> v + currentStock);
            return new PartQuantityInfoDto(partId, currentStock);
        }).toList();

        return new ExpectedQuantityResponse(total.get(), infoDtos);
    }

    @Override
    public ExpectedQuantityTomorrowResponse getTomorrowStockList() {
        List<GetProgressPercentResponse> scheduleList = purchaseSchedulePort.getTomorrowSchedule();

        List<ExpectedQuantityTomorrowDto> response = scheduleList.stream()
                .map(ExpectedQuantityTomorrowDto::of)
                .toList();

        return new ExpectedQuantityTomorrowResponse(response);
    }
}
