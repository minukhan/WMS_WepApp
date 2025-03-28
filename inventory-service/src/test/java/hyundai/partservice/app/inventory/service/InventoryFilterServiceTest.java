package hyundai.partservice.app.inventory.service;

import hyundai.partservice.app.inventory.adapter.dto.InventoryFilterResponse;
import hyundai.partservice.app.inventory.application.entity.Inventory;
import hyundai.partservice.app.inventory.application.port.out.InventoryFilterPort;
import hyundai.partservice.app.inventory.application.service.InventoryFilterService;
import hyundai.partservice.app.inventory.fake.FakeInventoryFactory;
import hyundai.partservice.app.part.application.entity.Part;
import hyundai.partservice.app.section.application.entity.Section;
import hyundai.partservice.app.section.fake.FakeSectionFactory;
import hyundai.partservice.app.supplier.application.entity.Supplier;
import hyundai.partservice.app.supplier.fake.FakeSupplierFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static hyundai.partservice.app.part.fake.FakePartFactory.createFakePartWithSupplier;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InventoryFilterServiceTest {

    @Mock
    private InventoryFilterPort inventoryFilterPort;

    @InjectMocks
    private InventoryFilterService inventoryFilterService;

    private Inventory inventory1;
    private Inventory inventory2;
    private Part fakePartWithSupplier;
    private Section fakeSectionA;
    private Section fakeSectionB;
    private Supplier fakeSupplier;
    private List<Section> sections;
    private List<Supplier> suppliers;
    private List<Inventory> fakeInventoryList;

    @BeforeEach
    void setUp() {
        // 가짜 공급업체 생성
        fakeSupplier = FakeSupplierFactory.createFakeSupplierWithParts();

        // 가짜 부품 생성 (공급업체 포함)
        fakePartWithSupplier = createFakePartWithSupplier();

        // 가짜 섹션 생성
        fakeSectionA = FakeSectionFactory.createFakeSectionWithCapacity();
        fakeSectionB = FakeSectionFactory.createFakeSectionWithCapacity2();

        // 가짜 재고 생성 (부품, 섹션 포함)
        inventory1 = FakeInventoryFactory.createFakeInventoryWithPartAndSection(fakePartWithSupplier, fakeSectionA, 50);
        inventory2 = FakeInventoryFactory.createFakeInventoryWithPartAndSection(fakePartWithSupplier, fakeSectionB, 40);

        // 가짜 섹션에 재고 추가
        fakeSectionA = FakeSectionFactory.createFakeSectionWithInventoriesWithParts(fakeSectionA, fakePartWithSupplier);
        fakeSectionB = FakeSectionFactory.createFakeSectionWithInventoriesWithParts(fakeSectionB, fakePartWithSupplier);

        // 가짜 섹션 리스트
        sections = List.of(fakeSectionA, fakeSectionB);

        // 가짜 공급업체 리스트
        suppliers = List.of(fakeSupplier);
    }

    @Test
    void testFilterByPartId() {
        // Given
        when(inventoryFilterPort.findAllInventory()).thenReturn(FakeInventoryFactory.createFakeInventoryList());

        Part fakePart = createFakePartWithSupplier();

        when(inventoryFilterPort.findByPartId("TEST456")).thenReturn(fakePart);

        // When
        InventoryFilterResponse response = inventoryFilterService.filter(
                "TEST456", null, null, null, null, null, PageRequest.of(0, 10));

        // Then
        assertThat(response.inventoryPartSupplierSectionDtos()).hasSize(2);
        assertThat(response.inventoryPartSupplierSectionDtos().get(0).partId()).isEqualTo("TEST456");

        verify(inventoryFilterPort).findByPartId("TEST456");
    }

    @Test
    void testFilterByPartName() {
        // Given
        when(inventoryFilterPort.findByName("Fake Part B")).thenReturn(fakePartWithSupplier);

        // When
        InventoryFilterResponse response = inventoryFilterService.filter(
                null, "Fake Part B", null, null, null, null, PageRequest.of(0, 10));

        // Then
        assertThat(response.inventoryPartSupplierSectionDtos()).hasSize(2);
        assertThat(response.inventoryPartSupplierSectionDtos().get(0).partName()).isEqualTo("Fake Part B");

        verify(inventoryFilterPort).findByName("Fake Part B");
    }

    @Test
    void testFilterBySectionName() {
        // Given
        when(inventoryFilterPort.findBySectionName("Fake Section A")).thenReturn(fakeSectionA);

        // When
        InventoryFilterResponse response = inventoryFilterService.filter(
                null, null, "Fake Section A", null, null, null, PageRequest.of(0, 10));

        // Then
        assertThat(response.inventoryPartSupplierSectionDtos()).hasSize(1);
        assertThat(response.inventoryPartSupplierSectionDtos().get(0).sectionName()).isEqualTo("Fake Section A");

        verify(inventoryFilterPort).findBySectionName("Fake Section A");
    }

    @Test
    void testFilterBySupplierName() {
        // Given
        when(inventoryFilterPort.findBySupplierName("Fake Supplier Co. 2")).thenReturn(fakeSupplier);

        // When
        InventoryFilterResponse response = inventoryFilterService.filter(
                null, null, null, "Fake Supplier Co. 2", null, null, PageRequest.of(0, 10));

        // Then
        assertThat(response.inventoryPartSupplierSectionDtos()).hasSize(2);
        assertThat(response.inventoryPartSupplierSectionDtos().get(0).supplierName()).isEqualTo("Fake Supplier Co. 2");

        verify(inventoryFilterPort).findBySupplierName("Fake Supplier Co. 2");
    }

    @Test
    void testFilterByCurrentQuantityDesc() {
        // Given
        when(inventoryFilterPort.findAllInventory()).thenReturn(List.of(inventory1, inventory2));

        // When
        InventoryFilterResponse response = inventoryFilterService.filter(
                null, null, null, null, "DESC", null, PageRequest.of(0, 10));

        // Then
        assertThat(response.inventoryPartSupplierSectionDtos()).hasSize(2);
        assertThat(response.inventoryPartSupplierSectionDtos().get(0).quantity()).isEqualTo(50);

        verify(inventoryFilterPort).findAllInventory();
    }

    @Test
    void testPagination() {
        // Given
        when(inventoryFilterPort.findAllInventory()).thenReturn(List.of(inventory1, inventory2));

        // When
        InventoryFilterResponse response = inventoryFilterService.filter(
                null, null, null, null, null, null, PageRequest.of(0, 1));

        // Then
        assertThat(response.inventoryPartSupplierSectionDtos()).hasSize(1);
        assertThat(response.totalPages()).isEqualTo(2);
        assertThat(response.totalElements()).isEqualTo(2);
        assertThat(response.isFirst()).isTrue();
        assertThat(response.isLast()).isFalse();

        verify(inventoryFilterPort).findAllInventory();
    }

}
