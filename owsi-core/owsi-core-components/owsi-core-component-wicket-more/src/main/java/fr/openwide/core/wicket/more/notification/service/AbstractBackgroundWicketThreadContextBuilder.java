package fr.openwide.core.wicket.more.notification.service;

import java.util.Locale;
import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;

import fr.openwide.core.context.IContextualService;

/**
 * @deprecated Define a {@link IWicketContextExecutor} bean in your application and use this bean instead of
 * extending AbstractBackgroundWicketThreadContextBuilder.
 */
public abstract class AbstractBackgroundWicketThreadContextBuilder implements IContextualService {
	
	@Autowired
	private IWicketContextExecutor wicketContextExecutor;
	
	protected abstract String getApplicationName();
	
	@Override
	public <T> T runWithContext(Callable<T> callable) throws Exception {
		return wicketContextExecutor.runWithContext(callable);
	}
	
	protected <T> T runWithContext(Callable<T> callable, Locale locale) throws Exception {
		return wicketContextExecutor.runWithContext(callable, locale);
	}

}