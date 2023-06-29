package test.jpa.more.business.task.config;

import org.apache.commons.lang3.EnumUtils;
import org.iglooproject.config.bootstrap.spring.annotations.IglooPropertySourcePriority;
import org.iglooproject.jpa.more.config.spring.ImmutableTaskManagement.Builder;
import org.iglooproject.jpa.more.config.spring.TaskManagementConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import test.jpa.more.business.task.model.TestQueueId;

@Configuration
@PropertySource(
	name = IglooPropertySourcePriority.APPLICATION,
	value = {
		"classpath:jpa-more-test-task-management.properties"
	},
	encoding = "UTF-8"
)
public class TestTaskManagementConfig {

	@Configuration
	public static class TestTaskManagementConfigurer implements TaskManagementConfigurer {
		@Override
		public void configure(Builder taskManagement) {
			taskManagement.addAllQueueIds(EnumUtils.getEnumList(TestQueueId.class));
		}
	}
	@Bean
	public TaskManagementConfigurer emptyTaskManagementConfigurer() {
		return new TaskManagementConfigurer() {};
	}

}
