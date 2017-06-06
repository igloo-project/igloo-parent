package fr.openwide.core.test.infinispan.base;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.remoting.transport.Address;
import org.junit.Test;

import com.google.common.collect.Maps;

import fr.openwide.core.infinispan.model.Message;
import fr.openwide.core.test.infinispan.util.TestCacheManagerBuilder;
import fr.openwide.core.test.infinispan.util.TestConstants;
import fr.openwide.core.test.infinispan.util.tasks.SimpleMessagingTask;

public class TestMessage extends TestBase {

	/**
	 * Simple messaging test.
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws TimeoutException
	 */
	@Test
	public void testMessage() throws IOException, InterruptedException, ExecutionException, TimeoutException {
		final int nodeNumber = 3;
		
		// start test instance
		EmbeddedCacheManager cacheManager = new TestCacheManagerBuilder("node main", null).build();
		this.cacheManager = cacheManager;
		
		final Object monitor = new Object();
		final Map<Address, String> messages = Maps.newConcurrentMap();
		
		cacheManager.start();
		cacheManager.getCache(TestConstants.CACHE_DEFAULT).addListener(new NodeMessageListener<String>() {
			@Override
			public void onMessage(Message<String> value) {
				messages.put(value.getAddress(), value.getMessage());
				synchronized (monitor) {
					monitor.notify();
				}
			}
		});
		
		prepareCluster(nodeNumber, SimpleMessagingTask.class);
		
		// wait for n (n=nodeNumber) messages
		Callable<Boolean> test = new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				return messages.keySet().size() >= nodeNumber;
			}
		};
		waitForEvent(monitor, test, 10, TimeUnit.SECONDS);
	}

}
