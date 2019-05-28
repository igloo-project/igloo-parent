package test.jpa.more.business.task.config;

import java.util.Collection;

import org.apache.commons.lang3.EnumUtils;
import org.iglooproject.config.bootstrap.spring.annotations.IglooPropertySourcePriority;
import org.iglooproject.jpa.more.business.task.model.IQueueId;
import org.iglooproject.jpa.more.config.spring.AbstractTaskManagementConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import test.jpa.more.business.task.model.TestQueueId;

@Configuration
@PropertySource(
	name = IglooPropertySourcePriority.APPLICATION,
	value = "classpath:jpa-more-test-task-management.properties"
)
public class TestTaskManagementConfig extends AbstractTaskManagementConfig {

	@Override
	public Collection<? extends IQueueId> queueIds() {
		return EnumUtils.getEnumList(TestQueueId.class);
	}

}
