package hyundai.purchaseservice.purchase.application.service;

import hyundai.purchaseservice.purchase.adapter.in.dto.*;
import hyundai.purchaseservice.purchase.adapter.out.dto.GetDayScheduleQuantitesResponse;
import hyundai.purchaseservice.purchase.adapter.out.dto.GetProgressPercentResponse;
import hyundai.purchaseservice.purchase.adapter.out.dto.GetScheduleDto;
import hyundai.purchaseservice.purchase.adapter.out.dto.GetScheduleResponse;
import hyundai.purchaseservice.purchase.adapter.out.dto.feign.*;
import hyundai.purchaseservice.purchase.application.dto.DayScheduleDetailDto;
import hyundai.purchaseservice.purchase.application.dto.DayScheduleDto;
import hyundai.purchaseservice.purchase.application.dto.DayTodoDto;
import hyundai.purchaseservice.purchase.application.dto.MonthScheduleDayDto;
import hyundai.purchaseservice.purchase.application.exception.*;
import hyundai.purchaseservice.purchase.application.port.in.GetScheduleUseCase;
import hyundai.purchaseservice.purchase.application.port.out.PurchaseSchedulePort;
import hyundai.purchaseservice.purchase.application.port.out.PurchaseStatisticsPort;
import hyundai.purchaseservice.purchase.application.port.out.feign.PartServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetScheduleService implements GetScheduleUseCase {
    private final PurchaseSchedulePort purchaseSchedulePort;
    private final PurchaseStatisticsPort purchaseStatisticsPort;
    private final PartServiceClient partServiceClient;

    @Override
    public CalendarDateResponse getCalendar(Integer year, Integer month) {
        if (month < 1 || month > 12) {
            throw new InvalidInputException(month.toString());
        }

        List<LocalDate> dates = purchaseSchedulePort.getDates(year, month).stream()
                .sorted().toList();

        return new CalendarDateResponse(dates);
    }

    @Override
    public MonthScheduleResponse getMonthSchedule(Integer year, Integer month) {
        if (month < 1 || month > 12) {
            throw new InvalidInputException(month.toString());
        }

        // 달에 해당하는 요청서 목록을 읽어온다. month에 존재하는 날짜 리스트를 구한다.
        List<GetScheduleResponse> scheduleDtos = purchaseSchedulePort.getMonthSchedule(year, month);
        List<LocalDate> dates = scheduleDtos.stream()
                .map(GetScheduleResponse::scheduledAt)
                .distinct()
                .sorted()
                .toList();

        // 목록에 존재하는 모든 품목 코드로 part service에서 납품회사명, 품목 명, section 이름을 가져온다.
        List<String> partIds = scheduleDtos.stream().map(GetScheduleResponse::partId).toList();
        List<Long> sectionIds = scheduleDtos.stream()
                .map(GetScheduleResponse::sectionId)
                .distinct()
                .toList();

        Map<String, PartWithSupplierDto> partMap = partServiceClient.getPartInfoList(partIds).partDtos().stream()
                .collect(Collectors.toMap(PartWithSupplierDto::partId, p -> p));
        Map<Long, List<String>> sectionMap = getSectionInfoMap(sectionIds);

        // 날짜별로 목록을 정리한다.
        List<MonthScheduleDayDto> monthSchedule = dates.stream().map(date -> {
            List<DayTodoDto> todo = scheduleDtos.stream()
                    .filter(dto -> dto.scheduledAt().isEqual(date))
                    .map(dto -> {
                        PartWithSupplierDto partWithSupplier = partMap.get(dto.partId());
                        List<String> section = sectionMap.get(dto.sectionId());
                        String sectionName = section.get(0) + "구역 " + section.get(1)+"열 "+section.get(2)+"층";

                        return DayTodoDto.of(dto, partWithSupplier, sectionName);
                    }).toList();
            return new MonthScheduleDayDto(date, todo);
        }).toList();

        return new MonthScheduleResponse(monthSchedule);
    }

    /*********************************** (수정 필요) ******************************/
    @Override
    public DaySchedulePageResponse getDayScheduleTable(String searchType, String searchText, String orderType, Boolean isOrderByDesc) {
        // 정렬 기준 체크
        String orderColumnName = orderTypeToColumnName(orderType, isOrderByDesc);
        String orderBy = Boolean.TRUE.equals(isOrderByDesc) ? "DESC" : "ASC";

        // 검색 기준 체크
        checkSearchType(searchType, searchText);

        List<GetScheduleResponse> resultList;
        //Long totalCount = 0L;
        List<DayTodoDto> todoDtos;

        switch (searchType != null ? searchType : "null"){
            case "품목 명":
                try {
                    List<PartWithSupplierDto> partWithSupplierNameDto = partServiceClient.getPartName(searchText).partDtos();
                    List<String> partIds = partWithSupplierNameDto.stream().map(PartWithSupplierDto::partId).toList();
                    GetScheduleDto getScheduleDto = new GetScheduleDto(null, null, partIds, null, orderColumnName, orderBy);

                    resultList = purchaseSchedulePort.getDayScheduleWithPartIds(getScheduleDto);
                    //totalCount = purchaseSchedulePort.getDayScheduleTotalPartIds(partIds);
                    todoDtos = makeInfoWithPartWithSupplierDto(resultList, partWithSupplierNameDto);
                }catch (FeignResponseException e){
                    todoDtos = List.of();
                }
                break;
            case "납품 업체":
                try {
                    List<SupplierPartDto> supplierPartListDto = partServiceClient.getSupplierPartInfo(searchText).supplierPartDtos();
                    List<String> partIds = supplierPartListDto.stream().flatMap(spd -> spd.parts().stream())
                            .map(PartDto::partId).toList();
                    GetScheduleDto getScheduleDto = new GetScheduleDto(null, null, partIds, null, orderColumnName, orderBy);

                    resultList = purchaseSchedulePort.getDayScheduleWithPartIds(getScheduleDto);
                    //totalCount = purchaseSchedulePort.getDayScheduleTotalPartIds(partIds);
                    todoDtos = makeInfoWithSupplierPartDto(resultList, supplierPartListDto);
                }catch (FeignResponseException e){
                    todoDtos = List.of();
                }
                break;
            case "적치 구역":
                if(!searchText.matches("[A-X]")){
                    todoDtos = List.of();
                    //totalCount = 0L;
                    break;
                }

                try {
                    List<Long> inventory = partServiceClient.getInventoryList(searchText).inventoryDtos()
                            .stream().map(InventoryDto::sectionId).toList();

                    GetScheduleDto getScheduleDto = new GetScheduleDto("section_id", searchText, null, inventory, orderColumnName, orderBy);

                    resultList = purchaseSchedulePort.getDayScheduleWithSectionIds(getScheduleDto);
                    //totalCount = purchaseSchedulePort.getDayScheduleTotalSectionIds(inventory);
                    todoDtos = getPartNameAndSupplierName(resultList);
                }catch (FeignResponseException e){
                    todoDtos = List.of();
                }
                break;
            default:
                String searchColumnName = searchTypeToColumnName(searchType);
                GetScheduleDto getScheduleDto = new GetScheduleDto(searchColumnName, searchText, null, null, orderColumnName, orderBy);

                resultList = purchaseSchedulePort.getDaySchedule(getScheduleDto);
                //totalCount = purchaseSchedulePort.getDayScheduleTotal(searchColumnName, searchText);
                todoDtos = getPartNameAndSupplierName(resultList);
                break;
        }

        return new DaySchedulePageResponse(todoDtos);
    }


    @Override
    public DayScheduleResponse getDaySchedule() {
        LocalDate today = LocalDate.now();
        List<GetDayScheduleQuantitesResponse> daySchedule = purchaseStatisticsPort.getDaySchedule();

        List<DayScheduleDto> dayDto;
        if(daySchedule.isEmpty()){
           return new DayScheduleResponse(today, List.of());
        }

        List<String> partIds = daySchedule.stream().map(GetDayScheduleQuantitesResponse::partId).toList();
        Map<String, String> partIdNameMap = partServiceClient.getPartInfoList(partIds).partDtos().stream()
                .collect(Collectors.toMap(PartWithSupplierDto::partId, PartWithSupplierDto::partName));

        dayDto = daySchedule.stream().map(
                day -> new DayScheduleDto(
                        partIdNameMap.get(day.partId()),
                        day.quantity(),
                        day.processedQuantity()
                )
        ).toList();

        return new DayScheduleResponse(today, dayDto);
    }

    @Override
    public WorkerDayScheduleResponse getQRDaySchedule() {
        LocalDate today = LocalDate.now();
        List<GetProgressPercentResponse> daySchedule = purchaseStatisticsPort.getProgressPercent();

        if(daySchedule.isEmpty()) {
            return new WorkerDayScheduleResponse(today, List.of());
        }

        List<String> partIds = daySchedule.stream().map(GetProgressPercentResponse::partId).toList();
        List<Long> sectionIds = daySchedule.stream().map(GetProgressPercentResponse::sectionId).toList();

        Map<String, String> partIdNameMap = partServiceClient.getPartInfoList(partIds).partDtos().stream()
                .collect(Collectors.toMap(PartWithSupplierDto::partId, PartWithSupplierDto::partName));
        Map<Long, List<String>> sectionMap = getSectionInfoMap(sectionIds);

        List<DayScheduleDetailDto> detailDtos;
        detailDtos = daySchedule.stream().map(day -> {
            List<String> section = sectionMap.get(day.sectionId());
            return new DayScheduleDetailDto(partIdNameMap.get(day.partId()),
                    (long) day.requestedQuantity(), (long) day.processedQuantity(),
                    section.get(0) + "구역", section.get(1)+"열 "+section.get(2)+"층");
        }).toList();


        return new WorkerDayScheduleResponse(today, detailDtos);
    }


    //정렬 기준 체크
    private String orderTypeToColumnName(String orderType, Boolean isOrderByDesc){
        return switch (orderType != null ? orderType : "null") {
            case "null" -> {
                if(isOrderByDesc != null){
                    throw new OrderTypeNotFoundException(orderType, "수량, 금액");
                }
                yield null;
            }
            case "수량" -> "quantity";
            case "금액" -> "total_price";
            default -> throw new OrderTypeNotFoundException(orderType, "수량, 금액");
        };
    }

    //검색 기준 체크
    private void checkSearchType(String searchType, String searchText){
        Set<String> searchTypeList = Set.of("품목 코드", "품목 명", "납품 업체", "적치 구역");
        if(searchType != null && !searchTypeList.contains(searchType)){
            throw new SearchTypeNotFoundException(searchType, "품목 코드, 품목 명, 납품 업체, 적치 구역");
        }
        if(searchType != null && (searchText == null || searchText.isBlank())){
            throw new SearchTextNotFoundException();
        }
        if(searchType == null && searchText != null && !searchText.isBlank()){
            throw new SearchTypeNotFoundException(searchType, "품목 코드, 품목 명, 납품 업체, 적치 구역");
        }
    }

    private String searchTypeToColumnName(String searchType){
        return switch (searchType != null ? searchType : "null"){
            case "null" -> null;
            case "품목 코드"-> "part_id";
            default -> throw new SearchTypeNotFoundException(searchType, "품목 코드, 품목 명, 납품 업체, 적치 구역");
        };
    }

    //Part 서비스로부터 품목 명, 납품 회사명 검색
    private List<DayTodoDto> getPartNameAndSupplierName(List<GetScheduleResponse> resultList){
        if(!resultList.isEmpty()){
            List<String> partIds = resultList.stream().map(GetScheduleResponse::partId).toList();

            PartWithSupplierListResponse partWithSupplierNameDto = partServiceClient.getPartInfoList(partIds);
            return makeInfoWithPartWithSupplierDto(resultList, partWithSupplierNameDto.partDtos());
        }else{
            return List.of();
        }
    }

    private List<DayTodoDto> makeInfoWithSupplierPartDto(List<GetScheduleResponse> resultList, List<SupplierPartDto> supplierPartDtos){
        List<Long> sectionIds = resultList.stream().map(GetScheduleResponse::sectionId).distinct().toList();

        Map<String, SupplierPartDto> supplierPartMap = supplierPartDtos.stream()
                .flatMap(supplierPart -> supplierPart.parts().stream()
                        .map(part -> Map.entry(part.partId(), supplierPart)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        Map<Long, List<String>> sectionMap = getSectionInfoMap(sectionIds);

        return resultList.stream().map(result -> {
            SupplierPartDto supplierPart = Optional.ofNullable(supplierPartMap.get(result.partId()))
                    .orElseThrow(() -> new SearchErrorException("part not found at supplier"));

            String name = supplierPart.parts().stream()
                    .filter(p -> p.partId().equals(result.partId()))
                    .map(PartDto::partName)
                    .findFirst().orElse(null);

            List<String> section = sectionMap.get(result.sectionId());
            String sectionName = section.get(0) + "구역 " + section.get(1)+"열 "+section.get(2)+"층";

            return DayTodoDto.of(result, name, supplierPart.supplierName(), sectionName);
        }).toList();
    }

    private List<DayTodoDto> makeInfoWithPartWithSupplierDto(List<GetScheduleResponse> resultList, List<PartWithSupplierDto> partWithSupplier){
        List<Long> sectionIds = resultList.stream().map(GetScheduleResponse::sectionId).distinct().toList();

        Map<String, PartWithSupplierDto> partMap = partWithSupplier.stream()
                .collect(Collectors.toMap(PartWithSupplierDto::partId, p -> p));
        Map<Long, List<String>> sectionMap = getSectionInfoMap(sectionIds);

        return resultList.stream().map(result -> {
            List<String> section = sectionMap.get(result.sectionId());
            String sectionName = section.get(0) + "구역 " + section.get(1)+"열 "+section.get(2)+"층";

            PartWithSupplierDto dto = Optional.ofNullable(partMap.get(result.partId())).orElseThrow(() -> new SearchErrorException("part id not found"));
            return DayTodoDto.of(result, dto, sectionName);
        }).toList();
    }


    // section name과 floor -> 구역, 열, 층으로 구분
    private Map<Long, List<String>> getSectionInfoMap(List<Long> sectionIds){
        return partServiceClient.getSectionInfoList(sectionIds).sectionDtos().stream()
                .collect(Collectors.toMap(SectionDto::sectionId,section -> {
                    String[] sectionArea = section.sectionName().split("-");
                    List<String> sectionInfo = new ArrayList<>(Arrays.asList(sectionArea));
                    sectionInfo.add(section.floor().toString());
                    return sectionInfo;
                }));
    }
}
