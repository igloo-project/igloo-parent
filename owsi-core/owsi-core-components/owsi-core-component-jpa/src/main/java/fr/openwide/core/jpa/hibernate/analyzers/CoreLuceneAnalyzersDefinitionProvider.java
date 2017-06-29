package fr.openwide.core.jpa.hibernate.analyzers;

import org.apache.lucene.analysis.core.KeywordTokenizerFactory;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.core.WhitespaceTokenizerFactory;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilterFactory;
import org.apache.lucene.analysis.miscellaneous.TrimFilterFactory;
import org.apache.lucene.analysis.miscellaneous.WordDelimiterFilterFactory;
import org.apache.lucene.analysis.pattern.PatternReplaceFilterFactory;
import org.hibernate.search.analyzer.definition.LuceneAnalyzerDefinitionRegistryBuilder;
import org.hibernate.search.analyzer.definition.spi.LuceneAnalyzerDefinitionProvider;

import fr.openwide.core.jpa.search.analysis.fr.CoreFrenchMinimalStemFilterFactory;
import fr.openwide.core.jpa.search.util.HibernateSearchAnalyzer;

public class CoreLuceneAnalyzersDefinitionProvider implements LuceneAnalyzerDefinitionProvider{

	@Override
	public void register(LuceneAnalyzerDefinitionRegistryBuilder builder) {
		registerWithPrefix("", builder);
	}

	protected void registerWithPrefix(String prefix, LuceneAnalyzerDefinitionRegistryBuilder builder) {
		builder.analyzer(prefix + HibernateSearchAnalyzer.KEYWORD).tokenizer(KeywordTokenizerFactory.class);
		
		builder.analyzer(prefix + HibernateSearchAnalyzer.KEYWORD_CLEAN).tokenizer(KeywordTokenizerFactory.class)
			.tokenFilter(ASCIIFoldingFilterFactory.class)
			.tokenFilter(LowerCaseFilterFactory.class);
		
		builder.analyzer(prefix + HibernateSearchAnalyzer.TEXT).tokenizer(WhitespaceTokenizerFactory.class)
				.tokenFilter(ASCIIFoldingFilterFactory.class)
				.tokenFilter(WordDelimiterFilterFactory.class)
						.param("generateWordParts", "1")
						.param("generateNumberParts", "1")
						.param("catenateWords", "0")
						.param("catenateNumbers", "0")
						.param("catenateAll", "0")
						.param("splitOnCaseChange", "0")
						.param("splitOnNumerics", "0")
						.param("preserveOriginal", "1")
				.tokenFilter(LowerCaseFilterFactory.class);
		
		builder.analyzer(prefix + HibernateSearchAnalyzer.TEXT_STEMMING).tokenizer(WhitespaceTokenizerFactory.class)
				.tokenFilter(ASCIIFoldingFilterFactory.class)
				.tokenFilter(WordDelimiterFilterFactory.class)
						.param("generateWordParts", "1")
						.param("generateNumberParts", "1")
						.param("catenateWords", "0")
						.param("catenateNumbers", "0")
						.param("catenateAll", "0")
						.param("splitOnCaseChange", "0")
						.param("splitOnNumerics", "0")
						.param("preserveOriginal", "1")
				.tokenFilter(LowerCaseFilterFactory.class)
				.tokenFilter(CoreFrenchMinimalStemFilterFactory.class);
		
		builder.analyzer(prefix + HibernateSearchAnalyzer.TEXT_SORT).tokenizer(KeywordTokenizerFactory.class)
				.tokenFilter(ASCIIFoldingFilterFactory.class)
				.tokenFilter(LowerCaseFilterFactory.class)
				.tokenFilter(PatternReplaceFilterFactory.class)
						.param("pattern", "('-&\\.,\\(\\))")
						.param("replacement", " ")
						.param("replace", "all")
				.tokenFilter(PatternReplaceFilterFactory.class)
						.param("pattern", "([^0-9\\p{L} ])")
						.param("replacement", "")
						.param("replace", "all")
				.tokenFilter(TrimFilterFactory.class);
		
	}

}
