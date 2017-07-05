package fr.openwide.core.jpa.search.util;

import java.util.Collection;

import javax.persistence.EntityManager;

import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.query.dsl.sort.SortContext;
import org.hibernate.search.query.dsl.sort.SortFieldContext;

public final class SortFieldUtil {

	public static final Sort getSort(EntityManager entityManager, Class<?> entityClass, SortField... sortFields) {
		if (sortFields == null || sortFields.length == 0) {
			return null;
		} else {
			FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
			QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory().buildQueryBuilder()
					.forEntity(entityClass).get();
			SortContext context = queryBuilder.sort();
			SortFieldContext fieldContext = null;
			for (SortField sortField : sortFields) {
				if (fieldContext == null) {
					fieldContext = context.byField(sortField.getField());
				} else {
					fieldContext = fieldContext.andByField(sortField.getField());
				}
				if ( ! sortField.getReverse()) {
					fieldContext.asc();
				} else {
					fieldContext.desc();
				}
			}
			if (fieldContext != null) {
				return fieldContext.createSort();
			} else {
				return null;
			}
		}
	}

	public static void setSort(FullTextQuery ftq, EntityManager entityManager, Class<?> entityClass, SortField... sortFields) {
		ftq.setSort(getSort(entityManager, entityClass, sortFields));
	}

	public static void setSort(FullTextQuery ftq, EntityManager entityManager, Class<?> entityClass, Collection<SortField> sortFields) {
		ftq.setSort(getSort(entityManager, entityClass, sortFields.toArray(new SortField[sortFields.size()])));
	}

	public static void setSort(FullTextQuery ftq, EntityManager entityManager, Class<?> entityClass, Sort sort) {
		setSort(ftq, entityManager, entityClass, sort.getSort());
	}

	private SortFieldUtil() {} // NOSONAR
}
