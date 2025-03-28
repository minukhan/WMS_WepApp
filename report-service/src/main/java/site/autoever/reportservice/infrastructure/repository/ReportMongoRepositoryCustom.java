package site.autoever.reportservice.infrastructure.repository;

import site.autoever.reportservice.report.application.domain.Report;

public interface ReportMongoRepositoryCustom {
    Report updateIsModifiedById(String id);
}
