package fr.openwide.core.jpa.more.business.parameter.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.bindgen.Bindable;
import org.hibernate.annotations.Type;

import fr.openwide.core.commons.util.CloneUtils;
import fr.openwide.core.jpa.business.generic.model.GenericEntity;

@Entity
@Bindable
public class Parameter extends GenericEntity<Integer, Parameter> {
	
	private static final long serialVersionUID = 4739408616523513971L;

	@Id
	@GeneratedValue
	private Integer id;

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
		return name.toString();
	}

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}
}
