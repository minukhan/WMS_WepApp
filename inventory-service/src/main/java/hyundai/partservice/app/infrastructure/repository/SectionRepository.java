package hyundai.partservice.app.infrastructure.repository;

import hyundai.partservice.app.section.application.entity.Section;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface SectionRepository extends JpaRepository<Section, Long> {
    Page<Section> findAll(Pageable pageable);

    Optional<Section> findByName(String name);

    List<Section> findAllByName(String name);

    @Query("SELECT p FROM Section p JOIN FETCH p.inventories WHERE p.id = :id")
    Optional<Section> findByIdWithInventoryList(@Param("id") Long id);

    Optional<Section> findSectionByNameAndFloor(String sectionName, int floor);

    List<Section> findByNameContaining(String name);

    @Query("SELECT SUM(s.maxCapacity) FROM Section s")
    int findTotalMaxCapacity();

    @Query("SELECT SUM(s.quantity) FROM Section s")
    int findTotalCurrentQuantity();
}

