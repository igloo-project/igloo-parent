package fr.openwide.core.test.jpa.more.util.search.analysis.fr;

import org.junit.Assert;
import org.junit.Test;

import fr.openwide.core.jpa.more.util.search.analysis.fr.CoreFrenchMinimalStemmer;

public class TestCoreFrenchMinimalStemmer {
	
	private static final CoreFrenchMinimalStemmer STEMMER = new CoreFrenchMinimalStemmer();
	
	@Test
	public void testStem() {
		Assert.assertEquals("stag", stem("stage"));
		Assert.assertEquals("stag", stem("stages"));
		
		Assert.assertEquals("cree", stem("creer"));
		
		Assert.assertEquals("noir", stem("noir"));
		Assert.assertEquals("noir", stem("noire"));
		Assert.assertEquals("noir", stem("noires"));
		Assert.assertEquals("noir", stem("noirs"));
		
		Assert.assertEquals("cheval", stem("cheval"));
		Assert.assertEquals("cheval", stem("chevaux"));
		
		Assert.assertEquals("chapeau", stem("chapeau"));
		Assert.assertEquals("chapeau", stem("chapeaux"));
		
		Assert.assertEquals("kit", stem("kits"));
		Assert.assertEquals("kit", stem("kit"));
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
