package hyundai.partservice.app.infrastructure.repository;


import hyundai.partservice.app.inventory.application.entity.Inventory;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    @Query("SELECT i FROM Inventory i " +
            "JOIN FETCH i.section s " +
            "JOIN FETCH i.part p " +
            "WHERE s.name = :sectionName AND s.floor = :floor AND p.id = :partId")
    Optional<Inventory> findInventory(@Param("sectionName") String sectionName,
                                      @Param("floor") int floor,
                                      @Param("partId") String partId);

    @Query("SELECT SUM(i.partQuantity) FROM Inventory i")
    Integer getCurrentTotalCount();

    @Query("SELECT i FROM Inventory i WHERE i.part.id IN :partIds")
    List<Inventory> findAllByPartIdIn(@Param("partIds") List<String> partIds);


}
