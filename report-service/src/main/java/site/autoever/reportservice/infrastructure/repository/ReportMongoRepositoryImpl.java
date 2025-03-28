package site.autoever.reportservice.infrastructure.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import site.autoever.reportservice.report.application.domain.Report;

@Repository
public class ReportMongoRepositoryImpl implements ReportMongoRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public ReportMongoRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Report updateIsModifiedById(String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        Update update = new Update().set("isModified", true);
        FindAndModifyOptions options = new FindAndModifyOptions().returnNew(true);
        return mongoTemplate.findAndModify(query, update, options, Report.class);
    }

}
