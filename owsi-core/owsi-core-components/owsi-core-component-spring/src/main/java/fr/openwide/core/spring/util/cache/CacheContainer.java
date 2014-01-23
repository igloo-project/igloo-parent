package fr.openwide.core.spring.util.cache;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheContainer<K, V> {

	private static final Logger LOGGER = LoggerFactory.getLogger(CacheContainer.class);
	
	private final ICacheRegion cacheRegion;

	private final Ehcache cache;
	
	public CacheContainer(final CacheManager cacheManager, final ICacheRegion cacheRegion) {
		this(cacheManager, cacheRegion, true);
	}

	public CacheContainer(final CacheManager cacheManager, final ICacheRegion cacheRegion, boolean cacheEnabled) {
		if (cacheEnabled) {
			cache = cacheManager.getEhcache(cacheRegion.getName());
			
			if (cache == null) {
				LOGGER.warn("No cache configured for name " + cacheRegion.getName() + ": cache disabled.");
			}
		} else {
			cache = null;
			
			LOGGER.warn("Cache " + cacheRegion.getName() + " is disabled.");
		}
		
		this.cacheRegion = cacheRegion;
	}

	public void put(final K key, final V value) {
		if (cache != null) {
			cache.put(new Element(key, value));
		}
	}

	@SuppressWarnings("unchecked")
	public V get(final K key) {
		if (cache != null) {
			Element element = cache.get(key);
			if (element != null) {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Cache hit for cache " + cacheRegion.getName() + " and key " + key.toString());
				}
				
				return (V) element.getObjectValue();
			}
		}
		return null;
	}

	public void remove(final K key) {
		if (cache != null) {
			cache.remove(key);
		}
	}

	public void removeAll() {
		if (cache != null) {
			cache.removeAll();
		}
	}

}