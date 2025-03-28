package hyundai.supplyservice.app.supply.application.port.in.statistic;

import hyundai.supplyservice.app.supply.application.port.in.commondto.PartCountDto;

import java.util.List;

public interface GetStatisticUsecase {
    // 월별 출고량 상위 10 부품
    MonthlyTopPartsResponseDto getMonthlyTopParts(int year, int month);

    // 월별 카테고리별 출고율
    MonthlyCategoryResponseDto getMonthlyCategory(int year, int month);

    // 한달 전체 출고 개수 및 주문 승인 건수
    MonthlyTotalCountResponseDto getMonthlyTotalCount(int year, int month);

    // 이번달 지난달 출고 부품 top 10
    CurrentLastMonthTopPartsResponseDto getCurrentLastMonthTopParts(int year, int month);

    // 부품별 한달동안 출고한 갯수
    List<PartCountDto> getMonthlyTotalQuantityByPart(int year, int month);

}
