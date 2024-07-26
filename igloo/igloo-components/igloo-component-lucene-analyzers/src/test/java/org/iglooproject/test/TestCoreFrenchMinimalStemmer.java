package org.iglooproject.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.iglooproject.lucene.analysis.french.CoreFrenchMinimalStemmer;
import org.junit.jupiter.api.Test;

class TestCoreFrenchMinimalStemmer {

  private static final CoreFrenchMinimalStemmer STEMMER = new CoreFrenchMinimalStemmer();

  @Test
  void testStem() {
    assertEquals("stag", stem("stage"));
    assertEquals("stag", stem("stages"));

    assertEquals("cree", stem("creer"));

    assertEquals("noir", stem("noir"));
    assertEquals("noir", stem("noire"));
    assertEquals("noir", stem("noires"));
    assertEquals("noir", stem("noirs"));

    assertEquals("cheval", stem("cheval"));
    assertEquals("cheval", stem("chevaux"));

    assertEquals("chapeau", stem("chapeau"));
    assertEquals("chapeau", stem("chapeaux"));

    assertEquals("kit", stem("kits"));
    assertEquals("kit", stem("kit"));
  }

  public String stem(String word) {
    if (word == null) {
      throw new IllegalArgumentException("word cannot be null");
    }
    char[] charArray = word.toCharArray();
    int len = STEMMER.stem(charArray, word.length());
    return String.valueOf(charArray).substring(0, len);
  }
}
