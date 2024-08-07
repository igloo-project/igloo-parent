package org.iglooproject.test.business.person.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import org.iglooproject.jpa.business.generic.model.GenericEntity;

@Entity
public class PersonReference extends GenericEntity<Long, PersonReference> {

  private static final long serialVersionUID = -4701015363376112544L;

  @Id @GeneratedValue private Long id;

  @ManyToOne(optional = false, fetch = FetchType.LAZY /* Pour tester les proxys */)
  private Person person;

  public PersonReference() {}

  public PersonReference(Person person) {
    this.person = person;
  }

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }

  public Person getPerson() {
    return person;
  }

  public void setPerson(Person person) {
    this.person = person;
  }
}
