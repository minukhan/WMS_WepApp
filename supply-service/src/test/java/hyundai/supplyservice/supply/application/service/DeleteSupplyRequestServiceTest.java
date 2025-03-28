package hyundai.supplyservice.supply.application.service;

import hyundai.supplyservice.app.supply.application.entity.SupplyRequest;
import hyundai.supplyservice.app.supply.application.port.in.delete.DeleteSupplyResponseDto;
import hyundai.supplyservice.app.supply.application.port.out.SupplyRequestPort;
import hyundai.supplyservice.app.supply.application.service.DeleteSupplyRequestService;
import hyundai.supplyservice.app.supply.exception.RequestCannotBeDeletedException;
import hyundai.supplyservice.app.supply.exception.RequestNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class DeleteSupplyRequestServiceTest {

    @InjectMocks
    private DeleteSupplyRequestService deleteSupplyRequestService;

    @Mock
    private SupplyRequestPort supplyRequestPort;

    @Test
    @DisplayName("승인전(waiting) 요청 삭제 성공")
    void deleteSupplyRequest_Success(){
        // given
        Long requestId = 1L;
        LocalDate now = LocalDate.now();

        SupplyRequest supplyRequest = SupplyRequest.builder()
                .id(requestId)
                .userId(10L)
                .requestedAt(now)
                .deadline(now.plusDays(7))
                .status("WAITING")
                .supplySchedule(null)
                .supplyRequestParts(new ArrayList<>())
                .build();


        when(supplyRequestPort.findById(requestId)).thenReturn(supplyRequest);
        doNothing().when(supplyRequestPort).delete(requestId);

        // when
        DeleteSupplyResponseDto response = deleteSupplyRequestService.deleteSupplyRequest(requestId);

        // then
        verify(supplyRequestPort).findById(requestId);
        verify(supplyRequestPort).delete(requestId);
        assertEquals(requestId, response.requestId());
        assertEquals("WAITING", response.status());
        assertEquals("삭제 성공!", response.message());

    }

    @Test
    @DisplayName("대기 상태가 아닌 경우 삭제 불가")
    void deleteSupplyRequest_FailsIfNotWaiting() {
        // given
        Long requestId = 1L;
        LocalDate now = LocalDate.now();

        SupplyRequest supplyRequest = SupplyRequest.builder()
                .id(requestId)
                .userId(100L)
                .requestedAt(now)
                .deadline(now.plusDays(7))
                .status("APPROVED")
                .supplySchedule(null)
                .supplyRequestParts(new ArrayList<>())
                .build();

        when(supplyRequestPort.findById(requestId)).thenReturn(supplyRequest);

        // when & then
        assertThrows(RequestCannotBeDeletedException.class,
                () -> deleteSupplyRequestService.deleteSupplyRequest(requestId));

        verify(supplyRequestPort).findById(requestId);
        verify(supplyRequestPort, never()).delete(requestId);
        //never은 delete가 호출되지 않아야 통과
    }

    @Test
    @DisplayName("존재하지 않는 요청 ID로 삭제 시도시 예외발생")
    void deleteSupplyRequest_FailsIdNotFound() {
        // given
        Long requestId = 999L;
        when(supplyRequestPort.findById(requestId))
                .thenThrow(new RequestNotFoundException("해당 요청은 찾을 수 없습니다"));

        // when & then
        assertThrows(RequestNotFoundException.class,
                () -> deleteSupplyRequestService.deleteSupplyRequest(requestId));

        verify(supplyRequestPort).findById(requestId);
        verify(supplyRequestPort, never()).delete(requestId);



    }

}
