package org.iglooproject.test.business.label.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import org.iglooproject.jpa.business.generic.model.GenericEntity;

@Entity
public class Label extends GenericEntity<String, Label> {

  private static final long serialVersionUID = -2200458949166945096L;

  @Id private String id;

  /** 2022-01: value is a reserved name with h2 2.x */
  @Column(name = "_value")
  private String value;

  public Label() {}

  public Label(String id, String value) {
    this.id = id;
    this.value = value;
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public void setId(String id) {
    this.id = id;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }
}
