package org.iglooproject.test.infinispan.util.process;

import org.infinispan.manager.EmbeddedCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.iglooproject.test.infinispan.util.TestCacheManagerBuilder;

public class SimpleProcess implements Runnable {

	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleProcess.class);

	private final String nodeName;

	private final String taskName;

	private final Integer expectedViewSize;

	public SimpleProcess(String nodeName, Integer expectedViewSize, String taskName) {
		super();
		this.nodeName = nodeName;
		this.taskName = taskName;
		this.expectedViewSize = expectedViewSize;
	}

	@Override
	public void run() {
		final EmbeddedCacheManager cacheManager =
				new TestCacheManagerBuilder(nodeName, expectedViewSize, taskName, "test").build();
		cacheManager.start();
		
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
			new SimpleProcess(parameters[0], expectedViewSize, taskName).run();
		}
	}

}
