package org.iglooproject.jpa.more.config.spring;

public interface TaskManagementConfigurer {

  default void configure(ImmutableTaskManagement.Builder taskManagement) {
    // nothing
  }
}
