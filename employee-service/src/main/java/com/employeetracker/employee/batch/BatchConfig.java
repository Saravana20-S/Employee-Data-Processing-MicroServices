package com.employeetracker.employee.batch;

import com.employeetracker.employee.dto.EmployeeRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.batch.core.configuration.annotation.StepScope;

import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;

import org.springframework.batch.infrastructure.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class BatchConfig {

    private final JobRepository jobRepository;

    private final PlatformTransactionManager
            transactionManager;

    private final EmployeeProcessor
            employeeProcessor;

    private final EmployeeWriter
            employeeWriter;

    private final JobCompletionListener
            jobCompletionListener;

    private final ImportSkipListener
            importSkipListener;


    @Bean
    @StepScope
    public ItemReader<EmployeeRequest>
    employeeReader(
            @Value(
                    "#{jobParameters['filePath']}"
            )
            String filePath
    ) {

        return new EmployeeExcelReader(
                filePath
        );
    }


    @Bean
    public Step employeeImportStep(
            ItemReader<EmployeeRequest>
                    employeeReader
    ) {

        return new StepBuilder(
                "employeeImportStep",
                jobRepository
        )

                .<EmployeeRequest, EmployeeRequest>
                        chunk(100)

                .transactionManager(
                        transactionManager
                )

                .reader(
                        employeeReader
                )

                .processor(
                        employeeProcessor
                )

                .writer(
                        employeeWriter
                )

                .faultTolerant()

                .skip(Exception.class)

                .skipLimit(1000)

                .listener(
                        importSkipListener
                )

                .build();
    }


    @Bean
    public Job employeeImportJob(
            Step employeeImportStep
    ) {

        return new JobBuilder(
                "employeeImportJob",
                jobRepository
        )
                .start(
                        employeeImportStep
                )
                .listener(
                        jobCompletionListener
                )
                .build();
    }
}