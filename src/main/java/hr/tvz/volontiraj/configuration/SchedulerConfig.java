package hr.tvz.volontiraj.configuration;

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
                        .withIntervalInSeconds(5) // Adjust the interval as needed
                        .repeatForever();

        return TriggerBuilder.newTrigger()
                .forJob(notificationJobDetail())
                .withIdentity("notificationTrigger")
                .withSchedule(scheduleBuilder)
                .build();
    }

}
