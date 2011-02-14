package fr.openwide.core.wicket.more.application;

import org.apache.wicket.IRequestTarget;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.RequestParameters;
import org.apache.wicket.request.target.basic.URIRequestTargetUrlCodingStrategy;
import org.apache.wicket.request.target.resource.ResourceStreamRequestTarget;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.util.resource.PackageResourceStream;

import fr.openwide.core.wicket.more.markup.html.template.AbstractWebPageTemplate;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.tipsy.TipsyHelper;

public abstract class AbstractWicketApplication extends WebApplication {
	
	public AbstractWicketApplication() {
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
	
	private final void mountCommonPages() {
	}
	
	private final void mountCommonResources() {
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

}