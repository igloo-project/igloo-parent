package org.iglooproject.jpa.query;

import java.util.List;

import com.querydsl.core.QueryModifiers;
import com.querydsl.core.support.FetchableQueryBase;

public final class Queries {

	private Queries() { }

	/**
	 * A simple wrapper from {@link FetchableQueryBase} to {@link IQuery}.
	 * <p>The resulting query is not thread-safe.
	 */
	public static <T> IQuery<T> fromQueryDsl(FetchableQueryBase<T, ?> fetchableQuery) {
		return new QueryDslSearchQuery<>(fetchableQuery);
	}

	private static class QueryDslSearchQuery<T> implements IQuery<T> {

		private final FetchableQueryBase<T, ?> fetchableQuery;

		public QueryDslSearchQuery(FetchableQueryBase<T, ?> fetchableQuery) {
			this.fetchableQuery = fetchableQuery;
		}

		@Override
		public List<T> fullList() {
			// Handle multiple calls to fullList/list
			fetchableQuery.restrict(QueryModifiers.EMPTY);
			return fetchableQuery.fetch();
		}

		@Override
		public List<T> list(long offset, long limit) {
			fetchableQuery.offset(offset);
			fetchableQuery.limit(limit);
			return fetchableQuery.fetch();
		}

		@Override
		public long count() {
			return fetchableQuery.fetchCount();
		}

	
	}

}
