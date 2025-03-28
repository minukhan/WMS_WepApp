package hyundai.supplyservice.app.supply.application.port.out.feign;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "part-service")
public interface PartController {
    // 검색을 위한 전체 부품 조회
    @GetMapping("/parts")
    JsonNode getPartInfo(@RequestParam("size") int size);

    //부품 Id로 부품 정보 조회
    @GetMapping("/parts/{partId}")
    JsonNode getPartInfo(@PathVariable("partId") String partId);

    // 부품 Id로 저장된 위치 및 개수 반환
    @GetMapping("/parts/inventory/part/{partId}")
    JsonNode getPartInventoryInfo(@PathVariable("partId") String partId);

    // qr 스캔시 부품 개수 감소
    @PostMapping("/parts/inventory/decrement")
    String decrementPartInventory(
            @RequestParam("sectionName") String sectionName,
            @RequestParam("floor")int floor,
            @RequestParam("partId") String partId
    );

}