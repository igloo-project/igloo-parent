package fr.openwide.core.jpa.search.analysis.fr;

import org.apache.lucene.analysis.TokenStream;
import org.apache.solr.analysis.BaseTokenFilterFactory;

public class CoreFrenchMinimalStemFilterFactory extends BaseTokenFilterFactory {

	@Override
	public TokenStream create(TokenStream input) {
		return new CoreFrenchMinimalStemFilter(input);
	}

}
