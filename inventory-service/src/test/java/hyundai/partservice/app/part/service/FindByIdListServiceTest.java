package hyundai.partservice.app.part.service;

import hyundai.partservice.app.part.adapter.dto.PartListPurchaseResponse;
import hyundai.partservice.app.part.application.entity.Part;
import hyundai.partservice.app.part.application.port.out.FindByIdListPort;
import hyundai.partservice.app.part.application.service.FindByIdListService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

import static hyundai.partservice.app.part.fake.FakePartFactory.createFakePartWithSupplier;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class FindByIdListServiceTest {

    @InjectMocks
    private FindByIdListService findByIdListService;

    @Mock
    private FindByIdListPort findByIdListPort;

    @BeforeEach
    void setUp() {
    }

    @Test
    void findByIdList_ShouldReturnPartListPurchaseResponse() {
        // Given
        List<String> partIds = Arrays.asList("1", "2", "3");
        List<Part> parts = Arrays.asList(
                createFakePartWithSupplier()
        );

        when(findByIdListPort.findByIdList(partIds)).thenReturn(parts);

        // When
        PartListPurchaseResponse response = findByIdListService.FindByIdList(partIds);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.partDtos()).hasSize(1);
        assertThat(response.partDtos().get(0).partName()).isEqualTo("Fake Part B");
    }
}
