package org.iglooproject.jpa.more.business.search.query;

/** Refers to the default behavior of a search query when no criterion has been set. */
public enum SearchQueryDefaultResult {

  /** Do not return anything. */
  NOTHING,
  /** Return every item. */
  EVERYTHING;
}
