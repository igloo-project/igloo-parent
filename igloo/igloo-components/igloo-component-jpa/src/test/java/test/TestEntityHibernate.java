package test;

import org.hibernate.Hibernate;
import org.iglooproject.jpa.business.generic.model.GenericEntity;

public class TestEntityHibernate extends GenericEntity<Long, TestEntityHibernate> {

  private static final long serialVersionUID = 5968050124765460930L;

  public TestEntityHibernate() {}

  private Long id;

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * This method is the old equals implementation from GenericEntity, without lambda static
   * switching.
   */
  @Override
  public boolean equals(Object object) {
    if (object == null) {
      return false;
    }
    if (object == this) {
      return true;
    }

    // process class comparison; old code without lambda-controlled static switch
    if (Hibernate.getClass(object) != Hibernate.getClass(this)) {
      return false;
    }

    TestEntityHibernate entity = (TestEntityHibernate) object;
    Long id = getId();

    if (id == null) {
      return false;
    }

    return id.equals(entity.getId());
  }
}
