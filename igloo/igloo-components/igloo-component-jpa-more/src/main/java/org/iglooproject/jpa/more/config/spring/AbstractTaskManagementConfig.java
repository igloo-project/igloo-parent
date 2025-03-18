package org.iglooproject.jpa.more.config.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.Collection;
import java.util.TreeSet;
import org.iglooproject.jpa.more.business.CoreJpaMoreBusinessPackage;
import org.iglooproject.jpa.more.business.task.dao.IQueuedTaskHolderDao;
import org.iglooproject.jpa.more.business.task.dao.QueuedTaskHolderDaoImpl;
import org.iglooproject.jpa.more.business.task.model.IQueueId;
import org.iglooproject.jpa.more.business.task.service.IQueuedTaskHolderManager;
import org.iglooproject.jpa.more.business.task.service.IQueuedTaskHolderService;
import org.iglooproject.jpa.more.business.task.service.QueuedTaskHolderManagerImpl;
import org.iglooproject.jpa.more.business.task.service.QueuedTaskHolderServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackageClasses = {CoreJpaMoreBusinessPackage.class})
public abstract class AbstractTaskManagementConfig {

  private static final Logger LOGGER = LoggerFactory.getLogger(AbstractTaskManagementConfig.class);

  public static final String OBJECT_MAPPER_BEAN_NAME = "queuedTaskHolderObjectMapper";

  /**
   * Base configuration for task serialization. We use a permissive deserializer config as data are
   * internally managed by application.
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
  public IQueuedTaskHolderService queuedTaskHolderService(
      IQueuedTaskHolderDao queuedTaskHolderDao) {
    return new QueuedTaskHolderServiceImpl(queuedTaskHolderDao);
  }

  @Bean
  public IQueuedTaskHolderManager queuedTaskHolderManager(
      @Autowired(required = false) Collection<? extends IQueueId> queueIdsBean,
      Collection<TaskManagementConfigurer> configurers) {
    Collection<IQueueId> queueIds = new TreeSet<>();
    if (queueIdsBean != null) {
      LOGGER.warn("Please replace existing IQueueId collection beans by a TaskConfigurer bean.");
      queueIds.addAll(queueIdsBean);
    }
    ImmutableTaskManagement.Builder builder = ImmutableTaskManagement.builder();
    for (TaskManagementConfigurer configurer : configurers) {
      configurer.configure(builder);
      queueIds.addAll(builder.build().queueIds());
    }
    return new QueuedTaskHolderManagerImpl(queueIds);
  }

  /** Ensure that we have at least one configurer. */
  @Bean
  public TaskManagementConfigurer emptyTaskManagementConfigurer() {
    return new TaskManagementConfigurer() {};
  }
}
