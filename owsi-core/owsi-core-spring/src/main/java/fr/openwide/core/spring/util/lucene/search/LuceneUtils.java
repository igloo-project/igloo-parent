package fr.openwide.core.spring.util.lucene.search;

import fr.openwide.core.spring.util.StringUtils;

public final class LuceneUtils {
	
	public static String getAutocompleteQuery(String searchPattern) {
		String cleanSearchPattern = cleanSearchPattern(searchPattern);
		
		if(StringUtils.hasText(cleanSearchPattern)) {
			StringBuilder autocompleteQuery = new StringBuilder();
			
			autocompleteQuery.append(cleanSearchPattern);
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
	
	private LuceneUtils() {
	}
	
}
