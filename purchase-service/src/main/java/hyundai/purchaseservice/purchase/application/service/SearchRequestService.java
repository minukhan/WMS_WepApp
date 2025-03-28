package hyundai.purchaseservice.purchase.application.service;

import hyundai.purchaseservice.purchase.adapter.in.dto.*;
import hyundai.purchaseservice.purchase.adapter.out.dto.*;
import hyundai.purchaseservice.purchase.adapter.out.dto.feign.*;
import hyundai.purchaseservice.purchase.application.dto.PurchaseDetailItemInfoDto;
import hyundai.purchaseservice.purchase.application.dto.PurchaseDetailOrderInfoDto;
import hyundai.purchaseservice.purchase.application.dto.PurchaseRequestInfoDto;
import hyundai.purchaseservice.purchase.application.exception.*;
import hyundai.purchaseservice.purchase.application.port.in.SearchRequestUseCase;
import hyundai.purchaseservice.purchase.application.port.out.SearchRequestPort;
import hyundai.purchaseservice.purchase.application.port.out.feign.PartServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchRequestService implements SearchRequestUseCase {

    private final SearchRequestPort searchRequestPort;
    private final PartServiceClient partServiceClient;

    @Override
    public PurchaseRequestListResponse searchPurchaseRequests(
            Integer page, Integer size, String type, String searchType, String searchText, String orderType, Boolean isOrderByDesc){
        // 조회 타입 체크
        Set<String> typeList = Set.of("전체", "요청 중", "완료");
        if(!typeList.contains(type)){
            throw new TypeNotFoundException(type);
        }

        // 정렬 기준 체크
        String orderColumnName = orderTypeToColumnName(orderType, isOrderByDesc);
        String orderBy = Boolean.TRUE.equals(isOrderByDesc) ? "DESC" : "ASC";

        // 검색 기준 체크
        checkSearchType(searchType, searchText);

        // 페이지 정보
        int offset = (page - 1) * size;
        Long totalCount = 0L;

        // 검색
        List<SearchResponse> resultList;
        List<PurchaseRequestInfoDto> infoDtos;
        List<String> partIds = new ArrayList<>();

        switch (searchType != null ? searchType : "null"){
            case "품목 명":
                try {
                    List<PartWithSupplierDto> partWithSupplierNameDto = partServiceClient.getPartName(searchText).partDtos();
                    partWithSupplierNameDto.forEach(part -> partIds.add(part.partId()));

                    resultList = searchRequestPort.searchByIds(offset, size, type, partIds, orderColumnName, orderBy);
                    totalCount = searchRequestPort.getTotalPageById(type, partIds);
                    infoDtos = makeInfoWithPartWithSupplierDto(resultList, partWithSupplierNameDto);
                } catch (FeignResponseException e){
                    infoDtos = List.of();
                }
                break;
            case "납품 회사명":
                try {
                    List<SupplierPartDto> supplierPartListDto = partServiceClient.getSupplierPartInfo(searchText).supplierPartDtos();
                    supplierPartListDto.stream().flatMap(spd -> spd.parts().stream())
                            .forEach(partDto -> partIds.add(partDto.partId()));

                    resultList = searchRequestPort.searchByIds(offset, size, type, partIds, orderColumnName, orderBy);
                    totalCount = searchRequestPort.getTotalPageById(type, partIds);
                    infoDtos = makeInfoWithSupplierPartDto(resultList, supplierPartListDto);
                } catch (FeignResponseException e){
                    infoDtos = List.of();
                }
                break;
            default:
                String searchColumnName = searchTypeToColumnName(searchType);
                resultList = searchRequestPort.search(offset, size, type, searchColumnName, searchText, orderColumnName, orderBy);
                totalCount = searchRequestPort.getTotalPage(type, searchColumnName, searchText);
                infoDtos = getPartNameAndSupplierName(resultList);
                break;
        }

        return PurchaseRequestListResponse.of(infoDtos, page, size, totalCount);
    }

    @Override
    public PurchaseDetailResponse searchPurchaseDetails(Long requestId) {
        SearchResponse response = Optional.ofNullable(searchRequestPort.getPurchaseRequest(requestId))
                .orElseThrow(PurchaseRequestIdNotFoundException::new) ;
        PartWithSupplierDto part = partServiceClient.getPartInfo(response.partId()).partSupplierDto();

        return new PurchaseDetailResponse(
                PurchaseDetailOrderInfoDto.of(response, part),
                PurchaseDetailItemInfoDto.of(response, part.partName())
        );
    }


    //정렬 기준 체크
    private String orderTypeToColumnName(String orderType, Boolean isOrderByDesc){
        return switch (orderType != null ? orderType : "null") {
            case "null" -> {
                if(isOrderByDesc != null){
                    throw new OrderTypeNotFoundException(orderType, "요청 일시, 수량, 금액");
                }
                yield null;
            }
            case "요청 일시" -> "ordered_at";
            case "수량" -> "quantity";
            case "금액" -> "total_price";
            default -> throw new OrderTypeNotFoundException(orderType, "요청 일시, 수량, 금액");
        };
    }

    //검색 기준 체크
    private void checkSearchType(String searchType, String searchText){
        Set<String> searchTypeList = Set.of("품목 코드", "품목 명", "요청자", "납품 회사명");
        if(searchType != null && !searchTypeList.contains(searchType)){
            throw new SearchTypeNotFoundException(searchType, "품목 코드, 품목 명, 요청자, 납품 회사명");
        }
        if(searchType != null && (searchText == null || searchText.isBlank())){
            throw new SearchTextNotFoundException();
        }
        if(searchType == null && searchText != null && !searchText.isBlank()){
            throw new SearchTypeNotFoundException(searchType, "품목 코드, 품목 명, 요청자, 납품 회사명");
        }
    }

    private String searchTypeToColumnName(String searchType){
        return switch (searchType != null ? searchType : "null"){
            case "null" -> null;
            case "품목 코드"-> "part_id";
            case "요청자" -> "requester_name";
            default -> throw new SearchTypeNotFoundException(searchType, "품목 코드, 품목 명, 요청자, 납품 회사명");
        };
    }

    //Part 서비스로부터 품목 명, 납품 회사명 검색
    private List<PurchaseRequestInfoDto> getPartNameAndSupplierName(List<SearchResponse> resultList){
        if(!resultList.isEmpty()){
            List<String> partIds = resultList.stream().map(SearchResponse::partId).toList();
            PartWithSupplierListResponse partWithSupplierNameDto = partServiceClient.getPartInfoList(partIds);
            return makeInfoWithPartWithSupplierDto(resultList, partWithSupplierNameDto.partDtos());
        }else{
            return List.of();
        }
    }

    //Part 서비스로부터 받은 정보와 합치기
    private List<PurchaseRequestInfoDto> makeInfoWithSupplierPartDto(List<SearchResponse> resultList, List<SupplierPartDto> supplierPartDtos){
        Map<String, SupplierPartDto> supplierPartMap = supplierPartDtos.stream()
                .flatMap(supplierPart -> supplierPart.parts().stream()
                        .map(part -> Map.entry(part.partId(), supplierPart)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        return resultList.stream().map(result -> {
            SupplierPartDto supplierPart = Optional.ofNullable(supplierPartMap.get(result.partId()))
                    .orElseThrow(() -> new SearchErrorException("part not found at supplier"));

            String name = supplierPart.parts().stream()
                    .filter(p -> p.partId().equals(result.partId()))
                    .map(PartDto::partName)
                    .findFirst().orElse(null);

            return PurchaseRequestInfoDto.of(result, name, supplierPart.supplierName());
        }).toList();
    }

    private List<PurchaseRequestInfoDto> makeInfoWithPartWithSupplierDto(List<SearchResponse> resultList, List<PartWithSupplierDto> partWithSupplier){
        Map<String, PartWithSupplierDto> partMap = partWithSupplier.stream()
                .collect(Collectors.toMap(PartWithSupplierDto::partId, p -> p));

        return resultList.stream().map(result -> {
            PartWithSupplierDto dto = Optional.ofNullable(partMap.get(result.partId()))
                    .orElseThrow(() -> new SearchErrorException("part id not found"));
            return PurchaseRequestInfoDto.of(result, dto.partName(), dto.supplierName());
        }).toList();
    }
}
