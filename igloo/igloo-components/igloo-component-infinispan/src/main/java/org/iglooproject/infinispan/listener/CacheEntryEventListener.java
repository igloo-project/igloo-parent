package org.iglooproject.infinispan.listener;

import org.infinispan.notifications.Listener;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryCreated;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryModified;
import org.infinispan.notifications.cachelistener.event.CacheEntryEvent;

@Listener(clustered = true, sync = false)
public abstract class CacheEntryEventListener<V> {

	@CacheEntryCreated
	@CacheEntryModified
	public void handleCacheEntry(CacheEntryEvent<String, V> cacheEvent) {
		onAction(cacheEvent);
	}

	public abstract void onAction(CacheEntryEvent<String, V> value);

}
