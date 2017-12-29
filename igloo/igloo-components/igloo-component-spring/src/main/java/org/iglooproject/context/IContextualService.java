package org.iglooproject.context;

import java.util.concurrent.Callable;

import org.iglooproject.commons.util.context.IExecutionContext;

/**
 * @deprecated Make your service implement a method that returns a {@link IExecutionContext} instead.
 */
@Deprecated
public interface IContextualService {
	
	/**
	 * Optimisation.
	 * <p>Lorsque de nombreux appels au service doivent être faits dans un traitement, il vaut mieux utiliser
	 * cette méthode pour wrapper le traitement.
	 * @throws Exception l'exception envoyée par le Callable.
	 */
	<T> T runWithContext(Callable<T> callable) throws Exception;

}
