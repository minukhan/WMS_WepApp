package hyundai.partservice.app.supplier.service;

import hyundai.partservice.app.supplier.adapter.dto.SupplierResponse;
import hyundai.partservice.app.supplier.application.entity.Supplier;
import hyundai.partservice.app.supplier.application.port.out.FindByIdSupplierPort;
import hyundai.partservice.app.supplier.application.service.FindByIdSupplierService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class FindByIdSupplierServiceTest {

    @Mock
    private FindByIdSupplierPort findByIdSupplierPort;

    @InjectMocks
    private FindByIdSupplierService findByIdSupplierService;

    private Supplier supplier;

    @BeforeEach
    void setUp() {
        supplier = Supplier.builder()
                .id(1L)
                .name("민욱공장")
                .build();
    }

    @Test
    void findById_ShouldReturnSupplierResponse() {
        // Given
        Long supplierId = 1L;
        when(findByIdSupplierPort.findById(supplierId)).thenReturn(supplier);

        // When
        SupplierResponse response = findByIdSupplierService.findById(supplierId);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.supplierDto().supplierName()).isEqualTo("민욱공장");

    }
}