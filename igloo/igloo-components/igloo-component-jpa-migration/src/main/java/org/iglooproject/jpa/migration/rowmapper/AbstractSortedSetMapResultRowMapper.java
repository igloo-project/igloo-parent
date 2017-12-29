package org.iglooproject.jpa.migration.rowmapper;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.SortedSet;

import com.google.common.base.Supplier;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimaps;
import com.google.common.collect.SortedSetMultimap;

import org.iglooproject.commons.util.functional.Suppliers2;

public abstract class AbstractSortedSetMapResultRowMapper<K,V> extends AbstractResultRowMapper<Map<K, SortedSet<V>>> {
	
	private final SortedSetMultimap<K, V> multimap;
	
	protected static <K, V> SortedSetMultimap<K, V> newTreeSetHashMultimap(int expectedKeys, int expectedValuesPerKey, Comparator<? super V> comparator) {
		Map<K, Collection<V>> map = Maps.newHashMapWithExpectedSize(expectedKeys);
		Supplier<? extends SortedSet<V>> factory = Suppliers2.treeSet(comparator);
		return Multimaps.newSortedSetMultimap(map, factory);
	}
	
	protected AbstractSortedSetMapResultRowMapper(SortedSetMultimap<K, V> results) {
		this(results, Multimaps.asMap(results));
	}

	private AbstractSortedSetMapResultRowMapper(SortedSetMultimap<K, V> multimap, Map<K, SortedSet<V>> mapView) {
		super(mapView);
		this.multimap = multimap;
	}
	
	protected SortedSetMultimap<K, V> getMultimapResults() {
		return multimap;
	}
}
