package org.iglooproject.jpa.more.config.spring;

import static org.iglooproject.jpa.more.property.JpaMoreTaskPropertyIds.*;
import static org.iglooproject.jpa.more.property.JpaMoreTaskPropertyIds.START_MODE;
import static org.iglooproject.jpa.more.property.JpaMoreTaskPropertyIds.STOP_TIMEOUT;

import org.iglooproject.spring.config.spring.AbstractApplicationPropertyRegistryConfig;
import org.iglooproject.spring.config.util.TaskQueueStartMode;
import org.iglooproject.spring.property.service.IPropertyRegistry;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JpaMoreTaskApplicationPropertyRegistryConfig
    extends AbstractApplicationPropertyRegistryConfig {

  @Override
  public void register(IPropertyRegistry registry) {
    registry.registerInteger(STOP_TIMEOUT, 70000);
    registry.registerEnum(START_MODE, TaskQueueStartMode.class, TaskQueueStartMode.manual);
    registry.registerInteger(QUEUE_NUMBER_OF_THREADS_TEMPLATE, 1);
    registry.registerLong(QUEUE_START_DELAY_TEMPLATE, 0l);
    registry.registerBoolean(QUEUE_START_EXECUTION_CONTEXT_WAIT_READY_TEMPLATE, true);
  }
}
