package org.iglooproject.test.search.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.bindgen.Bindable;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Fields;
import org.hibernate.search.annotations.Indexed;
import org.iglooproject.jpa.search.util.HibernateSearchAnalyzer;

@Entity
@Indexed
@Bindable
public class Searchable {
	public static final String MULTIPLE_INDEXES_FIELD_NAME = "multipleIndexes";
	public static final String MULTIPLE_INDEXES_SORT_FIELD_NAME = MULTIPLE_INDEXES_FIELD_NAME + "Sort";
	
	@Id
	@GeneratedValue
	@DocumentId
	public Long id;

	@Field()
	public String autocomplete;

	@Field(analyzer = @Analyzer(definition = HibernateSearchAnalyzer.KEYWORD))
	public String keyword;
	
	@Fields({
		@Field(analyzer = @Analyzer(definition = HibernateSearchAnalyzer.TEXT)),
		@Field(name = MULTIPLE_INDEXES_SORT_FIELD_NAME, analyzer = @Analyzer(definition = HibernateSearchAnalyzer.TEXT_SORT))
	})
	public String multipleIndexes;
	
	public String notIndexed;

}
