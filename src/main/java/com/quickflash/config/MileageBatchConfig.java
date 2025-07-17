package com.quickflash.config;

import com.quickflash.meeting_join.dto.MeetingJoinDtoForBatch;
import com.quickflash.meeting_join.service.MeetingJoinItemReader;
import com.quickflash.mileage.domain.Mileage;
import com.quickflash.mileage.service.MileageBO;
import com.quickflash.mileage.service.MileageItemProcessor;
import com.quickflash.mileage.service.MileageItemReader;
import com.quickflash.trust.service.TrustItemProcessor;
import com.quickflash.trust.service.TrustItemWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class MileageBatchConfig {

    private final PlatformTransactionManager transactionManager;
    private final JobRepository jobRepository;

    private final MileageItemReader mileageItemReader;
    private final MileageItemProcessor mileageItemProcessor;
    // 만약 Writer 필요하면 선언


    @Bean
    public Step mileageStep() {
        return new StepBuilder("mileageStep", jobRepository)
                .<List<Mileage>, Map<String,Object>>chunk(10, transactionManager)
                .reader(mileageItemReader)
                .processor(mileageItemProcessor)
                .writer(items -> {
                    // 아무 작업도 하지 않는 빈 writer
                    // Processor에서 Redis 등에 저장 완료했으면 빈 Writer로 충분
                })
                .build();
    }

    @Bean("mileageJob")
    public Job mileageJob() {
        return new JobBuilder("mileageJob", jobRepository)
                .start(mileageStep())
                .build();
    }
}

