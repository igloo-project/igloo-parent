package fr.openwide.core.test.jpa.more.business.task.config;

import java.util.Collection;

import org.apache.commons.lang3.EnumUtils;
import org.springframework.context.annotation.Configuration;

import fr.openwide.core.jpa.more.business.task.model.IQueueId;
import fr.openwide.core.jpa.more.config.spring.AbstractTaskManagementConfig;
import fr.openwide.core.test.jpa.more.business.task.model.TestQueueId;

@Configuration
public class TestTaskManagementConfig extends AbstractTaskManagementConfig {

	@Override
	public Collection<? extends IQueueId> queueIds() {
		return EnumUtils.getEnumList(TestQueueId.class);
	}

}
