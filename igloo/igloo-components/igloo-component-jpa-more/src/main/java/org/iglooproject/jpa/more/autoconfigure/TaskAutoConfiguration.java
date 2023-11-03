package org.iglooproject.jpa.more.autoconfigure;

import static org.iglooproject.jpa.more.property.JpaMoreTaskPropertyIds.QUEUE_NUMBER_OF_THREADS_TEMPLATE;
import static org.iglooproject.jpa.more.property.JpaMoreTaskPropertyIds.QUEUE_START_DELAY_TEMPLATE;
import static org.iglooproject.jpa.more.property.JpaMoreTaskPropertyIds.QUEUE_START_EXECUTION_CONTEXT_WAIT_READY_TEMPLATE;
import static org.iglooproject.jpa.more.property.JpaMoreTaskPropertyIds.START_MODE;
import static org.iglooproject.jpa.more.property.JpaMoreTaskPropertyIds.STOP_TIMEOUT;

import java.util.ArrayList;
import java.util.Collection;

import org.iglooproject.jpa.more.business.task.dao.IQueuedTaskHolderDao;
import org.iglooproject.jpa.more.business.task.dao.QueuedTaskHolderDaoImpl;
import org.iglooproject.jpa.more.business.task.model.IQueueId;
import org.iglooproject.jpa.more.business.task.service.IQueuedTaskHolderManager;
import org.iglooproject.jpa.more.business.task.service.IQueuedTaskHolderService;
import org.iglooproject.jpa.more.business.task.service.QueuedTaskHolderManagerImpl;
import org.iglooproject.jpa.more.business.task.service.QueuedTaskHolderServiceImpl;
import org.iglooproject.jpa.more.config.spring.ImmutableTaskManagement;
import org.iglooproject.jpa.more.config.spring.TaskManagementConfigurer;
import org.iglooproject.spring.config.spring.IPropertyRegistryConfig;
import org.iglooproject.spring.config.util.TaskQueueStartMode;
import org.iglooproject.spring.property.service.IPropertyRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
@ConditionalOnBean(TaskManagementConfigurer.class)
public class TaskAutoConfiguration implements IPropertyRegistryConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(TaskAutoConfiguration.class);
	
	public static final String OBJECT_MAPPER_BEAN_NAME = "queuedTaskHolderObjectMapper";

	/**
	 * Base configuration for task serialization. We use a permissive deserializer config as data are internally
	 * managed by application.
	 */
	@Bean(name = OBJECT_MAPPER_BEAN_NAME)
	public ObjectMapper queuedTaskHolderObjectMapper() {
		return new ObjectMapper()
			.registerModule(new JavaTimeModule())
			.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, DefaultTyping.NON_FINAL);
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
	public IQueuedTaskHolderManager queuedTaskHolderManager(
			@Autowired(required = false) Collection<? extends IQueueId> queueIdsBean,
			Collection<TaskManagementConfigurer> configurers) {
		Collection<IQueueId> queueIds = new ArrayList<>();
		if (queueIdsBean != null) {
			LOGGER.warn("Please replace existing IQueueId collection beans by a TaskConfigurer bean.");
			queueIds.addAll(queueIdsBean);
		}
		ImmutableTaskManagement.Builder builder = ImmutableTaskManagement.builder();
		for (TaskManagementConfigurer configurer : configurers) {
			configurer.configure(builder);
		}
		return new QueuedTaskHolderManagerImpl(queueIds);
	}

	@Override
	public void register(IPropertyRegistry registry) {
		registry.registerInteger(STOP_TIMEOUT, 70000);
		registry.registerEnum(START_MODE, TaskQueueStartMode.class, TaskQueueStartMode.manual);
		registry.registerInteger(QUEUE_NUMBER_OF_THREADS_TEMPLATE, 1);
		registry.registerLong(QUEUE_START_DELAY_TEMPLATE, 0l);
		registry.registerBoolean(QUEUE_START_EXECUTION_CONTEXT_WAIT_READY_TEMPLATE, true);
	}

}
