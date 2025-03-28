package hyundai.purchaseservice.purchase.application.port.out.feign;

import hyundai.purchaseservice.common.config.FeignConfig;
import hyundai.purchaseservice.purchase.adapter.out.dto.feign.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "part-service", configuration = FeignConfig.class)
public interface PartServiceClient {
    @GetMapping("/parts")
    PartsResponse getParts(@RequestParam(required = false) Integer page,
                           @RequestParam(required = false) Integer size,
                           @RequestParam(required = false) String sort);

    @GetMapping("/parts/category")
    PartsResponse getPartsByCategory(@RequestParam String category,
                                     @RequestParam(required = false) Integer page,
                                     @RequestParam(required = false) Integer size,
                                     @RequestParam(required = false) String sort);

    @GetMapping("/parts/{partId}")
    PartWithSupplierResponse getPartInfo(@PathVariable("partId") String id);

    @GetMapping("/parts/supplier/contain/name")
    SupplierPartListResponse getSupplierPartInfo(@RequestParam("name") String supplierName);

    @GetMapping("/parts/contain/name")
    PartWithSupplierListResponse getPartName(@RequestParam("name") String partName);

    @PostMapping("/parts/partIds")
    PartWithSupplierListResponse getPartInfoList(@RequestBody List<String> partIds);

    @PostMapping("/parts/sectionIds")
    SectionListDto getSectionInfoList(@RequestBody List<Long> sectionIds);

    @GetMapping("/parts/section/storage/all")
    SectionListDto getAllSectionInfoList();

    @GetMapping("/parts/section/name")
    SectionListDto getSectionInfoWithName(@RequestParam("name") String sectionName);

    @GetMapping("/parts/section/alpa")
    InventoryListDto getInventoryList(@RequestParam("alpa") String sectionArea);

    //입고 QR - part 부품 재고 업데이트
    @PostMapping("/parts/inventory/increment")
    String incrementInventory(@RequestParam("partId") String partId, @RequestParam("floor") Integer floor, @RequestParam("sectionName") String sectionName);

    //부품 요청서 작성시 위치 구하기
    @GetMapping("/parts/inventory/part/{partid}")
    InventoryListByPartIdDto getInvetoryInfoOfPart(@PathVariable("partid") String partId);

    @GetMapping("/parts/space")
    SpaceResponse getSpaceInfo();
}
