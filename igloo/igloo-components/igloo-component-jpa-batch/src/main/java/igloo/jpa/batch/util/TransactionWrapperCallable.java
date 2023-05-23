package igloo.jpa.batch.util;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.concurrent.Callable;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionOperations;

public class TransactionWrapperCallable<T> implements Callable<T> {

	private final TransactionOperations transactionTemplate;

	private final Callable<T> callable;

	public TransactionWrapperCallable(TransactionOperations transactionTemplate, Callable<T> callable) {
		super();
		this.transactionTemplate = transactionTemplate;
		this.callable = callable;
	}

	@Override
	public T call() throws Exception {
		return transactionTemplate.execute(new TransactionCallback<T>() {
			@Override
			public T doInTransaction(TransactionStatus transactionStatus) {
				try {
					return callable.call();
				} catch (RuntimeException e) {
					throw e;
				} catch (Exception e) {
					if (e instanceof InterruptedException) {
						Thread.currentThread().interrupt();
					}
					throw new UndeclaredThrowableException(e);
				}
			}
		});
	}

}
