package hyundai.purchaseservice.purchase.adapter.out;

import hyundai.purchaseservice.infrastructure.mapper.PurchaseMapper;
import hyundai.purchaseservice.purchase.adapter.out.dto.GetMonthExpensesDto;
import hyundai.purchaseservice.purchase.adapter.out.dto.GetMonthRequestDto;
import hyundai.purchaseservice.purchase.adapter.out.dto.PartIdAndPriceResponse;
import hyundai.purchaseservice.purchase.adapter.out.dto.PartIdAndQuantityResponse;
import hyundai.purchaseservice.purchase.application.port.out.ReportPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ReportAdapter implements ReportPort {
    private final PurchaseMapper purchaseMapper;

    @Override
    public List<GetMonthExpensesDto> getMonthExpenses(LocalDate from, LocalDate to) {
        return purchaseMapper.getCurrentAndLastMonthExpenses(from, to);
    }

    @Override
    public List<GetMonthRequestDto> getMonthRequest(LocalDate from, LocalDate to) {
        return purchaseMapper.getCurrentAndLastMonthRequests(from, to);
    }

    @Override
    public List<PartIdAndQuantityResponse> getTopTenCurrentMonthAutoRequest(String requesterName, LocalDate from, LocalDate to) {
        return purchaseMapper.getTopTenCurrentMonthAutoRequest(requesterName, from, to);
    }

    @Override
    public List<PartIdAndQuantityResponse> getLastMonthAutoRequestByPartIds(List<String> partIds, String requesterName, LocalDate from, LocalDate to) {
        return purchaseMapper.getLastMonthAutoRequestByPartIds(partIds, requesterName, from, to);
    }

    @Override
    public List<PartIdAndPriceResponse> getTopTenCurrentMonthExpenses(LocalDate from, LocalDate to) {
        return purchaseMapper.getTopTenCurrentMonthExpenses(from, to);
    }

    @Override
    public List<PartIdAndPriceResponse> getLastMonthExpensesByPartIds(List<String> partIds, LocalDate from, LocalDate to) {
        return purchaseMapper.getLastMonthExpensesByPartIds(partIds, from, to);
    }
}
