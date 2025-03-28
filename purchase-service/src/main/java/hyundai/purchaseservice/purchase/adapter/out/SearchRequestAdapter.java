package hyundai.purchaseservice.purchase.adapter.out;

import hyundai.purchaseservice.infrastructure.mapper.PurchaseMapper;
import hyundai.purchaseservice.purchase.adapter.out.dto.SearchResponse;
import hyundai.purchaseservice.purchase.application.port.out.SearchRequestPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SearchRequestAdapter implements SearchRequestPort {

    private final PurchaseMapper purchaseMapper;

    @Override
    public List<SearchResponse> search(Integer offset, Integer size, String type, String searchType, String searchText, String orderType, String orderBy) {
        return purchaseMapper.searchRequests(offset, size, type, searchType, searchText, orderType, orderBy);
    }

    @Override
    public List<SearchResponse> searchByIds(Integer offset, Integer size, String type, List<String> ids, String orderType, String orderBy) {
        return purchaseMapper.searchRequestsByIds(offset, size, type, ids, orderType, orderBy);
    }

    @Override
    public Long getTotalPage(String type, String searchType, String searchText) {
        return purchaseMapper.countRequests(type, searchType, searchText);
    }

    @Override
    public Long getTotalPageById(String type, List<String> ids) {
        return purchaseMapper.countRequestsByIds(type, ids);
    }

    @Override
    public SearchResponse getPurchaseRequest(Long id) {
        return purchaseMapper.searchRequestDetail(id);
    }
}
