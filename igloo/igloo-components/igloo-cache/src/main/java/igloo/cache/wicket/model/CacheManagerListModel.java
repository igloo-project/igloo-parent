package igloo.cache.wicket.model;

import java.util.List;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import igloo.cache.monitor.CacheManagerWrapper;
import igloo.cache.spring.ICacheManagerRegistry;

/**
 * Model that loads Cache
 */
public class CacheManagerListModel extends LoadableDetachableModel<List<CacheManagerWrapper>> {

	private static final long serialVersionUID = 2905490587596353984L;

	@SpringBean
	private ICacheManagerRegistry registry;

	public CacheManagerListModel() {
		Injector.get().inject(this);
	}

	@Override
	protected List<CacheManagerWrapper> load() {
		return registry.getCacheManagers();
	}

}
