package fr.openwide.core.infinispan.listener;

import org.infinispan.notifications.Listener;
import org.infinispan.notifications.cachemanagerlistener.annotation.ViewChanged;
import org.infinispan.notifications.cachemanagerlistener.event.ViewChangedEvent;

import fr.openwide.core.infinispan.service.IInfinispanClusterService;

@Listener(sync = false)
public class ViewChangedEventCoordinatorListener {

	private final IInfinispanClusterService infinispanClusterService;

	public ViewChangedEventCoordinatorListener(IInfinispanClusterService infinispanClusterService) {
		super();
		this.infinispanClusterService = infinispanClusterService;
	}

	@ViewChanged
	public void onViewChange(ViewChangedEvent viewChangedEvent) {
		if (viewChangedEvent.getCacheManager().isCoordinator()) {
			infinispanClusterService.onViewChangedEvent(viewChangedEvent);
		}
	}
}
