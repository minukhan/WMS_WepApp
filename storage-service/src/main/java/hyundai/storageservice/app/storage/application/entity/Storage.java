package hyundai.storageservice.app.storage.application.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "storage")
public class Storage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "part_id", nullable = false, unique = true)
    private String partId; // 부품 ID

    @Column(name = "part_name", nullable = false)
    private String partName; // 부품 이름

    @Column(name = "part_price", nullable = false)
    private int partPrice; // 부품 가격

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt; // 최근 업데이트 시간

}
