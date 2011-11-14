package fr.openwide.core.wicket.more.application;

import org.apache.wicket.Application;
import org.apache.wicket.IRequestTarget;
import org.apache.wicket.Page;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.RequestParameters;
import org.apache.wicket.request.target.basic.URIRequestTargetUrlCodingStrategy;
import org.apache.wicket.request.target.resource.ResourceStreamRequestTarget;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.util.resource.PackageResourceStream;
import org.springframework.beans.factory.annotation.Autowired;

import fr.openwide.core.spring.config.CoreConfigurer;
import fr.openwide.core.wicket.more.markup.html.template.AbstractWebPageTemplate;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.tipsy.TipsyHelper;

public abstract class CoreWicketApplication extends WebApplication {
	
	@Autowired
	private CoreConfigurer configurer;
	
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
		getResourceSettings().setAddLastModifiedTimeToResourceReferenceUrl(true);
		
		addComponentInstantiationListener(new SpringComponentInjector(this));
		
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
		mount(new URIRequestTargetUrlCodingStrategy("/static" + path) {
			@Override
			public IRequestTarget decode(RequestParameters requestParameters) {
				final String uri = getURI(requestParameters);
				
				return new ResourceStreamRequestTarget(new PackageResourceStream(clazz, uri));
			}
		});
	}
	
	/**
	 * Fermeture automatique des tipsy sur tout retour Ajax.
	 */
	@Override
	public AjaxRequestTarget newAjaxRequestTarget(Page page) {
		AjaxRequestTarget target = super.newAjaxRequestTarget(page);
		target.appendJavascript(TipsyHelper.closeTipsyStatement().render().toString());
		return target;
	}

	/**
	 * Overriden to integrate configurationType via bean injection and
	 * properties file in place of web.xml or system properties definition
	 */
	@Override
	public String getConfigurationType() {
		return configurer.getConfigurationType();
	}

}