package org.iglooproject.wicket.more.notification.service;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.time.Instant;
import java.util.Map;

import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.wicket.more.css.ICssResourceReference;
import org.iglooproject.wicket.more.notification.service.impl.SimplePhlocCssHtmlNotificationCssRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.helger.commons.io.IHasInputStream;
import com.helger.css.ECSSVersion;
import com.helger.css.decl.CascadingStyleSheet;
import com.helger.css.reader.CSSReader;

public class PhlocCssHtmlNotificationCssServiceImpl implements IHtmlNotificationCssService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PhlocCssHtmlNotificationCssServiceImpl.class);
	
	private static final String DEFAULT_VARIATION = "##DEFAULT##";
	
	private final Map<ICssResourceReference, Pair<IHtmlNotificationCssRegistry, Instant>> registryCache = Maps.newHashMap();
	
	private final Map<String, ICssResourceReference> registrySpecs = Maps.newHashMap();
	
	/**
	 * Return queried styles or default ones. Return null if no defaults are registered.
	 */
	@Override
	public synchronized IHtmlNotificationCssRegistry getRegistry(String componentVariation) throws ServiceException {
		final ICssResourceReference cssResourceReference;
		if (componentVariation != null && registrySpecs.containsKey(componentVariation)) {
			cssResourceReference = registrySpecs.get(componentVariation);
		} else if (registrySpecs.containsKey(DEFAULT_VARIATION)) {
			cssResourceReference = registrySpecs.get(DEFAULT_VARIATION);
		} else {
			cssResourceReference = null;
		}
		if (cssResourceReference == null) {
			return null;
		} else {
			return getRegistry(cssResourceReference);
		}
	}

	private synchronized IHtmlNotificationCssRegistry getRegistry(ICssResourceReference cssResourceReference) throws ServiceException {
		IResourceStream resourceStream = cssResourceReference.getResource().getResourceStream();
		if (resourceStream == null) { // NOSONAR findbugs:RCN_REDUNDANT_NULLCHECK_OF_NONNULL_VALUE
			throw new ServiceException("Could not retrieve resource stream for resource reference " + cssResourceReference + " when accessing a notification CSS style registry");
		}
		
		Instant currentResourceLastModifiedTime = resourceStream.lastModifiedTime();
		Pair<IHtmlNotificationCssRegistry, Instant> cacheEntry = registryCache.get(cssResourceReference);
		if (cacheEntry != null && cacheEntry.getRight().equals(currentResourceLastModifiedTime)) {
			return cacheEntry.getLeft();
		} else {
			IHtmlNotificationCssRegistry registry = createRegistry(resourceStream);
			registryCache.put(cssResourceReference, Pair.of(registry, currentResourceLastModifiedTime));
			return registry;
		}
	}
	
	/**
	 * Register default styles; this style will be used if queried variation does not exist.
	 */
	@Override
	public synchronized void registerDefaultStyles(ICssResourceReference cssResourceReference) {
		registerStyles(DEFAULT_VARIATION, cssResourceReference);
	}
	
	@Override
	public synchronized void registerStyles(String componentVariation, ICssResourceReference cssResourceReference) {
		if (registrySpecs.containsKey(componentVariation)) {
			LOGGER.warn("Overrding Html notification style registry for component variation " + componentVariation);
		}
		registrySpecs.put(componentVariation, cssResourceReference);
		registryCache.remove(cssResourceReference);
	}
	
	private IHtmlNotificationCssRegistry createRegistry(IResourceStream resourceStream) throws ServiceException {
		CascadingStyleSheet sheet = CSSReader.readFromStream(new WicketResourceStreamToPhlocInputStreamProviderWrapper(resourceStream),
				Charset.defaultCharset(), ECSSVersion.CSS30);
		
		if (sheet == null) {
			throw new ServiceException("An error occurred while parsing notification CSS; see the logs for details.");
		} else {
			return new SimplePhlocCssHtmlNotificationCssRegistry(sheet);
		}
	}
	
	private static class WicketResourceStreamToPhlocInputStreamProviderWrapper implements IHasInputStream {
		private final IResourceStream resourceStream;
		
		public WicketResourceStreamToPhlocInputStreamProviderWrapper(IResourceStream resourceStream) {
			super();
			this.resourceStream = resourceStream;
		}
		
		@Override
		@Nullable
		public InputStream getInputStream() {
			try {
				return new FilterInputStream(resourceStream.getInputStream()) {
					@Override
					public void close() throws IOException {
						resourceStream.close(); // The wicket way: close the resource stream, not the input stream
					}
				};
			} catch (ResourceStreamNotFoundException e) {
				LOGGER.error("Error while getting a resource for CSS parsing", e);
				return null; // The phloc way: return null, do not throw exceptions
			}
		}

		@Override
		public boolean isReadMultiple() {
			return false;
		}
	}

}
