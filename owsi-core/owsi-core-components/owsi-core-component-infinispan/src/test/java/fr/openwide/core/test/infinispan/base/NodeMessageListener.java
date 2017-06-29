package fr.openwide.core.test.infinispan.base;

import java.io.Serializable;

import org.infinispan.notifications.Listener;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryCreated;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryModified;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryRemoved;
import org.infinispan.notifications.cachelistener.event.CacheEntryCreatedEvent;
import org.infinispan.notifications.cachelistener.event.CacheEntryEvent;
import org.infinispan.notifications.cachelistener.event.CacheEntryModifiedEvent;

import fr.openwide.core.infinispan.model.Message;

@Listener(clustered = true, includeCurrentState = true, sync = false)
public abstract class NodeMessageListener<V extends Serializable> {
	
	@CacheEntryCreated
	@CacheEntryRemoved
	@CacheEntryModified
	public void handleCacheEntry(CacheEntryEvent<String, Message<V>> cacheEvent) {
		if (cacheEvent instanceof CacheEntryCreatedEvent) {
			onMessage(cacheEvent.getValue());
		} else if (cacheEvent instanceof CacheEntryModifiedEvent) {
			onMessage(cacheEvent.getValue());
		}
	}

	public abstract void onMessage(Message<V> value);
}