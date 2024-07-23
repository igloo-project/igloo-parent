package org.iglooproject.basicapp.core.config.spring;

import org.apache.commons.lang3.EnumUtils;
import org.iglooproject.basicapp.core.business.task.model.BasicApplicationTaskQueueId;
import org.iglooproject.jpa.more.config.spring.ImmutableTaskManagement.Builder;
import org.iglooproject.jpa.more.config.spring.TaskManagementConfigurer;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BasicApplicationCoreTaskManagementConfig {

  @Configuration
  public static class ApplicationTaskManagementConfigurer implements TaskManagementConfigurer {
    @Override
    public void configure(Builder taskManagement) {
      taskManagement.addAllQueueIds(EnumUtils.getEnumList(BasicApplicationTaskQueueId.class));
    }
  }
}
