package hyundai.partservice.app.part.service;

import hyundai.partservice.app.part.application.service.FindByIdModifySafetyStockService;
import org.junit.jupiter.api.extension.ExtendWith;

import hyundai.partservice.app.part.application.entity.Part;
import hyundai.partservice.app.part.application.port.out.FindByIdModifySafetyStockAndOptimalStockPort;
import hyundai.partservice.app.part.exception.ExceedsOptimalStockException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static hyundai.partservice.app.part.fake.FakePartFactory.createFakePartWithoutSupplier;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindByIdModifySafetyStockServiceTest {

    @Mock
    private FindByIdModifySafetyStockAndOptimalStockPort findByIdModifySafetyStockPort;

    @InjectMocks
    private FindByIdModifySafetyStockService findByIdModifySafetyStockService;

    private Part part;

    @BeforeEach
    void setUp() {
        part = createFakePartWithoutSupplier();
    }

    @Test
    void modifySafetyStock_정상적으로_변경된다() {
        // given
        when(findByIdModifySafetyStockPort.findById("P123")).thenReturn(part);

        // when
        findByIdModifySafetyStockService.modifySafetyStock("P123", 80);

        // then
        verify(findByIdModifySafetyStockPort, times(1)).save(any(Part.class));
    }

    @Test
    void modifySafetyStock_최적재고_초과시_예외발생() {
        // given
        when(findByIdModifySafetyStockPort.findById("P123")).thenReturn(part);

        // when & then
        assertThatThrownBy(() -> findByIdModifySafetyStockService.modifySafetyStock("P123", 160))
                .isInstanceOf(ExceedsOptimalStockException.class);

        verify(findByIdModifySafetyStockPort, never()).save(any(Part.class));
    }
}
