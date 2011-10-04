package fr.openwide.core.test.spring.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import fr.openwide.core.spring.util.lucene.search.LuceneUtils;

public class TestLuceneUtils {

	@Test
	public void testGetAutocompleteQuery() {
		assertNull(LuceneUtils.getAutocompleteQuery(null));
		assertEquals("", LuceneUtils.getAutocompleteQuery(""));
		assertEquals("alfresc*", LuceneUtils.getAutocompleteQuery("alfresc"));
		assertEquals("alfresco-sha*", LuceneUtils.getAutocompleteQuery("alfresco-sha"));
		assertEquals("alfresco-sha1*", LuceneUtils.getAutocompleteQuery("alfresco-sha1"));
		assertEquals("alfresco sha1*", LuceneUtils.getAutocompleteQuery("alfresco sha1"));
		assertEquals("t es t*", LuceneUtils.getAutocompleteQuery("t' -_es**t"));
	}
	
	@Test
	public void testGetSimilarityQuery() {
		assertNull(LuceneUtils.getSimilarityQuery(null, 0.7f));
		assertEquals("", LuceneUtils.getSimilarityQuery("", 0.7f));
		assertEquals("alfresc~0.7", LuceneUtils.getSimilarityQuery("alfresc", 0.7f));
		assertEquals("alfresco-sha~0.7", LuceneUtils.getSimilarityQuery("alfresco-sha", 0.7f));
		assertEquals("alfresco-sha1~0.7", LuceneUtils.getSimilarityQuery("alfresco-sha1", 0.7f));
		assertEquals("alfresco~0.7 sha1~0.7", LuceneUtils.getSimilarityQuery("alfresco sha1", 0.7f));
		assertEquals("t~0.7 es~0.7 t~0.7", LuceneUtils.getSimilarityQuery("t' -_es**t", 0.7f));
	}
	
}
