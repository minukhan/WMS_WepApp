package hyundai.partservice.app.infrastructure.repository;
import hyundai.partservice.app.supplier.application.entity.Supplier;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SupplierRepositroy extends JpaRepository<Supplier, Long> {

    @Query("SELECT DISTINCT s FROM Supplier s JOIN FETCH s.parts p WHERE s.name LIKE %:name%")
    List<Supplier> findByNameContains(String name);


    Supplier findByName(String name);
}
