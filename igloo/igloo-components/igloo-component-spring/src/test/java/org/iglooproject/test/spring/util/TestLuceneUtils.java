package org.iglooproject.test.spring.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.QueryParser.Operator;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.BoostQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.WildcardQuery;
import org.assertj.core.api.Assertions;
import org.iglooproject.spring.util.lucene.search.LuceneUtils;
import org.iglooproject.spring.util.lucene.search.RawLuceneQuery;
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
    assertEquals(
        "alfresco AND sha*", LuceneUtils.getAutocompleteQuery("alfresco-sha", Operator.AND));
    assertEquals(
        "alfresco AND sha1*", LuceneUtils.getAutocompleteQuery("alfresco-sha1", Operator.AND));
    assertEquals(
        "alfresco AND sha1*", LuceneUtils.getAutocompleteQuery("alfresco sha1", Operator.AND));
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
    assertEquals(
        "alfresco~2 AND sha~2", LuceneUtils.getSimilarityQuery("alfresco-sha", 2, Operator.AND));
    assertEquals(
        "alfresco~2 AND sha1~2", LuceneUtils.getSimilarityQuery("alfresco-sha1", 2, Operator.AND));
    assertEquals(
        "alfresco~2 AND sha1~2", LuceneUtils.getSimilarityQuery("alfresco sha1", 2, Operator.AND));
    assertEquals(
        "t~2 AND es~2 AND t~2", LuceneUtils.getSimilarityQuery("t' -_es**t", 2, Operator.AND));
  }

  @Test
  void testGetQuery() {
    assertEquals("elephant de test", LuceneUtils.getQuery("éléphant de test"));
    assertEquals("elephant de* test*", LuceneUtils.getQuery("éléphant de* test*"));
    assertEquals("elephant test*", LuceneUtils.getQuery("éléphant * test*"));

    assertEquals("elephant OR de OR test", LuceneUtils.getQuery("éléphant de test", Operator.OR));
    assertEquals(
        "elephant OR de* OR test*", LuceneUtils.getQuery("éléphant de* test*", Operator.OR));
    assertEquals("elephant OR test*", LuceneUtils.getQuery("éléphant * test*", Operator.OR));
  }

  @Test
  void testToFilterRangeQuery() {
    // LAL - changed on 2018.03.06 - throw an exception for min and max == null
    Assertions.assertThatCode(() -> LuceneUtils.toFilterRangeQuery("number", null, null))
        .isInstanceOf(IllegalArgumentException.class);
    assertEquals("number:[* TO 5]", LuceneUtils.toFilterRangeQuery("number", null, 5).toString());
    assertEquals("number:[5 TO *]", LuceneUtils.toFilterRangeQuery("number", 5, null).toString());
    assertEquals("number:[1 TO 5]", LuceneUtils.toFilterRangeQuery("number", 1, 5).toString());
    assertEquals(
        "number:{1 TO 5]", LuceneUtils.toFilterRangeQuery("number", 1, 5, false, true).toString());
    assertEquals(
        "number:[1 TO 5}", LuceneUtils.toFilterRangeQuery("number", 1, 5, true, false).toString());
    assertEquals(
        "number:{1 TO 5}", LuceneUtils.toFilterRangeQuery("number", 1, 5, false, false).toString());
  }

  @Test
  void testBooleanQueryToString() throws ParseException {
    BooleanQuery.Builder bq1Builder = new BooleanQuery.Builder();
    TermQuery queryTerm = new TermQuery(new Term("field1", "text1"));
    BoostQuery query = new BoostQuery(queryTerm, 2.0f);

    bq1Builder.add(query, Occur.MUST);
    bq1Builder.add(new TermQuery(new Term("field2", "text2")), Occur.MUST_NOT);

    BooleanQuery.Builder bq2Builder = new BooleanQuery.Builder();
    bq2Builder.add(new FuzzyQuery(new Term("field3", "text3"), 1), Occur.MUST);
    bq2Builder.add(new FuzzyQuery(new Term("field4", "text4"), 2), Occur.SHOULD);
    bq2Builder.add(new TermQuery(new Term("field4", "text4")), Occur.SHOULD);
    bq2Builder.add(new WildcardQuery(new Term("field8", "t*t?")), Occur.MUST);

    BooleanQuery.Builder bq3Builder = new BooleanQuery.Builder();
    bq3Builder.add(new PrefixQuery(new Term("field5", "text5")), Occur.SHOULD);
    bq3Builder.add(new PrefixQuery(new Term("field6", "text6")), Occur.MUST_NOT);

    BooleanQuery.Builder bq4Builder = new BooleanQuery.Builder();
    bq4Builder.add(new WildcardQuery(new Term("field7", "text?")), Occur.SHOULD);
    bq4Builder.add(new WildcardQuery(new Term("field8", "t*t?")), Occur.MUST);

    BooleanQuery.Builder bq5Builder = new BooleanQuery.Builder();
    bq5Builder.add(new TermQuery(new Term("", "text9")), Occur.MUST_NOT);
    bq5Builder.add(new TermQuery(new Term("", "text10")), Occur.SHOULD);

    BooleanQuery.Builder bq6Builder = new BooleanQuery.Builder();
    bq6Builder.add(new TermQuery(new Term("", "text11")), Occur.MUST_NOT);
    bq6Builder.add(new TermQuery(new Term("", "text12")), Occur.MUST);

    BooleanQuery bq6Boolean = bq6Builder.build();
    BoostQuery bq6 = new BoostQuery(bq6Boolean, 0.8f);

    BooleanQuery.Builder finalQueryBuilder = new BooleanQuery.Builder();
    finalQueryBuilder.add(bq1Builder.build(), Occur.MUST);
    finalQueryBuilder.add(bq2Builder.build(), Occur.SHOULD);
    finalQueryBuilder.add(bq3Builder.build(), Occur.MUST_NOT);
    finalQueryBuilder.add(bq4Builder.build(), Occur.SHOULD);
    finalQueryBuilder.add(bq5Builder.build(), Occur.MUST);
    finalQueryBuilder.add(bq6, Occur.SHOULD);

    String stringQuery = LuceneUtils.queryToString(finalQueryBuilder.build());

    QueryParser parser = new QueryParser("", new StandardAnalyzer());
    Query parsedQuery = parser.parse(stringQuery);
    assertEquals(parser.parse(finalQueryBuilder.build().toString()), parsedQuery);
  }

  @Test
  void testBooleanQueryWithOneClause() throws ParseException {
    BooleanQuery.Builder bq1Builder = new BooleanQuery.Builder();
    bq1Builder.add(new TermQuery(new Term("", "text9")), Occur.MUST_NOT);

    BooleanQuery.Builder finalQueryBuilder = new BooleanQuery.Builder();
    finalQueryBuilder.add(bq1Builder.build(), Occur.MUST);

    String stringQuery = LuceneUtils.queryToString(finalQueryBuilder.build());

    QueryParser parser = new QueryParser("", new StandardAnalyzer());
    Query parsedQuery = parser.parse(stringQuery);

    assertEquals(stringQuery, LuceneUtils.queryToString(parsedQuery));
  }

  @Test
  void testRawLuceneQueryToString() {
    assertEquals(
        "(field1:\"text1\")", LuceneUtils.queryToString(new RawLuceneQuery("field1:\"text1\"")));
  }

  @Test
  void testFuzzyQueryToString() {
    assertEquals(
        "field1:text1~1",
        LuceneUtils.queryToString(new FuzzyQuery(new Term("field1", "text1"), 1)));
    assertEquals("text2~1", LuceneUtils.queryToString(new FuzzyQuery(new Term("", "text2"), 1)));
  }

  @Test
  void testPrefixQueryToString() {
    assertEquals(
        "field1:prefix1*",
        LuceneUtils.queryToString(new PrefixQuery(new Term("field1", "prefix1"))));
    assertEquals("prefix2*", LuceneUtils.queryToString(new PrefixQuery(new Term("", "prefix2"))));
  }

  @Test
  void testWildcardQueryToString() {
    assertEquals(
        "field1:t?xt1", LuceneUtils.queryToString(new WildcardQuery(new Term("field1", "t?xt1"))));
    assertEquals("t*t2", LuceneUtils.queryToString(new WildcardQuery(new Term("", "t*t2"))));
    assertEquals("text3", LuceneUtils.queryToString(new WildcardQuery(new Term("", "text3"))));
  }
}
