package org.iglooproject.jpa.hibernate.analyzers;

import org.hibernate.search.elasticsearch.analyzer.definition.ElasticsearchAnalysisDefinitionProvider;
import org.hibernate.search.elasticsearch.analyzer.definition.ElasticsearchAnalysisDefinitionRegistryBuilder;
import org.iglooproject.jpa.search.util.HibernateSearchAnalyzer;
import org.iglooproject.jpa.search.util.HibernateSearchNormalizer;

public class CoreElasticSearchAnalyzersDefinitionProvider implements ElasticsearchAnalysisDefinitionProvider {

	private static final String KEYWORDTOKENIZER = "KeywordTokenizer";
	private static final String WHITESPACETOKENIZER = "WhitespaceTokenizer";
	
	private static final String ASCIIFOLDINGFILTER = "ASCIIFoldingFilter";
	private static final String LOWERCASEFILTER= "LowerCaseFilter";
	private static final String WORDDELIMITERFILTER = "WordDelimiterFilter";
	private static final String ELASTICSEARCHTOKENFILTER = "CoreFrenchMinimalStem";
	private static final String PATTERNREPLACEFILTERPUNCTUATION = "text_sort_replace_punctuations";
	private static final String PATTERNREPLACEFILTERNUMBER = "text_sort_replace_numbers";
	private static final String TRIMFILTER = "TrimFilter";
	
	@Override
	public void register(ElasticsearchAnalysisDefinitionRegistryBuilder builder) {
		builder.tokenizer(KEYWORDTOKENIZER).type("keyword");
		builder.tokenizer(WHITESPACETOKENIZER).type("whitespace");
		
		builder.tokenFilter(ASCIIFOLDINGFILTER).type("asciifolding");
		builder.tokenFilter(LOWERCASEFILTER).type("lowercase");
		builder.tokenFilter(WORDDELIMITERFILTER).type("word_delimiter")
				.param("generateWordParts", "1")
				.param("generateNumberParts", "1")
				.param("catenateWords", "0")
				.param("catenateNumbers", "0")
				.param("catenateAll", "0")
				.param("splitOnCaseChange", "0")
				.param("splitOnNumerics", "0")
				.param("preserveOriginal", "1");
		builder.tokenFilter(ELASTICSEARCHTOKENFILTER).type("corefrenchminimalstem");
		builder.tokenFilter(PATTERNREPLACEFILTERPUNCTUATION).type("pattern_replace")
				.param("pattern", "('-&\\.,\\(\\))")
				.param("replacement", " ")
				.param("replace", "all");
		builder.tokenFilter(PATTERNREPLACEFILTERNUMBER).type("pattern_replace")
				.param("pattern", "([^0-9\\p{L} ])")
				.param("replacement", "")
				.param("replace", "all");
		builder.tokenFilter(TRIMFILTER).type("trim");
		
		
		builder.analyzer(HibernateSearchAnalyzer.KEYWORD).withTokenizer(KEYWORDTOKENIZER);
		
		builder.analyzer(HibernateSearchAnalyzer.KEYWORD_CLEAN).withTokenizer(KEYWORDTOKENIZER)
				.withTokenFilters(ASCIIFOLDINGFILTER, LOWERCASEFILTER);
		
		builder.analyzer(HibernateSearchAnalyzer.TEXT).withTokenizer(WHITESPACETOKENIZER)
				.withTokenFilters(ASCIIFOLDINGFILTER, WORDDELIMITERFILTER, LOWERCASEFILTER);
		
		builder.analyzer(HibernateSearchAnalyzer.TEXT_STEMMING).withTokenizer(WHITESPACETOKENIZER)
				.withTokenFilters(ASCIIFOLDINGFILTER, WORDDELIMITERFILTER, LOWERCASEFILTER, ELASTICSEARCHTOKENFILTER);
		
		builder.normalizer(HibernateSearchNormalizer.KEYWORD);
		
		builder.normalizer(HibernateSearchNormalizer.KEYWORD_CLEAN)
				.withTokenFilters(ASCIIFOLDINGFILTER, LOWERCASEFILTER);
		
		builder.normalizer(HibernateSearchNormalizer.TEXT)
				.withTokenFilters(ASCIIFOLDINGFILTER, LOWERCASEFILTER, PATTERNREPLACEFILTERPUNCTUATION, PATTERNREPLACEFILTERNUMBER, TRIMFILTER);
	}

}
