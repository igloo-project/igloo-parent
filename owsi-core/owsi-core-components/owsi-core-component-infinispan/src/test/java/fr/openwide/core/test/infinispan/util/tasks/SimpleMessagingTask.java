package fr.openwide.core.test.infinispan.util.tasks;

import org.infinispan.manager.EmbeddedCacheManager;

import fr.openwide.core.infinispan.model.Message;
import fr.openwide.core.test.infinispan.util.TestConstants;

public class SimpleMessagingTask extends AbstractTask {

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
	}

}
