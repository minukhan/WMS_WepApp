package hyundai.purchaseservice.purchase.application.port.out;

import hyundai.purchaseservice.purchase.adapter.out.dto.GetMonthExpensesDto;
import hyundai.purchaseservice.purchase.adapter.out.dto.GetMonthRequestDto;
import hyundai.purchaseservice.purchase.adapter.out.dto.PartIdAndPriceResponse;
import hyundai.purchaseservice.purchase.adapter.out.dto.PartIdAndQuantityResponse;

import java.time.LocalDate;
import java.util.List;

public interface ReportPort {
    List<GetMonthExpensesDto> getMonthExpenses(LocalDate from, LocalDate to);
    List<GetMonthRequestDto> getMonthRequest(LocalDate from, LocalDate to);
    List<PartIdAndQuantityResponse> getTopTenCurrentMonthAutoRequest(String requesterName, LocalDate from, LocalDate to);
    List<PartIdAndQuantityResponse> getLastMonthAutoRequestByPartIds(List<String> partIds, String requesterName, LocalDate from, LocalDate to);
    List<PartIdAndPriceResponse> getTopTenCurrentMonthExpenses(LocalDate from, LocalDate to);
    List<PartIdAndPriceResponse> getLastMonthExpensesByPartIds(List<String> partIds, LocalDate from, LocalDate to);
}
