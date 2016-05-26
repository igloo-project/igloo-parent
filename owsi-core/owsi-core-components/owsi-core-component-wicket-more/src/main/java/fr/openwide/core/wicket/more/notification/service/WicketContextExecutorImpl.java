package fr.openwide.core.wicket.more.notification.service;

import java.util.Locale;
import java.util.concurrent.Callable;

import org.apache.wicket.Application;
import org.apache.wicket.protocol.http.WebApplication;

@SuppressWarnings("deprecation")
public class WicketContextExecutorImpl implements IWicketContextExecutor {
	
	private final IWicketContextProvider contextProvider;
	
	public WicketContextExecutorImpl(IWicketContextProvider contextProvider) {
		super();
		this.contextProvider = contextProvider;
	}

	@Override
	public <T> T runWithContext(Callable<T> callable) throws Exception {
		return contextProvider.context().run(callable);
	}
	
	@Override
	public <T> T runWithContext(String applicationName, Callable<T> callable) throws Exception {
		return contextProvider.context(retrieveWebApplication(applicationName)).run(callable);
	}
	
	@Override
	public <T> T runWithContext(WebApplication application, Callable<T> callable) throws Exception {
		return contextProvider.context(application).run(callable);
	}

	@Override
	public <T> T runWithContext(Callable<T> callable, Locale locale) throws Exception {
		return contextProvider.context(locale).run(callable);
	}

	@Override
	public <T> T runWithContext(String applicationName, Callable<T> callable, Locale locale) throws Exception {
		return contextProvider.context(retrieveWebApplication(applicationName), locale).run(callable);
	}

	@Override
	public <T> T runWithContext(WebApplication application, Callable<T> callable, Locale locale) throws Exception {
		return contextProvider.context(application, locale).run(callable);
	}
	
	private WebApplication retrieveWebApplication(String applicationName) {
		WebApplication webApplication;
		if (WebApplication.exists()) {
			webApplication = WebApplication.get();
		} else {
			Application application = WebApplication.get(applicationName);
			if (!(application instanceof WebApplication)) {
				throw new IllegalStateException("Application is not a WebApplication");
			}
			webApplication = (WebApplication) application;
		}
		return webApplication;
	}

}
