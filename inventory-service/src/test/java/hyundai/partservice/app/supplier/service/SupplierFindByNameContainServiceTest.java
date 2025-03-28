package hyundai.partservice.app.supplier.service;


import hyundai.partservice.app.part.application.entity.Part;
import hyundai.partservice.app.supplier.adapter.dto.SupplierPartDto;
import hyundai.partservice.app.supplier.adapter.dto.SupplierPartResponse;
import hyundai.partservice.app.supplier.application.entity.Supplier;
import hyundai.partservice.app.supplier.application.port.out.SupplierFindByNameContainPort;
import hyundai.partservice.app.supplier.application.service.SupplierFindByNameContainService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SupplierFindByNameContainServiceTest {

    @Mock
    private SupplierFindByNameContainPort findByNameContainPort;

    @InjectMocks
    private SupplierFindByNameContainService supplierFindByNameContainService;

    private Supplier supplier1;
    private Supplier supplier2;

    @BeforeEach
    void setUp() {
        // Mock 데이터 생성
        Part part1 = Part.builder().id("1L").name("Part A").build();
        Part part2 = Part.builder().id("2L").name("Part B").build();
        Part part3 = Part.builder().id("3L").name("Part C").build();

        supplier1 = Supplier.builder()
                .id(1L)
                .name("Hyundai Motors")
                .parts(Arrays.asList(part1, part2))
                .build();

        supplier2 = Supplier.builder()
                .id(2L)
                .name("Hyundai Heavy Industries")
                .parts(List.of(part3))
                .build();
    }

    @Test
    void findByNameContains_Success() {
        // Given
        String searchName = "Hyundai";
        when(findByNameContainPort.findByNameContains(searchName))
                .thenReturn(List.of(supplier1, supplier2));

        // When
        SupplierPartResponse response = supplierFindByNameContainService.findByNameContains(searchName);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.supplierPartDtos()).hasSize(2);

        SupplierPartDto dto1 = response.supplierPartDtos().get(0);
        SupplierPartDto dto2 = response.supplierPartDtos().get(1);

        assertThat(dto1.supplierName()).isEqualTo("Hyundai Motors");
        assertThat(dto1.parts()).hasSize(2);
        assertThat(dto1.parts().get(0).partName()).isEqualTo("Part A");

        assertThat(dto2.supplierName()).isEqualTo("Hyundai Heavy Industries");
        assertThat(dto2.parts()).hasSize(1);
        assertThat(dto2.parts().get(0).partName()).isEqualTo("Part C");

        verify(findByNameContainPort, times(1)).findByNameContains(searchName);
    }

    @Test
    void findByNameContainsIsEmpty() {
        // Given
        String searchName = "Unknown";
        when(findByNameContainPort.findByNameContains(searchName))
                .thenReturn(List.of());

        // When
        SupplierPartResponse response = supplierFindByNameContainService.findByNameContains(searchName);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.supplierPartDtos()).isEmpty();

        verify(findByNameContainPort, times(1)).findByNameContains(searchName);
    }
}