package org.iglooproject.test.infinispan.util.process;

import org.iglooproject.infinispan.service.InfinispanClusterServiceImpl;
import org.iglooproject.test.infinispan.util.TestCacheManagerBuilder;
import org.iglooproject.test.infinispan.util.roles.SimpleRolesProvider;
import org.infinispan.manager.EmbeddedCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InfinispanClusterProcess implements Runnable {

	private static final Logger LOGGER = LoggerFactory.getLogger(InfinispanClusterProcess.class);

	private final String nodeName;

	private Integer expectedViewSize;

	private final String taskName;

	public InfinispanClusterProcess(String nodeName, Integer expectedViewSize, String taskName) {
		super();
		this.nodeName = nodeName;
		this.taskName = taskName;
		this.expectedViewSize = expectedViewSize;
	}

	@Override
	public void run() {
		final EmbeddedCacheManager cacheManager = new TestCacheManagerBuilder(nodeName, expectedViewSize, taskName, "test").build();
		InfinispanClusterServiceImpl cluster =
				new InfinispanClusterServiceImpl(nodeName, cacheManager, new SimpleRolesProvider(), null, null);
		cluster.init();
		
		while (!Thread.currentThread().isInterrupted()) {
			try {
				Object object = new Object();
				// synchronized to become object owner
				LOGGER.debug("Start main loop");
				synchronized (object) {
					object.wait();
				}
			} catch (InterruptedException e) {
				LOGGER.warn("Thread interrupted while wait");
				Thread.currentThread().interrupt();
			}
		}
	}

	public static void main(String[] parameters) {
		if (parameters.length != 1 && parameters.length != 3) {
			System.err.println("Usage: <node name> [<expected-view-size> <task.class.name>]");
			System.exit(1);
		} else {
			String taskName = null;
			Integer expectedViewSize = null;
			if (parameters.length == 3) {
				expectedViewSize = Integer.parseInt(parameters[1]);
				taskName = parameters[2];
			}
			new InfinispanClusterProcess(parameters[0], expectedViewSize, taskName).run();
		}
	}

}
