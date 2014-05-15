package fr.openwide.core.wicket.more.markup.html.sort.model;

import static com.google.common.base.Predicates.in;
import static com.google.common.base.Predicates.not;

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
		return ImmutableMap.<T, SortOrder>builder()
				.putAll(map)
				.putAll(Maps.filterKeys(defaultSort, not(in(map.keySet()))))
				.build();
	}
	
	/**
	 * Used to highlight the sort links if the sort is enabled.
	 * 
	 * We only highlight the current selection, not the default sort added in the CompositeSortModel.
	 */
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
