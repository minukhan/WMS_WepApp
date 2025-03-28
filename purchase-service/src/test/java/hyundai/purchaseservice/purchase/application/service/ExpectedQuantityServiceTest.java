package hyundai.purchaseservice.purchase.application.service;


import hyundai.purchaseservice.purchase.adapter.in.dto.ExpectedQuantityRequest;
import hyundai.purchaseservice.purchase.adapter.in.dto.ExpectedQuantityResponse;
import hyundai.purchaseservice.purchase.application.dto.PartQuantityInfoDto;
import hyundai.purchaseservice.purchase.adapter.out.dto.PartIdAndQuantityResponse;
import hyundai.purchaseservice.purchase.application.exception.LocalDateError;
import hyundai.purchaseservice.purchase.application.port.out.PurchaseSchedulePort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ExpectedQuantityServiceTest {
    @InjectMocks
    private ExpectedQuantityService service;

    @Mock
    private PurchaseSchedulePort port;

    @Test
    @DisplayName("입고 예정 수량 조회 - 지난 날짜 입력")
    void prefixSumStockAmountWithPastDate(){
        //Given
        LocalDate date = LocalDate.now().minusDays(1);
        List<String> partIds = List.of("BHP001");

        ExpectedQuantityRequest request = new ExpectedQuantityRequest(partIds, date);

        //When
        LocalDateError error = assertThrows(LocalDateError.class, () -> service.prefixSumStockAmount(request));

        //Then
        assertEquals("[Error] " + date.toString() + "는 현재와 같거나 미래여야 합니다.", error.getMessage());
    }

    @Test
    @DisplayName("입고 예정 수량 조회 - 빈 품목 코드 배열")
    void prefixSumStockAmountWithEmptyList(){
        //Given
        LocalDate date = LocalDate.now();
        ExpectedQuantityRequest expectedRequest = new ExpectedQuantityRequest(List.of(), date);
        ExpectedQuantityResponse expectedResponse = new ExpectedQuantityResponse(0L, List.of());

        //When
        ExpectedQuantityResponse response = service.prefixSumStockAmount(expectedRequest);

        //Then
        assertEquals(expectedResponse, response);
    }

    @Test
    @DisplayName("입고 예정 수량 조회 - 정상")
    void prefixSumStockAmountSuccess(){
        //Given
        LocalDate date = LocalDate.now();
        List<String> partIds = List.of("BHP001", "BHP002", "BHP003");
        ExpectedQuantityRequest expectedRequest = new ExpectedQuantityRequest(partIds, date);

        when(port.getPartIds(expectedRequest.partIds(), LocalDate.now(), expectedRequest.due()))
                .thenReturn(List.of(new PartIdAndQuantityResponse("BHP001", 12),
                        new PartIdAndQuantityResponse("BHP002", 10),
                        new PartIdAndQuantityResponse("BHP001", 13)));

        ExpectedQuantityResponse expectedResponse = new ExpectedQuantityResponse(35L, List.of(
                new PartQuantityInfoDto("BHP001", 25L),
                new PartQuantityInfoDto("BHP002", 10L),
                new PartQuantityInfoDto("BHP003", 0L)
        ));

        //When
        ExpectedQuantityResponse response = service.prefixSumStockAmount(expectedRequest);

        //Then
        assertEquals(expectedResponse, response);
    }
}
