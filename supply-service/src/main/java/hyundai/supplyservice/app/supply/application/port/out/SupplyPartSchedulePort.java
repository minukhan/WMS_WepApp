package hyundai.supplyservice.app.supply.application.port.out;

import hyundai.supplyservice.app.supply.application.entity.SupplyPartSchedule;
import hyundai.supplyservice.app.supply.application.entity.SupplyPartScheduleId;
import hyundai.supplyservice.app.supply.application.port.in.commondto.PartCountDto;
import hyundai.supplyservice.app.supply.application.port.in.schedule.DailySupplyScheduleResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SupplyPartSchedulePort {
    Optional<SupplyPartSchedule> findById(SupplyPartScheduleId id);

    List<SupplyPartSchedule> findAllByScheduledAtBetween(LocalDate start, LocalDate end);

    SupplyPartSchedule save(SupplyPartSchedule supplyPartSchedule);

    // 해당일의 모든 출고 부품
    List<SupplyPartSchedule> findAllByDate(LocalDate date);

    // 현재부터 입력된 날까지 총 출고될 수량 계산
    Long sumTotalRequestedQuantityBetweenDates(LocalDate startDate, LocalDate endDate);


    List<SupplyPartSchedule> findByIdScheduledAtBetweenAndIdPartId(LocalDate startDate, LocalDate endDate, String partId);


}
