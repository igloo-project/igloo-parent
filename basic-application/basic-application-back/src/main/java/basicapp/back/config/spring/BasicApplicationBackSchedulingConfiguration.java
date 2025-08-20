package basicapp.back.config.spring;

import basicapp.back.config.scheduling.service.ISchedulingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class BasicApplicationBackSchedulingConfiguration {

  @Autowired private ISchedulingService schedulingService;

  @Scheduled(cron = "${tmp.clean.cron}")
  public void temporaryFilesCleaning() {
    schedulingService.temporaryFilesCleaning();
  }
}
