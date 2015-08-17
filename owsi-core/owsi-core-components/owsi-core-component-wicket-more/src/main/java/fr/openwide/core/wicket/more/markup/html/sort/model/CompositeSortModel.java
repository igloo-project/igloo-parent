package fr.openwide.core.wicket.more.markup.html.sort.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import fr.openwide.core.jpa.more.business.sort.ISort;
import fr.openwide.core.jpa.more.business.sort.ISort.SortOrder;
import fr.openwide.core.wicket.more.markup.html.sort.TableSortLink;

public class CompositeSortModel<T extends ISort<?>> extends AbstractReadOnlyModel<Map<T, SortOrder>> {
	
	private static final long serialVersionUID = -3881057053212320743L;
	
	private final CompositingStrategy compositingStrategy;
	
	private final Map<T, SortOrder> map = Maps.newLinkedHashMap();
	
	private final Map<T, SortOrder> defaultSort;
	
	private final Map<T, SortOrder> stabilizationSort;
	
	private List<IModel<? extends Map<? extends T, SortOrder>>> beforeSortModels = Lists.newArrayList();
	private List<IModel<? extends Map<? extends T, SortOrder>>> beforeDefaultSortModels = Lists.newArrayList();
	private List<IModel<? extends Map<? extends T, SortOrder>>> afterDefaultSortModels = Lists.newArrayList();
	private List<IModel<? extends Map<? extends T, SortOrder>>> afterSortModels = Lists.newArrayList();
	
	private static final <T extends ISort<?>> ImmutableMap<T, SortOrder> toMap(T item) {
		return item == null ? ImmutableMap.<T, SortOrder>of() : ImmutableMap.of(item, item.getDefaultOrder());
	}
	
	public CompositeSortModel(CompositingStrategy compositingStrategy) {
		this(compositingStrategy, (T) null, null);
	}

	/**
	 * @see CompositeSortModel#CompositeSortModel(CompositingStrategy, Map, Map)
	 */
	public CompositeSortModel(CompositingStrategy compositingStrategy, T defaultAndStabilizationSort) {
		this(compositingStrategy, defaultAndStabilizationSort, defaultAndStabilizationSort);
	}

	/**
	 * @see CompositeSortModel#CompositeSortModel(CompositingStrategy, Map, Map)
	 */
	public CompositeSortModel(CompositingStrategy compositingStrategy, T defaultSort, T concatenatedSort) {
		this(compositingStrategy, toMap(defaultSort), toMap(concatenatedSort));
	}
	
	/**
	 * @param defaultSort The sort to be used when no sort was selected. This will be displayed when using {@link TableSortLink}s if no sort was selected.
	 * @param stabilizationSort The sort to be performed after the selected sort was performed, in order to make sure the elements order will not depend
	 * on some implementation detail. This will be ignored when using {@link TableSortLink}s.
	 */
	public CompositeSortModel(CompositingStrategy compositingStrategy, Map<T, SortOrder> defaultSort, Map<T, SortOrder> stabilizationSort) {
		this.compositingStrategy = compositingStrategy;
		this.defaultSort = ImmutableMap.copyOf(defaultSort);
		this.stabilizationSort = ImmutableMap.copyOf(stabilizationSort);
	}
	
	public CompositeSortModel<T> addBefore(IModel<? extends Map<? extends T, SortOrder>> sortModel) {
		beforeSortModels.add(sortModel);
		return this;
	}
	
	public CompositeSortModel<T> addBeforeDefault(IModel<? extends Map<? extends T, SortOrder>> sortModel) {
		beforeDefaultSortModels.add(sortModel);
		return this;
	}
	
	public CompositeSortModel<T> addAfterDefault(IModel<? extends Map<? extends T, SortOrder>> sortModel) {
		afterDefaultSortModels.add(sortModel);
		return this;
	}
	
	public CompositeSortModel<T> addAfter(IModel<? extends Map<? extends T, SortOrder>> sortModel) {
		afterSortModels.add(sortModel);
		return this;
	}
	
	private void putNewKeys(Map<T, SortOrder> map, Iterable<IModel<? extends Map<? extends T, SortOrder>>> models) {
		for (IModel<? extends Map<? extends T, SortOrder>> sortModel : models) {
			putNewKeys(map, sortModel.getObject());
		}
	}
	
	private void putNewKeys(Map<T, SortOrder> map, Map<? extends T, SortOrder> addedMap) {
		for (Map.Entry<? extends T, SortOrder> addedEntry : addedMap.entrySet()) {
			T sort = addedEntry.getKey();
			if (!map.containsKey(sort)) {
				SortOrder sortOrder = addedEntry.getValue();
				map.put(sort, sortOrder);
			}
		}
	}
	
	@Override
	public Map<T, SortOrder> getObject() {
		Map<T, SortOrder> map = new LinkedHashMap<>();
		putNewKeys(map, beforeSortModels);
		putNewKeys(map, getSelectedSort());
		putNewKeys(map, afterSortModels);
		putNewKeys(map, stabilizationSort);
		return map;
	}

	public Map<T, SortOrder> getSelectedSort() {
		if (map.isEmpty()) {
			Map<T, SortOrder> map = new LinkedHashMap<>();
			putNewKeys(map, beforeDefaultSortModels);
			putNewKeys(map, defaultSort);
			putNewKeys(map, afterDefaultSortModels);
			return map;
		} else {
			return map;
		}
	}
	
	/**
	 * Used to highlight the sort links if the sort is enabled.
	 * 
	 * We only highlight the current selection (or the default sort if there is no selection). The concatenated sort is ignored here.
	 */
	public SortOrder getOrder(T sort) {
		return getSelectedSort().get(sort);
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
	
	@Override
	public void detach() {
		for (IDetachable detachable : Iterables.concat(beforeSortModels, beforeDefaultSortModels, afterDefaultSortModels, afterSortModels)) {
			detachable.detach();
		}
	}
}
