package org.iglooproject.jpa.more.business.parameter.model;

import com.google.common.base.MoreObjects.ToStringHelper;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.bindgen.Bindable;
import org.hibernate.Length;
import org.hibernate.annotations.NaturalId;
import org.iglooproject.jpa.business.generic.model.GenericEntity;

@Entity
@Bindable
public class Parameter extends GenericEntity<Long, Parameter> {

  private static final long serialVersionUID = 4739408616523513971L;

  @Id @GeneratedValue private Long id;

  @NaturalId
  @Column(nullable = false, unique = true)
  private String name;

  @Column(length = Length.LONG32)
  private String stringValue;

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

  @Override
  protected ToStringHelper toStringHelper() {
    return super.toStringHelper().add("name", getName()).add("value", getStringValue());
  }
}
