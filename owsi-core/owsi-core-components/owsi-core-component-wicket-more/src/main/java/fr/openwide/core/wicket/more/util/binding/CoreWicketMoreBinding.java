package fr.openwide.core.wicket.more.util.binding;

import fr.openwide.core.jpa.more.business.task.model.QueuedTaskHolderBinding;
import fr.openwide.core.wicket.more.console.maintenance.ehcache.model.EhCacheCacheInformationBinding;

public final class CoreWicketMoreBinding {

	private static final EhCacheCacheInformationBinding EH_CACHE_CACHE_INFORMATION = new EhCacheCacheInformationBinding();
	
	private static final QueuedTaskHolderBinding QUEUED_TASK_HOLDER_BINDING = new QueuedTaskHolderBinding();

	public static EhCacheCacheInformationBinding ehCacheCacheInformation() {
		return EH_CACHE_CACHE_INFORMATION;
	}
	
	public static QueuedTaskHolderBinding queuedTaskHolderBinding() {
		return QUEUED_TASK_HOLDER_BINDING;
	}
	
	private CoreWicketMoreBinding() {
	}

}
