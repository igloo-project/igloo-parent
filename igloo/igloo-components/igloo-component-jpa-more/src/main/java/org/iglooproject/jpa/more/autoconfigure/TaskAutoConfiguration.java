package org.iglooproject.jpa.more.autoconfigure;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;

@Configuration
@ConditionalOnBean(TaskManagementConfigurer.class)
public class TaskAutoConfiguration {

	private static final Logger LOGGER = LoggerFactory.getLogger(TaskAutoConfiguration.class);
	
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

}
