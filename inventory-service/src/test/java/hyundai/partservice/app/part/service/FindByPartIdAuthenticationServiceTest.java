package hyundai.partservice.app.part.service;

import hyundai.partservice.app.part.adapter.dto.PartAuthenticationResponse;
import hyundai.partservice.app.part.adapter.dto.PartIdRequest;
import hyundai.partservice.app.part.application.entity.Part;
import hyundai.partservice.app.part.application.port.out.FindByPartIdAuthenticationPort;
import hyundai.partservice.app.part.application.service.FindByPartIdAuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static hyundai.partservice.app.part.fake.FakePartFactory.createFakePartWithoutSupplier;
import static hyundai.partservice.app.part.fake.FakePartFactory.createFakePartWithoutSupplier2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindByPartIdAuthenticationServiceTest {

    @Mock
    private FindByPartIdAuthenticationPort findByPartIdAuthenticationPort;

    @InjectMocks
    private FindByPartIdAuthenticationService findByPartIdAuthenticationService;

    private Part part1;
    private Part part2;

    @BeforeEach
    void setUp() {
        // Part(부품 ID, 부품명, 최대재고, 최적재고, 안전재고)
        part1 = createFakePartWithoutSupplier();
        part2 = createFakePartWithoutSupplier2();
    }

    @Test
    void findByPartIds_정상적으로_응답을_생성한다() {
        // given
        List<String> partIds = List.of("TEST123", "TEST456");
        PartIdRequest request = new PartIdRequest(partIds);

        when(findByPartIdAuthenticationPort.findByPartId("TEST123")).thenReturn(part1);
        when(findByPartIdAuthenticationPort.findByPartId("TEST456")).thenReturn(part2);
        when(findByPartIdAuthenticationPort.partCount("TEST123")).thenReturn(120);
        when(findByPartIdAuthenticationPort.partCount("TEST456")).thenReturn(180);
        when(findByPartIdAuthenticationPort.currentTotalCount()).thenReturn(5000);

        // when
        PartAuthenticationResponse response = findByPartIdAuthenticationService.findByPartIds(request);

        // then
        assertThat(response.total()).isEqualTo(120 + 180); // 현재 파트 재고 총합
        assertThat(response.wareHouseCurrentTotalCount()).isEqualTo(5000);
        assertThat(response.maxWareHouseTotal()).isEqualTo(51840);
        assertThat(response.partAuthenticationDtos()).hasSize(2);

        verify(findByPartIdAuthenticationPort, times(1)).findByPartId("TEST123");
        verify(findByPartIdAuthenticationPort, times(1)).findByPartId("TEST456");
        verify(findByPartIdAuthenticationPort, times(1)).partCount("TEST123");
        verify(findByPartIdAuthenticationPort, times(1)).partCount("TEST456");
        verify(findByPartIdAuthenticationPort, times(1)).currentTotalCount();
    }
}
