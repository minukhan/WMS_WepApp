package hyundai.supplyservice.app.supply.application.service;

import ch.qos.logback.core.joran.sanity.Pair;
import com.fasterxml.jackson.databind.JsonNode;
import hyundai.supplyservice.app.infrastructure.config.Ai;
import hyundai.supplyservice.app.supply.application.entity.SupplyPartSchedule;
import hyundai.supplyservice.app.supply.application.entity.SupplyRequest;
import hyundai.supplyservice.app.supply.application.port.in.commondto.PartCountDto;
import hyundai.supplyservice.app.supply.application.port.in.statistic.*;
import hyundai.supplyservice.app.supply.application.port.out.SupplyPartSchedulePort;
import hyundai.supplyservice.app.supply.application.port.out.SupplyRequestPort;
import hyundai.supplyservice.app.supply.application.port.out.feign.PartController;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Float.NEGATIVE_INFINITY;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class StatisticService implements GetStatisticUsecase {

    private final SupplyPartSchedulePort supplyPartSchedulePort;
    private final SupplyRequestPort supplyRequestPort;
    private final PartController partController;
    private final Ai ai;


    public MonthlyTopPartsResponseDto getMonthlyTopParts(int year, int month) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.with(TemporalAdjusters.lastDayOfMonth());

        // 해당 월의 전체 출고 부품 조회
        List<SupplyPartSchedule> monthlySchedules = supplyPartSchedulePort
                .findAllByScheduledAtBetween(startDate, endDate);

        // 모든 부품의 출고 개수 합산 -> 월별 전체 출고량
        int totalMonthlyQuantity = monthlySchedules.stream()
                .mapToInt(SupplyPartSchedule::getTotalRequestedQuantity)
                .sum();

        // 부품 ID별 카테고리 정보를 미리 조회
        Set<String> uniquePartIds = monthlySchedules.stream()
                .map(schedule -> schedule.getId().getPartId())
                .collect(Collectors.toSet());

        Map<String, String> partCategories = uniquePartIds.stream()
                .collect(Collectors.toMap(
                        partId -> partId,
                        partId -> {
                            JsonNode partInfo = partController.getPartInfo(partId);
                            return partInfo.get("partSupplierDto").get("category").asText();
                        }
                ));

        List<TopPartDetail> topParts = monthlySchedules.stream()
                .collect(Collectors.groupingBy(
                        schedule -> schedule.getId().getPartId(),
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                parts -> {
                                    String partName = parts.get(0).getPartName();
                                    int totalQuantity = parts.stream()
                                            .mapToInt(SupplyPartSchedule::getTotalRequestedQuantity)
                                            .sum();
                                    return new TopPartDetail(
                                            parts.get(0).getId().getPartId(),
                                            partName,
                                            partCategories.get(parts.get(0).getId().getPartId()),
                                            totalQuantity,
                                            calculatePercentage(totalQuantity, totalMonthlyQuantity)
                                    );
                                }
                        )))
                .values()
                .stream()
                .sorted(Comparator.comparing(TopPartDetail::totalQuantity).reversed())
                .limit(10)
                .collect(Collectors.toList());

        return new MonthlyTopPartsResponseDto(year, month, totalMonthlyQuantity, topParts);

    }

    private double calculatePercentage(int partQuantity, int totalQuantity) {
        return totalQuantity > 0
                ? Math.round((double) partQuantity / totalQuantity * 1000.0) / 10.0
                : 0.0;
    }


    @Override
    public MonthlyCategoryResponseDto getMonthlyCategory(int year, int month) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.with(TemporalAdjusters.lastDayOfMonth());

        // 해당 월의 모든 출고 부품 조회
        List<SupplyPartSchedule> schedules = supplyPartSchedulePort
                .findAllByScheduledAtBetween(startDate, endDate);

        // 카테고리 별 부품 수
        Map<String, Integer> categoryQuantities = new HashMap<>();
        // 보든 부품의 partId, 카테고리 매핑
        Map<String, String> partCategories = new HashMap<>();

        JsonNode allParts = partController.getPartInfo(450);
        allParts.get("content").forEach(part ->
                partCategories.put(
                        part.get("partId").asText(),
                        part.get("category").asText()
                )
        );

        schedules.forEach(schedule -> {
            String partId = schedule.getId().getPartId();
            String category = partCategories.get(partId);
            categoryQuantities.merge(category, schedule.getTotalRequestedQuantity(), Integer::sum);
        });


        int totalQuantity = categoryQuantities.values().stream()
                .mapToInt(Integer::intValue)
                .sum();

        List<CategoryRatioDto> categoryRatios = categoryQuantities.entrySet().stream()
                .map(entry -> new CategoryRatioDto(
                        entry.getKey(),
                        entry.getValue(),
                        calculatePercentage(entry.getValue(), totalQuantity)
                ))
                .collect(Collectors.toList());

        return new MonthlyCategoryResponseDto(year, month, totalQuantity, categoryRatios);
    }

    // 보고서 부품 수량 및 승인률 통계
    public MonthlyTotalCountResponseDto getMonthlyTotalCount(int year, int month) {

        // 이번달
        LocalDate currentMonthStart = LocalDate.of(year, month, 1);
        LocalDate currentMonthEnd = currentMonthStart.withDayOfMonth(currentMonthStart.lengthOfMonth());

        // 지난달
        LocalDate lastMonthStart = currentMonthStart.minusMonths(1);
        LocalDate lastMonthEnd = lastMonthStart.withDayOfMonth(lastMonthStart.lengthOfMonth());

        // 현재 월 데이터 조회
        List<SupplyRequest> currentMonthRequests = supplyRequestPort.findByDeadlineBetween(
                currentMonthStart, currentMonthEnd);

        // 이전 월 데이터 조회
        List<SupplyRequest> lastMonthRequests = supplyRequestPort.findByDeadlineBetween(
                lastMonthStart, lastMonthEnd);

        // 이번달 통계 계산
        int currentMonthRequestedOrders = currentMonthRequests.size();
        int currentMonthApprovedOrders = (int) currentMonthRequests.stream()
                .filter(request -> "APPROVED".equals(request.getStatus()))
                .count();
        double currentMonthApprovalRate = currentMonthRequestedOrders > 0
                ? Math.round((double) currentMonthApprovedOrders / currentMonthRequestedOrders * 1000.0) / 10.0
                : 0.0;

        // 지난달 통계 계산
        int lastMonthRequestedOrders = lastMonthRequests.size();
        int lastMonthApprovedOrders = (int) lastMonthRequests.stream()
                .filter(request -> "APPROVED".equals(request.getStatus()))
                .count();
        double lastMonthApprovalRate = lastMonthRequestedOrders > 0
                ? Math.round((double) lastMonthApprovedOrders / lastMonthRequestedOrders * 1000.0) / 10.0
                : 0.0;


        // 이번달 출고된 전체 부품 박스
        Long currentMonthTotalPartBox = supplyPartSchedulePort.findAllByScheduledAtBetween(
                        currentMonthStart, currentMonthEnd)
                .stream()
                .mapToLong(schedule -> schedule.getTotalRequestedQuantity())
                .sum();

        // 지난달 출고된 전체 부품 박스
        Long lastMonthTotalPartBox = supplyPartSchedulePort.findAllByScheduledAtBetween(
                        lastMonthStart, lastMonthEnd)
                .stream()
                .mapToLong(schedule -> schedule.getTotalRequestedQuantity())
                .sum();

        // 출고수량 전월대비 변동률 계산
        double changeRate;
        if (lastMonthTotalPartBox == 0) {
            changeRate = Double.NEGATIVE_INFINITY;  // 전월 대비 변동률 계산 불가능
        } else {
            changeRate = Math.round(((double)(currentMonthTotalPartBox - lastMonthTotalPartBox) / lastMonthTotalPartBox * 100) * 10) / 10.0;
        }

        return new MonthlyTotalCountResponseDto(
                new MonthlyTotalCountResponseDto.CountSummary(
                        currentMonthTotalPartBox,
                        lastMonthTotalPartBox,
                        currentMonthTotalPartBox-lastMonthTotalPartBox,
                        changeRate
                ),
                new MonthlyTotalCountResponseDto.ApproveSummary(
                        currentMonthRequestedOrders,
                        currentMonthApprovedOrders,
                        currentMonthApprovalRate,
                        lastMonthApprovalRate

                )
        );
    }

    public CurrentLastMonthTopPartsResponseDto getCurrentLastMonthTopParts(int year, int month) {

        // 해당 월과 전 월의 날짜 계산
        LocalDate currentMonthStart = LocalDate.of(year, month, 1);
        LocalDate currentMonthEnd = currentMonthStart.withDayOfMonth(currentMonthStart.lengthOfMonth());
        LocalDate lastMonthStart = currentMonthStart.minusMonths(1);
        LocalDate lastMonthEnd = lastMonthStart.withDayOfMonth(lastMonthStart.lengthOfMonth());

        List<SupplyPartSchedule> currentMonthParts  = supplyPartSchedulePort
                .findAllByScheduledAtBetween(currentMonthStart, currentMonthEnd);

        List<SupplyPartSchedule> lastMonthParts = supplyPartSchedulePort.findAllByScheduledAtBetween(
                lastMonthStart, lastMonthEnd);

        // 이번달 부품별 수량
        Map<String, Integer> currentMonthQuantities =currentMonthParts.stream()
                .collect(Collectors.groupingBy(
                        schedule -> schedule.getId().getPartId(),
                        Collectors.summingInt(SupplyPartSchedule::getTotalRequestedQuantity)
                ));

        Map<String, String> partIdToName = currentMonthParts.stream()
                .collect(Collectors.toMap(
                        part -> part.getId().getPartId(),
                        SupplyPartSchedule::getPartName,
                        (name1, name2) -> name1
                ));

        // 지난달 부품별 수량
        Map<String, Integer> lastMonthQuantities = lastMonthParts.stream()
                .collect(Collectors.groupingBy(
                        schedule -> schedule.getId().getPartId(),
                        Collectors.summingInt(SupplyPartSchedule::getTotalRequestedQuantity)
                ));

        // 상위 10개 부품 data
        List<CurrentLastMonthTopPartsResponseDto.PartData> topParts = currentMonthQuantities.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(10)
                .map(entry -> {
                    String partId = entry.getKey();
                    String partName = partIdToName.get(partId);
                    int currentQuantity = entry.getValue();
                    int lastQuantity = lastMonthQuantities.getOrDefault(partId, 0);
                    int changeQuantity = currentQuantity - lastQuantity;

                    Double changeRate;
                    if (lastQuantity == 0) {
                        changeRate = Double.NEGATIVE_INFINITY;
                    } else {
                        changeRate = Math.round(((double) changeQuantity / lastQuantity * 100) * 100) / 100.0;
                    }

                    Integer predictQuantity = ai.predictNextMonthQuantity(partId, currentQuantity);

                    // 예측 실패 시 기본값 사용
                    if (predictQuantity == null) {
                        predictQuantity = Integer.MIN_VALUE;;
                    }

                    return new CurrentLastMonthTopPartsResponseDto.PartData(
                            partName,
                            currentQuantity,
                            lastQuantity,
                            predictQuantity,
                            changeQuantity,
                            changeRate
                    );
                })
                .collect(Collectors.toList());

        return new CurrentLastMonthTopPartsResponseDto(topParts);



    }


    @Override
    public List<PartCountDto> getMonthlyTotalQuantityByPart(int year, int month) {

        // 1. Feign Client로 전체 부품 정보 조회
        JsonNode response = partController.getPartInfo(450);
        JsonNode content = response.get("content");

        // 2. 첫날과 마지막날 계산
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.with(TemporalAdjusters.lastDayOfMonth());

        List<PartCountDto> result = new ArrayList<>();

        // 3. 각 부품별로 해당 기간의 총 출고량 계산
        content.forEach(part -> {
            String partId = part.get("partId").asText();

            // 4. 해당 기간의 출고 스케줄 조회 및 수량 합산
            int totalQuantity = supplyPartSchedulePort
                    .findByIdScheduledAtBetweenAndIdPartId(
                            startDate,
                            endDate,
                            partId
                    )
                    .stream()
                    .mapToInt(SupplyPartSchedule::getTotalRequestedQuantity)
                    .sum();

            result.add(new PartCountDto(partId, totalQuantity));
        });

        return result;
    }
}
