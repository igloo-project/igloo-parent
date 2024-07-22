package test;

import org.iglooproject.jpa.business.generic.model.GenericEntity;

public class TestEntity extends GenericEntity<Long, TestEntity> {

  private static final long serialVersionUID = 5968050124765460930L;

  public TestEntity() {}

  private Long id;

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }
}
