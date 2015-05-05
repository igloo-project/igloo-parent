package fr.openwide.core.jpa.more.business.sort;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.solr.search.Sorting;
import org.bindgen.Binding;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;

import fr.openwide.core.jpa.more.business.sort.ISort.NullSortValue;
import fr.openwide.core.jpa.more.business.sort.ISort.SortNull;
import fr.openwide.core.jpa.more.business.sort.ISort.SortOrder;

public final class SortUtils {
	
	private SortUtils() { }
	
	@SafeVarargs
	public static <T extends ISort<?>> Map<T, SortOrder> composite(T first, T ... rest) {
		return composite(Lists.asList(first, rest));
	}
	
	public static <T extends ISort<?>> Map<T, SortOrder> composite(Collection<T> sorts) {
		Map<T, SortOrder> map = Maps.newLinkedHashMap();
		for (T sort : sorts) {
			map.put(sort, sort.getDefaultOrder());
		}
		return map;
	}
	
	public static SortField luceneSortField(ISort<SortField> sort, SortOrder order, SortField.Type type, Binding<?> binding, String ... otherFieldParts) {
		List<String> fieldParts = Lists.asList(binding.getPath(), otherFieldParts);
		String fieldName = Joiner.on(".").join(fieldParts);
		return luceneSortField(sort, order, type, fieldName);
	}
	
	public static SortField luceneSortField(ISort<SortField> sort, SortOrder order, SortField.Type type, String fieldName) {
		return new SortField(fieldName, type, isReverse(sort, order));
	}
	
	/**
	 * @deprecated Use {@link #luceneStringSortField(ISort, SortOrder, String, NullSortValue)} instead.
	 * This method sorts nulls in the same order independently from the given SortOrder, which probably is a bug.
	 */
	@Deprecated
	public static SortField luceneStringSortField(ISort<SortField> sort, SortOrder order, String fieldName, SortNull sortNull) {
		return Sorting.getStringSortField(fieldName, isReverse(sort, order),
				SortNull.NULL_LAST.equals(sortNull), SortNull.NULL_FIRST.equals(sortNull));
	}
	
	public static SortField luceneStringSortField(ISort<SortField> sort, SortOrder order, String fieldName, NullSortValue sortNull) {
		SortOrder defaultedOrder = sort.getDefaultOrder().asDefaultFor(order);
		return Sorting.getStringSortField(fieldName, isReverse(sort, order),
				sortNull.isLast(defaultedOrder), sortNull.isFirst(defaultedOrder));
	}
	
	private static boolean isReverse(ISort<SortField> sort, SortOrder order) {
		return SortOrder.DESC == sort.getDefaultOrder().asDefaultFor(order);
	}

	public static <T extends ISort<SortField>> List<SortField> collectSortFields(Map<T, SortOrder> sortsMap) {
		List<SortField> sortFields = Lists.newArrayList();
		if (sortsMap != null) {
			for (Entry<T, SortOrder> sortOrderEntry : sortsMap.entrySet()) {
				sortFields.addAll(sortOrderEntry.getKey().getSortFields(sortOrderEntry.getValue()));
			}
		}
		return sortFields;
	}

	@SafeVarargs
	public static <T extends ISort<SortField>> Sort getLuceneSortWithDefaults(
			Map<T, SortOrder> sortsMap, T firstDefaultSort, T ... otherDefaultSorts) {
		return getLuceneSortWithDefaults(sortsMap, Lists.asList(firstDefaultSort, otherDefaultSorts));
	}

	public static <T extends ISort<SortField>> Sort getLuceneSortWithDefaults(
				Map<T, SortOrder> sortsMap, List<T> list) {
		List<SortField> sortFields = collectSortFields(sortsMap);
		for (T defaultSort : list) {
			sortFields.addAll(defaultSort.getSortFields(defaultSort.getDefaultOrder()));
		}
		return new Sort(sortFields.toArray(new SortField[sortFields.size()]));
	}

	private static <T extends ISort<? extends C>, C extends Comparator<?>> List<C> collectComparators(Map<T, SortOrder> sortsMap) {
		List<C> sortFields = Lists.newArrayList();
		if (sortsMap != null) {
			for (Entry<T, SortOrder> sortOrderEntry : sortsMap.entrySet()) {
				sortFields.addAll(sortOrderEntry.getKey().getSortFields(sortOrderEntry.getValue()));
			}
		}
		return sortFields;
	}

	@SafeVarargs
	public static <S extends ISort<? extends Comparator<? super T>>, T> Ordering<T> getComparatorSortWithDefaults(
			Map<? extends S, SortOrder> sortsMap, S firstDefaultSort, S ... otherDefaultSorts) {
		List<Comparator<? super T>> sortFields = SortUtils.collectComparators(sortsMap);
		for (S defaultSort : Lists.asList(firstDefaultSort, otherDefaultSorts)) {
			sortFields.addAll(defaultSort.getSortFields(defaultSort.getDefaultOrder()));
		}
		return Ordering.compound(sortFields);
	}

}
