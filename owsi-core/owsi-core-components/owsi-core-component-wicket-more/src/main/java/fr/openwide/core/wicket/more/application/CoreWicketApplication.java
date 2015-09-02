package fr.openwide.core.wicket.more.application;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.search.BooleanQuery;
import org.apache.wicket.Application;
import org.apache.wicket.Page;
import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.markup.head.PriorityFirstComparator;
import org.apache.wicket.markup.html.SecurePackageResourceGuard;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.resource.caching.FilenameWithVersionResourceCachingStrategy;
import org.apache.wicket.request.resource.caching.version.LastModifiedResourceVersion;
import org.apache.wicket.resource.NoOpTextCompressor;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.util.lang.Bytes;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.time.Duration;
import org.odlabs.wiquery.ui.themes.WiQueryCoreThemeResourceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import fr.openwide.core.spring.config.CoreConfigurer;
import fr.openwide.core.spring.util.StringUtils;
import fr.openwide.core.wicket.more.console.template.style.CoreConsoleCssScope;
import fr.openwide.core.wicket.more.css.lesscss.LessCssResourceReference;
import fr.openwide.core.wicket.more.css.lesscss.service.ILessCssService;
import fr.openwide.core.wicket.more.link.descriptor.IPageLinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import fr.openwide.core.wicket.more.markup.html.template.AbstractWebPageTemplate;
import fr.openwide.core.wicket.more.markup.html.template.css.bootstrap2.CoreBootstrap2CssScope;
import fr.openwide.core.wicket.more.markup.html.template.css.bootstrap2.jqueryui.JQueryUiCssResourceReference;
import fr.openwide.core.wicket.more.markup.html.template.css.bootstrap3.CoreBootstrap3CssScope;
import fr.openwide.core.wicket.more.markup.html.template.css.bootstrap3.fontawesome.CoreFontAwesomeCssScope;
import fr.openwide.core.wicket.more.notification.listener.HtmlNotificationComponentCssClassHandler;
import fr.openwide.core.wicket.more.notification.markup.parser.MarkupFactoryWithHtmlNotificationSupport;
import fr.openwide.core.wicket.request.mapper.NoVersionMountedMapper;
import fr.openwide.core.wicket.request.mapper.PageParameterAwareMountedMapper;
import fr.openwide.core.wicket.request.mapper.StaticResourceMapper;
import fr.openwide.core.wicket.resource.JQueryUpdateResourceReference;

public abstract class CoreWicketApplication extends WebApplication {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CoreWicketApplication.class);
	
	@Autowired
	private CoreConfigurer configurer;
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@Autowired
	protected ILessCssService lessCssService;
	
	/**
	 * Déclaré au démarrage de l'application ; ne doit pas être modifié par la suite
	 */
	private Locale numberFormatLocale = Locale.FRENCH;
	
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
		
		// gestion des styles lors du rendu HTML des mails de notification
		getMarkupSettings().setMarkupFactory(new MarkupFactoryWithHtmlNotificationSupport()); // Handles tags that are not attached to wicket components
		getComponentInitializationListeners().add(new HtmlNotificationComponentCssClassHandler()); // Handles tags that *are* attached to wicket components
		
		// mise en place d'un timeout plus élevé histoire d'éviter les timeouts lors des téléchargements
		getRequestCycleSettings().setTimeout(DEFAULT_TIMEOUT);
		
		// configuration des ressources
		// depuis Wicket 1.5, il faut ajouter les patterns sur les ressources qu'on souhaite rendre accessible
		// on ajoute globalement l'accès aux ressources less et aux joyeusetés liés aux polices.
		SecurePackageResourceGuard packageResourceGuard = (SecurePackageResourceGuard) getResourceSettings().getPackageResourceGuard();
		packageResourceGuard.addPattern("+*.less");
		packageResourceGuard.addPattern("+*.woff");
		packageResourceGuard.addPattern("+*.eot");
		packageResourceGuard.addPattern("+*.svg");
		packageResourceGuard.addPattern("+*.ttf");
		packageResourceGuard.addPattern("+*.css.map");
		
		// on autorise l'accès aux .htc pour CSS3PIE
		packageResourceGuard.addPattern("+*.htc");
		
		// la compression se fait au build quand c'est nécessaire ; on n'utilise pas la compression Wicket
		getResourceSettings().setJavaScriptCompressor(new NoOpTextCompressor());
		// utilisation des ressources minifiées que si on est en mode DEPLOYMENT
		getResourceSettings().setUseMinifiedResources(RuntimeConfigurationType.DEPLOYMENT.equals(getConfigurationType()));
		
		// gestion du cache sur les ressources
		getResourceSettings().setCachingStrategy(new FilenameWithVersionResourceCachingStrategy(new LastModifiedResourceVersion()));
		
		// surcharge des ressources jQuery et jQuery UI
		addResourceReplacement(WiQueryCoreThemeResourceReference.get(), JQueryUiCssResourceReference.get());
		
		// on place les éléments présents dans le wicket:head en premier
		getResourceSettings().setHeaderItemComparator(new PriorityFirstComparator(true));
		
		// Update de jQuery : à commenter quand on se remet en ligne avec Wicket
		getJavaScriptLibrarySettings().setJQueryReference(JQueryUpdateResourceReference.get());
			
		// configuration du disk data store de Wicket
		getStoreSettings().setInmemoryCacheSize(configurer.getWicketDiskDataStoreInMemoryCacheSize());
		getStoreSettings().setMaxSizePerSession(Bytes.megabytes(configurer.getWicketDiskDataStoreMaxSizePerSession()));
		
		String wicketDiskDataStorePath = configurer.getWicketDiskDataStorePath();
		if (StringUtils.hasText(wicketDiskDataStorePath)) {
			try {
				File wicketDiskDataStoreFolder = new File(wicketDiskDataStorePath);
				FileUtils.forceMkdir(wicketDiskDataStoreFolder);
				
				getStoreSettings().setFileStoreFolder(wicketDiskDataStoreFolder);
			} catch (Exception e) {
				LOGGER.error(String.format("Unable to define a specific path (%1$s) for wicket data store. Using the default one.",
						wicketDiskDataStorePath), e);
			}
		}
		
		mountCommonResources();
		mountCommonPages();
		
		mountApplicationResources();
		mountApplicationPages();
		
		registerLessImportScopes();
		
		BooleanQuery.setMaxClauseCount(configurer.getLuceneBooleanQueryMaxClauseCount());
	}
	
	protected void registerLessImportScopes() {
		lessCssService.registerImportScope("core", CoreBootstrap2CssScope.class);
		lessCssService.registerImportScope("core-bs3", CoreBootstrap3CssScope.class);
		lessCssService.registerImportScope("core-console", CoreConsoleCssScope.class);
		lessCssService.registerImportScope("core-font-awesome", CoreFontAwesomeCssScope.class);
	}
	
	protected void mountCommonPages() {
	}
	
	protected void mountCommonResources() {
		mountStaticResourceDirectory("/common", AbstractWebPageTemplate.class);
		mountStaticResourceDirectory("/font-awesome", CoreFontAwesomeCssScope.class);
	}
	
	protected abstract void mountApplicationPages();
	
	protected abstract void mountApplicationResources();
	
	protected final void mountStaticResourceDirectory(final String path, final Class<?> clazz) {
		mount(new StaticResourceMapper("/static" + path, clazz));
	}
	
	public final <T extends Page> void mountUnversionedPage(final String path, final Class<T> pageClass) {
		mount(new NoVersionMountedMapper(path, pageClass));
	}
	
	public final <T extends Page> void mountParameterizedPage(final String path, final Class<T> pageClass) {
		mount(new PageParameterAwareMountedMapper(path, pageClass));
	}

	protected void preloadStyleSheets(LessCssResourceReference... lessCssResourcesReferences) {
		for (LessCssResourceReference resourceReference : lessCssResourcesReferences) {
			LOGGER.info("Preloading stylesheet '{}/{}'...", resourceReference.getScope().getName(),
					resourceReference.getName());
			IResourceStream resourceStream = null;
			try {
				// Just initialize the underlying cache, whatever the content is.
				resourceStream = resourceReference.getResource().getResourceStream();
			} finally {
				if (resourceStream != null) {
					try {
						resourceStream.close();
					} catch (IOException e) {
						LOGGER.error("Error when closing a stream while trying to preload stylesheets.", e);
					}
				}
			}
		}
	}

	/**
	 * Overriden to integrate configurationType via bean injection and
	 * properties file in place of web.xml or system properties definition
	 */
	@Override
	public RuntimeConfigurationType getConfigurationType() {
		return RuntimeConfigurationType.valueOf(configurer.getConfigurationType().toUpperCase(Locale.ROOT));
	}
	
	public final IPageLinkDescriptor getHomePageLinkDescriptor() {
		return new LinkDescriptorBuilder().page(getHomePage()).build();
	}

	public Locale getNumberFormatLocale() {
		return numberFormatLocale;
	}
	
	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

}