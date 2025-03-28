package hyundai.partservice.app.section.fake;


import hyundai.partservice.app.part.application.entity.Part;
import hyundai.partservice.app.section.application.entity.Section;
import hyundai.partservice.app.inventory.application.entity.Inventory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FakeSectionFactory {

    public static Section createFakeSectionWithCapacity() {
        return Section.builder()
                .id(1L)
                .name("Fake Section A")
                .quantity(50)
                .floor(1)
                .maxCapacity(100)
                .inventories(Collections.emptyList())
                .build();
    }

    public static Section createFakeSectionWithCapacity2() {
        return Section.builder()
                .id(2L)
                .name("Fake Section B")
                .quantity(40)
                .floor(2)
                .maxCapacity(120)
                .inventories(Collections.emptyList())
                .build();
    }

    public static Section createFakeSectionWithCapacity3() {
        return Section.builder()
                .id(3L)
                .name("Fake Section C")
                .quantity(30)
                .floor(5)
                .maxCapacity(130)
                .inventories(Collections.emptyList())
                .build();
    }

    public static Section createFakeSectionWithInventories(Section section) {
        Inventory fakeInventory = Inventory.builder()
                .id(1L)
                .partQuantity(10)
                .section(section)
                .build();

        // 🔥 불변 리스트 → 가변 리스트(ArrayList)로 변경
        List<Inventory> inventories = new ArrayList<>();
        inventories.add(fakeInventory);

        return Section.builder()
                .id(2L)
                .name("Fake Section B")
                .quantity(inventories.size())
                .floor(2)
                .maxCapacity(200)
                .inventories(inventories) // 이제 가변 리스트라 add 가능!
                .build();
    }
    public static Section createFakeSectionWithInventoriesWithParts(Section section, Part part) {
        Inventory fakeInventory = Inventory.builder()
                .id(1L)
                .part(part)
                .partQuantity(10)
                .section(section)
                .build();

        // 🔥 불변 리스트 → 가변 리스트(ArrayList)로 변경
        List<Inventory> inventories = new ArrayList<>();
        inventories.add(fakeInventory);

        return Section.builder()
                .id(2L)
                .name("Fake Section B")
                .quantity(inventories.size())
                .floor(2)
                .maxCapacity(200)
                .inventories(inventories) // 이제 가변 리스트라 add 가능!
                .build();
    }

    // ✅ 새로운 메서드 추가 (특정 조건의 창고 섹션 생성)
    public static Section createFakeSection(String name, int maxCapacity) {
        return Section.builder()
                .id(3L)
                .name(name)
                .quantity(0)
                .floor(1)
                .maxCapacity(maxCapacity)
                .inventories(Collections.emptyList())
                .build();
    }
}