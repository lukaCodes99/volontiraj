package hr.tvz.volontiraj.configuration;

import hr.tvz.volontiraj.jobs.ClearExpiredTokensJob;
import hr.tvz.volontiraj.jobs.NotifyVolunteersJob;

import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SchedulerConfig {

    @Bean
    public JobDetail notificationJobDetail() {
        return JobBuilder.newJob(NotifyVolunteersJob.class)
                .withIdentity("notificationJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger notificationJobTrigger() {
        SimpleScheduleBuilder scheduleBuilder =
                SimpleScheduleBuilder
                        .simpleSchedule()
                        .withIntervalInHours(1)
                        .repeatForever();

        return TriggerBuilder.newTrigger()
                .forJob(notificationJobDetail())
                .withIdentity("notificationTrigger")
                .withSchedule(scheduleBuilder)
                .build();
    }

    @Bean
    public JobDetail clearExpiredTokensJobDetail() {
        return JobBuilder.newJob(ClearExpiredTokensJob.class)
                .withIdentity("clearExpiredTokensJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger clearExpiredTokensJobTrigger() {
        SimpleScheduleBuilder scheduleBuilder =
                SimpleScheduleBuilder
                        .simpleSchedule()
                        .withIntervalInHours(1)
                        .repeatForever();

        return TriggerBuilder.newTrigger()
                .forJob(clearExpiredTokensJobDetail())
                .withIdentity("clearExpiredTokensTrigger")
                .withSchedule(scheduleBuilder)
                .build();
    }

}
