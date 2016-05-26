package fr.openwide.core.commons.util.context;

import java.util.concurrent.Callable;

import javax.annotation.concurrent.NotThreadSafe;

/**
 * Encapsulates the concept of setting up and tearing down an implementation-defined context.
 * 
 * <p>Setting up a context typically involves altering a state, and tearing it down typically means restoring the
 * previous state (or any clean state, depending on the implementor's constraints)).
 * <p>The state may for example be stored in a {@link ThreadLocal}, or in the object that created this
 * {@link IExecutionContext}.
 * 
 * <p>Implementations are not required to be thread-safe.
 */
@NotThreadSafe
public interface IExecutionContext {
	
	/**
	 * Set up the context for execution and return an {@link ITearDownHandle} that will tear down the context upon
	 * {@link ITearDownHandle#close()}.
	 * 
	 * <p>This is generally used this way:
	 * <code><pre>
	 * IExecutionContext context = ... ;
	 * try (AutoCloseable openContext = context.open()) {
	 *   // Do stuff that requires the context
	 * }
	 * // Here the context has been automatically closed 
	 * </pre></code>
	 */
	ITearDownHandle open();
	
	/**
	 * An {@link AutoCloseable} that may not throw checked exceptions.
	 */
	public interface ITearDownHandle extends AutoCloseable {
		@Override
		public void close();
	}
	
	/**
	 * Set up the context, run the given {@link Runnable}, then tear down the context.
	 * <p>The implementation must tear down the context even if the runnable throws an exception.
	 */
	void run(Runnable runnable);
	
	/**
	 * Set up the context, run the given {@link callable}, tear down the context, and return the callable's result
	 * (if any, i.e. if it didn't throw an exception).
	 * <p>The implementation must tear down the context even if the runnable throws an exception.
	 */
	<T> T run(Callable<T> callable) throws Exception;

}
