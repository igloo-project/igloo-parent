package org.iglooproject.lucene.analysis.french;

import java.io.IOException;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.KeywordAttribute;

@SuppressWarnings(
    "squid:S2160") // equals automatically handle custom attributes with addAttribute() method
public final class CoreFrenchMinimalStemFilter extends TokenFilter {

  public static final String STEMMER_NAME = "corefrenchminimalstem";
  private static final CoreFrenchMinimalStemmer STEMMER = new CoreFrenchMinimalStemmer();

  private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
  private final KeywordAttribute keywordAttr = addAttribute(KeywordAttribute.class);

  public CoreFrenchMinimalStemFilter(TokenStream input) {
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
