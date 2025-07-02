package hr.tvz.volontiraj.configuration;

import hr.tvz.volontiraj.jobs.ClearExpiredTokensJob;

import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClearExpiredTokensSchedulerConfig {

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
