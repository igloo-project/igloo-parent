package fr.openwide.core.test.infinispan.base;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import fr.openwide.core.infinispan.service.InfinispanClusterServiceImpl;
import fr.openwide.core.test.infinispan.util.TestCacheManagerBuilder;
import fr.openwide.core.test.infinispan.util.process.InfinispanClusterProcess;
import fr.openwide.core.test.infinispan.util.roles.SimpleRolesProvider;

public class TestInfinispanCluster extends TestBase {

	@Test
	public void testInfinispanCluster() throws IOException, InterruptedException {
		// start infinispan
		final int nodeNumber = 3;
		
		// start test instance
		String nodeName = "node main";
		this.cacheManager = new TestCacheManagerBuilder(nodeName, null).build();
		InfinispanClusterServiceImpl cluster =
				new InfinispanClusterServiceImpl(nodeName, cacheManager, new SimpleRolesProvider());
		cluster.init();
		
		// start other nodes
		prepareCluster(nodeNumber, null);
		
		Thread.sleep(TimeUnit.SECONDS.toMillis(30));
	}

	@Override
	protected String getProcessClassName() {
		return InfinispanClusterProcess.class.getCanonicalName();
	}

}
