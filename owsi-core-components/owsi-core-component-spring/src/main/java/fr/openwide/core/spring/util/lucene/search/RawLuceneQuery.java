package fr.openwide.core.spring.util.lucene.search;

import org.apache.lucene.search.Query;

public final class RawLuceneQuery extends Query {
	private static final long serialVersionUID = 1L;
	
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
	
}
