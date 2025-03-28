package hyundai.partservice.app.part.service;


import hyundai.partservice.app.part.application.entity.Part;
import hyundai.partservice.app.part.application.port.out.FindByNamePartPort;
import hyundai.partservice.app.part.application.service.FindByNamePartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;


@ExtendWith(SpringExtension.class)
public class FindByNamePartServiceTest {

    @Mock
    private FindByNamePartPort findByNamePartPort;

    @InjectMocks
    private FindByNamePartService findByNamePartService;

    private Part part1;

    @BeforeEach
    void setUp() {
        // 테스트용 데이터 준비
         part1 = Part.builder()
                .id("P001")
                .name("브레이크 패드")
                .quantity(50)
                .safetyStock(20)
                .maxStock(100)
                .optimalStock(60)
                .deliveryDuration(5)
                .price(50000L)
                .category("엔진 부품")
                .build();

    }

    @Test
    void testFindByNamePart() {

        //given
        String partName = "브레이크 패드";
        when(findByNamePartPort.findByNamePart(partName)).thenReturn(part1);

        // When (서비스 실행)
        var response = findByNamePartService.findByNamePart(partName);

        // Then (검증)
        assertThat(response.partDto().partName()).isEqualTo("브레이크 패드");
        assertThat(response.partDto().partId()).isEqualTo("P001");

        // Mock 메서드가 정확히 실행되었는지 검증
        verify(findByNamePartPort, times(1)).findByNamePart(partName);
    }
}
