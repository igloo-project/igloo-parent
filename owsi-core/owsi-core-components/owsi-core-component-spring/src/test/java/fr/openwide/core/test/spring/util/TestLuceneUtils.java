package fr.openwide.core.test.spring.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.QueryParser.Operator;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.WildcardQuery;
import org.junit.Test;

import fr.openwide.core.spring.util.lucene.search.LuceneUtils;
import fr.openwide.core.spring.util.lucene.search.RawLuceneQuery;

public class TestLuceneUtils {

	@Test
	public void testGetAutocompleteQuery() {
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
	public void testGetSimilarityQuery() {
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
	public void testGetQuery() {
		assertEquals("elephant de test", LuceneUtils.getQuery("éléphant de test"));
		assertEquals("elephant de* test*", LuceneUtils.getQuery("éléphant de* test*"));
		assertEquals("elephant test*", LuceneUtils.getQuery("éléphant * test*"));
		
		assertEquals("elephant OR de OR test", LuceneUtils.getQuery("éléphant de test", Operator.OR));
		assertEquals("elephant OR de* OR test*", LuceneUtils.getQuery("éléphant de* test*", Operator.OR));
		assertEquals("elephant OR test*", LuceneUtils.getQuery("éléphant * test*", Operator.OR));
	}
	
	@Test
	public void testToFilterRangeQuery() {
		assertNull(LuceneUtils.toFilterRangeQuery("number", null, null));
		assertEquals("number:[* TO 5]", LuceneUtils.toFilterRangeQuery("number", null, 5).toString());
		assertEquals("number:[5 TO *]", LuceneUtils.toFilterRangeQuery("number", 5, null).toString());
		assertEquals("number:[1 TO 5]", LuceneUtils.toFilterRangeQuery("number", 1, 5).toString());
		assertEquals("number:{1 TO 5]", LuceneUtils.toFilterRangeQuery("number", 1, 5, false, true).toString());
		assertEquals("number:[1 TO 5}", LuceneUtils.toFilterRangeQuery("number", 1, 5, true, false).toString());
		assertEquals("number:{1 TO 5}", LuceneUtils.toFilterRangeQuery("number", 1, 5, false, false).toString());
	}
	
	@Test
	public void testBooleanQueryToString() throws ParseException {
		BooleanQuery bq1 = new BooleanQuery();
		TermQuery query = new TermQuery(new Term("field1", "text1"));
		query.setBoost(2.0f);
		bq1.add(query, Occur.MUST);
		bq1.add(new TermQuery(new Term("field2", "text2")), Occur.MUST_NOT);
		
		BooleanQuery bq2 = new BooleanQuery();
		bq2.add(new FuzzyQuery(new Term("field3", "text3"), 1), Occur.MUST);
		bq2.add(new FuzzyQuery(new Term("field4", "text4"), 2), Occur.SHOULD);
		bq2.add(new TermQuery(new Term("field4", "text4")), Occur.SHOULD);
		bq2.add(new WildcardQuery(new Term("field8", "t*t?")), Occur.MUST);
		
		BooleanQuery bq3 = new BooleanQuery();
		bq3.add(new PrefixQuery(new Term("field5", "text5")), Occur.SHOULD);
		bq3.add(new PrefixQuery(new Term("field6", "text6")), Occur.MUST_NOT);
		
		BooleanQuery bq4 = new BooleanQuery();
		bq4.add(new WildcardQuery(new Term("field7", "text?")), Occur.SHOULD);
		bq4.add(new WildcardQuery(new Term("field8", "t*t?")), Occur.MUST);
		
		BooleanQuery bq5 = new BooleanQuery();
		bq5.add(new TermQuery(new Term("", "text9")), Occur.MUST_NOT);
		bq5.add(new TermQuery(new Term("", "text10")), Occur.SHOULD);
		
		BooleanQuery bq6 = new BooleanQuery();
		bq6.add(new TermQuery(new Term("", "text11")), Occur.MUST_NOT);
		bq6.add(new TermQuery(new Term("", "text12")), Occur.MUST);
		bq6.setBoost(0.8f);
		
		BooleanQuery finalQuery = new BooleanQuery();
		finalQuery.add(bq1, Occur.MUST);
		finalQuery.add(bq2, Occur.SHOULD);
		finalQuery.add(bq3, Occur.MUST_NOT);
		finalQuery.add(bq4, Occur.SHOULD);
		finalQuery.add(bq5, Occur.MUST);
		finalQuery.add(bq6, Occur.SHOULD);
		
		String stringQuery = LuceneUtils.queryToString(finalQuery);
		
		QueryParser parser = new QueryParser("", new StandardAnalyzer());
		Query parsedQuery = parser.parse(stringQuery);
		assertEquals(finalQuery, parsedQuery);
	}
	
	@Test
	public void testBooleanQueryWithOneClause() throws ParseException {
		BooleanQuery bq1 = new BooleanQuery();
		bq1.add(new TermQuery(new Term("", "text9")), Occur.MUST_NOT);
		
		BooleanQuery finalQuery = new BooleanQuery();
		finalQuery.add(bq1, Occur.MUST);
		
		String stringQuery = LuceneUtils.queryToString(finalQuery);
		
		QueryParser parser = new QueryParser("", new StandardAnalyzer());
		Query parsedQuery = parser.parse(stringQuery);
		
		assertEquals(stringQuery, LuceneUtils.queryToString(parsedQuery));
	}
	
	@Test
	public void testRawLuceneQueryToString() {
		assertEquals("(field1:\"text1\")", LuceneUtils.queryToString(new RawLuceneQuery("field1:\"text1\"")));
	}
	
	@Test
	public void testFuzzyQueryToString() {
		assertEquals("field1:text1~1", LuceneUtils.queryToString(new FuzzyQuery(new Term("field1", "text1"), 1)));
		assertEquals("text2~1", LuceneUtils.queryToString(new FuzzyQuery(new Term("", "text2"), 1)));
	}
	
	@Test
	public void testPrefixQueryToString() {
		assertEquals("field1:prefix1*", LuceneUtils.queryToString(new PrefixQuery(new Term("field1", "prefix1"))));
		assertEquals("prefix2*", LuceneUtils.queryToString(new PrefixQuery(new Term("", "prefix2"))));
	}
	
	@Test
	public void testWildcardQueryToString() {
		assertEquals("field1:t?xt1", LuceneUtils.queryToString(new WildcardQuery(new Term("field1", "t?xt1"))));
		assertEquals("t*t2", LuceneUtils.queryToString(new WildcardQuery(new Term("", "t*t2"))));
		assertEquals("text3", LuceneUtils.queryToString(new WildcardQuery(new Term("", "text3"))));
	}
}
