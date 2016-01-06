package fr.openwide.core.jpa.batch.util;

import java.util.concurrent.Callable;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

public class TransactionWrapperCallable<T> implements Callable<T> {

	private final TransactionTemplate transactionTemplate;

	private final Callable<T> callable;

	public TransactionWrapperCallable(TransactionTemplate transactionTemplate, Callable<T> callable) {
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
					/* RuntimeException will be handled by the TransactionTemplate itself,
					 * and will be propagated to the  caller unchanged.
					 */
					throw e;
				} catch (Exception e) {
					if (e instanceof InterruptedException) {
						Thread.currentThread().interrupt();
					}
					/* Hack: we just want the stack trace to be clean (without unnecessary wrappers), and the
					 * exception to be propagated to the caller unchanged.
					 * It's ugly, but safe: TransactionTemplate already handles the case
					 * of an unexpected checked exception because of Spring proxies.
					 */
					throw TransactionWrapperCallable.<RuntimeException>checkedExceptionCastHack(e);
				}
			}
		});
	}

	@SuppressWarnings("unchecked")
	private static final <E extends Throwable> E checkedExceptionCastHack(Throwable e) throws E {
		return (E) e;
	}

}
