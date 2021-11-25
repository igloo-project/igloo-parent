package org.igloo.spring.autoconfigure.task;

import java.util.Collection;

import org.apache.commons.lang3.EnumUtils;
import org.igloo.spring.autoconfigure.property.IglooPropertyAutoConfiguration;
import org.iglooproject.jpa.more.business.CoreJpaMoreBusinessPackage;
import org.iglooproject.jpa.more.business.task.dao.IQueuedTaskHolderDao;
import org.iglooproject.jpa.more.business.task.dao.QueuedTaskHolderDaoImpl;
import org.iglooproject.jpa.more.business.task.model.IQueueId;
import org.iglooproject.jpa.more.business.task.search.IQueuedTaskHolderSearchQuery;
import org.iglooproject.jpa.more.business.task.search.QueuedTaskHolderSearchQueryImpl;
import org.iglooproject.jpa.more.business.task.service.IQueuedTaskHolderManager;
import org.iglooproject.jpa.more.business.task.service.IQueuedTaskHolderService;
import org.iglooproject.jpa.more.business.task.service.QueuedTaskHolderManagerImpl;
import org.iglooproject.jpa.more.business.task.service.QueuedTaskHolderServiceImpl;
import org.iglooproject.jpa.more.config.spring.JpaMoreTaskApplicationPropertyRegistryConfig;
import org.iglooproject.jpa.more.util.transaction.CoreJpaMoreUtilTransactionPackage;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;

@Configuration
@ConditionalOnProperty(name = "igloo-ac.tasks.disabled", havingValue = "false", matchIfMissing = true)
@ComponentScan(
		basePackageClasses = { CoreJpaMoreBusinessPackage.class, CoreJpaMoreUtilTransactionPackage.class }
)
@Import(JpaMoreTaskApplicationPropertyRegistryConfig.class)
@AutoConfigureAfter(IglooPropertyAutoConfiguration.class)
public class IglooTaskManagementAutoConfiguration {
	
	public static final String OBJECT_MAPPER_BEAN_NAME = "queuedTaskHolderObjectMapper";

	/**
	 * Base configuration for task serialization. We use a permissive deserializer config as data are internally
	 * managed by application.
	 */
	@Bean(name = OBJECT_MAPPER_BEAN_NAME)
	public ObjectMapper queuedTaskHolderObjectMapper() {
		return new ObjectMapper().activateDefaultTyping(
				LaissezFaireSubTypeValidator.instance,
				DefaultTyping.NON_FINAL);
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
	
	@Bean
	@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public IQueuedTaskHolderSearchQuery queuedTaskHolderSearchQuery() {
		return new QueuedTaskHolderSearchQueryImpl();
	}

	/**
	 * Must return all the {@link IQueueId queue IDs} that are valid in this application.
	 */
	@Bean
	@ConditionalOnMissingBean
	// TODO fixme - this method only defines a bean of type Collection ; this may conflict with any other
	// Collection bean
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