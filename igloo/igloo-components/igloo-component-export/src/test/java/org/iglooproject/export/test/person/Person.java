package org.iglooproject.export.test.person;

import java.time.LocalDateTime;

public class Person {

  String username;

  String firstName;

  String lastName;

  LocalDateTime birthDateTime;

  int age;

  double size;

  double percentage;

  public Person(
      String username,
      String firstName,
      String lastName,
      LocalDateTime birthDateTime,
      int age,
      double size,
      double percentage) {
    super();
    this.username = username;
    this.firstName = firstName;
    this.lastName = lastName;
    this.birthDateTime = birthDateTime;
    this.age = age;
    this.size = size;
    this.percentage = percentage;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
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

  public LocalDateTime getBirthDateTime() {
    return birthDateTime;
  }

  public void setBirthDateTime(LocalDateTime birthDateTime) {
    this.birthDateTime = birthDateTime;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }

  public double getSize() {
    return size;
  }

  public void setSize(double size) {
    this.size = size;
  }

  public double getPercentage() {
    return percentage;
  }

  public void setPercentage(double percentage) {
    this.percentage = percentage;
  }
}
