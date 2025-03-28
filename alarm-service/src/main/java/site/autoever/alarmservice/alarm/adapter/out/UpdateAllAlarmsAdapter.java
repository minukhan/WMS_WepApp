package site.autoever.alarmservice.alarm.adapter.out;

import com.mongodb.client.result.UpdateResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import site.autoever.alarmservice.alarm.application.domain.model.Alarm;
import site.autoever.alarmservice.alarm.application.port.out.UpdateAllAlarmsPort;


@Component
@RequiredArgsConstructor
public class UpdateAllAlarmsAdapter implements UpdateAllAlarmsPort {

    private final ReactiveMongoTemplate mongoTemplate;

    @Override
    public Mono<Long> markAllAsReadByUserId(Long userId) {
        Query query = new Query(Criteria.where("userId").is(userId).and("isRead").is(false));
        Update update = new Update().set("isRead", true);

        return mongoTemplate.updateMulti(query, update, Alarm.class)
                .map(UpdateResult::getModifiedCount); // 변경된 개수 반환
    }
}
