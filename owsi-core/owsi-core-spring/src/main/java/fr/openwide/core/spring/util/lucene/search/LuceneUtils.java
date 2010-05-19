package fr.openwide.core.spring.util.lucene.search;

import fr.openwide.core.spring.util.StringUtils;

public final class LuceneUtils {
	
	public static String getAutocompleteQuery(String searchPattern) {
		String cleanSearchPattern = StringUtils.clean(searchPattern);
		
		StringBuilder autocompleteQuery = new StringBuilder();
		
		if(StringUtils.hasText(cleanSearchPattern)) {
			autocompleteQuery.append(cleanSearchPattern);
			autocompleteQuery.append("*");
		}
		
		return autocompleteQuery.toString();
	}
	
	public static String getSimilarityQuery(String searchPattern, Float minSimilarity) {
		String cleanSearchPattern = StringUtils.clean(searchPattern);
		
		StringBuilder similarityQuery = new StringBuilder();
		
		if(StringUtils.hasText(cleanSearchPattern)) {
			String[] searchPatternElements = StringUtils.delimitedListToStringArray(cleanSearchPattern, StringUtils.SPACE);
			
			for (int i = 0; i < searchPatternElements.length; i++) {
				similarityQuery.append(searchPatternElements[i]).append("~").append(minSimilarity.toString());
			}
		}
		
		return similarityQuery.toString();
	}
	
	private LuceneUtils() {
	}
	
}
