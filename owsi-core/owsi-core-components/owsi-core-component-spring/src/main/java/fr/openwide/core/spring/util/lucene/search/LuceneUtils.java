package fr.openwide.core.spring.util.lucene.search;

import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;

import fr.openwide.core.spring.util.StringUtils;

public final class LuceneUtils {
	
	private static final int ENABLE_WILDCARD_MIN_CHARS = 2;
	
	public static final String LOWERCASE_OPERATORS_PARAM = "lowercaseOperators";
	
	public static final String BOOST_PARAMETER_PREFIX = "^";
	
	public static final String WILDCARD_SUFFIX = "*";
	
	public static String getAutocompleteQuery(String searchPattern) {
		String cleanSearchPattern = cleanSearchPattern(searchPattern);
		
		if(StringUtils.hasText(cleanSearchPattern) && cleanSearchPattern.length() >= ENABLE_WILDCARD_MIN_CHARS
				&& !cleanSearchPattern.endsWith(WILDCARD_SUFFIX)) {
			StringBuilder autocompleteQuery = new StringBuilder(cleanSearchPattern);
			autocompleteQuery.append(WILDCARD_SUFFIX);
			
			cleanSearchPattern = autocompleteQuery.toString();
		}
		
		return cleanSearchPattern;
	}
	
	public static String getSimilarityQuery(String searchPattern, Float minSimilarity) {
		String cleanSearchPattern = cleanSearchPattern(searchPattern);
		
		if(StringUtils.hasText(cleanSearchPattern)) {
			StringBuilder similarityQuery = new StringBuilder();
			
			String[] searchPatternElements = StringUtils.delimitedListToStringArray(cleanSearchPattern, StringUtils.SPACE);
			
			for (int i = 0; i < searchPatternElements.length; i++) {
				similarityQuery.append(searchPatternElements[i]).append("~").append(minSimilarity.toString()).append(" ");
			}
			
			cleanSearchPattern = similarityQuery.toString().trim();
		}
		
		return cleanSearchPattern;
	}
	
	private static String cleanSearchPattern(String searchPattern) {
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
	
	public static RawLuceneQuery toFilterRangeQuery(String solrField, Number min, Number max) {
		if (min == null && max == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		sb.append(solrField);
		sb.append(":");
		sb.append("[");
		if (min != null) {
			sb.append(min.toString());
		} else {
			sb.append("*");
		}
		sb.append(" TO ");
		if (max != null) {
			sb.append(max.toString());
		} else {
			sb.append("*");
		}
		sb.append("]");
		return new RawLuceneQuery(sb.toString());
	}
	
	public static String queryToString(Query luceneQuery) {
		StringBuilder sb = new StringBuilder();
		if (luceneQuery instanceof BooleanQuery) {
			BooleanQuery booleanQuery = (BooleanQuery) luceneQuery;
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
					sb.append("(");
					sb.append(booleanQuerySb);
					sb.append(")");
				}
			}
		} else if (luceneQuery instanceof TermQuery) {
			TermQuery termQuery = (TermQuery) luceneQuery;
			Term term = termQuery.getTerm();
			if (StringUtils.hasText(term.field())) {
				sb.append(term.field());
				sb.append(":");
			}
			sb.append("\"");
			sb.append(QueryParser.escape(term.text()));
			sb.append("\"");
		} else if (luceneQuery instanceof RawLuceneQuery) {
			RawLuceneQuery simpleQuery = (RawLuceneQuery) luceneQuery;
			if (StringUtils.hasText(simpleQuery.getQuery())) {
				sb.append("(");
				sb.append(simpleQuery.getQuery());
				sb.append(")");
			}
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
	
	private LuceneUtils() {
	}
	
}
