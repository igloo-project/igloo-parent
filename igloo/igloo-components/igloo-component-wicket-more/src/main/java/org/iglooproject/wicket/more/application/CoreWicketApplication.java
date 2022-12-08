package org.iglooproject.wicket.more.application;

import static org.iglooproject.spring.property.SpringPropertyIds.CONFIGURATION_TYPE;
import static org.iglooproject.wicket.more.property.WicketMorePropertyIds.WICKET_DISK_DATA_STORE_MAX_SIZE_PER_SESSION;
import static org.iglooproject.wicket.more.property.WicketMorePropertyIds.WICKET_DISK_DATA_STORE_PATH;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.wicket.Application;
import org.apache.wicket.Page;
import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.markup.html.SecurePackageResourceGuard;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.resource.PackageResource;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.request.resource.caching.FilenameWithVersionResourceCachingStrategy;
import org.apache.wicket.request.resource.caching.version.LastModifiedResourceVersion;
import org.apache.wicket.resource.JQueryResourceReference;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.util.lang.Bytes;
import org.apache.wicket.util.resource.IResourceStream;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.spring.util.StringUtils;
import org.iglooproject.wicket.more.css.scss.service.ICachedScssService;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import org.iglooproject.wicket.more.markup.head.CoreHeaderItemComparator;
import org.iglooproject.wicket.more.markup.html.template.AbstractWebPageTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;

import de.agilecoders.wicket.webjars.WicketWebjars;
import de.agilecoders.wicket.webjars.settings.WebjarsSettings;
import igloo.bootstrap.BootstrapSettings;
import igloo.bootstrap.BootstrapVersion;
import igloo.bootstrap.IBootstrapApplication;
import igloo.bootstrap.IBootstrapProvider;
import igloo.fontawesome.CoreFontAwesomeCssScope;
import igloo.jqueryui.JQueryUIJavaScriptResourceReference;
import igloo.jqueryui.JQueryUiCssResourceReference;
import igloo.select2.Select2JavaScriptResourceReference;
import igloo.select2.Select2MoreJavaScriptResourceReference;
import igloo.wicket.application.ICoreApplication;
import igloo.wicket.request.mapper.NoVersionMountedMapper;
import igloo.wicket.request.mapper.PageParameterAwareMountedMapper;
import igloo.wicket.request.mapper.StaticResourceMapper;

public abstract class CoreWicketApplication extends WebApplication implements ICoreApplication, IBootstrapApplication {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CoreWicketApplication.class);
	
	@Autowired
	private IPropertyService propertyService;
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@Autowired
	protected ICachedScssService scssService;
	
	/**
	 * Initialized with an empty list to circumvent {@link NullPointerException} when no autowirable bean are available.
	 */
	@Autowired(required = false)
	protected List<IWicketModule> modules = Lists.newArrayList();

	private BootstrapSettings bootstrapSettings;

	/**
	 * Déclaré au démarrage de l'application ; ne doit pas être modifié par la suite
	 */
	private Locale numberFormatLocale = Locale.FRENCH;
	
	private static final Duration DEFAULT_TIMEOUT = Duration.ofMinutes(10);
	
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
		initBootstrap(applicationContext);
		
		// handle webjars
		Stopwatch watch = Stopwatch.createStarted();
		WebjarsSettings settings = new WebjarsSettings();
		WicketWebjars.install(this, settings);
		watch.stop();
		LOGGER.info("Webjars installed in {} ms.", watch.elapsed().toMillis());
		
		// disable CSP
		getCspSettings().blocking().disabled();
		
		// injection Spring
		getComponentInstantiationListeners().add(new SpringComponentInjector(this, applicationContext));
		
		// nettoyage des tags Wicket
		getMarkupSettings().setStripWicketTags(true);
		
		// mise en place d'un timeout plus élevé histoire d'éviter les timeouts lors des téléchargements
		getRequestCycleSettings().setTimeout(DEFAULT_TIMEOUT);
		
		// configuration des ressources
		SecurePackageResourceGuard packageResourceGuard = (SecurePackageResourceGuard) getResourceSettings().getPackageResourceGuard();
		packageResourceGuard.addPattern("+*.less");
		packageResourceGuard.addPattern("+*.scss");
		packageResourceGuard.addPattern("+*.json");
		packageResourceGuard.addPattern("+*.webmanifest");
		
		// utilisation des ressources minifiées que si on est en mode DEPLOYMENT
		getResourceSettings().setUseMinifiedResources(RuntimeConfigurationType.DEPLOYMENT.equals(getConfigurationType()));
		
		// gestion du cache sur les ressources
		getResourceSettings().setCachingStrategy(new FilenameWithVersionResourceCachingStrategy(new LastModifiedResourceVersion()));
		
		// Custom comparator to have a specific order for some header items, fallback to default comparator.
		// Global final order: specific order > priority > page > component.
		getResourceSettings().setHeaderItemComparator(CoreHeaderItemComparator.get());
		
		// configuration du disk data store de Wicket
		getStoreSettings().setMaxSizePerSession(Bytes.megabytes(propertyService.get(WICKET_DISK_DATA_STORE_MAX_SIZE_PER_SESSION)));
		
		String wicketDiskDataStorePath = propertyService.get(WICKET_DISK_DATA_STORE_PATH);
		if (StringUtils.hasText(wicketDiskDataStorePath)) {
			try {
				File wicketDiskDataStoreFolder = new File(wicketDiskDataStorePath);
				FileUtils.forceMkdir(wicketDiskDataStoreFolder);
				
				getStoreSettings().setFileStoreFolder(wicketDiskDataStoreFolder);
			} catch (RuntimeException | IOException e) {
				LOGGER.error(String.format("Unable to define a specific path (%1$s) for wicket data store. Using the default one.",
						wicketDiskDataStorePath), e);
			}
		}
		
		updateJavaScriptLibrarySettings();
		
		updateSelect2ApplicationSettings();
		
		addResourceReplacements();
		
		updateResourceBundles();
		
		mountCommonResources();
		mountCommonPages();
		
		mountApplicationResources();
		mountApplicationPages();
		
		registerImportScopes();
		
		updateResourceSettings();
	}

	private void initBootstrap(ApplicationContext applicationContext) {
		Map<String, IBootstrapProvider> providers = applicationContext.getBeansOfType(IBootstrapProvider.class);
		IBootstrapProvider bootstrap4Provider = providers.values().stream().filter(p -> BootstrapVersion.BOOTSTRAP_4.equals(p.getVersion())).findFirst().orElse(null);
		IBootstrapProvider bootstrap5Provider = providers.values().stream().filter(p -> BootstrapVersion.BOOTSTRAP_5.equals(p.getVersion())).findFirst().orElse(null);
		bootstrapSettings = new BootstrapSettings(BootstrapVersion.BOOTSTRAP_5, bootstrap4Provider, bootstrap5Provider);
	}

	protected void updateJavaScriptLibrarySettings() {
		getJavaScriptLibrarySettings().setJQueryReference(JQueryResourceReference.getV3());
	}
	
	protected void updateSelect2ApplicationSettings() {
		// Don't include css files from wicketstuff-select2.
		// We take care of Select2 css file and Select2 Bootstrap scss files on our side.
		// We also override select2 js file to choose specific version and also fix stuff.
		org.wicketstuff.select2.ApplicationSettings.get()
			.setIncludeCss(false)
			.setJavascriptReferenceFull(Select2JavaScriptResourceReference.get());
	}
	
	protected void addResourceReplacements() {
		addResourceReplacement(org.wicketstuff.wiquery.ui.JQueryUIJavaScriptResourceReference.get(), JQueryUIJavaScriptResourceReference.get());
		addResourceReplacement(org.wicketstuff.wiquery.ui.themes.WiQueryCoreThemeResourceReference.get(), JQueryUiCssResourceReference.get());
	}
	
	protected void updateResourceBundles() {
		getResourceBundles().addJavaScriptBundle(getClass(), "select2-bundle.js",
			Select2JavaScriptResourceReference.get(),
			Select2MoreJavaScriptResourceReference.get()
		);
		modules.stream()
			.forEach(module -> module.updateResourceBundles(getResourceBundles()));
	}
	
	protected void mountCommonResources() {
		mount(staticResourceMapper("/common", AbstractWebPageTemplate.class));
		mount(staticResourceMapper("/font-awesome", CoreFontAwesomeCssScope.class));
	}
	
	private StaticResourceMapper staticResourceMapper(final String path, final Class<?> clazz) {
		return new StaticResourceMapper("/static" + path, clazz);
	}
	
	protected void mountCommonPages() {
	}
	
	protected void registerImportScopes() {
		scssService.registerImportScope("core-fa", CoreFontAwesomeCssScope.class);
		modules.stream()
			.forEach(module -> module.registerImportScopes(scssService));
	}
	
	protected void updateResourceSettings() {
		modules.stream()
			.forEach(module -> module.updateResourceSettings(getResourceSettings()));
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

	protected void preloadStyleSheets(ResourceReference... resourcesReferences) {
		for (ResourceReference resourceReference : resourcesReferences) {
			LOGGER.info("Preloading stylesheet '{}/{}'...", resourceReference.getScope().getName(),
					resourceReference.getName());
			IResourceStream resourceStream = null;
			try {
				// Just initialize the underlying cache, whatever the content is.
				resourceStream = ((PackageResource) resourceReference.getResource()).getResourceStream();
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
		return RuntimeConfigurationType.valueOf(propertyService.get(CONFIGURATION_TYPE).toUpperCase(Locale.ROOT));
	}

	@Override
	public BootstrapSettings getBootstrapSettings() {
		return bootstrapSettings;
	}
	
	public final IPageLinkDescriptor getHomePageLinkDescriptor() {
		return LinkDescriptorBuilder.start().page(getHomePage());
	}

	@Override
	public Locale getNumberFormatLocale() {
		return numberFormatLocale;
	}
	
	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

}
