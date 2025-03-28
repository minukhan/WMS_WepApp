package hyundai.partservice.app.part.service;


import hyundai.partservice.app.part.adapter.dto.PartPurchaseResponse;
import hyundai.partservice.app.part.adapter.dto.PartResponse;
import hyundai.partservice.app.part.application.entity.Part;
import hyundai.partservice.app.part.application.port.out.FindPartPort;
import hyundai.partservice.app.part.application.service.FindPartService;
import hyundai.partservice.app.supplier.application.entity.Supplier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static hyundai.partservice.app.supplier.fake.FakeSupplierFactory.createFakeSupplier;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
public class FindPartServiceTest {

    @Mock
    private FindPartPort findPartPort;

    @InjectMocks
    private FindPartService findPartService;

    private Part part1;

    @BeforeEach
    void setUp() {

        Supplier supplier = createFakeSupplier();

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
                 .supplier(supplier)
                .build();


    }
    @Test
    void findPart() {

        //when
        String id = "P001";
        Mockito.when(findPartPort.findPart(id)).thenReturn(part1);

        //given
        PartPurchaseResponse response = findPartService.findPart(id);

        //then
        assertThat(response.partSupplierDto().partId()).isEqualTo(part1.getId());
    }

}
