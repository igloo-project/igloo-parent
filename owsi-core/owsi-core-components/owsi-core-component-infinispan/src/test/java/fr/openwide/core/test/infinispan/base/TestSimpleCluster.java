package fr.openwide.core.test.infinispan.base;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.infinispan.manager.EmbeddedCacheManager;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.openwide.core.infinispan.service.IInfinispanClusterService;
import fr.openwide.core.test.infinispan.util.TestCacheManagerBuilder;

/**
 * Test {@link IInfinispanClusterService} wrapper.
 */
public class TestSimpleCluster extends TestBase {

	private static final Logger LOGGER = LoggerFactory.getLogger(TestSimpleCluster.class);

	/**
	 * Run multiple instances and check that all instances are online.
	 * 
	 * @throws ExecutionException 
	 * @throws TimeoutException 
	 */
	@Test
	public void testStart() throws IOException, InterruptedException, ExecutionException {
		int nodeNumber = 3;
		prepareCluster(nodeNumber, null);
		
		// start test instance
		EmbeddedCacheManager cacheManager = new TestCacheManagerBuilder("node main", null).build();
		this.cacheManager = cacheManager;
		cacheManager.start();
		
		LOGGER.debug("waiting nodes");
		try {
			waitNodes(cacheManager, nodeNumber + 1, 30, TimeUnit.SECONDS);
		} catch (TimeoutException e) {
			Assert.fail(String.format("Node number %d not reached before timeout", nodeNumber + 1));
		}
	}

}
