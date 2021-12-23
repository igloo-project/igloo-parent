package org.iglooproject.wicket.more.application;

import static org.iglooproject.spring.property.SpringPropertyIds.CONFIGURATION_TYPE;
import static org.iglooproject.wicket.more.property.WicketMorePropertyIds.WICKET_DISK_DATA_STORE_IN_MEMORY_CACHE_SIZE;
import static org.iglooproject.wicket.more.property.WicketMorePropertyIds.WICKET_DISK_DATA_STORE_MAX_SIZE_PER_SESSION;
import static org.iglooproject.wicket.more.property.WicketMorePropertyIds.WICKET_DISK_DATA_STORE_PATH;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

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
import org.apache.wicket.resource.NoOpTextCompressor;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.util.lang.Bytes;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.time.Duration;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.spring.util.StringUtils;
import org.iglooproject.wicket.more.css.scss.service.ICachedScssService;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import org.iglooproject.wicket.more.markup.head.CoreHeaderItemComparator;
import org.iglooproject.wicket.request.mapper.NoVersionMountedMapper;
import org.iglooproject.wicket.request.mapper.PageParameterAwareMountedMapper;
import org.iglooproject.wicket.request.mapper.StaticResourceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;

import de.agilecoders.wicket.webjars.WicketWebjars;
import de.agilecoders.wicket.webjars.settings.WebjarsSettings;

public abstract class CoreWicketApplication extends WebApplication {
	
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
		
		// handle webjars
		Stopwatch watch = Stopwatch.createStarted();
		WebjarsSettings settings = new WebjarsSettings();
		WicketWebjars.install(this, settings);
		watch.stop();
		LOGGER.info("Webjars installed in {} ms.", watch.elapsed().toMillis());
		
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
		
		// la compression se fait au build quand c'est nécessaire ; on n'utilise pas la compression Wicket
		getResourceSettings().setJavaScriptCompressor(new NoOpTextCompressor());
		// utilisation des ressources minifiées que si on est en mode DEPLOYMENT
		getResourceSettings().setUseMinifiedResources(RuntimeConfigurationType.DEPLOYMENT.equals(getConfigurationType()));
		
		// gestion du cache sur les ressources
		getResourceSettings().setCachingStrategy(new FilenameWithVersionResourceCachingStrategy(new LastModifiedResourceVersion()));
		
		// Custom comparator to have a specific order for some header items, fallback to default comparator.
		// Global final order: specific order > priority > page > component.
		getResourceSettings().setHeaderItemComparator(CoreHeaderItemComparator.get());
		
		// configuration du disk data store de Wicket
		getStoreSettings().setInmemoryCacheSize(propertyService.get(WICKET_DISK_DATA_STORE_IN_MEMORY_CACHE_SIZE));
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
	
	protected void updateJavaScriptLibrarySettings() {
		modules.stream()
			.forEach(module -> module.updateJavaScriptLibrarySettings(getJavaScriptLibrarySettings()));
	}
	
	protected void updateSelect2ApplicationSettings() {
		modules.stream()
			.forEach(module -> module.updateSelect2ApplicationSettings(org.wicketstuff.select2.ApplicationSettings.get()));
	}
	
	protected void addResourceReplacements() {
		modules.stream()
			.forEach(module -> module.addResourceReplacements(this));
	}
	
	protected void updateResourceBundles() {
		modules.stream()
			.forEach(module -> module.updateResourceBundles(getResourceBundles()));
	}
	
	protected void mountCommonResources() {
		modules.stream()
			.forEach(module -> {
				for (StaticResourceMapper mapper : module.listStaticResources()) {
					mount(mapper);
				}
			});
	}
	
	protected void mountCommonPages() {
	}
	
	protected void registerImportScopes() {
		modules.stream()
			.forEach(module -> module.registerImportScopes());
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
	
	public final IPageLinkDescriptor getHomePageLinkDescriptor() {
		return LinkDescriptorBuilder.start().page(getHomePage());
	}

	public Locale getNumberFormatLocale() {
		return numberFormatLocale;
	}
	
	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

}
