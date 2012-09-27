package fr.openwide.core.jpa.more.util.search.analysis.fr;

import java.io.IOException;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.KeywordAttribute;

public final class CoreMinimalFrenchStemFilter extends TokenFilter {
	private static final CoreMinimalFrenchStemmer STEMMER = new CoreMinimalFrenchStemmer();
	private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
	private final KeywordAttribute keywordAttr = addAttribute(KeywordAttribute.class);

	public CoreMinimalFrenchStemFilter(TokenStream input) {
		super(input);
	}

	@Override
	public boolean incrementToken() throws IOException {
		if (input.incrementToken()) {
			if (!keywordAttr.isKeyword()) {
				final int newlen = STEMMER.stem(termAtt.buffer(), termAtt.length());
				termAtt.setLength(newlen);
			}
			return true;
		} else {
			return false;
		}
	}
}