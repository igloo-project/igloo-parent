package org.iglooproject.lucene.analysis.french;

import java.util.Map;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.util.TokenFilterFactory;

public class CoreFrenchMinimalStemFilterFactory extends TokenFilterFactory {

  public CoreFrenchMinimalStemFilterFactory(Map<String, String> args) {
    super(args);
    if (!args.isEmpty()) {
      throw new IllegalArgumentException("Unknown parameters: " + args);
    }
  }

  @Override
  public TokenStream create(TokenStream input) {
    return new CoreFrenchMinimalStemFilter(input);
  }
}
