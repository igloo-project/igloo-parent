package fr.openwide.core.jpa.more.business.sort;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.bindgen.Binding;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;

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
	
	public static SortField luceneSortField(ISort<SortField> sort, SortOrder order, int type, Binding<?> binding, String ... otherFieldParts) {
		List<String> fieldParts = Lists.asList(binding.getPath(), otherFieldParts);
		String fieldName = Joiner.on(".").join(fieldParts);
		return luceneSortField(sort, order, type, fieldName);
	}
	
	public static SortField luceneSortField(ISort<SortField> sort, SortOrder order, int type, String fieldName) {
		return new SortField(fieldName, type, SortOrder.DESC == sort.getDefaultOrder().asDefaultFor(order));
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
		List<SortField> sortFields = collectSortFields(sortsMap);
		for (T defaultSort : Lists.asList(firstDefaultSort, otherDefaultSorts)) {
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
