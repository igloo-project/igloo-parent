package fr.openwide.core.jpa.more.util.search.analysis.fr;

import org.apache.lucene.analysis.TokenStream;
import org.apache.solr.analysis.BaseTokenFilterFactory;

public class CoreMinimalFrenchStemFilterFactory extends BaseTokenFilterFactory {

	@Override
	public TokenStream create(TokenStream input) {
		return new CoreMinimalFrenchStemFilter(input);
	}

}
