package fr.openwide.core.wicket.more.util.binding;

import fr.openwide.core.wicket.more.console.maintenance.ehcache.model.EhCacheCacheInformationBinding;
import fr.openwide.core.wicket.more.model.IBindableDataProviderBinding;

public final class CoreWicketMoreBindings {

	private static final EhCacheCacheInformationBinding EH_CACHE_CACHE_INFORMATION = new EhCacheCacheInformationBinding();
	
	private static final IBindableDataProviderBinding IBINDABLE_DATA_PROVIDER = new IBindableDataProviderBinding();
	
	public static EhCacheCacheInformationBinding ehCacheCacheInformation() {
		return EH_CACHE_CACHE_INFORMATION;
	}
	
	public static IBindableDataProviderBinding iBindableDataProvider() {
		return IBINDABLE_DATA_PROVIDER;
	}
	
	private CoreWicketMoreBindings() {
	}

}
