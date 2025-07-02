package com.quickflash.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@EnableScheduling
public class TrustJobScheduler {

    private final JobLauncher jobLauncher;
    private final Job trustJob;

    @Scheduled(cron = "*/10 * * * * ?")// 매일 새벽 2시에 실행
    public void runTrustJob() {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("time", LocalDateTime.now().toString()) // 매번 다른 파라미터로 실행
                    .toJobParameters();

            jobLauncher.run(trustJob, jobParameters);

        } catch (Exception e) {
            // 로깅 등 에러 처리
            e.printStackTrace();
        }
    }
}