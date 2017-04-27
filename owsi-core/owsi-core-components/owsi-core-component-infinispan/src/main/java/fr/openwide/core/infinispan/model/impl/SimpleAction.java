package fr.openwide.core.infinispan.model.impl;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.jgroups.Address;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.openwide.core.infinispan.model.IAction;
import fr.openwide.core.infinispan.service.IInfinispanClusterService;

public abstract class SimpleAction<V> implements IAction<V> {

	private static final long serialVersionUID = 2478882549352256098L;

	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleAction.class);

	protected transient IInfinispanClusterService infinispanClusterService;

	private final Address target;

	private V value;

	private Throwable exception;

	private boolean cancelled = false;

	private boolean done = false;

	protected SimpleAction(Address target) {
		super();
		this.target = target;
	}

	public Address getTarget() {
		return target;
	}

	@Override
	public synchronized boolean cancel(boolean mayInterruptIfRunning) {
		throw new IllegalStateException("Not implemented");
	}

	@Override
	public synchronized boolean isCancelled() {
		return cancelled;
	}

	@Override
	public synchronized boolean isDone() {
		return done;
	}

	@Override
	public V get() throws InterruptedException, ExecutionException {
		if (isDone()) {
			if (exception != null) {
				throw new ExecutionException(exception);
			} else {
				return value;
			}
		} else if (isCancelled()) {
			throw new CancellationException();
		} else {
			// TODO LAL
			throw new IllegalStateException();
		}
	}

	@Override
	public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		throw new IllegalStateException("not implemented");
	}

	@Override
	public synchronized void run() {
		if (isDone()) {
			LOGGER.warn("{} already done ; run() ignored", getClass().getSimpleName());
			return;
		}
		if (isCancelled()) {
			LOGGER.warn("{} has been cancelled ; subsequent run ignored", getClass().getSimpleName());
		}
		try {
			V value = call();
			doneWithValue(value);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			doneWithException(e);
		} catch (Exception e) {
			doneWithException(e);
		}
	}

	private void doneWithException(Exception e) {
		done = true;
		value = null;
		exception = e;
		cancelled = false;
	}

	private void doneWithValue(V o) {
		done = true;
		value = o;
		exception = null;
		cancelled = false;
	}

	@Override
	public void setInfinispanClusterService(IInfinispanClusterService infinispanClusterService) {
		this.infinispanClusterService = infinispanClusterService;
	}

	public abstract V call() throws Exception;

}
