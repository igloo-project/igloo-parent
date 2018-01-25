package org.iglooproject.jpa.more.business.parameter.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.apache.lucene.analysis.core.KeywordTokenizerFactory;
import org.bindgen.Bindable;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Type;
import org.hibernate.search.annotations.AnalyzerDef;
import org.hibernate.search.annotations.TokenizerDef;
import org.iglooproject.commons.util.CloneUtils;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.spring.property.model.PropertyId;

@Entity
@Bindable
//Needed to trigger LuceneEmbeddedIndexManagerType.INSTANCE for registry
@AnalyzerDef(name = "FakeAnalyzer",
	tokenizer = @TokenizerDef(factory = KeywordTokenizerFactory.class)
)
public class Parameter extends GenericEntity<Long, Parameter> {
	
	private static final long serialVersionUID = 4739408616523513971L;

	@Id
	@GeneratedValue
	private Long id;

	@NaturalId
	@Column(nullable = false, unique = true)
	private String name;

	@Column
	@Type(type = "org.iglooproject.jpa.hibernate.usertype.StringClobType")
	private String stringValue;

	/**
	 * @deprecated Use {@code stringValue} instead with {@link PropertyId}.
	 * Previously, old field for parameter date value storage.
	 * Now, Misuse for DataUpgrade execution by flyway.
	 */
	@Deprecated
	@Column
	private Date dateValue; // 
	
	public Parameter() {
		super();
	}

	public Parameter(String name, String value) {
		super();
		setName(name);
		setStringValue(value);
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStringValue() {
		return stringValue;
	}

	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}

	/**
	 * @deprecated Use {@code stringValue} instead with {@link PropertyId}.
	 * Previously, old field for parameter date value storage.
	 * Now, Misuse for DataUpgrade execution by flyway.
	 */
	@Deprecated
	public Date getDateValue() {
		return CloneUtils.clone(dateValue);
	}

	/**
	 * @deprecated Use {@code stringValue} instead with {@link PropertyId}.
	 * Previously, old field for parameter date value storage.
	 * Now, Misuse for DataUpgrade execution by flyway.
	 */
	@Deprecated
	public void setDateValue(Date dateValue) {
		this.dateValue = CloneUtils.clone(dateValue);
	}

	@Override
	public String getDisplayName() {
		StringBuilder displayName = new StringBuilder();
		displayName.append(name);
		displayName.append(':');
		displayName.append(getStringValue());

		return displayName.toString();
	}

	@Override
	public String getNameForToString() {
		return name;
	}

}
