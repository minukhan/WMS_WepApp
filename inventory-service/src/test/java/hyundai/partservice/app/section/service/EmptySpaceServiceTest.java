package hyundai.partservice.app.section.service;

import hyundai.partservice.app.section.adapter.dto.EmptySpaceResponse;
import hyundai.partservice.app.section.application.port.out.EmptySpacePort;
import hyundai.partservice.app.section.application.service.EmptySpaceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmptySpaceServiceTest {

    @Mock
    private EmptySpacePort emptySpacePort;

    @InjectMocks
    private EmptySpaceService emptySpaceService;

    @BeforeEach
    void setUp() {
        // 테스트 초기 설정 (선택 사항)
    }

    @Test
    void 창고_빈공간_조회_정상작동() {
        // Given (Mock 데이터 설정)
        int currentQuantity = 400;  // 현재 차있는 개수
        int maxQuantity = 1000;  // 창고 최대 수용량

        when(emptySpacePort.currentQuantity()).thenReturn(currentQuantity);
        when(emptySpacePort.getTotalQuantity()).thenReturn(maxQuantity);

        // When
        EmptySpaceResponse response = emptySpaceService.emptySpaceResponse();

        // Then (검증)
        assertThat(response.currentCount()).isEqualTo(currentQuantity);
        assertThat(response.totalCount()).isEqualTo(maxQuantity);
        assertThat(response.emptySpace()).isEqualTo(maxQuantity - currentQuantity);
        assertThat(response.persent()).isEqualTo(40.0);  // 400/1000 * 100 = 40.0%
    }

    @Test
    void 창고가_비어있을때_퍼센트_계산_정상작동() {
        // Given
        int currentQuantity = 0;  // 차있는 공간이 없음
        int maxQuantity = 1000;   // 창고 최대 수용량

        when(emptySpacePort.currentQuantity()).thenReturn(currentQuantity);
        when(emptySpacePort.getTotalQuantity()).thenReturn(maxQuantity);

        // When
        EmptySpaceResponse response = emptySpaceService.emptySpaceResponse();

        // Then
        assertThat(response.persent()).isEqualTo(0.0); // 비율 0%
    }

    @Test
    void 창고_최대수용량이_0일때_퍼센트_정상작동() {
        // Given
        int currentQuantity = 0;
        int maxQuantity = 0; // 창고가 없거나 수용량이 0

        when(emptySpacePort.currentQuantity()).thenReturn(currentQuantity);
        when(emptySpacePort.getTotalQuantity()).thenReturn(maxQuantity);

        // When
        EmptySpaceResponse response = emptySpaceService.emptySpaceResponse();

        // Then
        assertThat(response.persent()).isEqualTo(0.0); // 나눗셈 오류 없이 0% 반환
    }
}
