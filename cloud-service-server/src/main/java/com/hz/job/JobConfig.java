package com.hz.job;

import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 定时任务配置
 */
@Configuration
public class JobConfig {

    private static final String DEFAULT_GROUP_JOB = "default_job";

    @Bean
    public JobDetail examCountJob() {
        return JobBuilder.newJob(TestJob1.class)
                .withIdentity(TestJob1.class.getSimpleName(), DEFAULT_GROUP_JOB)
                .storeDurably().build();
    }

    @Bean
    public Trigger uploadTaskTrigger() {
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("*/1 * * * * ?");
        return TriggerBuilder.newTrigger().forJob(examCountJob().getKey())
                // .startAt(new Date(System.currentTimeMillis() + 1000 * 60))
                .withIdentity(TestJob1.class.getSimpleName())
                .withSchedule(scheduleBuilder)
                .build();
    }

    @Bean
    public JobDetail rabbitMqMessageJob() {
        return JobBuilder.newJob(TestJob2.class)
                .withIdentity(TestJob2.class.getSimpleName(), DEFAULT_GROUP_JOB)
                .storeDurably().build();
    }

    @Bean
    public Trigger rabbitMqMessageTaskTrigger() {
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0 */5 * * * ?");
        return TriggerBuilder.newTrigger().forJob(rabbitMqMessageJob().getKey())
                .withIdentity(TestJob2.class.getSimpleName())
                .withSchedule(scheduleBuilder)
                .build();
    }

}
