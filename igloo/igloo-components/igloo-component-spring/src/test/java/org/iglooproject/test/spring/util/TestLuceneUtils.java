package org.iglooproject.test.spring.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.lucene.queryparser.classic.QueryParser.Operator;
import org.iglooproject.spring.util.lucene.search.LuceneUtils;
import org.junit.jupiter.api.Test;

class TestLuceneUtils {

	@Test
	void testGetAutocompleteQuery() {
		assertEquals(null, LuceneUtils.getAutocompleteQuery(null));
		assertEquals("", LuceneUtils.getAutocompleteQuery(""));
		
		assertEquals("alfresc*", LuceneUtils.getAutocompleteQuery("alfresc"));
		assertEquals("alfresc*", LuceneUtils.getAutocompleteQuery("alfresc*"));
		assertEquals("alfresco sha*", LuceneUtils.getAutocompleteQuery("alfresco-sha"));
		assertEquals("alfresco sha1*", LuceneUtils.getAutocompleteQuery("alfresco-sha1"));
		assertEquals("alfresco sha1*", LuceneUtils.getAutocompleteQuery("alfresco sha1"));
		assertEquals("t es t*", LuceneUtils.getAutocompleteQuery("t' -_es**t"));
		
		assertEquals("alfresc*", LuceneUtils.getAutocompleteQuery("alfresc", Operator.AND));
		assertEquals("alfresc*", LuceneUtils.getAutocompleteQuery("alfresc*", Operator.AND));
		assertEquals("alfresco AND sha*", LuceneUtils.getAutocompleteQuery("alfresco-sha", Operator.AND));
		assertEquals("alfresco AND sha1*", LuceneUtils.getAutocompleteQuery("alfresco-sha1", Operator.AND));
		assertEquals("alfresco AND sha1*", LuceneUtils.getAutocompleteQuery("alfresco sha1", Operator.AND));
		assertEquals("t AND es AND t*", LuceneUtils.getAutocompleteQuery("t' -_es**t", Operator.AND));
	}
	
	@Test
	void testGetSimilarityQuery() {
		assertEquals("alfresc~2", LuceneUtils.getSimilarityQuery("alfresc", 2));
		assertEquals("alfresco~2 sha~2", LuceneUtils.getSimilarityQuery("alfresco-sha", 2));
		assertEquals("alfresco~2 sha1~2", LuceneUtils.getSimilarityQuery("alfresco-sha1", 2));
		assertEquals("alfresco~2 sha1~2", LuceneUtils.getSimilarityQuery("alfresco sha1", 2));
		assertEquals("t~2 es~2 t~2", LuceneUtils.getSimilarityQuery("t' -_es**t", 2));
		
		assertEquals("alfresc~2", LuceneUtils.getSimilarityQuery("alfresc", 2, Operator.AND));
		assertEquals("alfresco~2 AND sha~2", LuceneUtils.getSimilarityQuery("alfresco-sha", 2, Operator.AND));
		assertEquals("alfresco~2 AND sha1~2", LuceneUtils.getSimilarityQuery("alfresco-sha1", 2, Operator.AND));
		assertEquals("alfresco~2 AND sha1~2", LuceneUtils.getSimilarityQuery("alfresco sha1", 2, Operator.AND));
		assertEquals("t~2 AND es~2 AND t~2", LuceneUtils.getSimilarityQuery("t' -_es**t", 2, Operator.AND));
	}
	
	@Test
	void testGetQuery() {
		assertEquals("elephant de test", LuceneUtils.getQuery("éléphant de test"));
		assertEquals("elephant de* test*", LuceneUtils.getQuery("éléphant de* test*"));
		assertEquals("elephant test*", LuceneUtils.getQuery("éléphant * test*"));
		
		assertEquals("elephant OR de OR test", LuceneUtils.getQuery("éléphant de test", Operator.OR));
		assertEquals("elephant OR de* OR test*", LuceneUtils.getQuery("éléphant de* test*", Operator.OR));
		assertEquals("elephant OR test*", LuceneUtils.getQuery("éléphant * test*", Operator.OR));
	}
	
}
