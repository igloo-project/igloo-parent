package org.iglooproject.wicket.more.console.maintenance.ehcache.model;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.wicket.more.util.model.Detachables;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

public class EhCacheCacheListModel extends LoadableDetachableModel<List<Cache>> {

	private static final long serialVersionUID = 2905490587596353984L;
	
	private IModel<String> cacheManagerNameModel;
	
	public EhCacheCacheListModel(IModel<String> cacheManagerNameModel) {
		this.cacheManagerNameModel = Objects.requireNonNull(cacheManagerNameModel);
	}

	@Override
	protected List<Cache> load() {
		String cacheManagerName = cacheManagerNameModel.getObject();
		ImmutableList.Builder<Cache> cachesBuilder = ImmutableList.builder();
		
		for (CacheManager cacheManager : CacheManager.ALL_CACHE_MANAGERS) {
			if (Objects.equals(cacheManager.getName(), cacheManagerName)) {
				Set<String> cacheNames = Sets.newTreeSet(GenericEntity.STRING_COLLATOR_FRENCH);
				cacheNames.addAll(Arrays.asList(cacheManager.getCacheNames()));
				
				for (String cacheName : cacheNames) {
					cachesBuilder.add(cacheManager.getCache(cacheName));
				}
			}
		}
		
		return cachesBuilder.build();
	}

	@Override
	protected void onDetach() {
		super.onDetach();
		Detachables.detach(cacheManagerNameModel);
	}

}
