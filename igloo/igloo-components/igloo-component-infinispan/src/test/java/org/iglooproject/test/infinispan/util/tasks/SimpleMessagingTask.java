package org.iglooproject.test.infinispan.util.tasks;

import org.infinispan.manager.EmbeddedCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.iglooproject.infinispan.model.Message;
import org.iglooproject.test.infinispan.util.TestConstants;

public class SimpleMessagingTask extends AbstractTask {

	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleMessagingTask.class);

	public SimpleMessagingTask(EmbeddedCacheManager cacheManager) {
		super(cacheManager);
	}

	@Override
	public void runTask() {
		getCacheManager().getCache(TestConstants.CACHE_DEFAULT)
			.put(
				getCacheManager().getAddress(),
				Message.from(getCacheManager().getAddress(), "online"
			)
		);
		LOGGER.debug("message sent from {}", getCacheManager().getAddress());
	}

}
