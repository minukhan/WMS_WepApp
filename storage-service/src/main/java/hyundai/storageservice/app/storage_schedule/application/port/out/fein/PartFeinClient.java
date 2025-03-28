package hyundai.storageservice.app.storage_schedule.application.port.out.fein;


import hyundai.storageservice.app.storage_schedule.adapter.dto.fein.*;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "part-service")
public interface PartFeinClient {

    @PostMapping("/parts/inventory/info/partids")
    public InventoryStorageResponse findPartInventoryByPartIds(@RequestBody List<String> partIds);

    @PostMapping("/parts/section/store")
    public SectionFindPositionResponse findStorePosition(@RequestBody SectionFindPositionRequest request);

    @GetMapping("/parts/section/storage/all")
    public SectionListStorageResponse findAllStorePosition();

    @GetMapping("/parts/{partId}")
    public PartPurchaseResponse findPart(@PathVariable String partId);

    @PostMapping("/parts/inventory/increment")
    public ResponseEntity<String> incrementInventory(
            @Parameter(description = "부품 ID", required = true, example = "GHP003")
            @RequestParam String partId,

            @Parameter(description = "부품이 위치한 층", required = true, example = "2")
            @RequestParam int floor,

            @Parameter(description = "부품이 위치한 섹션 이름", required = true, example = "A-2")
            @RequestParam String sectionName
    );

    @PostMapping("/parts/inventory/decrement")
    public ResponseEntity<String> decrementInventory(
            @Parameter(description = "부품이 위치한 섹션 이름", required = true, example = "A-2")
            @RequestParam String sectionName,

            @Parameter(description = "부품이 위치한 층", required = true, example = "2")
            @RequestParam int floor,

            @Parameter(description = "부품 ID", required = true, example = "GHP003")
            @RequestParam String partId
    );

}

