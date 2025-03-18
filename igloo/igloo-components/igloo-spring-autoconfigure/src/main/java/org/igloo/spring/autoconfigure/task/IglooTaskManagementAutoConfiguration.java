package org.igloo.spring.autoconfigure.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.Collection;
import java.util.TreeSet;
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
import org.iglooproject.jpa.more.config.spring.ImmutableTaskManagement;
import org.iglooproject.jpa.more.config.spring.JpaMoreTaskApplicationPropertyRegistryConfig;
import org.iglooproject.jpa.more.config.spring.TaskManagementConfigurer;
import org.iglooproject.jpa.more.util.transaction.CoreJpaMoreUtilTransactionPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;

@Configuration
@ConditionalOnProperty(
    name = "igloo-ac.tasks.disabled",
    havingValue = "false",
    matchIfMissing = true)
@ComponentScan(
    basePackageClasses = {
      CoreJpaMoreBusinessPackage.class,
      CoreJpaMoreUtilTransactionPackage.class
    })
@Import(JpaMoreTaskApplicationPropertyRegistryConfig.class)
@AutoConfigureAfter(IglooPropertyAutoConfiguration.class)
public class IglooTaskManagementAutoConfiguration {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(IglooTaskManagementAutoConfiguration.class);

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
  @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  public IQueuedTaskHolderSearchQuery queuedTaskHolderSearchQuery() {
    return new QueuedTaskHolderSearchQueryImpl();
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
