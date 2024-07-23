package org.iglooproject.jpa.search.util;

import java.util.Collection;
import javax.persistence.EntityManager;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.query.dsl.sort.SortAdditionalSortFieldContext;
import org.hibernate.search.query.dsl.sort.SortContext;
import org.hibernate.search.query.dsl.sort.SortFieldContext;
import org.hibernate.search.query.dsl.sort.SortOrder;
import org.hibernate.search.query.dsl.sort.SortTermination;

public final class SortFieldUtil {

  public static final Sort getSort(
      EntityManager entityManager, Class<?> entityClass, SortField... sortFields) {
    if (sortFields == null || sortFields.length == 0) {
      return null;
    }

    FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
    QueryBuilder queryBuilder =
        fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(entityClass).get();
    SortContext context = queryBuilder.sort();
    SortAdditionalSortFieldContext fieldContext = null;

    for (SortField sortField : sortFields) {
      if (sortField == null) {
        throw new IllegalStateException("SortField must not be null.");
      }

      switch (sortField.getType()) {
        case SCORE:
          fieldContext = fieldContext == null ? context.byScore() : fieldContext.andByScore();
          break;
        default:
          fieldContext =
              fieldContext == null
                  ? context.byField(sortField.getField())
                  : fieldContext.andByField(sortField.getField());
          break;
      }

      fieldContext = onMissingValue(fieldContext, sortField);
      fieldContext = order(fieldContext, sortField);
    }

    return create(fieldContext);
  }

  private static SortAdditionalSortFieldContext onMissingValue(
      SortAdditionalSortFieldContext fieldContext, SortField sortField) {
    if (!(fieldContext instanceof SortFieldContext)) {
      return fieldContext;
    }

    if (sortField.missingValue == null) {
      return fieldContext;
    }

    SortFieldContext sortFieldContext = (SortFieldContext) fieldContext;

    if (SortField.Type.STRING.equals(sortField.getType())
        || SortField.Type.STRING_VAL.equals(sortField.getType())) {
      if (SortField.STRING_FIRST.equals(sortField.missingValue)) {
        sortFieldContext.onMissingValue().sortFirst();
      } else if (SortField.STRING_LAST.equals(sortField.missingValue)) {
        sortFieldContext.onMissingValue().sortLast();
      }
    } else {
      sortFieldContext.onMissingValue().use(sortField.missingValue);
    }

    return fieldContext;
  }

  @SuppressWarnings("unchecked")
  private static SortAdditionalSortFieldContext order(
      SortAdditionalSortFieldContext fieldContext, SortField sortField) {
    if (!(fieldContext instanceof SortOrder)) {
      throw new IllegalStateException("FieldContext must be a SortOrder.");
    }

    return !sortField.getReverse()
        ? ((SortOrder<SortAdditionalSortFieldContext>) fieldContext).asc()
        : ((SortOrder<SortAdditionalSortFieldContext>) fieldContext).desc();
  }

  private static Sort create(SortAdditionalSortFieldContext fieldContext) {
    if (!(fieldContext instanceof SortTermination)) {
      throw new IllegalStateException("FieldContext must be a SortTermination.");
    }

    return ((SortTermination) fieldContext).createSort();
  }

  public static void setSort(
      FullTextQuery ftq,
      EntityManager entityManager,
      Class<?> entityClass,
      SortField... sortFields) {
    ftq.setSort(getSort(entityManager, entityClass, sortFields));
  }

  public static void setSort(
      FullTextQuery ftq,
      EntityManager entityManager,
      Class<?> entityClass,
      Collection<SortField> sortFields) {
    ftq.setSort(
        getSort(entityManager, entityClass, sortFields.toArray(new SortField[sortFields.size()])));
  }

  public static void setSort(
      FullTextQuery ftq, EntityManager entityManager, Class<?> entityClass, Sort sort) {
    setSort(ftq, entityManager, entityClass, sort.getSort());
  }

  private SortFieldUtil() {} // NOSONAR
}
