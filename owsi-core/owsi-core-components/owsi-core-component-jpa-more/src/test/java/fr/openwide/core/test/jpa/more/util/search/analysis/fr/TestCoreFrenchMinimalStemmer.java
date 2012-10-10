package fr.openwide.core.test.jpa.more.util.search.analysis.fr;

import junit.framework.Assert;

import org.junit.Test;

import fr.openwide.core.jpa.more.util.search.analysis.fr.CoreFrenchMinimalStemmer;

public class TestCoreFrenchMinimalStemmer {
	
	private static final CoreFrenchMinimalStemmer STEMMER = new CoreFrenchMinimalStemmer();
	
	@Test
	public void testStem() {
		Assert.assertEquals(4, stem("stage"));
		Assert.assertEquals(4, stem("stages"));
		
		Assert.assertEquals(4, stem("creer"));
		
		Assert.assertEquals(4, stem("noir"));
		Assert.assertEquals(4, stem("noire"));
		Assert.assertEquals(4, stem("noires"));
		Assert.assertEquals(4, stem("noirs"));
	}
	
	public int stem(String word) {
		if (word == null) {
			throw new IllegalArgumentException("word cannot be null");
		}
		return STEMMER.stem(word.toCharArray(), word.length());
	}

}
