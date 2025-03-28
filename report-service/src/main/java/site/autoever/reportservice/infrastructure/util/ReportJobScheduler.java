package site.autoever.reportservice.infrastructure.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReportJobScheduler {

    private final JobLauncher jobLauncher;
    private final Job monthlyReportJob;

    @Scheduled(cron = "0 0 0 L * ?")
    public void runMonthlyReportJob() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("uniqueId", UUID.randomUUID().toString())  // 고유한 값 추가
                .addLong("time", System.currentTimeMillis())          // 타임스탬프 추가
                .toJobParameters();

        log.info("🚀 월간 리포트 Job 실행: {}", jobParameters);
        jobLauncher.run(monthlyReportJob, jobParameters);
    }

}
