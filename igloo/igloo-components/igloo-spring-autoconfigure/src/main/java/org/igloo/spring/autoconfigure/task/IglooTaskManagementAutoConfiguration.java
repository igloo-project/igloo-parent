package org.igloo.spring.autoconfigure.task;

import java.util.Collection;

import org.apache.commons.lang3.EnumUtils;
import org.igloo.spring.autoconfigure.property.IglooPropertyAutoConfiguration;
import org.iglooproject.jpa.more.business.CoreJpaMoreBusinessPackage;
import org.iglooproject.jpa.more.business.task.dao.IQueuedTaskHolderDao;
import org.iglooproject.jpa.more.business.task.dao.QueuedTaskHolderDaoImpl;
import org.iglooproject.jpa.more.business.task.model.IQueueId;
import org.iglooproject.jpa.more.business.task.service.IQueuedTaskHolderManager;
import org.iglooproject.jpa.more.business.task.service.IQueuedTaskHolderService;
import org.iglooproject.jpa.more.business.task.service.QueuedTaskHolderManagerImpl;
import org.iglooproject.jpa.more.business.task.service.QueuedTaskHolderServiceImpl;
import org.iglooproject.jpa.more.config.spring.JpaMoreTaskApplicationPropertyRegistryConfig;
import org.iglooproject.jpa.more.util.transaction.CoreJpaMoreUtilTransactionPackage;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;

@Configuration
@ComponentScan(
		basePackageClasses = { CoreJpaMoreBusinessPackage.class, CoreJpaMoreUtilTransactionPackage.class }
)
@Import(JpaMoreTaskApplicationPropertyRegistryConfig.class)
@AutoConfigureAfter(IglooPropertyAutoConfiguration.class)
public class IglooTaskManagementAutoConfiguration {
	
	public static final String OBJECT_MAPPER_BEAN_NAME = "queuedTaskHolderObjectMapper";

	@Bean(name = OBJECT_MAPPER_BEAN_NAME)
	public ObjectMapper queuedTaskHolderObjectMapper() {
		return new ObjectMapper().enableDefaultTyping(DefaultTyping.NON_FINAL);
	}

	@Bean
	public IQueuedTaskHolderDao queuedTaskHolderDao() {
		return new QueuedTaskHolderDaoImpl();
	}

	@Bean
	public IQueuedTaskHolderService queuedTaskHolderService(IQueuedTaskHolderDao queuedTaskHolderDao) {
		return new QueuedTaskHolderServiceImpl(queuedTaskHolderDao);
	}
	
	@Bean
	public IQueuedTaskHolderManager queuedTaskHolderManager() {
		return new QueuedTaskHolderManagerImpl();
	}

	/**
	 * Must return all the {@link IQueueId queue IDs} that are valid in this application.
	 */
	@Bean
	@ConditionalOnMissingBean
	public Collection<? extends IQueueId> queueIds() {
		return EnumUtils.getEnumList(StubQueueId.class);
	}
	
	public enum StubQueueId implements IQueueId {
		;
		@Override
		public String getUniqueStringId() {
			return name();
		}
	}
}