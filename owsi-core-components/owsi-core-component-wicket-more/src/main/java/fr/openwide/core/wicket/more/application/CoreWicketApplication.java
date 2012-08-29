package fr.openwide.core.wicket.more.application;

import java.util.Locale;
import java.util.Map;

import org.apache.wicket.Application;
import org.apache.wicket.Component;
import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.Session;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.AjaxRequestTarget.IJavaScriptResponse;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.IHeaderResponseDecorator;
import org.apache.wicket.markup.html.SecurePackageResourceGuard;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.request.resource.caching.FilenameWithVersionResourceCachingStrategy;
import org.apache.wicket.request.resource.caching.version.LastModifiedResourceVersion;
import org.apache.wicket.resource.NoOpTextCompressor;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.util.time.Duration;
import org.odlabs.wiquery.core.WiQuerySettings;
import org.odlabs.wiquery.ui.themes.IThemableApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import fr.openwide.core.spring.config.CoreConfigurer;
import fr.openwide.core.wicket.more.WicketMorePackage;
import fr.openwide.core.wicket.more.console.template.style.CoreConsoleCssScope;
import fr.openwide.core.wicket.more.core.CoreWiQueryDecoratingHeaderResponse;
import fr.openwide.core.wicket.more.lesscss.service.ILessCssService;
import fr.openwide.core.wicket.more.markup.html.template.AbstractWebPageTemplate;
import fr.openwide.core.wicket.more.markup.html.template.css.CoreCssScope;
import fr.openwide.core.wicket.more.markup.html.template.css.jqueryui.JQueryUiCssResourceReference;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.tipsy.TipsyHelper;
import fr.openwide.core.wicket.request.mapper.StaticResourceMapper;

public abstract class CoreWicketApplication extends WebApplication implements IThemableApplication {
	
	@Autowired
	private CoreConfigurer configurer;
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@Autowired
	protected ILessCssService lessCssService;
	
	private static final Duration DEFAULT_TIMEOUT = Duration.minutes(10);
	
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
		
		// Avec wicket 1.5, il faut ajouter les patterns sur les ressources qu'on souhaite rendre accessible
		// On ajoute globalement l'accès aux ressources less.
		((SecurePackageResourceGuard) getResourceSettings().getPackageResourceGuard()).addPattern("+*.less");
		
		// on autorise l'accès aux .htc pour CSS3PIE
		((SecurePackageResourceGuard) getResourceSettings().getPackageResourceGuard()).addPattern("+*.htc");
		
		// la compression se fait au build quand c'est nécessaire ; on n'utilise pas la compression wicket
		getResourceSettings().setJavaScriptCompressor(new NoOpTextCompressor());
		
		getRequestCycleSettings().setTimeout(DEFAULT_TIMEOUT);
		
		getMarkupSettings().setStripWicketTags(true);
		getResourceSettings().setCachingStrategy(new FilenameWithVersionResourceCachingStrategy(new LastModifiedResourceVersion()));
		
		getComponentInstantiationListeners().add(new SpringComponentInjector(this, applicationContext));
		
		getAjaxRequestTargetListeners().add(new TipsyRequestTargetListener());
		
		mountCommonResources();
		mountCommonPages();
		
		mountApplicationResources();
		mountApplicationPages();
		
		registerLessImportScopes();
	}
	
	@Override
	protected void validateInit() {
		super.validateInit();
		// minification que si on est en mode DEPLOYMENT
		WiQuerySettings.get().setMinifiedJavaScriptResources(RuntimeConfigurationType.DEPLOYMENT.equals(getConfigurationType()));
		
		// on ajoute fr.openwide.core.wicket.more aux packages ayant une groupingKey définie, de manière à les charger avant
		// les potentiels javascripts de l'application
		WiQuerySettings.get().getResourceGroupingKeys().add(WicketMorePackage.class.getPackage().getName());
		
		// on substitue notre decorator qui est un peu plus fin sur la gestion de l'ordre de chargement des ressources
		setHeaderResponseDecorator(new IHeaderResponseDecorator() {
			@Override
			public IHeaderResponse decorate(IHeaderResponse response) {
				return new CoreWiQueryDecoratingHeaderResponse(response);
			}
		});
	}
	
	protected void registerLessImportScopes() {
		lessCssService.registerImportScope("core", CoreCssScope.class);
		lessCssService.registerImportScope("core-console", CoreConsoleCssScope.class);
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
	
	public static class TipsyRequestTargetListener implements AjaxRequestTarget.IListener {

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
	
	@Override
	public ResourceReference getTheme(Session session) {
		return JQueryUiCssResourceReference.get();
	}

}