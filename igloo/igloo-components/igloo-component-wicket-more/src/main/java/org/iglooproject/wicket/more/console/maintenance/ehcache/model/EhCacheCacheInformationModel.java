package org.iglooproject.wicket.more.console.maintenance.ehcache.model;

import java.util.Objects;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.iglooproject.wicket.more.util.model.Detachables;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

public class EhCacheCacheInformationModel extends LoadableDetachableModel<EhCacheCacheInformation> {

	private static final long serialVersionUID = 2905490587596353984L;

	private final IModel<Cache> cacheModel;

	public EhCacheCacheInformationModel(IModel<Cache> cacheModel) {
		this.cacheModel = Objects.requireNonNull(cacheModel);
	}

	@Override
	protected EhCacheCacheInformation load() {
		String cacheManagerName = cacheModel.getObject().getCacheManager().getName();
		String cacheName = cacheModel.getObject().getName();
		
		for (CacheManager cacheManager : CacheManager.ALL_CACHE_MANAGERS) {
			if (cacheManager.getName().equals(cacheManagerName)) {
				return new EhCacheCacheInformation(cacheManager.getCache(cacheName));
			}
		}
		
		return null;
	}

	@Override
	protected void onDetach() {
		super.onDetach();
		Detachables.detach(cacheModel);
	}

}
