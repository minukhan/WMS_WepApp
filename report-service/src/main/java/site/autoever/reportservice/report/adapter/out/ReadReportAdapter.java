package site.autoever.reportservice.report.adapter.out;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import site.autoever.reportservice.infrastructure.repository.ReportMongoRepository;
import site.autoever.reportservice.report.application.domain.Report;
import site.autoever.reportservice.report.application.port.out.ReadReportPort;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ReadReportAdapter implements ReadReportPort {

    private final ReportMongoRepository repository;

    @Override
    public Optional<Report> getReport(int year, int month) {
        return repository.findByYearAndMonth(year, month);
    }

    @Override
    public Optional<Report> getReportById(String reportId) {
        return repository.findById(reportId);
    }

    @Override
    public List<Report> getAllReports() {
        return repository.findAll();
    }

}
