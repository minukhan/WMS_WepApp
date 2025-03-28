package hyundai.partservice.app.infrastructure.repository;

import hyundai.partservice.app.part.application.entity.Part;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PartRepository extends JpaRepository<Part, String> {
    Optional<Part> findByName(String name);
    Optional<Page<Part>> findByCategory(String category, Pageable pageable);

    @Query("SELECT p FROM Part p JOIN FETCH p.supplier WHERE p.name LIKE %:name%")
    List<Part> findByNameContainingWithSupplier(@Param("name") String name);


    @Query("SELECT p FROM Part p JOIN FETCH p.inventoryList WHERE p.id = :id")
    Optional<Part> findByIdWithInventoryList(@Param("id") String id);

    @Query("SELECT p FROM Part p JOIN FETCH p.supplier WHERE p.id IN :partIds")
    List<Part> findByIdIn(@Param("partIds") List<String> partIds);

    @Query("SELECT SUM(i.partQuantity) FROM Inventory i WHERE i.part.id = :partId")
    Integer getTotalQuantityByPartId(@Param("partId") String partId);

    @Query("SELECT p FROM Part p LEFT JOIN FETCH p.inventoryList WHERE p.id = :partId")
    Part findByIdWithInventory(@Param("partId") String partId);

    @Query("SELECT p FROM Part p LEFT JOIN FETCH p.inventoryList WHERE p.name = :partName")
    Part findByNameWithInventory(@Param("partName") String partName);

    // Supplier ID를 이용하여 해당 Supplier의 Part 목록을 가져오기 (Inventory는 아직 불러오지 않음)
    @Query("SELECT p FROM Part p WHERE p.supplier.id = :supplierId")
    List<Part> findBySupplierId(@Param("supplierId") Long supplierId);

    List<Part> findByIdContaining(String id);
}
