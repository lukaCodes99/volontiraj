package hr.tvz.volontiraj.jobs;

import hr.tvz.volontiraj.service.EmailService;
import hr.tvz.volontiraj.service.EventService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

@Slf4j
public class NotifyVolunteersJob extends QuartzJobBean {

    private final EmailService emailService;

    public NotifyVolunteersJob(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    protected void executeInternal(JobExecutionContext context) {
        try{
            emailService.notifyVolunteers();
        }catch (Exception e){
            log.error("Error occurred while sending notification to volunteers: {}", e.getMessage());
        }
    }
}
