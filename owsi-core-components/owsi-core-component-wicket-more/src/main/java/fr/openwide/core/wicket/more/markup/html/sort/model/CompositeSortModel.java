package fr.openwide.core.wicket.more.markup.html.sort.model;

import java.util.Collections;
import java.util.Map;

import org.apache.wicket.model.AbstractReadOnlyModel;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import fr.openwide.core.jpa.more.business.sort.ISort;
import fr.openwide.core.jpa.more.business.sort.ISort.SortOrder;

public class CompositeSortModel<T extends ISort<?>> extends AbstractReadOnlyModel<Map<T, SortOrder>> {
	
	private static final long serialVersionUID = -3881057053212320743L;
	
	private final CompositingStrategy compositingStrategy;
	
	private final Map<T, SortOrder> map = Maps.newLinkedHashMap();
	
	private final Map<T, SortOrder> defaultSort;
	
	public CompositeSortModel(CompositingStrategy compositingStrategy) {
		this(compositingStrategy, (T) null);
	}
	
	public CompositeSortModel(CompositingStrategy compositingStrategy, T defaultSort) {
		this(compositingStrategy, defaultSort == null ? ImmutableMap.<T, SortOrder>of() : ImmutableMap.of(defaultSort, defaultSort.getDefaultOrder()));
	}
	
	public CompositeSortModel(CompositingStrategy compositingStrategy, Map<T, SortOrder> defaultSort) {
		this.compositingStrategy = compositingStrategy;
		this.defaultSort = ImmutableMap.copyOf(defaultSort);
	}

	@Override
	public Map<T, SortOrder> getObject() {
		if (map.isEmpty()) {
			return defaultSort; // immutable
		} else {
			return Collections.unmodifiableMap(map);
		}
	}
	
	public SortOrder getOrder(T sort) {
		return getObject().get(sort);
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
		 * Queues the sorts by order of insertion.
		 * <p>Modifying an already active sort will leave its position in the queue unchanged.
		 */
		QUEUE_BY_INSERTION {
			@Override
			protected <T extends ISort<?>> void setOrder(Map<T, SortOrder> map, T sort, SortOrder order) {
				if (order == null) {
					map.remove(sort);
				} else {
					map.put(sort, order);
				}
			}
		},
		/**
		 * Queues the sorts by order of modification.
		 * <p>Modifying an already active sort will push it to the back of the queue.
		 */
		QUEUE_BY_LAST_MODIFICATION {
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
