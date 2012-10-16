package fr.openwide.core.wicket.more.application;

import java.util.Locale;

import org.apache.wicket.Application;
import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.markup.html.SecurePackageResourceGuard;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.resource.caching.FilenameWithVersionResourceCachingStrategy;
import org.apache.wicket.request.resource.caching.version.LastModifiedResourceVersion;
import org.apache.wicket.resource.NoOpTextCompressor;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.util.time.Duration;
import org.odlabs.wiquery.ui.themes.WiQueryCoreThemeResourceReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import fr.openwide.core.spring.config.CoreConfigurer;
import fr.openwide.core.wicket.more.console.template.style.CoreConsoleCssScope;
import fr.openwide.core.wicket.more.lesscss.service.ILessCssService;
import fr.openwide.core.wicket.more.markup.html.template.AbstractWebPageTemplate;
import fr.openwide.core.wicket.more.markup.html.template.css.CoreCssScope;
import fr.openwide.core.wicket.more.markup.html.template.css.jqueryui.JQueryUiCssResourceReference;
import fr.openwide.core.wicket.more.markup.html.template.js.UpdatedJQueryResourceReference;
import fr.openwide.core.wicket.request.mapper.StaticResourceMapper;

public abstract class CoreWicketApplication extends WebApplication {
	
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
		
		// injection Spring
		getComponentInstantiationListeners().add(new SpringComponentInjector(this, applicationContext));
		
		// nettoyage des tags Wicket
		getMarkupSettings().setStripWicketTags(true);
		
		// mise en place d'un timeout plus élevé histoire d'éviter les timeouts lors des téléchargements
		getRequestCycleSettings().setTimeout(DEFAULT_TIMEOUT);
		
		// configuration des ressources
			// depuis Wicket 1.5, il faut ajouter les patterns sur les ressources qu'on souhaite rendre accessible
			// on ajoute globalement l'accès aux ressources less.
			((SecurePackageResourceGuard) getResourceSettings().getPackageResourceGuard()).addPattern("+*.less");
			
			// on autorise l'accès aux .htc pour CSS3PIE
			((SecurePackageResourceGuard) getResourceSettings().getPackageResourceGuard()).addPattern("+*.htc");
			
			// la compression se fait au build quand c'est nécessaire ; on n'utilise pas la compression Wicket
			getResourceSettings().setJavaScriptCompressor(new NoOpTextCompressor());
			// utilisation des ressources minifiées que si on est en mode DEPLOYMENT
			getResourceSettings().setUseMinifiedResources(RuntimeConfigurationType.DEPLOYMENT.equals(getConfigurationType()));
			
			// gestion du cache sur les ressources
			getResourceSettings().setCachingStrategy(new FilenameWithVersionResourceCachingStrategy(new LastModifiedResourceVersion()));
		
			// surcharge des ressources jQuery et jQuery UI
			getJavaScriptLibrarySettings().setJQueryReference(UpdatedJQueryResourceReference.get());
			addResourceReplacement(WiQueryCoreThemeResourceReference.get(), JQueryUiCssResourceReference.get());
		
		mountCommonResources();
		mountCommonPages();
		
		mountApplicationResources();
		mountApplicationPages();
		
		registerLessImportScopes();
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

}