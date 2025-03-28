package hyundai.partservice.app.supplier.fake;

import hyundai.partservice.app.part.application.entity.Part;
import hyundai.partservice.app.part.fake.FakePartFactory;
import hyundai.partservice.app.supplier.application.entity.Supplier;

import java.util.Collections;
import java.util.List;

public class FakeSupplierFactory {

    public static Supplier createFakeSupplier() {
        return Supplier.builder()
                .id(1L)
                .name("Fake Supplier Co.")
                .address("123 Fake Street, Seoul, Korea")
                .phoneNumber("010-9999-8888")
                .parts(Collections.emptyList())
                .build();
    }

    // ✅ 새로운 메서드 추가 (부품이 포함된 공급업체 생성)
    public static Supplier createFakeSupplierWithParts() {
        Part fakePart = FakePartFactory.createFakePartWithSupplier();
        List<Part> parts = List.of(fakePart);

        return Supplier.builder()
                .id(2L)
                .name("Fake Supplier Co. 2")
                .address("456 Another Street, Busan, Korea")
                .phoneNumber("010-7777-6666")
                .parts(parts)
                .build();
    }

    // ✅ 특정 조건의 공급업체 생성
    public static Supplier createFakeSupplier(String name, String address) {
        return Supplier.builder()
                .id(3L)
                .name(name)
                .address(address)
                .phoneNumber("010-5555-4444")
                .parts(Collections.emptyList())
                .build();
    }
}
