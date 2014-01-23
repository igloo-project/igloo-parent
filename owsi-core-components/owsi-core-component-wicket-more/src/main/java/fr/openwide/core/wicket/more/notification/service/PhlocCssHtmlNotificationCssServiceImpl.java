package fr.openwide.core.wicket.more.notification.service;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Map;

import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;
import org.apache.wicket.util.time.Time;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.phloc.commons.io.IInputStreamProvider;
import com.phloc.css.ECSSVersion;
import com.phloc.css.decl.CascadingStyleSheet;
import com.phloc.css.reader.CSSReader;

import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.wicket.more.lesscss.LessCssResourceReference;
import fr.openwide.core.wicket.more.notification.service.impl.SimplePhlocCssHtmlNotificationCssRegistry;

public class PhlocCssHtmlNotificationCssServiceImpl implements IHtmlNotificationCssService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PhlocCssHtmlNotificationCssServiceImpl.class);
	
	private final Map<LessCssResourceReference, Pair<IHtmlNotificationCssRegistry, Time>> registryCache = Maps.newHashMap();
	
	private final Map<String, LessCssResourceReference> registrySpecs = Maps.newHashMap();

	@Override
	public synchronized boolean hasRegistry(String componentVariation) {
		return registrySpecs.containsKey(componentVariation);
	}
	
	@Override
	public synchronized IHtmlNotificationCssRegistry getRegistry(String componentVariation) throws ServiceException {
		LessCssResourceReference cssResourceReference = registrySpecs.get(componentVariation);
		if (cssResourceReference == null) {
			return null;
		}
		
		return getRegistry(cssResourceReference);
	}

	private synchronized IHtmlNotificationCssRegistry getRegistry(LessCssResourceReference cssResourceReference) throws ServiceException {
		IResourceStream resourceStream = cssResourceReference.getResource().getResourceStream();
		if (resourceStream == null) {
			throw new ServiceException("Could not retrieve resource stream for resource reference " + cssResourceReference + " when accessing a notification CSS style registry");
		}
		
		Time currentResourceLastModifiedTime = resourceStream.lastModifiedTime();
		Pair<IHtmlNotificationCssRegistry, Time> cacheEntry = registryCache.get(cssResourceReference);
		if (cacheEntry != null && cacheEntry.getRight().equals(currentResourceLastModifiedTime)) {
			return cacheEntry.getLeft();
		} else {
			IHtmlNotificationCssRegistry registry = createRegistry(resourceStream);
			registryCache.put(cssResourceReference, Pair.of(registry, currentResourceLastModifiedTime));
			return registry;
		}
	}
	
	@Override
	public synchronized void registerStyles(String componentVariation, LessCssResourceReference cssResourceReference) throws ServiceException {
		if (registrySpecs.containsKey(componentVariation)) {
			LOGGER.warn("Overrding Html notification style registry for component variation " + componentVariation);
		}
		registrySpecs.put(componentVariation, cssResourceReference);
		registryCache.remove(componentVariation);
	}
	
	private IHtmlNotificationCssRegistry createRegistry(IResourceStream resourceStream) throws ServiceException {
		CascadingStyleSheet sheet = CSSReader.readFromStream(new WicketResourceStreamToPhlocInputStreamProviderWrapper(resourceStream),
				Charset.defaultCharset(), ECSSVersion.CSS30);
		
		if (sheet == null) {
			throw new ServiceException("An error occurred while parsing notification CSS ; see the logs for details.");
		} else {
			return new SimplePhlocCssHtmlNotificationCssRegistry(sheet);
		}
	}
	
	private static class WicketResourceStreamToPhlocInputStreamProviderWrapper implements IInputStreamProvider {
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
						resourceStream.close(); // The wicket way : close the resource stream, not the input stream
					}
				};
			} catch (ResourceStreamNotFoundException e) {
				LOGGER.error("Error while getting a resource for CSS parsing", e);
				return null; // The phloc way : return null, do not throw exceptions
			}
		}
	}

}
