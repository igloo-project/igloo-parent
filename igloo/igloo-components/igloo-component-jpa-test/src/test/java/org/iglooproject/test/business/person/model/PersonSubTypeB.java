package org.iglooproject.test.business.person.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class PersonSubTypeB extends Person {

  private static final long serialVersionUID = 5763892592246291007L;

  @Column private int specificData;

  public PersonSubTypeB() {
    super();
  }

  public PersonSubTypeB(String firstName, String lastName, int specificData) {
    super(firstName, lastName);
    this.specificData = specificData;
  }

  public int getSpecificData() {
    return specificData;
  }

  public void setSpecificData(int specificData) {
    this.specificData = specificData;
  }
}
