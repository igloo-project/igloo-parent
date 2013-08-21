package fr.openwide.core.spring.util.lucene.search;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.queryParser.QueryParser.Operator;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.util.Version;

import fr.openwide.core.spring.util.StringUtils;

public final class LuceneUtils {
	
	@SuppressWarnings("deprecation")
	public static final Version LUCENE_VERSION = Version.LUCENE_CURRENT;
	
	private static final int DEFAULT_ENABLE_WILDCARD_MIN_CHARS = 2;
	
	public static final String LOWERCASE_OPERATORS_PARAM = "lowercaseOperators";
	
	public static final String BOOST_PARAMETER_PREFIX = "^";
	
	public static final String WILDCARD_SUFFIX = "*";
	
	public static Query getAutocompleteQuery(String fieldName, Analyzer analyzer,
			String searchPattern, int enableWildcardMinChars) throws ParseException {
		QueryParser queryParser = new QueryParser(LUCENE_VERSION, fieldName, analyzer);
		queryParser.setDefaultOperator(Operator.AND);
		return queryParser.parse(getAutocompleteQuery(searchPattern, enableWildcardMinChars));
	}
	
	public static Query getAutocompleteQuery(String fieldName, Analyzer analyzer,
			String searchPattern) throws ParseException {
		return getAutocompleteQuery(fieldName, analyzer, searchPattern, DEFAULT_ENABLE_WILDCARD_MIN_CHARS);
	}
	
	public static String getAutocompleteQuery(String searchPattern) {
		return getAutocompleteQuery(searchPattern, DEFAULT_ENABLE_WILDCARD_MIN_CHARS);
	}
	
	public static String getAutocompleteQuery(String searchPattern, int enableWildcardMinChars) {
		String cleanSearchPattern = cleanSearchPattern(searchPattern);
		
		if(StringUtils.hasText(cleanSearchPattern) && cleanSearchPattern.length() >= enableWildcardMinChars
				&& !cleanSearchPattern.endsWith(WILDCARD_SUFFIX)) {
			StringBuilder autocompleteQuery = new StringBuilder(cleanSearchPattern);
			autocompleteQuery.append(WILDCARD_SUFFIX);
			
			cleanSearchPattern = autocompleteQuery.toString();
		}
		
		return cleanSearchPattern;
	}
	
	public static Query getSimilarityQuery(String fieldName, Analyzer analyzer,
			String searchPattern, Float minSimilarity) throws ParseException {
		if (minSimilarity == null) {
			throw new IllegalArgumentException("minSimilarity may not be null");
		}
		
		QueryParser queryParser = new QueryParser(LUCENE_VERSION, fieldName, analyzer);
		queryParser.setDefaultOperator(Operator.AND);
		return queryParser.parse(getSimilarityQuery(searchPattern, minSimilarity));
	}
	
	public static String getSimilarityQuery(String searchPattern, Float minSimilarity) {
		if (minSimilarity == null) {
			throw new IllegalArgumentException("minSimilarity may not be null");
		}
		
		String cleanSearchPattern = cleanSearchPattern(searchPattern);
		
		if (!StringUtils.hasText(cleanSearchPattern)) {
			throw new IllegalArgumentException("cleanSearchPattern may not be empty");
		}
		
		StringBuilder similarityQuery = new StringBuilder();
		
		String[] searchPatternElements = StringUtils.delimitedListToStringArray(cleanSearchPattern, StringUtils.SPACE);
		
		for (int i = 0; i < searchPatternElements.length; i++) {
			similarityQuery.append(searchPatternElements[i]).append("~").append(minSimilarity.toString()).append(" ");
		}
		
		return similarityQuery.toString().trim();
	}
	
	public static String cleanSearchPattern(String searchPattern) {
		String cleanSearchPattern = StringUtils.clean(searchPattern);
		
		if(StringUtils.hasText(cleanSearchPattern)) {
			StringBuilder cleanSearchPatternSb = new StringBuilder();
			
			String[] searchPatternElements = StringUtils.delimitedListToStringArray(cleanSearchPattern, StringUtils.SPACE);
			
			for (int i = 0; i < searchPatternElements.length; i++) {
				String fragment = StringUtils.trimLeadingCharacter(searchPatternElements[i], StringUtils.DASH_CHAR);
				
				if (StringUtils.hasText(fragment)) {
					cleanSearchPatternSb.append(fragment).append(StringUtils.SPACE);
				}
			}
			
			cleanSearchPattern = cleanSearchPatternSb.toString().trim();
		}
		
		return cleanSearchPattern;
	}
	
	public static RawLuceneQuery toFilterRangeQuery(String field, Number min, Number max) {
		return toFilterRangeQuery(field, min, max, true, true);
	}
	
	public static RawLuceneQuery toFilterRangeQuery(String field, Number min, Number max, boolean minInclusive, boolean maxInclusive) {
		if (min == null && max == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		if (StringUtils.hasText(field)) {
			sb.append(field);
			sb.append(":");
		}
		sb.append(minInclusive ? "[" : "{")
			.append((min == null) ? "*" : min.toString())
			.append(" TO ")
			.append((max == null) ? "*" : max.toString())
			.append(maxInclusive ? "]" : "}");
		return new RawLuceneQuery(sb.toString());
	}
	
	@SuppressWarnings("unchecked")
	public static String queryToString(Query luceneQuery) {
		StringBuilder sb = new StringBuilder();
		if (luceneQuery instanceof BooleanQuery) {
			sb.append(formatBooleanQuery((BooleanQuery) luceneQuery));
		} else if (luceneQuery instanceof TermQuery) {
			sb.append(formatTermQuery((TermQuery) luceneQuery));
		} else if (luceneQuery instanceof RawLuceneQuery) {
			sb.append(formatRawLuceneQuery((RawLuceneQuery) luceneQuery));
		} else if (luceneQuery instanceof FuzzyQuery) {
			sb.append(formatFuzzyQuery((FuzzyQuery) luceneQuery));
		} else if (luceneQuery instanceof PrefixQuery) {
			sb.append(formatPrefixQuery((PrefixQuery) luceneQuery));
		} else if (luceneQuery instanceof WildcardQuery) {
			sb.append(formatWildcardQuery((WildcardQuery) luceneQuery));
		} else if (luceneQuery instanceof NumericRangeQuery) {
			sb.append(formatNumericRangeQuery((NumericRangeQuery<? extends Number>) luceneQuery));
		} else {
			throw new IllegalStateException(String.format("Query of type %1$s not supported",
					luceneQuery.getClass().getName()));
		}
		if (StringUtils.hasText(sb) && Float.compare(1.0f, luceneQuery.getBoost()) != 0) {
			sb.append(BOOST_PARAMETER_PREFIX);
			sb.append(luceneQuery.getBoost());
		}
		return sb.toString();
	}
	
	private static String formatBooleanQuery(BooleanQuery booleanQuery) {
		StringBuilder sb = new StringBuilder();
		if (booleanQuery.getClauses().length > 0) {
			StringBuilder booleanQuerySb = new StringBuilder();
			for (BooleanClause clause : booleanQuery.getClauses()) {
				if (clause.getQuery() != null) {
					String query = queryToString(clause.getQuery());
					
					if (StringUtils.hasText(query)) {
						if (Occur.SHOULD.equals(clause.getOccur())) {
							// dans Solr, on peut définir l'opérateur implicite en AND et il faut donc qu'on soit précis
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
				if (booleanQuery.getClauses().length > 1
						|| booleanQuerySb.charAt(0) == '-' || booleanQuerySb.charAt(0) == '+'
						|| (booleanQuery.getClauses().length == 1 && (booleanQuery.getClauses()[0].getQuery() instanceof RawLuceneQuery))) {
					sb.append("(")
						.append(booleanQuerySb.toString().trim())
						.append(")");
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
		sb.append("\"")
			.append(QueryParser.escape(term.text()))
			.append("\"");
		return sb.toString();
	}
	
	private static String formatRawLuceneQuery(RawLuceneQuery simpleQuery) {
		StringBuilder sb = new StringBuilder();
		if (StringUtils.hasText(simpleQuery.getQuery())) {
			sb.append("(")
				.append(simpleQuery.getQuery())
				.append(")");
		}
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
			.append("~")
			.append(Float.toString(fuzzyQuery.getMinSimilarity()));
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
	
	private static String formatNumericRangeQuery(NumericRangeQuery<? extends Number> numericRangeQuery) {
		return toFilterRangeQuery(numericRangeQuery.getField(), numericRangeQuery.getMin(), numericRangeQuery.getMax(),
				numericRangeQuery.includesMin(), numericRangeQuery.includesMax()).getQuery();
	}
	
	private LuceneUtils() {
	}
	
}
