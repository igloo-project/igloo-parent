package test.jpa.more.search.query;

import static org.assertj.core.api.Assertions.assertThat;

import org.iglooproject.jpa.more.search.query.HibernateSearchUtils;
import org.junit.jupiter.api.Test;

class TestHibernateSearchUtils {

  @Test
  void test_escapeSpecialCharacters_nochange() {
    String term = "term";
    String expected = "term";

    String termEscaped = HibernateSearchUtils.escapeSpecialCharacters(term);

    assertThat(termEscaped).isEqualTo(expected);
  }

  @Test
  void test_escapeSpecialCharacters_and() {
    String term = "term+";
    String expected = "term\\+";

    String termEscaped = HibernateSearchUtils.escapeSpecialCharacters(term);

    assertThat(termEscaped).isEqualTo(expected);
  }

  @Test
  void test_escapeSpecialCharacters_or() {
    String term = "term|";
    String expected = "term\\|";

    String termEscaped = HibernateSearchUtils.escapeSpecialCharacters(term);

    assertThat(termEscaped).isEqualTo(expected);
  }

  @Test
  void test_escapeSpecialCharacters_not() {
    String term = "term-";
    String expected = "term\\-";

    String termEscaped = HibernateSearchUtils.escapeSpecialCharacters(term);

    assertThat(termEscaped).isEqualTo(expected);
  }

  @Test
  void test_escapeSpecialCharacters_wildcard() {
    String term = "term*";
    String expected = "term\\*";

    String termEscaped = HibernateSearchUtils.escapeSpecialCharacters(term);

    assertThat(termEscaped).isEqualTo(expected);
  }
}
