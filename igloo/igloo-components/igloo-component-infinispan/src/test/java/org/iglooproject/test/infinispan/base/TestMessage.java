package org.iglooproject.test.infinispan.base;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.remoting.transport.Address;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

import org.iglooproject.infinispan.model.Message;
import org.iglooproject.test.infinispan.util.TestCacheManagerBuilder;
import org.iglooproject.test.infinispan.util.TestConstants;
import org.iglooproject.test.infinispan.util.tasks.SimpleMessagingTask;

public class TestMessage extends TestBase {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TestMessage.class);

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
		EmbeddedCacheManager cacheManager = new TestCacheManagerBuilder("node main", "test").build();
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
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("checking messages expected {} : actual {}", nodeNumber, messages.keySet().size());
				}
				return messages.keySet().size() >= nodeNumber;
			}
		};
		waitForEvent(monitor, test, 20, TimeUnit.SECONDS);
	}

}
