package site.autoever.reportservice.report.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.autoever.reportservice.report.adapter.in.dto.ReportSummaryDto;
import site.autoever.reportservice.report.adapter.in.dto.ShowAllReportResponse;
import site.autoever.reportservice.report.application.port.in.ShowAllReportUseCase;
import site.autoever.reportservice.report.application.port.out.ReadReportPort;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShowAllReportService implements ShowAllReportUseCase {

    private final ReadReportPort readReportPort;

    @Override
    public ShowAllReportResponse showAll() {
        List<ReportSummaryDto> reportSummaryDtos = readReportPort.getAllReports().stream().map(
                report -> {
                    String id = report.getId();
                    int year = report.getYear();
                    int month = report.getMonth();
                    boolean modified = report.isModified();
                    return new ReportSummaryDto(id, year, month, modified);
                }
        ).toList();

        return new ShowAllReportResponse(reportSummaryDtos);
    }

}
