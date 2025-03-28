package hyundai.supplyservice.app.supply.adapter.out;

import hyundai.supplyservice.app.infrastructure.repository.SupplyScheduleRepository;
import hyundai.supplyservice.app.supply.application.entity.SupplySchedule;
import hyundai.supplyservice.app.supply.application.port.out.SupplySchedulePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SupplyScheduleAdapter implements SupplySchedulePort {
    private final SupplyScheduleRepository supplyScheduleJpaRepository;

    @Override
    public SupplySchedule save(SupplySchedule supplySchedule) {
        return supplyScheduleJpaRepository.save(supplySchedule);
    }

    // 해당 년도, 월의 모든 출고 일정 조회
    @Override
    public List<SupplySchedule> findByScheduledAtBetween(LocalDateTime start, LocalDateTime end) {
        return supplyScheduleJpaRepository.findByScheduledAtBetween(start, end);
    }

    @Override
    public List<LocalDate> findDistinctDatesByYearAndMonth(int year, int month) {
        return supplyScheduleJpaRepository.findDistinctDatesByYearAndMonth(year, month);
    }
}
