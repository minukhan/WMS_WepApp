package site.autoever.alarmservice.alarm.application.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "alarms")  // MongoDB 컬렉션 지정
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Alarm {
    @Id
    private String id;
    private long userId;
    private String type;
    private String message;
    private boolean isRead;
    private Instant createdAt;
}