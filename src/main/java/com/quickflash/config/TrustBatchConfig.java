package com.quickflash.config;

import com.quickflash.meeting_join.domain.MeetingJoin;
import com.quickflash.meeting_join.dto.MeetingJoinDtoForBatch;
import com.quickflash.meeting_join.service.MeetingJoinItemReader;
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
public class TrustBatchConfig {

    private final PlatformTransactionManager transactionManager;
    private final JobRepository jobRepository;
    private final MeetingJoinItemReader meetingJoinItemReader;
    private final TrustItemProcessor trustItemProcessor;
    private final TrustItemWriter trustItemWriter;





    @Bean
    public Step trustStep() {
        return new StepBuilder("trustStep", jobRepository)
                .<List<MeetingJoinDtoForBatch>, Map<Integer, Double>>chunk(1, transactionManager)
                .reader(meetingJoinItemReader)
                .processor(trustItemProcessor)
                .writer(trustItemWriter)
                .build();
    }

    @Bean
    public Job trustJob() {
        return new JobBuilder("trustJob", jobRepository)
                .start(trustStep())
                .build();
    }
}

