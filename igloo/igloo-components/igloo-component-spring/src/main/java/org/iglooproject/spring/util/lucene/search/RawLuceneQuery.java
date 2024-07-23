package org.iglooproject.spring.util.lucene.search;

import org.apache.lucene.search.Query;

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
}
