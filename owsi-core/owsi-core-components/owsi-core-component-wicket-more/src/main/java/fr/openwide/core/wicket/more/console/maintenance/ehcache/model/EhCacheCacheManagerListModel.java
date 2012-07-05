package fr.openwide.core.wicket.more.console.maintenance.ehcache.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.sf.ehcache.CacheManager;

import org.apache.wicket.model.LoadableDetachableModel;

import com.google.common.collect.Lists;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;

public class EhCacheCacheManagerListModel extends LoadableDetachableModel<List<CacheManager>> {

	private static final long serialVersionUID = 2905490587596353984L;

	@Override
	protected List<CacheManager> load() {
		List<CacheManager> cacheManagers = Lists.newArrayList(CacheManager.ALL_CACHE_MANAGERS);
		Collections.sort(cacheManagers, EhcacheCacheManagerComparator.INSTANCE);
		return cacheManagers;
	}
	
	private static final class EhcacheCacheManagerComparator implements Comparator<CacheManager>, Serializable {
		private static final long serialVersionUID = 1L;
		
		private static final EhcacheCacheManagerComparator INSTANCE = new EhcacheCacheManagerComparator();
		
		@Override
		public int compare(CacheManager o1, CacheManager o2) {
			return GenericEntity.DEFAULT_STRING_COLLATOR.compare(o1.getName(), o2.getName());
		}
		
	}

}
