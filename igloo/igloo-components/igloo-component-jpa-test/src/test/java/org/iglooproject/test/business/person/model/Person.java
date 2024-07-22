/*
 * Copyright (C) 2009-2010 Open Wide
 * Contact: contact@openwide.fr
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.iglooproject.test.business.person.model;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapKeyEnumerated;
import jakarta.persistence.OneToMany;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.test.business.company.model.Company;
import org.iglooproject.test.business.project.model.Project;

@Entity
@Indexed
@Inheritance(strategy = InheritanceType.JOINED)
public class Person extends GenericEntity<Long, Person> {

  private static final long serialVersionUID = -2471930493134125282L;

  @Id @GeneratedValue private Long id;

  @GenericField private String firstName;

  @GenericField private String lastName;

  @ManyToOne private Company company;

  @ManyToMany
  @JoinTable(
      joinColumns = @JoinColumn(name = "persons_id"),
      inverseJoinColumns = @JoinColumn(name = "project_id"))
  private List<Project> workedProjects = new LinkedList<Project>();

  private Instant creationDate;

  /** cf {@link TestMetaModel} */
  private PersonSubTypeA otherPerson;

  /** cf {@link TestMetaModel} */
  private StubEnumeration enumeration;

  /** cf {@link TestMetaModel} */
  @Enumerated(EnumType.STRING)
  private StubEnumeration enumerationString;

  /** cf {@link TestMetaModel} */
  @OneToMany private Map<StubEnumeration, PersonSubTypeA> enumMap;

  /** cf {@link TestMetaModel} */
  @OneToMany
  @MapKeyEnumerated(EnumType.STRING)
  private Map<StubEnumeration, PersonSubTypeA> enumMapString;

  /** cf {@link TestMetaModel} */
  @ElementCollection private List<StubEnumeration> enumList;

  /** cf {@link TestMetaModel} */
  @ElementCollection
  @Enumerated(EnumType.STRING)
  private List<StubEnumeration> enumListString;

  /** cf {@link TestMetaModel} */
  @ElementCollection private Map<Long, StubEnumeration> enumMapValue;

  /** cf {@link TestMetaModel} */
  @ElementCollection
  @Enumerated(EnumType.STRING)
  private Map<Long, StubEnumeration> enumMapValueString;

  public Person() {}

  public Person(String firstName, String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
  }

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public Company getCompany() {
    return company;
  }

  public void setCompany(Company company) {
    this.company = company;
  }

  public List<Project> getWorkedProjects() {
    return workedProjects;
  }

  public void setWorkedProjects(List<Project> workedProjects) {
    this.workedProjects = workedProjects;
  }

  public void addWorkedProjects(Project project) {
    this.workedProjects.add(project);
    project.getTeam().add(this);
  }

  public void removeWorkedProjects(Project project) {
    this.workedProjects.remove(project);
    project.getTeam().remove(this);
  }

  public Instant getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(Instant creationDate) {
    this.creationDate = creationDate;
  }

  public PersonSubTypeA getOtherPerson() {
    return otherPerson;
  }

  public void setOtherPerson(PersonSubTypeA otherPerson) {
    this.otherPerson = otherPerson;
  }

  public StubEnumeration getEnumeration() {
    return enumeration;
  }

  public void setEnumeration(StubEnumeration enumeration) {
    this.enumeration = enumeration;
  }

  public Map<StubEnumeration, PersonSubTypeA> getEnumMap() {
    return enumMap;
  }

  public void setEnumMap(Map<StubEnumeration, PersonSubTypeA> enumMap) {
    this.enumMap = enumMap;
  }
}
