package fr.openwide.core.wicket.more.application;

import java.util.Locale;
import java.util.Map;

import org.apache.wicket.Application;
import org.apache.wicket.Component;
import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.AjaxRequestTarget.IJavaScriptResponse;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.resource.caching.FilenameWithVersionResourceCachingStrategy;
import org.apache.wicket.request.resource.caching.version.LastModifiedResourceVersion;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import fr.openwide.core.spring.config.CoreConfigurer;
import fr.openwide.core.wicket.more.markup.html.template.AbstractWebPageTemplate;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.tipsy.TipsyHelper;
import fr.openwide.core.wicket.request.mapper.StaticResourceMapper;

public abstract class CoreWicketApplication extends WebApplication {
	
	@Autowired
	private CoreConfigurer configurer;
	
	@Autowired
	private ApplicationContext applicationContext;
	
	public static CoreWicketApplication get() {
		final Application application = Application.get();
		if (application instanceof CoreWicketApplication) {
			return (CoreWicketApplication) application;
		}
		throw new WicketRuntimeException("There is no CoreWicketApplication attached to current thread " +
				Thread.currentThread().getName());
	}
	
	public CoreWicketApplication() {
		super();
	}
	
	@Override
	public void init() {
		super.init();

		getMarkupSettings().setStripWicketTags(true);
		getResourceSettings().setCachingStrategy(new FilenameWithVersionResourceCachingStrategy(new LastModifiedResourceVersion()));
		
		getComponentInstantiationListeners().add(new SpringComponentInjector(this, applicationContext));
		
		getAjaxRequestTargetListeners().add(new TipsyRequestTargetListener());
		
		mountCommonResources();
		mountCommonPages();
		
		mountApplicationResources();
		mountApplicationPages();
	}
	
	protected void mountCommonPages() {
	}
	
	protected void mountCommonResources() {
		mountStaticResourceDirectory("/common", AbstractWebPageTemplate.class);
	}
	
	protected abstract void mountApplicationPages();
	
	protected abstract void mountApplicationResources();
	
	protected final void mountStaticResourceDirectory(final String path, final Class<?> clazz) {
		mount(new StaticResourceMapper("/static" + path, clazz));
	}

	/**
	 * Overriden to integrate configurationType via bean injection and
	 * properties file in place of web.xml or system properties definition
	 */
	@Override
	public RuntimeConfigurationType getConfigurationType() {
		return RuntimeConfigurationType.valueOf(configurer.getConfigurationType().toUpperCase(Locale.ROOT));
	}
	
	public class TipsyRequestTargetListener implements AjaxRequestTarget.IListener {

		@Override
		public void onBeforeRespond(Map<String, Component> map,
				AjaxRequestTarget target) {
		}

		@Override
		public void onAfterRespond(Map<String, Component> map,
				IJavaScriptResponse response) {
			response.addJavaScript(TipsyHelper.closeTipsyStatement().render().toString());
		}
		
	}

}