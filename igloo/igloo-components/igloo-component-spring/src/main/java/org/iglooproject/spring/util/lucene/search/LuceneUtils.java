package org.iglooproject.spring.util.lucene.search;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.QueryParser.Operator;
import org.apache.lucene.queryparser.simple.SimpleQueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.BoostQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.WildcardQuery;
import org.iglooproject.spring.util.StringUtils;

public final class LuceneUtils {

  private static final int DEFAULT_ENABLE_WILDCARD_MIN_CHARS = 2;

  public static final String LOWERCASE_OPERATORS_PARAM = "lowercaseOperators";

  public static final String BOOST_PARAMETER_PREFIX = "^";

  public static final String WILDCARD_SUFFIX = "*";

  public static final String FUZZY_PARAMETER_SUFFIX = "~";

  public static final TermQuery NO_RESULT_QUERY =
      new TermQuery(new Term("id", "__NEVER_MATCHING_ID__"));

  public static Query getAutocompleteQuery(
      String fieldName, Analyzer analyzer, String searchPattern, int enableWildcardMinChars) {
    return getAutocompleteQuery(
        ImmutableList.of(fieldName), analyzer, searchPattern, enableWildcardMinChars);
  }

  public static Query getAutocompleteQuery(
      String fieldName, Analyzer analyzer, String searchPattern) {
    return getAutocompleteQuery(
        fieldName, analyzer, searchPattern, DEFAULT_ENABLE_WILDCARD_MIN_CHARS);
  }

  public static Query getAutocompleteQuery(
      Iterable<String> fieldNames,
      Analyzer analyzer,
      String searchPattern,
      int enableWildcardMinChars) {
    Map<String, Float> fields = Maps.newHashMap();
    for (String fieldName : fieldNames) {
      fields.put(fieldName, 1.0f);
    }
    SimpleQueryParser queryParser = new SimpleQueryParser(analyzer, fields);
    queryParser.setDefaultOperator(BooleanClause.Occur.MUST);
    return queryParser.parse(getAutocompleteQuery(searchPattern, enableWildcardMinChars));
  }

  public static Query getAutocompleteQuery(
      Iterable<String> fieldNames, Analyzer analyzer, String searchPattern) {
    return getAutocompleteQuery(
        fieldNames, analyzer, searchPattern, DEFAULT_ENABLE_WILDCARD_MIN_CHARS);
  }

  public static String getAutocompleteQuery(String searchPattern) {
    return getAutocompleteQuery(searchPattern, DEFAULT_ENABLE_WILDCARD_MIN_CHARS, null);
  }

  public static String getAutocompleteQuery(String searchPattern, Operator operator) {
    return getAutocompleteQuery(searchPattern, DEFAULT_ENABLE_WILDCARD_MIN_CHARS, operator);
  }

  public static String getAutocompleteQuery(String searchPattern, int enableWildcardMinChars) {
    return getAutocompleteQuery(searchPattern, enableWildcardMinChars, null);
  }

  public static String getAutocompleteQuery(
      String searchPattern, int enableWildcardMinChars, Operator operator) {
    String cleanSearchPattern = StringUtils.clean(searchPattern);

    if (StringUtils.hasText(cleanSearchPattern)
        && cleanSearchPattern.length() >= enableWildcardMinChars) {
      List<String> searchPatternFragments = getSearchPatternFragments(cleanSearchPattern);

      StringBuilder autocompleteQuery = new StringBuilder();
      Iterator<String> searchPatternFragmentsIt = searchPatternFragments.iterator();

      while (searchPatternFragmentsIt.hasNext()) {
        if (autocompleteQuery.length() > 0) {
          autocompleteQuery.append(" ");
          if (operator != null) {
            autocompleteQuery.append(Operator.AND).append(" ");
          }
        }

        autocompleteQuery.append(searchPatternFragmentsIt.next());

        if (!searchPatternFragmentsIt.hasNext()) {
          autocompleteQuery.append(WILDCARD_SUFFIX);
        }
      }

      cleanSearchPattern = autocompleteQuery.toString().trim();
    }

    return cleanSearchPattern;
  }

  public static Query getSimilarityQuery(
      Iterable<String> fieldNames,
      Analyzer analyzer,
      String searchPattern,
      Integer maxEditDistance) {
    Map<String, Float> fields = Maps.newHashMap();
    for (String fieldName : fieldNames) {
      fields.put(fieldName, 1.0f);
    }
    SimpleQueryParser queryParser = new SimpleQueryParser(analyzer, fields);
    queryParser.setDefaultOperator(BooleanClause.Occur.MUST);
    return queryParser.parse(getSimilarityQuery(searchPattern, maxEditDistance));
  }

  public static Query getSimilarityQuery(
      String fieldName, Analyzer analyzer, String searchPattern, Integer maxEditDistance) {
    if (maxEditDistance == null) {
      throw new IllegalArgumentException("maxEditDistance may not be null");
    }

    SimpleQueryParser queryParser = new SimpleQueryParser(analyzer, fieldName);
    queryParser.setDefaultOperator(BooleanClause.Occur.MUST);
    return queryParser.parse(getSimilarityQuery(searchPattern, maxEditDistance));
  }

  public static String getSimilarityQuery(String searchPattern, Integer maxEditDistance) {
    return getSimilarityQuery(searchPattern, maxEditDistance, null);
  }

  public static String getSimilarityQuery(
      String searchPattern, Integer maxEditDistance, Operator operator) {
    if (maxEditDistance == null) {
      throw new IllegalArgumentException("maxEditDistance may not be null");
    }

    String cleanSearchPattern = StringUtils.clean(searchPattern);

    if (!StringUtils.hasText(cleanSearchPattern)) {
      throw new IllegalArgumentException("cleanSearchPattern may not be empty");
    }

    List<String> searchPatternFragments = getSearchPatternFragments(cleanSearchPattern);

    StringBuilder similarityQuery = new StringBuilder();
    for (String searchPatternFragment : searchPatternFragments) {
      if (similarityQuery.length() > 0) {
        similarityQuery.append(" ");
        if (operator != null) {
          similarityQuery.append(operator).append(" ");
        }
      }
      similarityQuery
          .append(searchPatternFragment)
          .append(FUZZY_PARAMETER_SUFFIX)
          .append(maxEditDistance.toString());
    }

    return similarityQuery.toString().trim();
  }

  /**
   * Nettoie la chaîne de recherche et autorise une recherche avec wildcard.
   *
   * <p>A noter que si stemming ou truc tordu il y a, il faut quand même faire la recherche à la
   * fois sur le champ stemmé et sur un champ non stemmé sinon le wildcard pourra ne pas renvoyer de
   * résultat.
   *
   * <p>Ne met pas d'opérateur explicite entre les différents mots.
   */
  public static String getQuery(String searchPattern) {
    return getQuery(searchPattern, null);
  }

  /**
   * Nettoie la chaîne de recherche et autorise une recherche avec wildcard.
   *
   * <p>A noter que si stemming ou truc tordu il y a, il faut quand même faire la recherche à la
   * fois sur le champ stemmé et sur un champ non stemmé sinon le wildcard pourra ne pas renvoyer de
   * résultat.
   */
  public static String getQuery(String searchPattern, Operator operator) {
    String cleanSearchPattern = StringUtils.cleanForQuery(searchPattern);

    if (StringUtils.hasText(cleanSearchPattern)) {
      List<String> searchPatternFragments = getSearchPatternFragments(cleanSearchPattern);

      StringBuilder query = new StringBuilder();
      for (String searchPatternFragment : searchPatternFragments) {
        if (WILDCARD_SUFFIX.equals(searchPatternFragment)) {
          // si c'est juste une *, on ne peut pas faire grand chose, passons...
          continue;
        }
        if (query.length() > 0) {
          query.append(" ");
          if (operator != null) {
            query.append(operator).append(" ");
          }
        }
        query.append(searchPatternFragment);
      }

      cleanSearchPattern = query.toString().trim();
    }

    return cleanSearchPattern;
  }

  private static List<String> getSearchPatternFragments(String searchPattern) {
    List<String> searchPatternFragments = Lists.newArrayList();

    if (StringUtils.hasText(searchPattern)) {
      searchPatternFragments =
          Splitter.on(CharMatcher.whitespace().or(CharMatcher.is('-')))
              .trimResults()
              .omitEmptyStrings()
              .splitToList(searchPattern);
    }

    return searchPatternFragments;
  }

  /** Igloo 6.0: NumericRangeQuery is no longer an available type and is removed from cases. */
  public static String queryToString(Query luceneQuery) {
    StringBuilder sb = new StringBuilder();
    if (luceneQuery instanceof BooleanQuery) {
      sb.append(formatBooleanQuery((BooleanQuery) luceneQuery));
    } else if (luceneQuery instanceof TermQuery) {
      sb.append(formatTermQuery((TermQuery) luceneQuery));
    } else if (luceneQuery instanceof FuzzyQuery) {
      sb.append(formatFuzzyQuery((FuzzyQuery) luceneQuery));
    } else if (luceneQuery instanceof PrefixQuery) {
      sb.append(formatPrefixQuery((PrefixQuery) luceneQuery));
    } else if (luceneQuery instanceof WildcardQuery) {
      sb.append(formatWildcardQuery((WildcardQuery) luceneQuery));
    } else if (luceneQuery instanceof IToQueryStringAwareLuceneQuery) {
      sb.append(((IToQueryStringAwareLuceneQuery) luceneQuery).toQueryString());
    } else if (luceneQuery instanceof BoostQuery) {
      sb.append(queryToString(((BoostQuery) luceneQuery).getQuery()));
      sb.append(BOOST_PARAMETER_PREFIX);
      sb.append(((BoostQuery) luceneQuery).getBoost());
    } else {
      throw new IllegalStateException(
          String.format("Query of type %1$s not supported", luceneQuery.getClass().getName()));
    }

    return sb.toString();
  }

  private static String formatBooleanQuery(BooleanQuery booleanQuery) {
    StringBuilder sb = new StringBuilder();
    if (booleanQuery.clauses().size() > 0) {
      StringBuilder booleanQuerySb = new StringBuilder();
      for (BooleanClause clause : booleanQuery.clauses()) {
        if (clause.getQuery() != null) {
          String query = queryToString(clause.getQuery());

          if (StringUtils.hasText(query)) {
            if (Occur.SHOULD.equals(clause.getOccur())) {
              // dans Solr, on peut définir l'opérateur implicite en AND et il faut donc qu'on soit
              // précis
              if (booleanQuerySb.length() > 0) {
                booleanQuerySb.append("OR ");
              }
            } else {
              booleanQuerySb.append(clause.getOccur().toString());
            }
            booleanQuerySb.append(query);
            booleanQuerySb.append(" ");
          }
        }
      }
      if (booleanQuerySb.length() > 0) {
        if (booleanQuery.clauses().size() > 1
            || booleanQuerySb.charAt(0) == '-'
            || booleanQuerySb.charAt(0) == '+') {
          sb.append("(").append(booleanQuerySb.toString().trim()).append(")");
        } else {
          sb.append(booleanQuerySb);
        }
      }
    }
    return sb.toString();
  }

  private static String formatTermQuery(TermQuery termQuery) {
    StringBuilder sb = new StringBuilder();
    Term term = termQuery.getTerm();
    if (StringUtils.hasText(term.field())) {
      sb.append(term.field());
      sb.append(":");
    }
    sb.append("\"").append(QueryParser.escape(term.text())).append("\"");
    return sb.toString();
  }

  private static String formatFuzzyQuery(FuzzyQuery fuzzyQuery) {
    StringBuilder sb = new StringBuilder();
    Term term = fuzzyQuery.getTerm();
    if (StringUtils.hasText(term.field())) {
      sb.append(term.field());
      sb.append(":");
    }
    sb.append(QueryParser.escape(term.text()))
        .append(FUZZY_PARAMETER_SUFFIX)
        .append(fuzzyQuery.getMaxEdits());
    return sb.toString();
  }

  private static String formatPrefixQuery(PrefixQuery prefixQuery) {
    StringBuilder sb = new StringBuilder();
    Term prefix = prefixQuery.getPrefix();
    if (StringUtils.hasText(prefix.field())) {
      sb.append(prefix.field());
      sb.append(":");
    }
    sb.append(QueryParser.escape(prefix.text()));
    sb.append("*");
    return sb.toString();
  }

  private static String formatWildcardQuery(WildcardQuery wildcardQuery) {
    StringBuilder sb = new StringBuilder();
    Term term = wildcardQuery.getTerm();
    if (StringUtils.hasText(term.field())) {
      sb.append(term.field());
      sb.append(":");
    }
    sb.append(term.text());
    return sb.toString();
  }

  private LuceneUtils() {}
}
