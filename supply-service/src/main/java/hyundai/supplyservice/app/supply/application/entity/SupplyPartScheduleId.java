package hyundai.supplyservice.app.supply.application.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Embeddable
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SupplyPartScheduleId implements Serializable {
    @Column(name = "scheduled_at")
    private LocalDate scheduledAt;

    @Column(name = "part_id")
    private String partId;
}
