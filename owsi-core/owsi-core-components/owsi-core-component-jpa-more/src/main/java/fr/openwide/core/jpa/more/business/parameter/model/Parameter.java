package fr.openwide.core.jpa.more.business.parameter.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.apache.solr.analysis.ASCIIFoldingFilterFactory;
import org.apache.solr.analysis.KeywordTokenizerFactory;
import org.apache.solr.analysis.LowerCaseFilterFactory;
import org.apache.solr.analysis.PatternReplaceFilterFactory;
import org.apache.solr.analysis.TrimFilterFactory;
import org.apache.solr.analysis.WhitespaceTokenizerFactory;
import org.apache.solr.analysis.WordDelimiterFilterFactory;
import org.bindgen.Bindable;
import org.hibernate.annotations.Type;
import org.hibernate.search.annotations.AnalyzerDef;
import org.hibernate.search.annotations.AnalyzerDefs;
import org.hibernate.search.annotations.TokenFilterDef;
import org.hibernate.search.annotations.TokenizerDef;

import fr.openwide.core.commons.util.CloneUtils;
import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.search.util.HibernateSearchAnalyzer;

@Entity
@Bindable
@AnalyzerDefs({
	@AnalyzerDef(name = HibernateSearchAnalyzer.KEYWORD,
			tokenizer = @TokenizerDef(factory = KeywordTokenizerFactory.class)
	),
	@AnalyzerDef(name = HibernateSearchAnalyzer.TEXT,
			tokenizer = @TokenizerDef(factory = WhitespaceTokenizerFactory.class),
			filters = {
					@TokenFilterDef(factory = ASCIIFoldingFilterFactory.class),
					@TokenFilterDef(factory = WordDelimiterFilterFactory.class, params = {
									@org.hibernate.search.annotations.Parameter(name = "generateWordParts", value = "1"),
									@org.hibernate.search.annotations.Parameter(name = "generateNumberParts", value = "1"),
									@org.hibernate.search.annotations.Parameter(name = "catenateWords", value = "0"),
									@org.hibernate.search.annotations.Parameter(name = "catenateNumbers", value = "0"),
									@org.hibernate.search.annotations.Parameter(name = "catenateAll", value = "0"),
									@org.hibernate.search.annotations.Parameter(name = "splitOnCaseChange", value = "0"),
									@org.hibernate.search.annotations.Parameter(name = "splitOnNumerics", value = "0"),
									@org.hibernate.search.annotations.Parameter(name = "preserveOriginal", value = "1")
							}
					),
					@TokenFilterDef(factory = LowerCaseFilterFactory.class)
			}
	),
	@AnalyzerDef(name = HibernateSearchAnalyzer.TEXT_SORT,
			tokenizer = @TokenizerDef(factory = KeywordTokenizerFactory.class),
			filters = {
					@TokenFilterDef(factory = ASCIIFoldingFilterFactory.class),
					@TokenFilterDef(factory = TrimFilterFactory.class),
					@TokenFilterDef(factory = LowerCaseFilterFactory.class),
					@TokenFilterDef(factory = PatternReplaceFilterFactory.class, params = {
						@org.hibernate.search.annotations.Parameter(name = "pattern", value = "([^0-9a-z ])"),
						@org.hibernate.search.annotations.Parameter(name = "replacement", value = ""),
						@org.hibernate.search.annotations.Parameter(name = "replace", value = "all")
					})
			}
	)
})
public class Parameter extends GenericEntity<Long, Parameter> {
	
	private static final long serialVersionUID = 4739408616523513971L;

	@Id
	@GeneratedValue
	private Long id;

	@Column(nullable = false, unique = true)
	private String name;

	@Column
	private Boolean booleanValue;

	@Column
	private Integer integerValue;

	@Column
	private Float floatValue;

	@Column
	@Type(type = "org.hibernate.type.StringClobType")
	private String stringValue;

	@Column
	private Date dateValue;

	public Parameter() {
		super();
	}

	public Parameter(String name, Boolean value) {
		super();

		setName(name);
		setBooleanValue(value);
	}

	public Parameter(String name, Integer value) {
		super();

		setName(name);
		setIntegerValue(value);
	}

	public Parameter(String name, Float value) {
		super();

		setName(name);
		setFloatValue(value);
	}

	public Parameter(String name, String value) {
		super();

		setName(name);
		setStringValue(value);
	}

	public Parameter(String name, Date value) {
		super();

		setName(name);
		setDateValue(value);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getBooleanValue() {
		return booleanValue;
	}

	public void setBooleanValue(Boolean booleanValue) {
		this.booleanValue = booleanValue;
	}

	public Integer getIntegerValue() {
		return integerValue;
	}

	public void setIntegerValue(Integer integerValue) {
		this.integerValue = integerValue;
	}

	public Float getFloatValue() {
		return floatValue;
	}

	public void setFloatValue(Float floatValue) {
		this.floatValue = floatValue;
	}

	public String getStringValue() {
		return stringValue;
	}

	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}

	public Date getDateValue() {
		return CloneUtils.clone(dateValue);
	}

	public void setDateValue(Date dateValue) {
		this.dateValue = CloneUtils.clone(dateValue);
	}

	public Object getValue() {
		if (booleanValue != null) {
			return booleanValue;
		}
		if (integerValue != null) {
			return integerValue;
		}
		if (floatValue != null) {
			return floatValue;
		}
		if (stringValue != null) {
			return stringValue;
		}
		if (dateValue != null) {
			return CloneUtils.clone(dateValue);
		}
		return null;
	}

	@Override
	public String getDisplayName() {
		StringBuilder displayName = new StringBuilder();
		displayName.append(name);
		displayName.append(':');
		displayName.append(getValue());

		return displayName.toString();
	}

	@Override
	public String getNameForToString() {
		return name;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}
}
