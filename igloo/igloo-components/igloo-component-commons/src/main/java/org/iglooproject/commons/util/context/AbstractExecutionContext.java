package org.iglooproject.commons.util.context;

import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractExecutionContext implements IExecutionContext {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractExecutionContext.class);

	@Override
	public abstract ITearDownHandle open();

	@Override
	public void run(Runnable runnable) {
		try (ITearDownHandle openContext = open()) {
			runnable.run();
		}
	}

	@Override
	public <T> T run(Callable<T> callable) throws Exception {
		try (ITearDownHandle openContext = open()) {
			return callable.call();
		}
	}

	@Override
	public boolean isReady() {
		try (ITearDownHandle openContext = open()) {
			return true;
		} catch (Exception e) {
			LOGGER.debug(getClass().getSimpleName() + " is not ready to be opened.", e);
			return false;
		}
	}
}
