package hyundai.purchaseservice.purchase.application.port.out;

import hyundai.purchaseservice.purchase.adapter.out.dto.SearchResponse;

import java.util.List;

public interface SearchRequestPort {
    List<SearchResponse> search(Integer offset, Integer size, String type, String searchType, String searchText, String orderType, String orderBy);
    List<SearchResponse> searchByIds(Integer offset, Integer size, String type, List<String> ids, String orderType, String orderBy);

    Long getTotalPage(String type, String searchType, String searchText);
    Long getTotalPageById(String type, List<String> ids);

    SearchResponse getPurchaseRequest(Long id);
}
