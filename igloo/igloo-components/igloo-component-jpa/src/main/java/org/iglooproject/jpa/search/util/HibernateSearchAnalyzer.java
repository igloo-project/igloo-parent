package org.iglooproject.jpa.search.util;

/**
 * @deprecated use {@link igloo.hibernateconfig.api.HibernateSearchAnalyzer}
 */
@Deprecated
public final class HibernateSearchAnalyzer {

  public static final String TEXT = igloo.hibernateconfig.api.HibernateSearchAnalyzer.TEXT;

  public static final String TEXT_STEMMING =
      igloo.hibernateconfig.api.HibernateSearchAnalyzer.TEXT_STEMMING;

  public static final String KEYWORD = igloo.hibernateconfig.api.HibernateSearchAnalyzer.KEYWORD;

  public static final String KEYWORD_CLEAN =
      igloo.hibernateconfig.api.HibernateSearchAnalyzer.KEYWORD_CLEAN;

  private HibernateSearchAnalyzer() {}
}
