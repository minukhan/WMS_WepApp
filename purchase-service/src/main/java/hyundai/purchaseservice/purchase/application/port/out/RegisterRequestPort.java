package hyundai.purchaseservice.purchase.application.port.out;

import hyundai.purchaseservice.purchase.adapter.out.dto.SaveRequestDto;
import hyundai.purchaseservice.purchase.adapter.out.dto.StoreDeliveryAmountBySectionIdDto;

import java.time.LocalDate;
import java.util.List;

public interface RegisterRequestPort {
    void save(SaveRequestDto saveDto, List<List<Long>> scheduleList);

    Long checkRequestExist(String partId, LocalDate when);

    List<StoreDeliveryAmountBySectionIdDto> storeUntilArrival(List<Long> sectionIds, LocalDate to);
    List<StoreDeliveryAmountBySectionIdDto> allSectionStoreUntilArrival(LocalDate to);
    Long getTotalStoreAmountUntilArrivalDate(LocalDate to);
}
