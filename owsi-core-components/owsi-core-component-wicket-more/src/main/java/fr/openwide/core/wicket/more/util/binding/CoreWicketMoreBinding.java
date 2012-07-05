package fr.openwide.core.wicket.more.util.binding;

import fr.openwide.core.wicket.more.console.maintenance.ehcache.model.EhCacheCacheInformationBinding;

public class CoreWicketMoreBinding {

	private static final EhCacheCacheInformationBinding EH_CACHE_CACHE_INFORMATION = new EhCacheCacheInformationBinding();

	public static EhCacheCacheInformationBinding ehCacheCacheInformation() {
		return EH_CACHE_CACHE_INFORMATION;
	}

}
