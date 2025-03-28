package hyundai.supplyservice.app.supply.application.service;

import com.fasterxml.jackson.databind.JsonNode;
import hyundai.supplyservice.app.infrastructure.repository.PickingLocationRepository;
import hyundai.supplyservice.app.supply.application.entity.PickingLocation;
import hyundai.supplyservice.app.supply.application.entity.SupplyPartSchedule;
import hyundai.supplyservice.app.supply.application.entity.SupplyPartScheduleId;
import hyundai.supplyservice.app.supply.application.port.in.schedule.*;
import hyundai.supplyservice.app.supply.application.port.out.SupplyPartSchedulePort;
import hyundai.supplyservice.app.supply.application.port.out.SupplySchedulePort;
import hyundai.supplyservice.app.supply.application.port.out.feign.PartController;
import hyundai.supplyservice.app.supply.application.port.out.feign.SSEController;
import hyundai.supplyservice.app.supply.exception.InvalidRequestException;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ScheduleService implements GetScheduleUseCase {
    private final SupplyPartSchedulePort supplyPartSchedulePort;
    private final SupplySchedulePort supplySchedulePort;
    private final PartController partController;
    private final SSEController sseController;
    private final PickingLocationRepository pickingLocationRepository;
    private final EntityManager entityManager;


    // 월별 출고 일정
    @Override
    public CalendarResponseDto getCalendar(Integer year, Integer month) {
        // 날짜 오름차순 정렬
        List<LocalDate> scheduleDates = supplySchedulePort.findDistinctDatesByYearAndMonth(year, month)
                .stream()
                .sorted()
                .collect(Collectors.toList());

        return new CalendarResponseDto(scheduleDates);
    }

    // 월별 출고 일정 전체 조회
    @Override
    public MonthlySupplyScheduleResponse getMonthlySupplySchedule(Integer year, Integer month) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.with(TemporalAdjusters.lastDayOfMonth());

        // 한 달치 데이터를 한 번에 조회
        List<SupplyPartSchedule> monthlySchedules = supplyPartSchedulePort
                .findAllByScheduledAtBetween(startDate, endDate);

        // 날짜별로 그룹화
        Map<LocalDate, List<SupplyPartSchedule>> schedulesByDate = monthlySchedules.stream()
                .collect(Collectors.groupingBy(schedule ->
                        schedule.getId().getScheduledAt()));

        // 각 날짜별 스케줄 생성
        List<DailySupplySchedulePartPriceList> monthlyResponses = schedulesByDate.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> {
                    LocalDate date = entry.getKey();
                    List<SupplyPartSchedule> schedules = entry.getValue();

                    List<DailySupplyPartPriceResponseDto> daySchedule = schedules.stream()
                            .map(part -> {
                                JsonNode partInfo = partController.getPartInfo(part.getId().getPartId());
                                long unitPrice = partInfo.get("partSupplierDto").get("price").asLong();

                                return new DailySupplyPartPriceResponseDto(
                                        part.getId().getPartId(),
                                        part.getPartName(),
                                        part.getTotalRequestedQuantity(),
                                        unitPrice * part.getTotalRequestedQuantity()
                                );
                            })
                            .collect(Collectors.toList());

                    return new DailySupplySchedulePartPriceList(
                            date,
                            daySchedule
                    );
                })
                .collect(Collectors.toList());

        return new MonthlySupplyScheduleResponse(monthlyResponses);

    }

    // 출고일별 부품 목록 및 진행현황
    public DailySupplyScheduleResponse getDailySupplyPartSchedule() {
        LocalDate today = LocalDate.now();
        List<SupplyPartSchedule> dailySchedules = supplyPartSchedulePort.findAllByDate(today);

        List<DailyPartQuantity> schedules = dailySchedules.stream()
                .map(schedule -> new DailyPartQuantity(
                        schedule.getPartName(),
                        schedule.getTotalRequestedQuantity(),
                        schedule.getCurrentQuantity()
                ))
                .collect(Collectors.toList());

        return new DailySupplyScheduleResponse(
                today,
                schedules
        );

    }

    // 출고일 별 작업 진척도 %
    public DailySupplyProgressResponseDto getDailyProgress() {
        LocalDate date = LocalDate.now();
        List<SupplyPartSchedule> dailySchedules = supplyPartSchedulePort.findAllByDate(date);

        // 출고해야 할 전체 부품 합
        int totalRequested = dailySchedules.stream()
                .mapToInt(SupplyPartSchedule::getTotalRequestedQuantity)
                .sum();

        // 현재 출고된 부품 합
        int totalCurrent = dailySchedules.stream()
                .mapToInt(SupplyPartSchedule::getCurrentQuantity)
                .sum();

        double progressRate = totalRequested > 0
                ? (double) totalCurrent / totalRequested * 100
                : 0.0;

        return new DailySupplyProgressResponseDto(
                date,
                totalRequested,
                totalCurrent,
                Math.round(progressRate * 10.0) / 10.0  // 소수점 첫째자리까지 표시
        );
    }


    public List<DailySupplyPartPriceResponseDto> getDailyParts(
            String searchType, String searchText, String orderType, Boolean isDesc) {
            LocalDate date = LocalDate.now();
        return supplyPartSchedulePort.findAllByDate(date).stream()
                .filter(part -> searchText == null || searchText.isEmpty() ||
                        ("partId".equals(searchType) && part.getId().getPartId().contains(searchText)) ||
                        ("partName".equals(searchType) && part.getPartName().contains(searchText)))
                .map(part -> {
                    JsonNode partInfo = partController.getPartInfo(part.getId().getPartId());
                    long unitPrice = partInfo.get("partSupplierDto").get("price").asLong();
                    return new DailySupplyPartPriceResponseDto(
                            part.getId().getPartId(),
                            part.getPartName(),
                            part.getTotalRequestedQuantity(),
                            unitPrice * part.getTotalRequestedQuantity()
                    );
                })
                .sorted((a, b) -> {
                    if (orderType == null) return 0;
                    int comparison = "quantity".equals(orderType) ?
                            Integer.compare(a.quantity(), b.quantity()) :
                            Long.compare(a.totalPrice(), b.totalPrice());
                    return isDesc ? -comparison : comparison;
                })
                .collect(Collectors.toList());

    }

    @Override
    public DailySectionScheduleResponseDto getDailySectionSchedule() {
        LocalDate today = LocalDate.now();
        List<SupplyPartSchedule> todaySchedules = supplyPartSchedulePort.findAllByDate(today);

        // 출고 위치 정보가 없는 스케줄에 대해 위치 정보 생성
        for (SupplyPartSchedule schedule : todaySchedules) {
            if (schedule.getPickingLocations().isEmpty()) {
                JsonNode inventoryInfo = partController.getPartInventoryInfo(schedule.getId().getPartId());
                createPickingLocations(schedule, inventoryInfo);
            }
        }

        entityManager.flush(); // 변경사항을 데이터베이스에 반영
        entityManager.clear(); // 영속성 컨텍스트 초기화

        todaySchedules = supplyPartSchedulePort.findAllByDate(today);

        List<PartSectionQuantityDto> partSectionQuantities = todaySchedules.stream()
                .flatMap(schedule -> schedule.getPickingLocations().stream()
                        .map(location -> {
                            String[] sectionParts = location.getSectionName().split("-");
                            String section = sectionParts[0] + "구역";
                            String column = sectionParts[1] + "열";
                            String floor = location.getFloor() + "층";

                            return new PartSectionQuantityDto(
                                    schedule.getPartName(),
                                    location.getPlannedQuantity(),
                                    location.getCurrentQuantity(),
                                    section,
                                    column + " " + floor
                            );
                        }))
                .toList();

        return new DailySectionScheduleResponseDto(today, partSectionQuantities);
    }



    // 부품 출고 위치 생성
    public void createPickingLocations(SupplyPartSchedule schedule, JsonNode inventoryInfo) {
        int remainingQuantity = schedule.getTotalRequestedQuantity();
        List<PickingLocation> pickingLocations = new ArrayList<>();

        JsonNode inventoryList = inventoryInfo.get("inventoryDtoList");
        for (JsonNode inventory : inventoryList) {
            if (remainingQuantity <= 0) break;

            int availableQuantity = inventory.get("partQuantity").asInt();
            int plannedQuantity = Math.min(remainingQuantity, availableQuantity);

            PickingLocation location = PickingLocation.builder()
                    .supplyPartSchedule(schedule)
                    .sectionId(inventory.get("sectionId").asLong())
                    .sectionName(inventory.get("sectionName").asText())
                    .floor(inventory.get("floor").asInt())
                    .plannedQuantity(plannedQuantity)
                    .currentQuantity(0)
                    .build();

            pickingLocations.add(location);
            remainingQuantity -= plannedQuantity;
        }

        pickingLocationRepository.saveAll(pickingLocations);
    }

    @Override
    public void processQrScanOutbound(String partId, String sectionName){

        String[] parts = sectionName.split(" ");

        String section = parts[0].replace("구역", "");
        String column = parts[1].replace("열", "");
        String floorStr = parts[2].replace("층", "");


        String formattedSectionName = section + "-" + column;
        int floor=Integer.parseInt(floorStr);
        LocalDate date = LocalDate.now();

        PickingLocation pickingLocation = pickingLocationRepository
                .findByPartIdAndSectionNameAndFloorAndScheduledDate(
                        partId,
                        formattedSectionName,
                        floor,
                        date)
                .orElseThrow(() -> new InvalidRequestException(
                        String.format("%s 구역의 %d층에 %s출고 일정이 없습니다.",
                                formattedSectionName, floor,partId)));


        SupplyPartScheduleId scheduleId = new SupplyPartScheduleId(date,partId);
        SupplyPartSchedule supplyPartSchedule = supplyPartSchedulePort
                .findById(scheduleId)
                .orElseThrow(() -> new InvalidRequestException("오늘 해당 부품 출고 일정이 없습니다."));


        // 해당 구역의 계획된 출고량 달성했을 경우
        if (pickingLocation.getCurrentQuantity() >= pickingLocation.getPlannedQuantity()) {
            throw new InvalidRequestException(
                    String.format("해당 구역에서 부품이 전부 출고되었습니다. (%d) /(%d)",
                            pickingLocation.getCurrentQuantity(),
                            pickingLocation.getPlannedQuantity()));
        }


        pickingLocation.updateCurrentQuantity();
        supplyPartSchedule.updateCurrentQuantity();

        // part feign client 파트 재고 감소.
        String str = partController.decrementPartInventory(formattedSectionName, floor, partId);

        SSESectionRequestDto requestDto = new SSESectionRequestDto(formattedSectionName, floor, false);
        sseController.notifyQrScanResult(requestDto);



    }

}
