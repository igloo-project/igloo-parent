package fr.openwide.core.jpa.more.business.parameter.model;

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

import fr.openwide.core.commons.util.CloneUtils;
import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.more.business.parameter.service.AbstractParameterServiceImpl;
import fr.openwide.core.spring.property.service.IPropertyService;

@SuppressWarnings("deprecation")
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

	/**
	 * @deprecated Only {@link AbstractParameterServiceImpl} made use of typed values. It is now deprecated in favor
	 * of {@link IPropertyService}, which only uses the string value.
	 * @see IPropertyService
	 */
	@Deprecated
	@Column
	private Boolean booleanValue;

	/**
	 * @deprecated Only {@link AbstractParameterServiceImpl} made use of typed values. It is now deprecated in favor
	 * of {@link IPropertyService}, which only uses the string value.
	 * @see IPropertyService
	 */
	@Deprecated
	@Column
	private Integer integerValue;
	
	/**
	 * @deprecated Only {@link AbstractParameterServiceImpl} made use of typed values. It is now deprecated in favor
	 * of {@link IPropertyService}, which only uses the string value.
	 * @see IPropertyService
	 */
	@Deprecated
	@Column
	private Float floatValue;

	@Column
	@Type(type = "fr.openwide.core.jpa.hibernate.usertype.StringClobType")
	private String stringValue;

	/**
	 * @deprecated Only {@link AbstractParameterServiceImpl} made use of typed values. It is now deprecated in favor
	 * of {@link IPropertyService}, which only uses the string value.
	 * @see IPropertyService
	 */
	@Deprecated
	@Column
	private Date dateValue;

	public Parameter() {
		super();
	}

	/**
	 * @deprecated Only {@link AbstractParameterServiceImpl} made use of typed values. It is now deprecated in favor
	 * of {@link IPropertyService}, which only uses the string value.
	 * @see IPropertyService
	 */
	@Deprecated
	public Parameter(String name, Boolean value) {
		super();

		setName(name);
		setBooleanValue(value);
	}

	/**
	 * @deprecated Only {@link AbstractParameterServiceImpl} made use of typed values. It is now deprecated in favor
	 * of {@link IPropertyService}, which only uses the string value.
	 * @see IPropertyService
	 */
	@Deprecated
	public Parameter(String name, Integer value) {
		super();

		setName(name);
		setIntegerValue(value);
	}

	/**
	 * @deprecated Only {@link AbstractParameterServiceImpl} made use of typed values. It is now deprecated in favor
	 * of {@link IPropertyService}, which only uses the string value.
	 * @see IPropertyService
	 */
	@Deprecated
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

	/**
	 * @deprecated Only {@link AbstractParameterServiceImpl} made use of typed values. It is now deprecated in favor
	 * of {@link IPropertyService}, which only uses the string value.
	 * @see IPropertyService
	 */
	@Deprecated
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

	/**
	 * @deprecated Only {@link AbstractParameterServiceImpl} made use of typed values. It is now deprecated in favor
	 * of {@link IPropertyService}, which only uses the string value.
	 * @see IPropertyService
	 */
	@Deprecated
	public Boolean getBooleanValue() {
		return booleanValue;
	}

	/**
	 * @deprecated Only {@link AbstractParameterServiceImpl} made use of typed values. It is now deprecated in favor
	 * of {@link IPropertyService}, which only uses the string value.
	 * @see IPropertyService
	 */
	@Deprecated
	public void setBooleanValue(Boolean booleanValue) {
		this.booleanValue = booleanValue;
	}

	/**
	 * @deprecated Only {@link AbstractParameterServiceImpl} made use of typed values. It is now deprecated in favor
	 * of {@link IPropertyService}, which only uses the string value.
	 * @see IPropertyService
	 */
	@Deprecated
	public Integer getIntegerValue() {
		return integerValue;
	}

	/**
	 * @deprecated Only {@link AbstractParameterServiceImpl} made use of typed values. It is now deprecated in favor
	 * of {@link IPropertyService}, which only uses the string value.
	 * @see IPropertyService
	 */
	@Deprecated
	public void setIntegerValue(Integer integerValue) {
		this.integerValue = integerValue;
	}

	/**
	 * @deprecated Only {@link AbstractParameterServiceImpl} made use of typed values. It is now deprecated in favor
	 * of {@link IPropertyService}, which only uses the string value.
	 * @see IPropertyService
	 */
	@Deprecated
	public Float getFloatValue() {
		return floatValue;
	}

	/**
	 * @deprecated Only {@link AbstractParameterServiceImpl} made use of typed values. It is now deprecated in favor
	 * of {@link IPropertyService}, which only uses the string value.
	 * @see IPropertyService
	 */
	@Deprecated
	public void setFloatValue(Float floatValue) {
		this.floatValue = floatValue;
	}

	public String getStringValue() {
		return stringValue;
	}

	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}

	/**
	 * @deprecated Only {@link AbstractParameterServiceImpl} made use of typed values. It is now deprecated in favor
	 * of {@link IPropertyService}, which only uses the string value.
	 * @see IPropertyService
	 */
	@Deprecated
	public Date getDateValue() {
		return CloneUtils.clone(dateValue);
	}

	/**
	 * @deprecated Only {@link AbstractParameterServiceImpl} made use of typed values. It is now deprecated in favor
	 * of {@link IPropertyService}, which only uses the string value.
	 * @see IPropertyService
	 */
	@Deprecated
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
