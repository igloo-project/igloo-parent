package fr.openwide.core.test.infinispan.base;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.notifications.cachemanagerlistener.annotation.ViewChanged;
import org.infinispan.notifications.cachemanagerlistener.event.ViewChangedEvent;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;

import fr.openwide.core.test.infinispan.util.TestCacheManagerBuilder;
import fr.openwide.core.test.infinispan.util.listener.MonitorNotifyListener;
import fr.openwide.core.test.infinispan.util.process.SimpleProcess;
import fr.openwide.core.test.infinispan.util.tasks.AbstractTask;

public class TestBase {

	private static final Logger LOGGER = LoggerFactory.getLogger(TestBase.class);

	protected Collection<Process> processesRegistry = Lists.newArrayList();
	protected EmbeddedCacheManager cacheManager = null;

	/**
	 * Run multiple instances and check that all instances are online.
	 */
	@Test
	public void testStart() throws IOException, InterruptedException {
		int nodeNumber = 3;
		prepareCluster(nodeNumber, null);
		
		// start test instance
		EmbeddedCacheManager cacheManager = new TestCacheManagerBuilder("node main", null).build();
		this.cacheManager = cacheManager;
		cacheManager.start();
		
		LOGGER.debug("waiting nodes");
		Thread.sleep(TimeUnit.SECONDS.toMillis(10));
		LOGGER.debug("testing nodes");
		
		Assert.assertEquals(nodeNumber + 1, cacheManager.getMembers().size());
	}

	protected Collection<Process> prepareCluster(int nodeNumber, Class<? extends AbstractTask> taskName) throws IOException {
		Collection<Process> processes = Lists.newArrayListWithExpectedSize(nodeNumber);
		
		// start other instances
		for (int i = 0; i < nodeNumber; i++) {
			Process process;
			if (taskName != null) {
				process = runInfinispan("node " + Integer.toString(i), taskName.getName());
			} else {
				process = runInfinispan("node " + Integer.toString(i));
			}
			processes.add(process);
			processesRegistry.add(process);
		}
		
		return processes;
	}

	protected final Process runInfinispan(String nodeName, String... customArguments) throws IOException {
		Function<URL, String> urlToFile = new Function<URL, String>() {
			@Override
			public String apply(URL input) {
				return input.getFile();
			}
		};
		String classpath = Joiner.on(File.pathSeparator).join(
				Lists.transform(
					// url collection
					Lists.newArrayList(((URLClassLoader) Thread.currentThread().getContextClassLoader()).getURLs()),
					// to files
					urlToFile
				) // to string with pathSeparator
		);
		List<String> arguments = Lists.newArrayList();
		arguments.add(System.getProperty("java.home") + "/bin/java");
		arguments.add("-classpath");
		arguments.add(classpath);
		arguments.add(getProcessClassName());
		arguments.add(nodeName);
		arguments.addAll(Lists.newArrayList(customArguments));
		
		Process process = new ProcessBuilder(arguments).inheritIO().start();
		LOGGER.debug("command launched {}", Joiner.on(" ").join(arguments));
		return process;
	}

	protected String getProcessClassName() {
		return SimpleProcess.class.getCanonicalName();
	}

	@Before
	public void initializeJmx() {
		ManagementFactory.getPlatformMBeanServer();
	}

	@After
	public void shutdownProcesses() throws InterruptedException, TimeoutException, ExecutionException {
		shutdownProcesses(true);
	}

	protected void shutdownProcesses(boolean shutdownCache) throws InterruptedException, TimeoutException, ExecutionException {
		final Object monitor = new Object();
		cacheManager.addListener(new MonitorNotifyListener() {
			@ViewChanged
			public void onViewChanged(ViewChangedEvent viewChangedEvent) {
				synchronized (monitor) {
					LOGGER.debug("notify monitor on view change");
					monitor.notify();
				}
			}
		});
		if (cacheManager != null) {
			for (Process process : processesRegistry) {
				LOGGER.debug("process destroy asked {}", process);
				final int numView = cacheManager.getMembers().size();
				process.destroy();
				
				// wait for shutdown before stopping next node
				waitForEvent(monitor, new Callable<Boolean>() {
					@Override
					public Boolean call() throws Exception {
						if (LOGGER.isDebugEnabled()) {
							LOGGER.debug("checking view size expected {} : actual {}",
									numView - 1, cacheManager.getMembers().size());
						}
						
						return cacheManager.getMembers().size() <= numView - 1;
					}
				}, 10, TimeUnit.SECONDS);
				LOGGER.debug("process destroy viewed in cluster {}", process);
			}
			
			for (Process process : processesRegistry) {
				process.waitFor();
				LOGGER.debug("process destroy detected {}", process);
			}
			
			processesRegistry.clear();
			
			if (shutdownCache) {
				// shutdown caches last (so that other processes terminates gracefully)
				LOGGER.debug("cache stop {}", cacheManager);
				cacheManager.stop();
				LOGGER.debug("cache stopped {}", cacheManager);
			}
		}
	}

	public void waitForEvent(Object monitor, Callable<Boolean> testEvent, long delay, TimeUnit unit)
			throws InterruptedException, TimeoutException, ExecutionException {
		Stopwatch stopwatch = Stopwatch.createUnstarted();
		while ( ! Thread.interrupted() && stopwatch.elapsed(unit) < delay) {
			if ( ! stopwatch.isRunning()) {
				stopwatch.start();
			}
			if (test(testEvent)) {
				return;
			}
			synchronized (monitor) {
				monitor.wait(Math.max(0, unit.toMillis(delay) - stopwatch.elapsed(TimeUnit.MILLISECONDS)));
			}
		}
		// test a last time
		if (test(testEvent)) {
			return;
		}
		// fails with timeout
		throw new TimeoutException();
	}

	private boolean test(Callable<Boolean> testEvent) throws ExecutionException {
		try {
			if (testEvent.call()) {
				return true;
			}
		} catch (Exception e) {
			if (e instanceof InterruptedException) {
				Thread.currentThread().interrupt();
			}
			throw new ExecutionException(e);
		}
		return false;
	}

}
