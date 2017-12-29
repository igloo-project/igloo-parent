package org.iglooproject.showcase.core.config.spring;

import java.util.Collection;

import org.apache.commons.lang3.EnumUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.iglooproject.jpa.more.business.task.model.IQueueId;
import org.iglooproject.jpa.more.config.spring.AbstractTaskManagementConfig;
import org.iglooproject.showcase.core.business.task.model.ShowcaseTaskQueueId;

@Configuration
public class ShowcaseCoreJpaMoreTaskManagementConfig extends AbstractTaskManagementConfig {

	@Override
	@Bean
	public Collection<? extends IQueueId> queueIds() {
		return EnumUtils.getEnumList(ShowcaseTaskQueueId.class);
	}

}
