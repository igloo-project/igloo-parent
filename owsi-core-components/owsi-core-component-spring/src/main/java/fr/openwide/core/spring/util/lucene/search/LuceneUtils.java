package fr.openwide.core.spring.util.lucene.search;

import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;

import fr.openwide.core.spring.util.StringUtils;

public final class LuceneUtils {
	
	private static final int ENABLE_WILDCARD_MIN_CHARS = 2;
	
	public static final String LOWERCASE_OPERATORS_PARAM = "lowercaseOperators";
	
	public static final String BOOST_PARAMETER_PREFIX = "^";
	
	public static String getAutocompleteQuery(String searchPattern) {
		String cleanSearchPattern = cleanSearchPattern(searchPattern);
		
		if(StringUtils.hasText(cleanSearchPattern) && cleanSearchPattern.length() >= ENABLE_WILDCARD_MIN_CHARS) {
			StringBuilder autocompleteQuery = new StringBuilder(cleanSearchPattern);
			autocompleteQuery.append("*");
			
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
	
	public static String toString(Query luceneQuery) {
		StringBuilder sb = new StringBuilder();
		if (luceneQuery instanceof BooleanQuery) {
			BooleanQuery booleanQuery = (BooleanQuery) luceneQuery;
			if (booleanQuery.getClauses().length > 0) {
				sb.append("(");
				for (BooleanClause clause : booleanQuery.getClauses()) {
					sb.append(clause.getOccur().toString());
					sb.append(toString(clause.getQuery()));
					sb.append(" ");
				}
				sb.append(")");
			}
		} else if (luceneQuery instanceof TermQuery) {
			TermQuery termQuery = (TermQuery) luceneQuery;
			Term term = termQuery.getTerm();
			sb.append(term.field());
			sb.append(":\"");
			sb.append(QueryParser.escape(term.text()));
			sb.append("\"");
		} else if (luceneQuery instanceof RawLuceneQuery) {
			sb.append("(");
			RawLuceneQuery simpleQuery = (RawLuceneQuery) luceneQuery;
			sb.append(simpleQuery.getQuery());
			sb.append(")");
		} else {
			throw new IllegalStateException(String.format("Query of type %1$s not supported",
					luceneQuery.getClass().getName()));
		}
		if (Float.compare(1.0f, luceneQuery.getBoost()) != 0) {
			sb.append(BOOST_PARAMETER_PREFIX);
			sb.append(luceneQuery.getBoost());
		}
		return sb.toString();
	}
	
	private LuceneUtils() {
	}
	
}
