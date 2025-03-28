package hyundai.purchaseservice.purchase.adapter.out;

import hyundai.purchaseservice.infrastructure.mapper.PurchaseMapper;
import hyundai.purchaseservice.infrastructure.mapper.ScheduleMapper;
import hyundai.purchaseservice.purchase.adapter.out.dto.*;
import hyundai.purchaseservice.purchase.application.port.out.PurchaseStatisticsPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PurchaseStatisticsAdapter implements PurchaseStatisticsPort {
    private final ScheduleMapper scheduleMapper;
    private final PurchaseMapper purchaseMapper;

    @Override
    public List<GetProgressPercentResponse> getProgressPercent() {
        return scheduleMapper.getProgressPercent();
    }

    @Override
    public List<GetMonthExpensesDto> getMonthExpenses() {
        return purchaseMapper.getYearlyMonthExpenses();
    }

    @Override
    public List<GetDayScheduleQuantitesResponse> getDaySchedule() {
        return scheduleMapper.getDaySchedule();
    }

    @Override
    public List<SearchResponse> getMonthRequest() {
        return purchaseMapper.searchMonthRequest();
    }

    @Override
    public List<SearchResponse> getMonthRequestByPartId(List<String> partIds) {
        return purchaseMapper.searchMonthRequestByPartId(partIds);
    }
}
