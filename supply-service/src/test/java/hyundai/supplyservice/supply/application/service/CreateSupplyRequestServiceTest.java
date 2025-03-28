package hyundai.supplyservice.supply.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import hyundai.supplyservice.app.infrastructure.util.UserIdResolver;
import hyundai.supplyservice.app.supply.application.entity.SupplyRequest;
import hyundai.supplyservice.app.supply.application.entity.SupplyRequestPart;
import hyundai.supplyservice.app.supply.application.port.in.commondto.PartCountDto;
import hyundai.supplyservice.app.supply.application.port.in.create.CreateSupplyRequestDto;
import hyundai.supplyservice.app.supply.application.port.in.create.CreateSupplyRequestResponseDto;
import hyundai.supplyservice.app.supply.application.port.out.SupplyRequestPartPort;
import hyundai.supplyservice.app.supply.application.port.out.SupplyRequestPort;
import hyundai.supplyservice.app.supply.application.port.out.feign.PartController;
import hyundai.supplyservice.app.supply.application.service.CreateSupplyRequestService;
import hyundai.supplyservice.app.supply.exception.AtLeastOnePartRequiredException;
import hyundai.supplyservice.app.supply.exception.InvalidPartRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CreateSupplyRequestServiceTest {

    @InjectMocks
    private CreateSupplyRequestService createSupplyRequestService;

    @Mock
    private SupplyRequestPort supplyRequestPort;

    @Mock
    private SupplyRequestPartPort supplyRequestPartPort;

    @Mock
    private PartController partController;

    @Mock
    private UserIdResolver userIdResolver;

    @Test
    @DisplayName("주문서 생성 성공")
    void createSupplyRequest() {
        // given
        LocalDate deadline = LocalDate.now().plusDays(7);
        List<PartCountDto> parts = List.of(
                new PartCountDto("GHP001", 100)
        );

        CreateSupplyRequestDto command = CreateSupplyRequestDto.builder()
                .deadline(deadline)
                .parts(parts)
                .build();

        Long userId = 1L;
        when(userIdResolver.getCurrentUserId()).thenReturn(userId);

        // feign client 테스트
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode partInfoNode = mapper.createObjectNode();
        ObjectNode partName = mapper.createObjectNode();
        partName.put("partName", "TestPart");
        partInfoNode.set("partSupplierDto", partName);
        when(partController.getPartInfo(any(String.class))).thenReturn(partInfoNode);

        SupplyRequest savedSupplyRequest = SupplyRequest.builder()
                .id(1L)
                .userId(userId)
                .deadline(deadline)
                .status("WAITING")
                .build();
        when(supplyRequestPort.save(any(SupplyRequest.class))).thenReturn(savedSupplyRequest);

        List<SupplyRequestPart> savedParts = List.of(
                SupplyRequestPart.builder()
                        .partId("GHP001")
                        .name("TestPart")
                        .quantity(100)
                        .supplyRequest(savedSupplyRequest)
                        .build()
        );
        when(supplyRequestPartPort.saveAll(any())).thenReturn(savedParts);

        // when
        CreateSupplyRequestResponseDto response = createSupplyRequestService.createSupplyRequest(command);

        // then
        assertNotNull(response);
        assertEquals(1L, response.requestId());
        assertEquals("WAITING", response.status());
        assertEquals("주문이 접수됨", response.message());

        verify(supplyRequestPort).save(any(SupplyRequest.class));
        verify(supplyRequestPartPort).saveAll(any());
        verify(partController).getPartInfo(any(String.class));

    }


    @Test
    @DisplayName("부품 없이 주문 생성 시 예외 발생")
    void createSupplyRequest_ThrowsExceptionWhenNoPartsProvided() {
        // given
        CreateSupplyRequestDto command = CreateSupplyRequestDto.builder()
                .deadline(LocalDate.now().plusDays(7))
                .parts(new ArrayList<>())
                .build();

        // when & then
        assertThrows(AtLeastOnePartRequiredException.class,
                () -> createSupplyRequestService.createSupplyRequest(command));

        verify(supplyRequestPort, never()).save(any());
        verify(supplyRequestPartPort, never()).saveAll(any());
    }

    @Test
    @DisplayName("수량 0으로 요청 시 예외 발생")
    void createSupplyRequest_ThrowsExceptionWhenInvalidQuantity() {
        // given
        List<PartCountDto> parts = List.of(
                new PartCountDto("GHP001", 0)
        );

        CreateSupplyRequestDto command = CreateSupplyRequestDto.builder()
                .deadline(LocalDate.now().plusDays(7))
                .parts(parts)
                .build();

        // when & then
        assertThrows(InvalidPartRequestException.class,
                () -> createSupplyRequestService.createSupplyRequest(command));

        verify(supplyRequestPort, never()).save(any());
        verify(supplyRequestPartPort, never()).saveAll(any());
    }


}
