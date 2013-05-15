package fr.openwide.core.test.spring.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.util.Version;
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
	
	@Test
	public void testToFilterRangeQuery() {
		assertNull(LuceneUtils.toFilterRangeQuery("number", null, null));
		assertEquals("number:[* TO 5]", LuceneUtils.toFilterRangeQuery("number", null, 5).toString());
		assertEquals("number:[5 TO *]", LuceneUtils.toFilterRangeQuery("number", 5, null).toString());
		assertEquals("number:[1 TO 5]", LuceneUtils.toFilterRangeQuery("number", 1, 5).toString());
	}
	
	@Test
	public void testQueryToString() throws ParseException {
		BooleanQuery bq1 = new BooleanQuery();
		bq1.add(new TermQuery(new Term("field1", "text1")), Occur.MUST);
		bq1.add(new TermQuery(new Term("field2", "text2")), Occur.MUST_NOT);
		
		BooleanQuery bq2 = new BooleanQuery();
		bq2.add(new TermQuery(new Term("field3", "text3")), Occur.MUST);
		bq2.add(new TermQuery(new Term("field4", "text4")), Occur.SHOULD);
		
		BooleanQuery bq3 = new BooleanQuery();
		bq3.add(new TermQuery(new Term("field5", "text5")), Occur.SHOULD);
		bq3.add(new TermQuery(new Term("field6", "text6")), Occur.MUST_NOT);
		
		BooleanQuery bq4 = new BooleanQuery();
		bq4.add(new TermQuery(new Term("field7", "text7")), Occur.SHOULD);
		bq4.add(new TermQuery(new Term("field8", "text8")), Occur.MUST);
		
		BooleanQuery bq5 = new BooleanQuery();
		bq5.add(new TermQuery(new Term("", "text9")), Occur.MUST_NOT);
		bq5.add(new TermQuery(new Term("", "text10")), Occur.SHOULD);
		
		BooleanQuery bq6 = new BooleanQuery();
		bq6.add(new TermQuery(new Term("", "text11")), Occur.MUST_NOT);
		bq6.add(new TermQuery(new Term("", "text12")), Occur.MUST);
		
		BooleanQuery finalQuery = new BooleanQuery();
		finalQuery.add(bq1, Occur.MUST);
		finalQuery.add(bq2, Occur.SHOULD);
		finalQuery.add(bq3, Occur.MUST_NOT);
		finalQuery.add(bq4, Occur.SHOULD);
		finalQuery.add(bq5, Occur.MUST);
		finalQuery.add(bq6, Occur.SHOULD);
		
		String stringQuery = LuceneUtils.queryToString(finalQuery);
		
		QueryParser parser = new QueryParser(Version.LUCENE_36, "", new StandardAnalyzer(Version.LUCENE_36));
		Query parsedQuery = parser.parse(stringQuery);
		assertEquals(finalQuery, parsedQuery);
	}
	
}
