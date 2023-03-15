package igloo.cache.spring;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.cache.CacheManager;

import com.google.common.collect.ImmutableList;

import igloo.cache.monitor.CacheManagerWrapper;

/**
 * <p>Bean that holds Spring {@link CacheManager} instances, wrapped by {@link CacheManagerWrapper}.</p>
 * 
 * <p>Bean are sorted by names.</p>
 */
public class CacheManagerRegistry implements ICacheManagerRegistry {

	private final List<CacheManagerWrapper> cacheManagers;

	public CacheManagerRegistry(Map<String, CacheManager> cms) {
		List<CacheManagerWrapper> cacheManagers = cms.entrySet().stream()
			.map(e -> new CacheManagerWrapper(e.getKey(), e.getValue())).collect(Collectors.toList());
		Collections.sort(cacheManagers, (a, b) -> a.getName().compareTo(b.getName()));
		this.cacheManagers = ImmutableList.copyOf(cacheManagers);
	}

	@Override
	public List<CacheManagerWrapper> getCacheManagers() {
		return cacheManagers;
	}
}
