package org.iglooproject.wicket.more.notification.service;

import com.google.common.collect.Maps;
import com.helger.base.io.iface.IHasInputStream;
import com.helger.css.decl.CascadingStyleSheet;
import com.helger.css.reader.CSSReader;
import jakarta.annotation.Nullable;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Map;
import java.util.function.Function;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.wicket.more.css.ICssResourceReference;
import org.iglooproject.wicket.more.notification.service.impl.SimplePhlocCssHtmlNotificationCssRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Deprecated
public class PhlocCssHtmlNotificationCssServiceImpl implements IHtmlNotificationCssService {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(PhlocCssHtmlNotificationCssServiceImpl.class);

  private static final String DEFAULT_VARIATION = "##DEFAULT##";

  private final Map<ICssResourceReference, Triple<IHtmlNotificationCssRegistry, String, Instant>>
      registryCache = Maps.newHashMap();

  private final Map<String, ICssResourceReference> registrySpecs = Maps.newHashMap();

  /** Return queried styles or default ones. Return null if no defaults are registered. */
  @Override
  public synchronized IHtmlNotificationCssRegistry getRegistry(String componentVariation)
      throws ServiceException {
    return getEntry(componentVariation, Pair<String, IHtmlNotificationCssRegistry>::getRight);
  }

  /** Return raw CSS. Return null if no defaults are registered. */
  @Override
  public synchronized String getCss(String componentVariation) throws ServiceException {
    return getEntry(componentVariation, Pair<String, IHtmlNotificationCssRegistry>::getLeft);
  }

  private synchronized <T> T getEntry(
      String componentVariation, Function<Pair<String, IHtmlNotificationCssRegistry>, T> mapper)
      throws ServiceException {
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
      return mapper.apply(getRegistry(cssResourceReference));
    }
  }

  private synchronized Pair<String, IHtmlNotificationCssRegistry> getRegistry(
      ICssResourceReference cssResourceReference) throws ServiceException {
    IResourceStream resourceStream = cssResourceReference.getResource().getResourceStream();
    if (resourceStream == null) { // NOSONAR findbugs:RCN_REDUNDANT_NULLCHECK_OF_NONNULL_VALUE
      throw new ServiceException(
          "Could not retrieve resource stream for resource reference "
              + cssResourceReference
              + " when accessing a notification CSS style registry");
    }

    Instant currentResourceLastModifiedTime = resourceStream.lastModifiedTime();
    Triple<IHtmlNotificationCssRegistry, String, Instant> cacheEntry =
        registryCache.get(cssResourceReference);
    if (cacheEntry != null && cacheEntry.getRight().equals(currentResourceLastModifiedTime)) {
      return Pair.of(cacheEntry.getMiddle(), cacheEntry.getLeft());
    } else {
      Pair<String, IHtmlNotificationCssRegistry> registry = createRegistry(resourceStream);
      registryCache.put(
          cssResourceReference,
          Triple.of(registry.getRight(), registry.getLeft(), currentResourceLastModifiedTime));
      return registry;
    }
  }

  /** Register default styles; this style will be used if queried variation does not exist. */
  @Override
  public synchronized void registerDefaultStyles(ICssResourceReference cssResourceReference) {
    registerStyles(DEFAULT_VARIATION, cssResourceReference);
  }

  @Override
  public synchronized void registerStyles(
      String componentVariation, ICssResourceReference cssResourceReference) {
    if (registrySpecs.containsKey(componentVariation)) {
      LOGGER.warn(
          "Overrding Html notification style registry for component variation "
              + componentVariation);
    }
    registrySpecs.put(componentVariation, cssResourceReference);
    registryCache.remove(cssResourceReference);
  }

  private Pair<String, IHtmlNotificationCssRegistry> createRegistry(IResourceStream resourceStream)
      throws ServiceException {
    try (InputStream is =
            new WicketResourceStreamToPhlocInputStreamProviderWrapper(resourceStream)
                .getInputStream();
        Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
      String css = IOUtils.toString(reader);
      CascadingStyleSheet sheet = CSSReader.readFromString(css);

      if (sheet == null) {
        throw new ServiceException(
            "An error occurred while parsing notification CSS; see the logs for details.");
      } else {
        return Pair.of(css, new SimplePhlocCssHtmlNotificationCssRegistry(sheet));
      }
    } catch (IOException e) {
      throw new ServiceException(String.format("Error extracting css %s", resourceStream));
    }
  }

  private static class WicketResourceStreamToPhlocInputStreamProviderWrapper
      implements IHasInputStream {
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
            resourceStream
                .close(); // The wicket way: close the resource stream, not the input stream
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
