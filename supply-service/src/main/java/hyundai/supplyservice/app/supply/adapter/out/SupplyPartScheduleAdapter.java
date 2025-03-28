package hyundai.supplyservice.app.supply.adapter.out;

import hyundai.supplyservice.app.infrastructure.repository.SupplyPartScheduleRepository;
import hyundai.supplyservice.app.supply.application.entity.SupplyPartSchedule;
import hyundai.supplyservice.app.supply.application.entity.SupplyPartScheduleId;
import hyundai.supplyservice.app.supply.application.port.out.SupplyPartSchedulePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SupplyPartScheduleAdapter implements SupplyPartSchedulePort {
    private final SupplyPartScheduleRepository supplyPartScheduleRepository;

    @Override
    public Optional<SupplyPartSchedule> findById(SupplyPartScheduleId id) {
        return supplyPartScheduleRepository.findById(id);
    }

    @Override
    public List<SupplyPartSchedule> findAllByScheduledAtBetween(LocalDate start, LocalDate end) {
        return supplyPartScheduleRepository.findAllByIdScheduledAtBetween(start, end);
    }

    @Override
    public SupplyPartSchedule save(SupplyPartSchedule supplyPartSchedule) {
        return supplyPartScheduleRepository.save(supplyPartSchedule);
    }

    @Override
    public List<SupplyPartSchedule> findAllByDate(LocalDate date) {
        return supplyPartScheduleRepository.findAllByDate(date);
    }

    @Override
    public Long sumTotalRequestedQuantityBetweenDates(LocalDate start, LocalDate end) {
        return supplyPartScheduleRepository.sumTotalRequestedQuantityBetweenDates(start, end);
    }

    @Override
    public List<SupplyPartSchedule> findByIdScheduledAtBetweenAndIdPartId(
            LocalDate startDate,
            LocalDate endDate,
            String partId
    ){
        return supplyPartScheduleRepository.findByIdScheduledAtBetweenAndIdPartId(startDate, endDate, partId);
    }
}
