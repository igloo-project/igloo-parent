package fr.openwide.core.basicapp.core.config.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import fr.openwide.core.basicapp.core.config.scheduling.service.ISchedulingService;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;

@EnableScheduling
@Configuration
public class BasicApplicationCoreSchedulingConfig {

	@Autowired
	private ISchedulingService schedulingService;

	@Scheduled(cron = "${tmp.clean.cron}")
	public void temporaryFilesCleaning() throws ServiceException {
		schedulingService.temporaryFilesCleaning();
	}
	
	@Scheduled(cron = "${dataupgrade.cron}")
	public void executeAutoPerformDataUpgrade() throws ServiceException, SecurityServiceException {
		// TODO voir si cron nécessaire
		schedulingService.executeAutoPerformDataUpgrade();
	}

}
