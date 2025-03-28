package hyundai.partservice.app.part.service;


import hyundai.partservice.app.part.adapter.dto.PartDto;
import hyundai.partservice.app.part.adapter.dto.PartListResponse;
import hyundai.partservice.app.part.application.entity.Part;
import hyundai.partservice.app.part.application.port.out.PartAllPort;
import hyundai.partservice.app.part.application.service.PartAllService;
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
public class PartAllServiceTest {

    @Mock
    private PartAllPort partAllPort;

    @InjectMocks
    private PartAllService partAllService;

    @Test
    public void testGetAllParts() {
        // Given
        Part part1 =  createFakePartWithoutSupplier();
        Part part2 = createFakePartWithoutSupplier2();

        when(partAllPort.getAllParts()).thenReturn(List.of(part1, part2));

        // When
        PartListResponse response = partAllService.getAllParts();

        // Then
        assertThat(response.partGetAllDtos()).hasSize(2);

        PartDto firstPart = response.partGetAllDtos().get(0);
        assertThat(firstPart.partId()).isEqualTo("TEST123");
        assertThat(firstPart.partName()).isEqualTo("Fake Part A");

        PartDto secondPart = response.partGetAllDtos().get(1);
        assertThat(secondPart.partId()).isEqualTo("TEST456");
        assertThat(secondPart.partName()).isEqualTo("Fake Part B");

        // Verify
        verify(partAllPort, times(1)).getAllParts();
    }
}
