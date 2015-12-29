package fr.openwide.core.jpa.batch.util;

import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

public class TransactionWrapperCallable<T> implements Callable<T> {

	private static final Logger LOGGER = LoggerFactory.getLogger(TransactionWrapperCallable.class);

	private final TransactionTemplate transactionTemplate;

	private final Callable<T> callable;

	public TransactionWrapperCallable(TransactionTemplate transactionTemplate, Callable<T> callable) {
		super();
		this.transactionTemplate = transactionTemplate;
		this.callable = callable;
	}

	@Override
	public T call() {
		return transactionTemplate.execute(new TransactionCallback<T>() {
			@Override
			public T doInTransaction(TransactionStatus transactionStatus) {
				try {
					return callable.call();
				} catch (Exception e) {
					if (e instanceof InterruptedException) {
						Thread.currentThread().interrupt();
					}
					LOGGER.error("The following error has not been caught; you *must* catch all the errors. Transaction rollback.", e);
					transactionStatus.setRollbackOnly();
					return null;
				}
			}
		});
	}

}
