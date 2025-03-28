package hyundai.purchaseservice.infrastructure.mapper;

import hyundai.purchaseservice.purchase.adapter.out.dto.*;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface PurchaseMapper {
    List<SearchResponse> searchMonthRequest();
    List<SearchResponse> searchMonthRequestByPartId(List<String> partIds);

    // 페이지, 페이지 크기, 조회 타입, 검색할 항목, 검색 내용, 정렬 항목, 정렬 순서
    List<SearchResponse> searchRequests(Integer page, Integer size, String type, String searchType, String searchText, String orderType, String orderBy);
    Long countRequests(String type, String searchType, String searchText);
    List<SearchResponse> searchRequestsByIds(Integer page, Integer size, String type, List<String> ids, String orderType, String orderBy);
    Long countRequestsByIds(String type, List<String> ids);

    // 상세 조회
    SearchResponse searchRequestDetail(Long id);

    /********************* 요청서 작성 *********************/

    // 품목 id로 당일 요청 기록 있는지 체크
    Long checkRequestExist(String partId, LocalDate when);

    // 요청서 저장
    void saveRequest(SaveRequestDto request);

    /********************* 통계 & 보고서 조회 *********************/
    List<GetMonthExpensesDto> getYearlyMonthExpenses();

    List<GetMonthExpensesDto> getCurrentAndLastMonthExpenses(LocalDate from, LocalDate to);

    List<GetMonthRequestDto> getCurrentAndLastMonthRequests(LocalDate from, LocalDate to);

    List<PartIdAndQuantityResponse> getTopTenCurrentMonthAutoRequest(String requesterName, LocalDate from, LocalDate to);

    List<PartIdAndQuantityResponse> getLastMonthAutoRequestByPartIds(List<String> partIds, String requesterName, LocalDate from, LocalDate to);

    List<PartIdAndPriceResponse> getTopTenCurrentMonthExpenses(LocalDate from, LocalDate to);

    List<PartIdAndPriceResponse> getLastMonthExpensesByPartIds(List<String> partIds, LocalDate from, LocalDate to);
}
