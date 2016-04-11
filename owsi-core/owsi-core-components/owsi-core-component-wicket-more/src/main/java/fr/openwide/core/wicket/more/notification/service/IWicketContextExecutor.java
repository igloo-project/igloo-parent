package fr.openwide.core.wicket.more.notification.service;

import java.util.Locale;
import java.util.concurrent.Callable;

import org.apache.wicket.protocol.http.WebApplication;

import fr.openwide.core.context.IContextualService;

/**
 * Executes callables, ensuring that Wicket's threadlocal context is available during the execution.
 * <p>The Wicket threadlocal context is necessary in order to be able to call methods such as
 * <code>org.apache.wicket.Session.get()</code> or <code>org.apache.wicket.Application.get()</code>,
 * which are required by a lot of Wicket components.
 */
public interface IWicketContextExecutor extends IContextualService {

	/**
	 * Executes a callable, ensuring that Wicket's threadlocal context is available during the execution.
	 * <p>This method uses a default locale and the default Wicket application (which is implementation-dependent).
	 */
	@Override
	<T> T runWithContext(Callable<T> callable) throws Exception;
	
	/**
	 * Executes a callable, ensuring that Wicket's threadlocal context is available during the execution.
	 * <p>This method sets the given locale on the Wicket Session and uses the default
	 * Wicket application (which is implementation-dependent).
	 */
	<T> T runWithContext(Callable<T> callable, Locale locale) throws Exception;

	/**
	 * Executes a callable, ensuring that Wicket's threadlocal context is available during the execution.
	 * <p>This method uses a default locale and the given Wicket application.
	 */
	<T> T runWithContext(WebApplication application, Callable<T> callable) throws Exception;

	/**
	 * Executes a callable, ensuring that Wicket's threadlocal context is available during the execution.
	 * <p>This method sets the given locale on the Wicket Session and uses the given Wicket application.
	 */
	<T> T runWithContext(WebApplication application, Callable<T> callable, Locale locale) throws Exception;
	
	/**
	 * Executes a callable, ensuring that Wicket's threadlocal context is available during the execution.
	 * <p>This method uses a default locale and the Wicket application with the given name.
	 */
	<T> T runWithContext(String applicationName, Callable<T> callable) throws Exception;

	/**
	 * Executes a callable, ensuring that Wicket's threadlocal context is available during the execution.
	 * <p>This method sets the given locale on the Wicket Session and uses the Wicket application
	 * with the given name.
	 */
	<T> T runWithContext(String applicationName, Callable<T> callable, Locale locale) throws Exception;

}
