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

public class TestRestart extends TestBase {

	@Test
	public void testMessage() throws IOException, InterruptedException, ExecutionException, TimeoutException {
		final int nodeNumber = 3;
		
		// start test instance
		final EmbeddedCacheManager cacheManager = new TestCacheManagerBuilder("node main", null).build();
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
		
		Callable<Boolean> testOne = new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				return messages.keySet().size() == 3;
			}
		};
		waitForEvent(monitor, testOne, 10, TimeUnit.SECONDS);
		
		// shutdown all nodes
		shutdownProcesses(false);
		
		// wait alone state
		Callable<Boolean> aloneTest = new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				return cacheManager.getMembers().size() == 1;
			}
		};
		waitForEvent(monitor, aloneTest, 20, TimeUnit.SECONDS);
		
		// start new nodes
		prepareCluster(nodeNumber, SimpleMessagingTask.class);
		
		// wait joining nodes
		Callable<Boolean> allTest = new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				return cacheManager.getMembers().size() == 4;
			}
		};
		waitForEvent(monitor, allTest, 20, TimeUnit.SECONDS);
		
		// wait 6 messages (as new nodes use new addresses, new messages are added)
		Callable<Boolean> testTwo = new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				return messages.keySet().size() == 6;
			}
		};
		waitForEvent(monitor, testTwo, 10, TimeUnit.SECONDS);
	}

}
