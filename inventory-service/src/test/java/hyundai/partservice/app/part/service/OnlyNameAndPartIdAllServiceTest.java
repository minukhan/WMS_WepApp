package hyundai.partservice.app.part.service;


import hyundai.partservice.app.part.adapter.dto.OnlyPartNamePartIdDto;
import hyundai.partservice.app.part.adapter.dto.OnlyPartNamePartIdListResponse;
import hyundai.partservice.app.part.application.entity.Part;
import hyundai.partservice.app.part.application.port.out.OnlyNameAndPartIdAllPort;
import hyundai.partservice.app.part.application.service.OnlyNameAndPartIdAllService;
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
class OnlyNameAndPartIdAllServiceTest {

    @Mock
    private OnlyNameAndPartIdAllPort onlyNameAndPartIdAllPort;

    @InjectMocks
    private OnlyNameAndPartIdAllService onlyNameAndPartIdAllService;

    private Part part1;
    private Part part2;

    @BeforeEach
    void setUp() {
        // Part(부품 ID, 부품명, 최대재고, 최적재고, 안전재고)
        part1 = createFakePartWithoutSupplier();
        part2 = createFakePartWithoutSupplier2();
    }

    @Test
    void onlyNameAndPartIdAllUseCase_모든_부품을_조회한다() {
        // given
        List<Part> parts = List.of(part1, part2);
        when(onlyNameAndPartIdAllPort.getAllParts()).thenReturn(parts);

        // when
        OnlyPartNamePartIdListResponse response = onlyNameAndPartIdAllService.onlyNameAndPartIdAllUseCase();

        // then
        assertThat(response.onlyPartNamePartIdDtos()).hasSize(2);
        assertThat(response.onlyPartNamePartIdDtos()).extracting(OnlyPartNamePartIdDto::partId)
                .containsExactly("TEST123", "TEST456");
        assertThat(response.onlyPartNamePartIdDtos()).extracting(OnlyPartNamePartIdDto::partName)
                .containsExactly("Fake Part A", "Fake Part B");

        verify(onlyNameAndPartIdAllPort, times(1)).getAllParts();
    }
}
