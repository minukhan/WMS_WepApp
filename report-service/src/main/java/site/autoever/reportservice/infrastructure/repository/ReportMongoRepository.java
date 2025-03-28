package site.autoever.reportservice.infrastructure.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import site.autoever.reportservice.report.application.domain.Report;

import java.util.Optional;

public interface ReportMongoRepository extends MongoRepository<Report, String>, ReportMongoRepositoryCustom  {

    Optional<Report> findByYearAndMonth(int year, int month);

    void deleteByYearAndMonth(int year, int month);

}
