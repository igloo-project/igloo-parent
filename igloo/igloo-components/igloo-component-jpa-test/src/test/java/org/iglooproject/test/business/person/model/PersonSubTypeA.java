package org.iglooproject.test.business.person.model;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class PersonSubTypeA extends Person {

  private static final long serialVersionUID = 5763892592246291007L;

  @Column private String specificData;

  public PersonSubTypeA() {
    super();
  }

  public PersonSubTypeA(String firstName, String lastName, String specificData) {
    super(firstName, lastName);
    this.specificData = specificData;
  }

  public String getSpecificData() {
    return specificData;
  }

  public void setSpecificData(String specificData) {
    this.specificData = specificData;
  }
}
