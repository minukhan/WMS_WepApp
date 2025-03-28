package hyundai.storageservice.app.storage_schedule.application.port.in;


import hyundai.storageservice.app.storage_schedule.adapter.dto.FilterResponse;

public interface StorageFilterUseCase {
    public abstract FilterResponse getFilterResponse(String searchType,String searchText,String orderType, boolean isDesc);
}
