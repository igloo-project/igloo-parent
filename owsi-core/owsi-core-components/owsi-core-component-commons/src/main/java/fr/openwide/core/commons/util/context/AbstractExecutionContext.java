package fr.openwide.core.commons.util.context;

import java.util.concurrent.Callable;

public abstract class AbstractExecutionContext implements IExecutionContext {

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

}
