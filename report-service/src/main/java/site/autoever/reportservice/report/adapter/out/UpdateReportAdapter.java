package site.autoever.reportservice.report.adapter.out;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import site.autoever.reportservice.infrastructure.repository.ReportMongoRepository;
import site.autoever.reportservice.report.application.domain.Report;
import site.autoever.reportservice.report.application.port.out.UpdateReportPort;

@Component
@RequiredArgsConstructor
public class UpdateReportAdapter implements UpdateReportPort {

    private final ReportMongoRepository repository;

    @Override
    public Report updateReport(String reportId) {
        return repository.updateIsModifiedById(reportId);
    }
}
