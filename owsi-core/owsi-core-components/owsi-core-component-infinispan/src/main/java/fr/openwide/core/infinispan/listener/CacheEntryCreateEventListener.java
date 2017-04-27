package fr.openwide.core.infinispan.listener;

import org.infinispan.notifications.Listener;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryCreated;
import org.infinispan.notifications.cachelistener.event.CacheEntryCreatedEvent;
import org.infinispan.notifications.cachelistener.event.CacheEntryEvent;

@Listener(clustered = true, sync = false)
public abstract class CacheEntryCreateEventListener<V> {

	@CacheEntryCreated
	public void handleCacheEntry(CacheEntryEvent<String, V> cacheEvent) {
		if (cacheEvent instanceof CacheEntryCreatedEvent) {
			onAction(cacheEvent);
		}
	}

	public abstract void onAction(CacheEntryEvent<String, V> value);

}
