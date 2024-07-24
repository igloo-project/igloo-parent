package org.iglooproject.jpa.more.business.sort;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.bindgen.Binding;
import org.iglooproject.jpa.more.business.sort.ISort.NullSortValue;
import org.iglooproject.jpa.more.business.sort.ISort.SortOrder;

public final class SortUtils {

  private SortUtils() {}

  // Common
  private static boolean isReverse(ISort<?> sort, SortOrder order) {
    return SortOrder.DESC == sort.getDefaultOrder().asDefaultFor(order);
  }

  public static <S, T extends ISort<S>> List<S> collectSortFields(Map<T, SortOrder> sortsMap) {
    List<S> sortFields = Lists.newArrayList();
    if (sortsMap != null) {
      for (Entry<T, SortOrder> sortOrderEntry : sortsMap.entrySet()) {
        sortFields.addAll(sortOrderEntry.getKey().getSortFields(sortOrderEntry.getValue()));
      }
    }
    return sortFields;
  }

  // Hibernate Search
  @SafeVarargs
  public static <T extends ISort<?>> Map<T, SortOrder> composite(T first, T... rest) {
    return composite(Lists.asList(first, rest));
  }

  public static <T extends ISort<?>> Map<T, SortOrder> composite(Collection<T> sorts) {
    Map<T, SortOrder> map = Maps.newLinkedHashMap();
    for (T sort : sorts) {
      map.put(sort, sort.getDefaultOrder());
    }
    return map;
  }

  public static <T extends ISort<?>> void appendTo(
      Map<T, SortOrder> dst, Map<? extends T, SortOrder> addedMap) {
    for (Map.Entry<? extends T, SortOrder> addedEntry : addedMap.entrySet()) {
      T sort = addedEntry.getKey();
      if (!dst.containsKey(sort)) {
        SortOrder sortOrder = addedEntry.getValue();
        dst.put(sort, sortOrder);
      }
    }
  }

  public static SortField luceneSortField(
      ISort<SortField> sort,
      SortOrder order,
      SortField.Type type,
      Binding<?> binding,
      String... otherFieldParts) {
    List<String> fieldParts = Lists.asList(binding.getPath(), otherFieldParts);
    String fieldName = Joiner.on(".").join(fieldParts);
    return luceneSortField(sort, order, type, fieldName);
  }

  public static SortField luceneSortField(
      ISort<SortField> sort, SortOrder order, SortField.Type type, String fieldName) {
    return new SortField(fieldName, type, isReverse(sort, order));
  }

  public static SortField luceneStringSortField(
      ISort<SortField> sort, SortOrder order, String fieldName, NullSortValue sortNull) {
    SortOrder defaultedOrder = sort.getDefaultOrder().asDefaultFor(order);
    return getStringSortField(
        fieldName,
        isReverse(sort, order),
        sortNull.isLast(defaultedOrder),
        sortNull.isFirst(defaultedOrder));
  }

  public static SortField luceneLongSortField(
      ISort<SortField> sort, SortOrder order, String fieldName, NullSortValue sortNull) {
    SortOrder defaultedOrder = sort.getDefaultOrder().asDefaultFor(order);
    return getLongSortField(
        fieldName,
        isReverse(sort, order),
        sortNull.isLast(defaultedOrder),
        sortNull.isFirst(defaultedOrder));
  }

  @SafeVarargs
  public static <T extends ISort<SortField>> Sort getLuceneSortWithDefaults(
      Map<T, SortOrder> sortsMap, T firstDefaultSort, T... otherDefaultSorts) {
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

  private static <T extends ISort<? extends C>, C extends Comparator<?>> List<C> collectComparators(
      Map<T, SortOrder> sortsMap) {
    List<C> sortFields = Lists.newArrayList();
    if (sortsMap != null) {
      for (Entry<T, SortOrder> sortOrderEntry : sortsMap.entrySet()) {
        sortFields.addAll(sortOrderEntry.getKey().getSortFields(sortOrderEntry.getValue()));
      }
    }
    return sortFields;
  }

  @SafeVarargs
  public static <S extends ISort<? extends Comparator<? super T>>, T>
      Ordering<T> getComparatorSortWithDefaults(
          Map<? extends S, SortOrder> sortsMap, S firstDefaultSort, S... otherDefaultSorts) {
    List<Comparator<? super T>> sortFields = SortUtils.collectComparators(sortsMap);
    for (S defaultSort : Lists.asList(firstDefaultSort, otherDefaultSorts)) {
      sortFields.addAll(defaultSort.getSortFields(defaultSort.getDefaultOrder()));
    }
    return Ordering.compound(sortFields);
  }

  /**
   * Comes from Sorting of Solr
   *
   * <p>Returns a {@link SortField} for a string field. If nullLast and nullFirst are both false,
   * then default lucene string sorting is used where null strings sort first in an ascending sort,
   * and last in a descending sort.
   *
   * @param fieldName the name of the field to sort on
   * @param reverse true for a reverse (desc) sort
   * @param nullLast true if null should come last, regardless of sort order
   * @param nullFirst true if null should come first, regardless of sort order
   * @return SortField
   */
  public static SortField getStringSortField(
      String fieldName, boolean reverse, boolean nullLast, boolean nullFirst) {
    if (nullFirst && nullLast) {
      throw new IllegalArgumentException("Cannot specify missing values as both first and last");
    }

    SortField sortField = new SortField(fieldName, SortField.Type.STRING, reverse);

    // 4 cases:
    // missingFirst / forward: default lucene behavior
    // missingFirst / reverse: set sortMissingLast
    // missingLast / forward: set sortMissingLast
    // missingLast / reverse: default lucene behavior

    if ((nullFirst && reverse) || (nullLast && !reverse)) {
      sortField.setMissingValue(SortField.STRING_LAST);
    }

    return sortField;
  }

  public static SortField getLongSortField(
      String fieldName, boolean reverse, boolean nullLast, boolean nullFirst) {
    if (nullFirst && nullLast) {
      throw new IllegalArgumentException("Cannot specify missing values as both first and last");
    }

    SortField sortField = new SortField(fieldName, SortField.Type.LONG, reverse);

    // 4 cases:
    // missingFirst / forward: default lucene behavior
    // missingFirst / reverse: set sortMissingLast
    // missingLast / forward: set sortMissingLast
    // missingLast / reverse: default lucene behavior

    if ((nullFirst && reverse) || (nullLast && !reverse)) {
      sortField.setMissingValue(Long.MAX_VALUE);
    } else {
      sortField.setMissingValue(Long.MIN_VALUE);
    }

    return sortField;
  }

  // JPA
  public static <T extends Comparable<?>> OrderSpecifier<T> orderSpecifier(
      ISort<OrderSpecifier<?>> sort, SortOrder order, ComparableExpressionBase<T> expressionBase) {
    if (isReverse(sort, order)) {
      return expressionBase.desc();
    } else {
      return expressionBase.asc();
    }
  }

  @SafeVarargs
  public static <T extends ISort<OrderSpecifier<?>>>
      List<OrderSpecifier<?>> getOrderSpecifierWithDefaults(
          Map<T, SortOrder> sortsMap, T firstDefaultSort, T... otherDefaultSorts) {
    return getOrderSpecifierWithDefaults(
        sortsMap, Lists.asList(firstDefaultSort, otherDefaultSorts));
  }

  public static <T extends ISort<OrderSpecifier<?>>>
      List<OrderSpecifier<?>> getOrderSpecifierWithDefaults(
          Map<T, SortOrder> sortsMap, List<T> list) {
    List<OrderSpecifier<?>> sortFields = collectSortFields(sortsMap);
    for (T defaultSort : list) {
      sortFields.addAll(defaultSort.getSortFields(defaultSort.getDefaultOrder()));
    }
    return sortFields;
  }

  // SQL
  public static String orderSpecifier(ISort<String> sort, SortOrder order, String expression) {
    if (isReverse(sort, order)) {
      return new StringBuilder().append(expression).append(" DESC").toString();
    } else {
      return new StringBuilder().append(expression).append(" ASC").toString();
    }
  }

  @SafeVarargs
  public static <T extends ISort<String>> List<String> getSQLOrderSpecifierWithDefaults(
      Map<T, SortOrder> sortsMap, T firstDefaultSort, T... otherDefaultSorts) {
    return getSQLOrderSpecifierWithDefaults(
        sortsMap, Lists.asList(firstDefaultSort, otherDefaultSorts));
  }

  public static <T extends ISort<String>> List<String> getSQLOrderSpecifierWithDefaults(
      Map<T, SortOrder> sortsMap, List<T> list) {
    List<String> sortFields = collectSortFields(sortsMap);
    for (T defaultSort : list) {
      sortFields.addAll(defaultSort.getSortFields(defaultSort.getDefaultOrder()));
    }
    return sortFields;
  }
}
