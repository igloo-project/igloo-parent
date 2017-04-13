package fr.openwide.core.infinispan.listener;

import org.infinispan.notifications.Listener;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryCreated;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryModified;
import org.infinispan.notifications.cachelistener.event.CacheEntryCreatedEvent;
import org.infinispan.notifications.cachelistener.event.CacheEntryEvent;
import org.infinispan.notifications.cachelistener.event.CacheEntryModifiedEvent;

@Listener(clustered = true, sync = false)
public abstract class CacheEntryEventListener<V> {

	@CacheEntryCreated
	@CacheEntryModified
	public void handleCacheEntry(CacheEntryEvent<String, V> cacheEvent) {
		if (cacheEvent instanceof CacheEntryCreatedEvent) {
			onAction(cacheEvent);
		} else if (cacheEvent instanceof CacheEntryModifiedEvent) {
			onAction(cacheEvent);
		}
	}

	public abstract void onAction(CacheEntryEvent<String, V> value);

}
