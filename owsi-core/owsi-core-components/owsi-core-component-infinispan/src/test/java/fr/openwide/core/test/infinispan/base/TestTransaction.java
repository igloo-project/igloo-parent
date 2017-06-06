package fr.openwide.core.test.infinispan.base;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.infinispan.AdvancedCache;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.remoting.transport.Address;
import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Maps;

import fr.openwide.core.infinispan.model.Message;
import fr.openwide.core.test.infinispan.util.TestCacheManagerBuilder;
import fr.openwide.core.test.infinispan.util.TestConstants;
import fr.openwide.core.test.infinispan.util.tasks.LockTask;

public class TestTransaction extends TestBase {

	/**
	 * Test {@link AdvancedCache#startBatch()} (to handle concurrent read / update).
	 * We lock to keys (A and B) and asks for second nodes (using message) to confirm that these keys cannot be locked
	 * concurrently.
	 * After keys' release, confirm that second node can now lock these keys.
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws TimeoutException
	 * @throws ExecutionException
	 */
	@Test
	public void testBatch() throws IOException, InterruptedException, TimeoutException, ExecutionException {
		// start infinispan
		final int nodeNumber = 1;
		
		// start test instance
		final EmbeddedCacheManager cacheManager = new TestCacheManagerBuilder("node main", null).build();
		this.cacheManager = cacheManager;
		
		final Object monitor = new Object();
		final Map<Address, String> messages = Maps.newConcurrentMap();
		
		cacheManager.start();
		cacheManager.getCache(TestConstants.CACHE_MESSAGE).addListener(new NodeMessageListener<String>() {
			@Override
			public void onMessage(Message<String> value) {
				messages.put(value.getAddress(), value.getMessage());
				synchronized (monitor) {
					monitor.notify();
				}
			}
		});
		// initializes two keys A and B
		cacheManager.getCache(TestConstants.CACHE_DEFAULT).put("A", "A");
		cacheManager.getCache(TestConstants.CACHE_DEFAULT).put("B", "B");
		
		// start another node that register a task that waits a message to lock A and B
		prepareCluster(nodeNumber, LockTask.class);
		
		// startBatch
		AdvancedCache<Object, Object> cache = cacheManager.<Object, Object>getCache(TestConstants.CACHE_DEFAULT).getAdvancedCache();
		cache.startBatch();
		boolean successful = false;
		final Address address = cacheManager.getAddress();
		
		Callable<Boolean> test = new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				return ! address.equals(cacheManager.<String, Message<String>>getCache(TestConstants.CACHE_MESSAGE).get(TestConstants.CACHE_KEY_MESSAGE_BUS).getAddress());
			}
		};
		
		try {
			// lock A and B
			cache.lock("A", "B");
			// send message -> second node cannot lock -> message TimeoutException
			cacheManager.getCache(TestConstants.CACHE_MESSAGE).put(TestConstants.CACHE_KEY_MESSAGE_BUS, Message.from(address, LockTask.TRY_LOCK));
			// must be set accordingly with lockAcquisitionTimeout
			waitForEvent(monitor, test, 25, TimeUnit.SECONDS);
			Assert.assertEquals(LockTask.TIMEOUT_EXCEPTION, cacheManager.<String, Message<String>>getCache(TestConstants.CACHE_MESSAGE).get(TestConstants.CACHE_KEY_MESSAGE_BUS).getMessage());
			successful = true;
		} finally {
			// stopBatch
			cache.endBatch(successful);
		}
		// second node can lock
		cacheManager.getCache(TestConstants.CACHE_MESSAGE).put(TestConstants.CACHE_KEY_MESSAGE_BUS, Message.from(address, LockTask.TRY_LOCK));
		// must be set accordingly with lockAcquisitionTimeout
		waitForEvent(monitor, test, 25, TimeUnit.SECONDS);
		Assert.assertEquals(LockTask.LOCK_SUCCESS, cacheManager.<String, Message<String>>getCache(TestConstants.CACHE_MESSAGE).get(TestConstants.CACHE_KEY_MESSAGE_BUS).getMessage());
	}

}
