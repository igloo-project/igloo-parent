package org.iglooproject.test.search.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.bindgen.Bindable;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

import igloo.hibernateconfig.api.HibernateSearchAnalyzer;

@Entity
@Indexed
@Bindable
public class Searchable {
	
	public static final String MULTIPLE_INDEXES = "multipleIndexes";
	public static final String MULTIPLE_INDEXES_AUTOCOMPLETE = MULTIPLE_INDEXES + "Autocomplete";
	
	@Id
	@GeneratedValue
	@DocumentId
	public Long id;

	@Field()
	public String autocomplete;

	@Field(analyzer = @Analyzer(definition = HibernateSearchAnalyzer.KEYWORD))
	public String keyword;
	
	@Field(name = MULTIPLE_INDEXES, analyzer = @Analyzer(definition = HibernateSearchAnalyzer.TEXT))
	@Field(name = MULTIPLE_INDEXES_AUTOCOMPLETE, analyzer = @Analyzer(definition = HibernateSearchAnalyzer.TEXT_STEMMING))
	public String multipleIndexes;
	
	public String notIndexed;

}
