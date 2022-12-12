package igloo.cache.binding;

import igloo.cache.monitor.ICacheWrapperBinding;

public class CacheBindings {

	private static final ICacheWrapperBinding CACHE_BINDING = new ICacheWrapperBinding();

	private CacheBindings() {}

	public static ICacheWrapperBinding cache() {
		return CACHE_BINDING;
	}
}
