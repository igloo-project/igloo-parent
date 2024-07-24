package org.iglooproject.wicket.more.notification.service;

import java.util.Locale;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.cycle.RequestCycle;
import org.iglooproject.commons.util.context.IExecutionContext;

/**
 * Provides Wicket {@link IExecutionContext execution contexts}, ensuring that Wicket's threadlocal
 * context is available during the execution.
 *
 * <p>Implementations may have to provide a simulated {@link RequestCycle}. When they do so, they
 * must ensure that the simulated request returns a server name, server port, servlet path and
 * context path that are consistant with the Wicket application in the Wicket context (i.e. the
 * servlet path, in particular, must match the application's servlet filter path).
 *
 * <h3>When is it useful?</h3>
 *
 * <p>The Wicket threadlocal context is necessary in order to be able to call methods such as <code>
 * org.apache.wicket.Session.get()</code> or <code>org.apache.wicket.Application.get()</code>, which
 * are required by a lot of Wicket components.
 */
public interface IWicketContextProvider {

  /**
   * Provides a Wicket context with the following setup:
   *
   * <ul>
   *   <li>the application currently attached to the thread, or (if there isn't any) a default
   *       Wicket application (which is implementation-dependent).
   *   <li>the session currently attached to the thread, or (if there isn't any) a new session with
   *       the default locale.
   * </ul>
   */
  IExecutionContext context();

  /**
   * Provides a Wicket context with the following setup:
   *
   * <ul>
   *   <li>the application currently attached to the thread, or (if there isn't any) a default
   *       Wicket application (which is implementation-dependent).
   *   <li>the session currently attached to the thread if there is one and the given locale is null
   *       or the same as the session's locale, or a new session with the given locale otherwise, or
   *       (if the given locale is null) a new session with the default locale.
   * </ul>
   */
  IExecutionContext context(Locale locale);

  /**
   * Provides a Wicket context with the following setup:
   *
   * <ul>
   *   <li>the given application, or (if the given application is null) the application currently
   *       attached to the thread, or (if there isn't any) a default Wicket application (which is
   *       implementation-dependent).
   *   <li>the session currently attached to the thread if there is one and the given locale is null
   *       or the same as the session's locale, or a new session with the given locale otherwise, or
   *       (if the given locale is null) a new session with the default locale.
   * </ul>
   */
  IExecutionContext context(WebApplication application);

  /**
   * Provides a Wicket context with the following setup:
   *
   * <ul>
   *   <li>the given application, or (if the given application is null) the application currently
   *       attached to the thread, or (if there isn't any) a default Wicket application (which is
   *       implementation-dependent).
   *   <li>the session currently attached to the thread if there is one and the given locale is null
   *       or the same as the session's locale, or a new session with the given locale otherwise, or
   *       (if the given locale is null) a new session with the default locale.
   * </ul>
   */
  IExecutionContext context(WebApplication application, Locale locale);
}
