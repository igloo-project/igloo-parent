package igloo.cache.spring;

import com.google.common.collect.ImmutableList;
import igloo.cache.monitor.CacheManagerWrapper;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.cache.CacheManager;

/**
 * Bean that holds Spring {@link CacheManager} instances, wrapped by {@link CacheManagerWrapper}.
 *
 * <p>Bean are sorted by names.
 */
public class CacheManagerRegistry implements ICacheManagerRegistry {

  private final List<CacheManagerWrapper> cacheManagers;

  public CacheManagerRegistry(Map<String, CacheManager> cms) {
    List<CacheManagerWrapper> cacheManagers =
        cms.entrySet().stream()
            .map(e -> new CacheManagerWrapper(e.getKey(), e.getValue()))
            .collect(Collectors.toList());
    Collections.sort(cacheManagers, (a, b) -> a.getName().compareTo(b.getName()));
    this.cacheManagers = ImmutableList.copyOf(cacheManagers);
  }

  @Override
  public List<CacheManagerWrapper> getCacheManagers() {
    return cacheManagers;
  }
}
