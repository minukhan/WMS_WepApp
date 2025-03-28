package hyundai.partservice.app.part.fake;


import hyundai.partservice.app.part.application.entity.Part;
import hyundai.partservice.app.supplier.application.entity.Supplier;

import java.util.Collections;

public class FakePartFactory {

    // Supplier가 없는 가짜 Part 생성
    public static Part createFakePartWithoutSupplier() {
        return Part.builder()
                .id("TEST123")
                .name("Fake Part A")
                .quantity(100)
                .safetyStock(5)
                .maxStock(200)
                .optimalStock(150)
                .deliveryDuration(3)
                .price(50000L)
                .category("Engine")
                .build();
    }

    public static Part createFakePartWithoutSupplier2() {
        return Part.builder()
                .id("TEST456")
                .name("Fake Part B")
                .quantity(100)
                .safetyStock(5)
                .maxStock(200)
                .optimalStock(150)
                .deliveryDuration(3)
                .price(50000L)
                .category("Engine")
                .build();
    }

    // Supplier가 있는 가짜 Part 생성
    public static Part createFakePartWithSupplier() {
        Supplier fakeSupplier = Supplier.builder()
                .id(1L)
                .name("Fake Supplier Co.")
                .address("123 Fake Street, Seoul, Korea")
                .phoneNumber("010-9999-8888")
                .parts(Collections.emptyList())
                .build();

        return Part.builder()
                .id("TEST456")
                .name("Fake Part B")
                .quantity(50)
                .safetyStock(3)
                .maxStock(150)
                .optimalStock(75)
                .deliveryDuration(5)
                .price(30000L)
                .category("Transmission")
                .supplier(fakeSupplier)
                .build();
    }
}
