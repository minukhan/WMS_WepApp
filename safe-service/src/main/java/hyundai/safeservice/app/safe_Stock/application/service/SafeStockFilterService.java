package hyundai.safeservice.app.safe_Stock.application.service;

import hyundai.safeservice.app.safe_Stock.adapter.dto.SafeFilterDto;
import hyundai.safeservice.app.safe_Stock.adapter.dto.SafeResponse;
import hyundai.safeservice.app.safe_Stock.application.entity.SafeStock;
import hyundai.safeservice.app.safe_Stock.application.port.in.SafeStockFilterUseCase;
import hyundai.safeservice.app.safe_Stock.application.port.out.SafeStockFilterPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SafeStockFilterService implements SafeStockFilterUseCase {

    private final SafeStockFilterPort safeStockFilterPort;

    @Override
    public SafeResponse filter(String partId, String searchType, String searchText, String orderType, boolean isDesc) {

        List<SafeStock> safeStocks = safeStockFilterPort.findByPartId(partId);

        //  "수정자" 검색 필터만 적용
        if ("수정자".equals(searchType) && searchText != null && !searchText.isBlank()) {
            String lowerSearchText = searchText.toLowerCase();
            safeStocks = safeStocks.stream()
                    .filter(safeStock -> safeStock.getAcceptName() != null && safeStock.getAcceptName().toLowerCase().contains(lowerSearchText))
                    .collect(Collectors.toList());
        }

        // 2. 정렬 (orderType + isDesc)
        Comparator<SafeStock> comparator = Comparator.comparing(SafeStock::getId);  // 기본: ID 기준

        if (orderType != null && !orderType.isBlank()) {
            switch (orderType) {
                case "수량" -> comparator = Comparator.comparing(SafeStock::getQuantity);
                case "수정일시" -> comparator = Comparator.comparing(SafeStock::getAcceptName);
            }
        }

        // 내림차순 여부 적용
        if (isDesc) {
            comparator = comparator.reversed();
        }

        safeStocks.sort(comparator);

        List<SafeFilterDto> safeFilterDtos = safeStocks.stream()
                .map(safeStock -> SafeFilterDto.of(
                        safeStock.getCreatedAt(),
                        safeStock.getQuantity(),
                        safeStock.getReason(),
                        safeStock.getAcceptName()
                )).collect(Collectors.toList());

        return SafeResponse.of(safeFilterDtos);
    }
}
