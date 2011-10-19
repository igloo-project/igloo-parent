package fr.openwide.core.jpa.security.acl.util;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AclCacheContainer<K, V> {

	private static final Log LOGGER = LogFactory.getLog(AclCacheContainer.class);
	
	private final AclCacheRegion cacheRegion;

	private final Ehcache cache;
	
	public AclCacheContainer(final CacheManager cacheManager, final AclCacheRegion cacheRegion) {
		this(cacheManager, cacheRegion, true);
	}

	public AclCacheContainer(final CacheManager cacheManager, final AclCacheRegion cacheRegion, boolean cacheEnabled) {
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
				
				return (V) element.getValue();
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