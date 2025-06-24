package hr.tvz.volontiraj.jobs;

import hr.tvz.volontiraj.model.Event;
import hr.tvz.volontiraj.model.UserEntity;
import hr.tvz.volontiraj.service.EmailService;
import hr.tvz.volontiraj.service.EventService;
import org.quartz.JobExecutionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NotifyVolunteersJob extends QuartzJobBean {

    private final EventService eventService;
    private final EmailService emailService;

    public NotifyVolunteersJob(EventService eventService, EmailService emailService) {
        this.eventService = eventService;
        this.emailService = emailService;
    }

    @Transactional
    @Override
    protected void executeInternal(JobExecutionContext context) {
        try{
            emailService.notifyVolunteers();
        }catch (Exception e){
            System.out.println(e);
        }
        // You can call services or perform tasks as needed
    }
}
