package hyundai.supplyservice.app.supply.application.port.out;

import hyundai.supplyservice.app.supply.application.entity.SupplySchedule;
import hyundai.supplyservice.app.supply.application.port.in.statistic.MonthlyTopPartsResponseDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface SupplySchedulePort {
    SupplySchedule save(SupplySchedule supplySchedule);

    List<SupplySchedule> findByScheduledAtBetween(LocalDateTime start, LocalDateTime end);

    List<LocalDate> findDistinctDatesByYearAndMonth(int year, int month);


}
