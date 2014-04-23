package fr.openwide.core.wicket.more.markup.html.sort.model;

import java.util.Collections;
import java.util.Map;

import org.apache.wicket.model.AbstractReadOnlyModel;

import com.google.common.collect.Maps;

import fr.openwide.core.jpa.more.business.sort.ISort;
import fr.openwide.core.jpa.more.business.sort.ISort.SortOrder;

public class CompositeSortModel<T extends ISort<?>> extends AbstractReadOnlyModel<Map<T, SortOrder>> {
	
	private static final long serialVersionUID = -3881057053212320743L;
	
	private final CompositingStrategy compositingStrategy;
	
	private final Map<T, SortOrder> map = Maps.newLinkedHashMap();
	
	public CompositeSortModel(CompositingStrategy compositingStrategy) {
		this(compositingStrategy, null);
	}
	
	public CompositeSortModel(CompositingStrategy compositingStrategy, T defaultSort) {
		this.compositingStrategy = compositingStrategy;
		if (defaultSort != null) {
			setOrder(defaultSort, defaultSort.getDefaultOrder());
		}
	}

	@Override
	public Map<T, SortOrder> getObject() {
		return Collections.unmodifiableMap(map);
	}
	
	public SortOrder getOrder(T sort) {
		return map.get(sort);
	}
	
	public void setOrder(T sort, SortOrder order) {
		compositingStrategy.setOrder(map, sort, order);
	}

	public static enum CompositingStrategy {
		/**
		 * Only the last sort will be actually applied.
		 */
		LAST_ONLY {
			@Override
			protected <T extends ISort<?>> void setOrder(Map<T, SortOrder> map, T sort, SortOrder order) {
				if (order == null) {
					map.remove(sort);
				} else {
					map.clear();
					map.put(sort, order);
				}
			}
		},
		/**
		 * The sorts will be queued: the last modified sort being applied last.
		 */
		QUEUE {
			@Override
			protected <T extends ISort<?>> void setOrder(Map<T, SortOrder> map, T sort, SortOrder order) {
				map.remove(sort);
				if (order != null) {
					map.put(sort, order);
				}
			}
		};
		
		protected abstract <T extends ISort<?>>
				void setOrder(Map<T, SortOrder> map, T sort, SortOrder order);
	}

}
