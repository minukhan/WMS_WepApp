package hyundai.partservice.app.part.service;

import hyundai.partservice.app.part.adapter.dto.PartListPurchaseResponse;
import hyundai.partservice.app.part.adapter.dto.PartListResponse;
import hyundai.partservice.app.part.application.entity.Part;
import hyundai.partservice.app.part.application.port.out.FIndByNameContainPort;
import hyundai.partservice.app.part.application.service.FindByNameContainService;
import hyundai.partservice.app.supplier.application.entity.Supplier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class FindByNameContainServiceTest {

    @Mock
    private FIndByNameContainPort fIndByNameContainPort;

    @InjectMocks
    private FindByNameContainService findByNameContainService;

    private Part part1;
    private Part part2;
    private Part part3;
    private List<Part> parts;

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
                .supplier(Supplier.builder()
                        .id(1L)
                        .name("현대모비스")
                        .build()
                )
                .build();
         part2 = Part.builder()
                .id("P002")
                .name("엔진 오일 필터")
                .quantity(30)
                .safetyStock(15)
                .maxStock(80)
                .optimalStock(50)
                .deliveryDuration(3)
                .price(15000L)
                .category("엔진 부품")
                 .supplier(Supplier.builder()
                         .id(1L)
                         .name("현대모비스")
                         .build()
                 )
                .build();

         part3 = Part.builder()
                .id("P003")
                .name("와이퍼 블레이드")
                .quantity(70)
                .safetyStock(25)
                .maxStock(120)
                .optimalStock(80)
                .deliveryDuration(2)
                .price(12000L)
                .category("엔진 부품")
                 .supplier(Supplier.builder()
                         .id(1L)
                         .name("현대모비스")
                         .build()
                 )
                .build();

         parts = List.of(part1,  part3);
    }

    @Test
    public void testFindByNameContain() {

        //given

        String name = "드";
        when(fIndByNameContainPort.findByNameContains(name)).thenReturn(parts);

        //when
        PartListPurchaseResponse response = findByNameContainService.findByNameContains(name);


        //then
        assertThat(response).isNotNull();
        assertThat(response.partDtos()).hasSize(2);
        assertThat(response.partDtos().get(0).partName()).isEqualTo(part1.getName());
        assertThat(response.partDtos().get(1).partName()).isEqualTo(part3.getName());

    }

}
