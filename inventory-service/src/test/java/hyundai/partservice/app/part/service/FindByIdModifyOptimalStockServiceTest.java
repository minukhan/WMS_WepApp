package hyundai.partservice.app.part.service;

import hyundai.partservice.app.part.application.entity.Part;
import hyundai.partservice.app.part.application.port.out.FindByIdModifySafetyStockAndOptimalStockPort;
import hyundai.partservice.app.part.application.service.FindByIdModifyOptimalStockService;
import hyundai.partservice.app.part.exception.BelowSafetyStockException;
import hyundai.partservice.app.part.exception.ExceedsMaxStockException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static hyundai.partservice.app.part.fake.FakePartFactory.createFakePartWithoutSupplier;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindByIdModifyOptimalStockServiceTest {

    @Mock
    private FindByIdModifySafetyStockAndOptimalStockPort findByIdModifySafetyStockAndOptimalStockPort;

    @InjectMocks
    private FindByIdModifyOptimalStockService findByIdModifyOptimalStockService;

    private Part part;

    @BeforeEach
    void setUp() {
        // Part(부품ID, 이름, 최대재고, 최적재고, 안전재고)
        part = createFakePartWithoutSupplier();
    }

    @Test
    void modifyOptimal_정상적으로_변경된다() {
        // given
        when(findByIdModifySafetyStockAndOptimalStockPort.findById("P123")).thenReturn(part);

        // when
        findByIdModifyOptimalStockService.modifyOptimal("P123", 150);

        // then
        verify(findByIdModifySafetyStockAndOptimalStockPort, times(1)).save(any(Part.class));
    }

    @Test
    void modifyOptimal_최대재고를_초과하면_예외발생() {
        // given
        when(findByIdModifySafetyStockAndOptimalStockPort.findById("P123")).thenReturn(part);

        // when & then
        assertThatThrownBy(() -> findByIdModifyOptimalStockService.modifyOptimal("P123", 250))
                .isInstanceOf(ExceedsMaxStockException.class);

        verify(findByIdModifySafetyStockAndOptimalStockPort, never()).save(any(Part.class));
    }

    @Test
    void modifyOptimal_안전재고보다_낮으면_예외발생() {
        // given
        when(findByIdModifySafetyStockAndOptimalStockPort.findById("P123")).thenReturn(part);

        // when & then
        assertThatThrownBy(() -> findByIdModifyOptimalStockService.modifyOptimal("P123", 1))
                .isInstanceOf(BelowSafetyStockException.class);

        verify(findByIdModifySafetyStockAndOptimalStockPort, never()).save(any(Part.class));
    }
}
