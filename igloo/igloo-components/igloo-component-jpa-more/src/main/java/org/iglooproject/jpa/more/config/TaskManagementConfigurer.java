package org.iglooproject.jpa.more.config;

public interface TaskManagementConfigurer {

  default void configure(ImmutableTaskManagement.Builder taskManagement) {
    // nothing
  }
}
