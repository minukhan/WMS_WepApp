package hyundai.purchaseservice.purchase.application.service;

import hyundai.purchaseservice.purchase.adapter.in.dto.MonthExpensesResponse;
import hyundai.purchaseservice.purchase.adapter.out.dto.GetMonthExpensesDto;
import hyundai.purchaseservice.purchase.adapter.out.dto.SearchResponse;
import hyundai.purchaseservice.purchase.application.dto.ExpensesDto;
import hyundai.purchaseservice.purchase.adapter.in.dto.ExpensesResponse;
import hyundai.purchaseservice.purchase.adapter.in.dto.FrequencyResponse;
import hyundai.purchaseservice.purchase.adapter.in.dto.ProgressChartResponse;
import hyundai.purchaseservice.purchase.adapter.out.dto.GetProgressPercentResponse;
import hyundai.purchaseservice.purchase.adapter.out.dto.GetScheduleResponse;
import hyundai.purchaseservice.purchase.adapter.out.dto.feign.PartDto;
import hyundai.purchaseservice.purchase.application.dto.FrequencyDto;
import hyundai.purchaseservice.purchase.application.dto.MonthExpensesDto;
import hyundai.purchaseservice.purchase.application.port.in.PurchaseStatisticsUseCase;
import hyundai.purchaseservice.purchase.application.port.out.PurchaseSchedulePort;
import hyundai.purchaseservice.purchase.application.port.out.PurchaseStatisticsPort;
import hyundai.purchaseservice.purchase.application.port.out.feign.PartServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class PurchaseStatisticsService implements PurchaseStatisticsUseCase {
    private final PurchaseStatisticsPort purchaseStatisticsPort;
    private final PurchaseSchedulePort purchaseSchedulePort;
    private final PartServiceClient partServiceClient;

    @Override
    public ProgressChartResponse getWorkingProgress() {
        List<GetProgressPercentResponse> todaySchedule = purchaseStatisticsPort.getProgressPercent();

        AtomicReference<Long> totalAmount = new AtomicReference<>(0L);
        AtomicReference<Long> processedAmount = new AtomicReference<>(0L);
        todaySchedule.forEach(schedule -> {
            totalAmount.updateAndGet(v -> v + schedule.requestedQuantity());
            processedAmount.updateAndGet(v -> v + schedule.processedQuantity());
        });

        return ProgressChartResponse.of(totalAmount.get(), processedAmount.get());
    }


    @Override
    public ExpensesResponse getExpensesByCategory() {
        return getByCategory(
                (responses, partIds) -> responses.stream()
                        .filter(response -> partIds.contains(response.partId()))
                        .mapToLong(SearchResponse::totalPrice).sum(),
                ExpensesDto::new,
                ExpensesResponse::new
        );
    }

    @Override
    public ExpensesResponse getExpensesByPart(String categoryName) {
        return getByPart(categoryName,
                (responses, partMap, part) -> responses.stream()
                        .filter(response -> partMap.get(part).equals(response.partId()))
                        .mapToLong(SearchResponse::totalPrice).sum(),
                ExpensesDto::new,
                ExpensesResponse::new
        );
    }

    @Override
    public FrequencyResponse getFrequencyByCategory() {
        return getByCategory(
                (responses, partIds) -> responses.stream()
                        .filter(response -> partIds.contains(response.partId()))
                        .count(),
                FrequencyDto::new,
                FrequencyResponse::new
        );
    }

    @Override
    public FrequencyResponse getFrequencyByPart(String categoryName) {
        return getByPart(categoryName,
                (responses, partMap, part) -> responses.stream()
                        .filter(response -> partMap.get(part).equals(response.partId()))
                        .count(),
                FrequencyDto::new,
                FrequencyResponse::new
        );
    }

    @Override
    public MonthExpensesResponse getMonthExpenses() {
        List<GetMonthExpensesDto> monthExpenses = purchaseStatisticsPort.getMonthExpenses();

        YearMonth end = YearMonth.now();
        YearMonth start = YearMonth.from(end.minusMonths(11));

        List<Integer> months = new ArrayList<>();
        List<MonthExpensesDto> dtos = IntStream.iterate(0, i -> i+1)
                .mapToObj(start::plusMonths)
                .takeWhile(month -> !month.isAfter(end))
                .map(month -> {
                    months.add(month.getMonthValue());

                    Long expenses = monthExpenses.stream()
                            .filter(expensesDto -> expensesDto.year() == month.getYear()
                                    && expensesDto.month() == month.getMonthValue())
                            .findFirst()
                            .map(GetMonthExpensesDto::expenses)
                            .orElse(0L);

                    return new MonthExpensesDto(month.getMonthValue(), expenses);
                }).toList();

        return new MonthExpensesResponse(months, dtos);
    }



    @FunctionalInterface
    public interface TriFunction<T, U, V, R> {
        R apply(T t, U u, V v);
    }

    // 지출비, 빈도수 - 카테고리 기준 조회
    public <T, U> T getByCategory(BiFunction<List<SearchResponse>, List<String>, Long> getTotal,
                               BiFunction<String, Long, U> dtoCreator,
                               BiFunction<List<String>, List<U>, T> responseCreator){
        Map<String, String> categoryMap = partServiceClient.getParts(null, 450, null).content()
                .stream().collect(Collectors.toMap(PartDto::partId, PartDto::category));
        List<String> categoryName = categoryMap.values().stream()
                .distinct()
                .sorted()
                .toList();

        List<SearchResponse> responses = purchaseStatisticsPort.getMonthRequest();

        List<U> dtos;
        if (responses.isEmpty()) {
            dtos = categoryName.stream()
                    .map(category -> dtoCreator.apply(category, 0L))
                    .toList();

            return responseCreator.apply(categoryName, dtos);
        }

        dtos = categoryName.stream().map(category-> {
            List<String> partIds = categoryMap.entrySet().stream()
                    .filter(entry -> entry.getValue().equals(category))
                    .map(Map.Entry::getKey)
                    .toList();

            Long total = getTotal.apply(responses, partIds);

            return dtoCreator.apply(category, total);
        }).toList();

        return responseCreator.apply(categoryName, dtos);
    }


    // 지출비, 빈도수 - 카테고리 내의 부품 기준 조회
    public <T, U> T getByPart(String categoryName,
                              TriFunction<List<SearchResponse>, Map<String, String>, String, Long> getTotal,
                              BiFunction<String, Long, U> dtoCreator,
                              BiFunction<List<String>, List<U>, T> responseCreator){
        Map<String, String> partMap = partServiceClient.getPartsByCategory(categoryName, null, 450, null).content().stream()
                .collect(Collectors.toMap(PartDto::partName, PartDto::partId));
        List<String> partIds = partMap.values().stream().toList();
        List<String> partName = partMap.keySet().stream().sorted().toList();

        List<SearchResponse> responses = purchaseStatisticsPort.getMonthRequestByPartId(partIds);

        List<U> dtos;
        if(responses.isEmpty()) {
            dtos = partName.stream().map(name -> dtoCreator.apply(name, 0L)).toList();
            return responseCreator.apply(partName, dtos);
        }

        dtos = partName.stream().map(part -> {
            Long total = getTotal.apply(responses, partMap, part);

            return dtoCreator.apply(part, total);
        }).toList();

        return responseCreator.apply(partName, dtos);
    }
}
