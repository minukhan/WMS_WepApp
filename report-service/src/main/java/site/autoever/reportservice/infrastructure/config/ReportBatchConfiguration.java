package site.autoever.reportservice.infrastructure.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import site.autoever.reportservice.report.application.port.in.GenerateMonthlyReportUseCase;

@Configuration
@RequiredArgsConstructor
public class ReportBatchConfiguration {

    private final GenerateMonthlyReportUseCase generateMonthlyReportUseCase;

    @Bean
    public Job monthlyReportJob(JobRepository jobRepository,
                                Step purchaseDataCollectionStep,
                                Step supplyDataCollectionStep,
                                Step safeDataCollectionStep,   // Safe 데이터 수집 Step 추가
                                Step dataProcessingStep,
                                Step reportGenerationStep,
                                Step sendAlarmStep) {
        return new JobBuilder("monthlyReportJob", jobRepository)
                .start(supplyDataCollectionStep)
                .next(purchaseDataCollectionStep)
                .next(safeDataCollectionStep)   // Safe 데이터 수집 Step 실행
                .next(dataProcessingStep)
                .next(reportGenerationStep)
                .next(sendAlarmStep)
                .incrementer(new RunIdIncrementer()) // 매번 새로운 JobParameter 생성
                .build();
    }

    @Bean
    public Step purchaseDataCollectionStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("purchaseDataCollectionStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    generateMonthlyReportUseCase.collectPurchaseData();
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step supplyDataCollectionStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("supplyDataCollectionStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    generateMonthlyReportUseCase.collectSupplyData();
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step safeDataCollectionStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("safeDataCollectionStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    generateMonthlyReportUseCase.collectSafeData();  // Safe 데이터 수집
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step dataProcessingStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("dataProcessingStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    generateMonthlyReportUseCase.processData();
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step reportGenerationStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("reportGenerationStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    generateMonthlyReportUseCase.generateMonthlyReport();
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step sendAlarmStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("sendAlarmStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    generateMonthlyReportUseCase.sendAlarm();
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }
}
