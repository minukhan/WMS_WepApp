package hyundai.partservice.app.inventory.fake;


import hyundai.partservice.app.inventory.application.entity.Inventory;
import hyundai.partservice.app.part.application.entity.Part;
import hyundai.partservice.app.section.application.entity.Section;
import hyundai.partservice.app.supplier.application.entity.Supplier;

import java.util.Arrays;
import java.util.List;

public class FakeInventoryFactory {

    public static Inventory createFakeInventory() {
        Part fakePart = Part.builder()
                .id("123123")
                .name("Fake Part Name")
                .build();

        Section fakeSection = Section.builder()
                .id(1L)
                .name("Fake Section Name")
                .floor(1)
                .quantity(10)
                .maxCapacity(100)
                .build();

        return Inventory.builder()
                .id(1L)
                .part(fakePart)
                .section(fakeSection)
                .partQuantity(50)
                .build();
    }

    public static Inventory createFakeInventory2() {
        Part fakePart = Part.builder()
                .id("234234")
                .name("Fake Part Name B")
                .build();

        Section fakeSection = Section.builder()
                .id(2L)
                .name("Fake Section Name B")
                .floor(2)
                .quantity(20)
                .maxCapacity(60)
                .build();

        return Inventory.builder()
                .id(1L)
                .part(fakePart)
                .section(fakeSection)
                .partQuantity(60)
                .build();
    }


    public static Inventory createFakeInventorywithSupply(Supplier supplier) {
        Part fakePart = Part.builder()
                .id("123123")
                .name("Fake Part Name")
                .supplier(supplier)
                .build();

        Section fakeSection = Section.builder()
                .id(1L)
                .name("Fake Section Name")
                .floor(1)
                .quantity(10)
                .maxCapacity(100)
                .build();

        return Inventory.builder()
                .id(1L)
                .part(fakePart)
                .section(fakeSection)
                .partQuantity(50)
                .build();
    }

    // ✅ 특정 부품과 창고 섹션을 연결한 재고 생성
    public static Inventory createFakeInventoryWithPartAndSection(Part part, Section section, int quantity) {
        return Inventory.builder()
                .id(2L)
                .part(part)
                .section(section)
                .partQuantity(quantity)
                .build();
    }

    // ✅ 재고 ID와 함께 특정 조건의 재고 생성
    public static Inventory createFakeInventory(long id, Part part, Section section, int quantity) {
        return Inventory.builder()
                .id(id)
                .part(part)
                .section(section)
                .partQuantity(quantity)
                .build();
    }
    // ✅ TEST456 Part ID를 가지는 Fake Inventory 생성
    public static Inventory createFakeInventoryWithTestPart() {
        Part testPart = Part.builder()
                .id("TEST456")
                .name("Test Part")
                .supplier(Supplier.builder()
                        .id(1L)
                        .name("한민욱")
                        .build())
                .build();

        Section testSection = Section.builder()
                .id(3L)
                .name("Test Section")
                .floor(3)
                .quantity(30)
                .maxCapacity(80)
                .build();

        return Inventory.builder()
                .id(3L)
                .part(testPart)
                .section(testSection)
                .partQuantity(70)
                .build();
    }
    // ✅ TEST456을 포함하는 Fake Inventory List 생성
    public static List<Inventory> createFakeInventoryList() {
        return Arrays.asList(
                createFakeInventory(),               // 기존 Fake Inventory
                createFakeInventory2(),              // 기존 Fake Inventory2
                createFakeInventoryWithTestPart()    // TEST456 포함
        );
    }

}