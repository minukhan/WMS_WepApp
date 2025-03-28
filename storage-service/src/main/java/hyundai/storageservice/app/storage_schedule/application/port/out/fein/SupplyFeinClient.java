package hyundai.storageservice.app.storage_schedule.application.port.out.fein;


import hyundai.storageservice.app.storage_schedule.adapter.dto.fein.MonthlyTopPartsResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = "supply-service")
public interface SupplyFeinClient {

    @GetMapping("/supply/monthly-top-parts")
    public MonthlyTopPartsResponseDto getSupply(
            @RequestParam int year,
            @RequestParam int month
    );

}
