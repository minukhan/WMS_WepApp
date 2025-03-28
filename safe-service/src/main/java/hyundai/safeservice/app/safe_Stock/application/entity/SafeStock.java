package hyundai.safeservice.app.safe_Stock.application.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Table(name ="safe_stock")
public class SafeStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate createdAt;

    private int quantity;

    private String reason;

    private String acceptName;

    private String partId;
}