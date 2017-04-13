package fr.openwide.core.test.infinispan.util.tasks;

import org.infinispan.AdvancedCache;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.remoting.transport.Address;
import org.infinispan.util.concurrent.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.openwide.core.infinispan.model.Message;
import fr.openwide.core.test.infinispan.base.NodeMessageListener;
import fr.openwide.core.test.infinispan.util.TestConstants;

public class LockTask extends AbstractTask {

	private static final Logger LOGGER = LoggerFactory.getLogger(LockTask.class);

	public static final String TRY_LOCK = "tryLock";
	public static final String LOCK_SUCCESS = "LockSuccess";
	public static final String TIMEOUT_EXCEPTION = "TimeoutException";

	public LockTask(EmbeddedCacheManager cacheManager) {
		super(cacheManager);
	}

	@Override
	public void runTask() {
		// wait for a message from coordinator to try to lock keys A and B
		// post a message TimeoutException if lock cannot be acquired
		// post a message LockSuccess if lock is successful
		getCacheManager().getCache(TestConstants.CACHE_MESSAGE).addListener(new NodeMessageListener<String>() {
			@Override
			public void onMessage(Message<String> value) {
				LOGGER.debug("message received");
				if (value.getAddress().equals(getCacheManager().getCoordinator())) {
					LOGGER.debug("message received from coordinator");
					if ("tryLock".equals(value.getMessage())) {
						LOGGER.debug("received tryLock message");
						AdvancedCache<String, Object> cache =
								getCacheManager().<String, Object>getCache(TestConstants.CACHE_DEFAULT).getAdvancedCache();
						LOGGER.debug("batch started");
						cache.startBatch();
						boolean successful = false;
						Address address = getCacheManager().getAddress();
						try {
							LOGGER.debug("try A, B locks");
							cache.lock("A", "B");
							LOGGER.debug("lock successful");
							successful = true;
							LOGGER.debug("send success message");
							getCacheManager().<String, Message<String>>getCache(TestConstants.CACHE_MESSAGE)
								.put("messageBus", Message.from(address, LOCK_SUCCESS));
						} catch (TimeoutException e) {
							LOGGER.debug("send failure message");
							getCacheManager().<String, Message<String>>getCache(TestConstants.CACHE_MESSAGE)
								.put("messageBus", Message.from(address, TIMEOUT_EXCEPTION));
						} finally {
							cache.endBatch(successful);
							LOGGER.debug("batch ended");
						}
					}
				}
			}
		});
	}

}
