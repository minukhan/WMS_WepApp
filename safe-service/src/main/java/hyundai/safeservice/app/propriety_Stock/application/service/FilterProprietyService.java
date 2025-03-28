package hyundai.safeservice.app.propriety_Stock.application.service;

import hyundai.safeservice.app.propriety_Stock.application.entity.ProprietyStock;
import hyundai.safeservice.app.propriety_Stock.application.port.in.FilterProprietyUseCase;
import hyundai.safeservice.app.propriety_Stock.application.port.out.FilterProprietyPort;
import hyundai.safeservice.app.safe_Stock.adapter.dto.SafeFilterDto;
import hyundai.safeservice.app.safe_Stock.adapter.dto.SafeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilterProprietyService implements FilterProprietyUseCase {

    private final FilterProprietyPort filterProprietyPort;

    @Override
    public SafeResponse filter(String partId,String searchType, String searchText, String orderType, boolean isDesc) {

        List<ProprietyStock> proprietyStocks = filterProprietyPort.findBypartId(partId);

        //  "수정자" 검색 필터만 적용
        if ("수정자".equals(searchType) && searchText != null && !searchText.isBlank()) {
            String lowerSearchText = searchText.toLowerCase();
            proprietyStocks = proprietyStocks.stream()
                    .filter(safeStock -> safeStock.getAcceptName() != null && safeStock.getAcceptName().toLowerCase().contains(lowerSearchText))
                    .collect(Collectors.toList());
        }

        // 2. 정렬 (orderType + isDesc)
        Comparator<ProprietyStock> comparator = Comparator.comparing(ProprietyStock::getId);  // 기본: ID 기준

        if (orderType != null && !orderType.isBlank()) {
            switch (orderType) {
                case "수량" -> comparator = Comparator.comparing(ProprietyStock::getQuantity);
                case "수정일시" -> comparator = Comparator.comparing(ProprietyStock::getAcceptName);
            }
        }

        // 내림차순 여부 적용
        if (isDesc) {
            comparator = comparator.reversed();
        }

        proprietyStocks.sort(comparator);

        List<SafeFilterDto> safeFilterDtos = proprietyStocks.stream()
                .map(safeStock -> SafeFilterDto.of(
                        safeStock.getCreatedAt(),
                        safeStock.getQuantity(),
                        safeStock.getReason(),
                        safeStock.getAcceptName()
                )).collect(Collectors.toList());

        return SafeResponse.of(safeFilterDtos);

    }
}
