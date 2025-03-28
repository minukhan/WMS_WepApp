package site.autoever.reportservice.report.adapter.out;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import site.autoever.reportservice.infrastructure.repository.ReportMongoRepository;
import site.autoever.reportservice.report.application.domain.Report;
import site.autoever.reportservice.report.application.port.out.CreateReportPort;

@Component
@RequiredArgsConstructor
public class CreateReportAdapter implements CreateReportPort {

    private final ReportMongoRepository repository;

    @Override
    public void save(Report report) {
        repository.deleteByYearAndMonth(report.getYear(), report.getMonth());
        repository.save(report);
    }
}
