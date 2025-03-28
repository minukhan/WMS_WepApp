package hyundai.purchaseservice.purchase.application.service;

import hyundai.purchaseservice.purchase.adapter.in.dto.ReportPurchaseAutoStockSummaryResponse;
import hyundai.purchaseservice.purchase.adapter.in.dto.ReportPurchaseExpensesSummaryResponse;
import hyundai.purchaseservice.purchase.adapter.in.dto.ReportPurchaseSummaryResponse;
import hyundai.purchaseservice.purchase.adapter.out.dto.GetMonthExpensesDto;
import hyundai.purchaseservice.purchase.adapter.out.dto.GetMonthRequestDto;
import hyundai.purchaseservice.purchase.adapter.out.dto.PartIdAndPriceResponse;
import hyundai.purchaseservice.purchase.adapter.out.dto.PartIdAndQuantityResponse;
import hyundai.purchaseservice.purchase.adapter.out.dto.feign.PartWithSupplierDto;
import hyundai.purchaseservice.purchase.application.dto.ReportPurchaseExpensesDto;
import hyundai.purchaseservice.purchase.application.dto.ReportPurchasePartAutoStockInfoDto;
import hyundai.purchaseservice.purchase.application.dto.ReportPurchasePartExpensesInfoDto;
import hyundai.purchaseservice.purchase.application.dto.ReportPurchaseRequestDto;
import hyundai.purchaseservice.purchase.application.port.in.ReportUseCase;
import hyundai.purchaseservice.purchase.application.port.out.ReportPort;
import hyundai.purchaseservice.purchase.application.port.out.feign.PartServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService implements ReportUseCase {
    private final ReportPort reportPort;
    private final PartServiceClient partServiceClient;

    @Override
    public ReportPurchaseSummaryResponse createSummary(Integer year, Integer month) {
        // 지출비 전체에 대한 변화와 부품 요청수 전체에 대한 변화를 조회
        LocalDate from = YearMonth.of(year, month).minusMonths(1).atDay(1);
        LocalDate to = YearMonth.of(year, month).atEndOfMonth();

        // 1. 지출비
        List<GetMonthExpensesDto> monthExpenses = reportPort.getMonthExpenses(from, to);
        long currentExpense = monthExpenses.stream()
                .filter(expense -> Objects.equals(expense.year(), year) && Objects.equals(expense.month(), month))
                .mapToLong(GetMonthExpensesDto::expenses)
                .findFirst()
                .orElse(0L);

        long lastMonthExpense = monthExpenses.stream()
                .filter(expense -> !Objects.equals(expense.year(), year) || !Objects.equals(expense.month(), month))
                .mapToLong(GetMonthExpensesDto::expenses)
                .findFirst()
                .orElse(0L);

        // 2. 부품 요청 수
        List<GetMonthRequestDto> monthRequest = reportPort.getMonthRequest(from, to);
        long currentRequest = monthRequest.stream()
                .filter(request -> Objects.equals(request.year(), year) && Objects.equals(request.month(), month))
                .mapToLong(GetMonthRequestDto::requestCount)
                .findFirst()
                .orElse(0L);

        long lastMonthRequest = monthRequest.stream()
                .filter(request -> !Objects.equals(request.year(), year) || !Objects.equals(request.month(), month))
                .mapToLong(GetMonthRequestDto::requestCount)
                .findFirst()
                .orElse(0L);

        return new ReportPurchaseSummaryResponse(
                ReportPurchaseExpensesDto.to(currentExpense, lastMonthExpense),
                ReportPurchaseRequestDto.to(currentRequest, lastMonthRequest)
        );
    }

    @Override
    public ReportPurchaseAutoStockSummaryResponse createAutoStockSummary(Integer year, Integer month) {
        // 특정 달의 자동 발주된 수량
        YearMonth currentMonth = YearMonth.of(year, month);
        LocalDate from = currentMonth.atDay(1);
        LocalDate to = currentMonth.atEndOfMonth();

        String requesterName = "자동 발주";
        List<PartIdAndQuantityResponse> topTen = reportPort.getTopTenCurrentMonthAutoRequest(requesterName, from, to);

        if (topTen.isEmpty()) {
            return new ReportPurchaseAutoStockSummaryResponse(List.of());
        }

        Map<String, Integer> currentMonthInfo = topTen.stream()
                .collect(Collectors.toMap(
                   PartIdAndQuantityResponse::partId,
                   PartIdAndQuantityResponse::quantity
                ));

        // 상위 10개의 part id 지난 자동 발주된 수량을 구한다.
        from = currentMonth.minusMonths(1).atDay(1);
        to = currentMonth.minusMonths(1).atEndOfMonth();

        List<String> partIds = new ArrayList<>(currentMonthInfo.keySet());
        Map<String, Integer> lastMonthInfo = reportPort.getLastMonthAutoRequestByPartIds(partIds, requesterName, from, to).stream()
                .collect(Collectors.toMap(
                        PartIdAndQuantityResponse::partId,
                        PartIdAndQuantityResponse::quantity
                ));

        // 품목 명을 가져온다.
        Map<String, String> partNames = partServiceClient.getPartInfoList(partIds).partDtos().stream()
                .collect(Collectors.toMap(
                        PartWithSupplierDto::partId,
                        PartWithSupplierDto::partName
                ));

        List<ReportPurchasePartAutoStockInfoDto> parts = topTen.stream().map(part ->
                ReportPurchasePartAutoStockInfoDto.of(
                        partNames.get(part.partId()),
                        currentMonthInfo.get(part.partId()),
                        lastMonthInfo.get(part.partId()) == null ? 0L : lastMonthInfo.get(part.partId())
                )
        ).toList();

        return new ReportPurchaseAutoStockSummaryResponse(parts);
    }

    @Override
    public ReportPurchaseExpensesSummaryResponse createExpensesSummary(Integer year, Integer month) {
        // 특정 달의 지출비 상위 10개의 part id와 금액
        YearMonth currentMonth = YearMonth.of(year, month);
        LocalDate from = currentMonth.atDay(1);
        LocalDate to = currentMonth.atEndOfMonth();

        List<PartIdAndPriceResponse> topTen = reportPort.getTopTenCurrentMonthExpenses(from, to);

        if (topTen.isEmpty()) {
            return new ReportPurchaseExpensesSummaryResponse(List.of());
        }

        Map<String, Long> currentMonthInfo = topTen.stream()
                .collect(Collectors.toMap(
                        PartIdAndPriceResponse::partId,
                        PartIdAndPriceResponse::totalPrice
                ));

        // 상위 10개의 part id 지난 달 지출비를 구한다.
        from = currentMonth.minusMonths(1).atDay(1);
        to = currentMonth.minusMonths(1).atEndOfMonth();

        List<String> partIds = new ArrayList<>(currentMonthInfo.keySet());
        Map<String, Long> lastMonthInfo = reportPort.getLastMonthExpensesByPartIds(partIds, from, to).stream()
                .collect(Collectors.toMap(
                        PartIdAndPriceResponse::partId,
                        PartIdAndPriceResponse::totalPrice
                ));

        // 품목 명을 가져온다.
        Map<String, String> partNames = partServiceClient.getPartInfoList(partIds).partDtos().stream()
                .collect(Collectors.toMap(
                        PartWithSupplierDto::partId,
                        PartWithSupplierDto::partName
                ));

        List<ReportPurchasePartExpensesInfoDto> parts = topTen.stream().map(part ->
                ReportPurchasePartExpensesInfoDto.of(
                        partNames.get(part.partId()),
                        currentMonthInfo.get(part.partId()),
                        lastMonthInfo.get(part.partId()) == null ? 0L : lastMonthInfo.get(part.partId())
                )
        ).toList();

        return new ReportPurchaseExpensesSummaryResponse(parts);
    }
}
