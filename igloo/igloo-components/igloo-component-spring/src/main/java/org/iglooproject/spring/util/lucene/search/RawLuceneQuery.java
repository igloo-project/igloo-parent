package org.iglooproject.spring.util.lucene.search;

import java.util.Objects;

import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryVisitor;

public final class RawLuceneQuery extends Query {
	
	private String query;
	
	public RawLuceneQuery(String query) {
		this.query = query;
	}
	
	public String getQuery() {
		return query;
	}
	
	@Override
	public String toString(String field) {
		return query;
	}

	@Override
	public void visit(QueryVisitor visitor) {

	}

	@Override
	public boolean equals(Object arg0) {
		if (arg0 == null) {
			return false;
		}
		if (arg0 instanceof RawLuceneQuery otherQuery) {
			return Objects.equals(query, otherQuery.query);
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		if (query == null) {
			return 0;
		} else {
			return query.hashCode();
		}
	}
	
}
