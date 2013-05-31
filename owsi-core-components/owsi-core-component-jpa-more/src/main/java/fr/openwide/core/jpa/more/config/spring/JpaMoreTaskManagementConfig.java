package fr.openwide.core.jpa.more.config.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import fr.openwide.core.jpa.more.business.JpaMoreBusinessPackage;
import fr.openwide.core.jpa.more.business.task.dao.IQueuedTaskHolderDao;
import fr.openwide.core.jpa.more.business.task.dao.QueuedTaskHolderDaoImpl;
import fr.openwide.core.jpa.more.business.task.service.IQueuedTaskHolderConsumer;
import fr.openwide.core.jpa.more.business.task.service.IQueuedTaskHolderManager;
import fr.openwide.core.jpa.more.business.task.service.IQueuedTaskHolderService;
import fr.openwide.core.jpa.more.business.task.service.QueuedTaskHolderConsumerImpl;
import fr.openwide.core.jpa.more.business.task.service.QueuedTaskHolderManagerImpl;
import fr.openwide.core.jpa.more.business.task.service.QueuedTaskHolderServiceImpl;

@ComponentScan(basePackageClasses = { JpaMoreBusinessPackage.class })
public class JpaMoreTaskManagementConfig {

	@Bean
	public IQueuedTaskHolderDao queuedTaskHolderDao() {
		return new QueuedTaskHolderDaoImpl();
	}

	@Bean
	public IQueuedTaskHolderService queuedTaskHolderService(IQueuedTaskHolderDao queuedTaskHolderDao) {
		return new QueuedTaskHolderServiceImpl(queuedTaskHolderDao);
	}

	@Bean
	public IQueuedTaskHolderConsumer queuedTaskHolderConsumer() {
		return new QueuedTaskHolderConsumerImpl();
	}

	@Bean
	public IQueuedTaskHolderManager queuedTaskHolderManager() {
		return new QueuedTaskHolderManagerImpl();
	}

}
