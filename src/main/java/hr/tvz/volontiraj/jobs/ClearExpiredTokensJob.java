package hr.tvz.volontiraj.jobs;

import hr.tvz.volontiraj.service.RefreshTokenService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

@Slf4j
public class ClearExpiredTokensJob extends QuartzJobBean {

    private final RefreshTokenService refreshTokenService;

    public ClearExpiredTokensJob(RefreshTokenService refreshTokenService) {
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        System.out.println("Executing ClearExpiredTokensJob...");
        refreshTokenService.deleteExpiredTokens();
    }
}
