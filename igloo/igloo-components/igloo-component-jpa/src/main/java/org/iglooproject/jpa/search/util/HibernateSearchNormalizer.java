package org.iglooproject.jpa.search.util;

/**
 * @deprecated use {@link igloo.hibernateconfig.api.HibernateSearchNormalizer}
 */
@Deprecated
public final class HibernateSearchNormalizer {

  public static final String KEYWORD = igloo.hibernateconfig.api.HibernateSearchNormalizer.KEYWORD;

  public static final String KEYWORD_CLEAN =
      igloo.hibernateconfig.api.HibernateSearchNormalizer.KEYWORD_CLEAN;

  public static final String TEXT = igloo.hibernateconfig.api.HibernateSearchNormalizer.TEXT;

  private HibernateSearchNormalizer() {}
}
