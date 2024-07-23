package org.iglooproject.spring.util.lucene.search;

/** Marque les Query dont le toString() est compatible avec l'écriture d'une requête. */
public interface IToQueryStringAwareLuceneQuery {

  String toQueryString();
}
