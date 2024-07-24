package org.iglooproject.test.search.model;

import igloo.hibernateconfig.api.HibernateSearchAnalyzer;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.bindgen.Bindable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;

@Entity
@Indexed
@Bindable
public class Searchable {

  public static final String KEYWORD = "KEYWORD";

  public static final String MULTIPLE_INDEXES = "multipleIndexes";
  public static final String MULTIPLE_INDEXES_AUTOCOMPLETE = MULTIPLE_INDEXES + "Autocomplete";

  @Id @GeneratedValue public Long id;

  @GenericField public String autocomplete;

  @KeywordField(name = KEYWORD)
  public String keyword;

  @FullTextField(name = MULTIPLE_INDEXES, analyzer = HibernateSearchAnalyzer.TEXT)
  @FullTextField(
      name = MULTIPLE_INDEXES_AUTOCOMPLETE,
      analyzer = HibernateSearchAnalyzer.TEXT_STEMMING)
  public String multipleIndexes;

  public String notIndexed;
}
