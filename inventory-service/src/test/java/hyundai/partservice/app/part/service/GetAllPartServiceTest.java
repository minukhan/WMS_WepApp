package hyundai.partservice.app.part.service;

import hyundai.partservice.app.part.adapter.dto.PartListPaginationResponse;
import hyundai.partservice.app.part.application.entity.Part;
import hyundai.partservice.app.part.application.port.out.GetAllPartPort;
import hyundai.partservice.app.part.application.service.GetAllPartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class GetAllPartServiceTest {


    @Mock
    private GetAllPartPort getAllPartPort;

    @InjectMocks
    private GetAllPartService getAllPartService;

    private Pageable pageable;
    private List<Part> partList;
    private Page<Part> partPage;

    @BeforeEach
    void setUp() {
        // 페이징 설정
        pageable = PageRequest.of(0, 10, Sort.by("id").ascending());

        // 테스트용 데이터 준비
        Part part1 = Part.builder()
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

        Part part2 = Part.builder()
                .id("P002")
                .name("엔진 오일 필터")
                .quantity(30)
                .safetyStock(15)
                .maxStock(80)
                .optimalStock(50)
                .deliveryDuration(3)
                .price(15000L)
                .category("엔진 부품")
                .build();

        Part part3 = Part.builder()
                .id("P003")
                .name("와이퍼 블레이드")
                .quantity(70)
                .safetyStock(25)
                .maxStock(120)
                .optimalStock(80)
                .deliveryDuration(2)
                .price(12000L)
                .category("엔진 부품")
                .build();

        partList = List.of(part1, part2, part3);
        partPage = new PageImpl<>(partList, pageable, partList.size());
    }

    @Test
    void getAllPart() {
        //given
        when(getAllPartPort.getAllParts(pageable)).thenReturn(partPage);

        //when
        PartListPaginationResponse response = getAllPartService.getAllParts(pageable);

        //then
        assertThat(response).isNotNull();
        assertThat(response.content()).hasSize(3);
        assertThat(response.content().get(0).partName()).isEqualTo("브레이크 패드");
        assertThat(response.content().get(1).partName()).isEqualTo("엔진 오일 필터");

    }


}
