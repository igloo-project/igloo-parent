package org.iglooproject.jpa.more.business.history.hibernate.composite;

import java.io.Serializable;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.metamodel.spi.ValueAccess;
import org.hibernate.usertype.CompositeUserType;
import org.iglooproject.jpa.more.business.history.hibernate.HistoryEntityReferenceTypeContributor;
import org.iglooproject.jpa.more.business.history.model.embeddable.HistoryEntityReference;
import org.iglooproject.jpa.more.business.history.model.embeddable.HistoryValue;

/**
 * Common base to configure {@link HistoryValue} database storage. Concrete implementation chose the
 * correct embeddable. Appropriate {@link CompositeUserType} is setup by {@link
 * HistoryEntityReferenceTypeContributor}, based on hibernate properties.
 */
public abstract class AbstractHistoryValueCompositeType
    implements CompositeUserType<HistoryEntityReference> {

  @Override
  public Object getPropertyValue(HistoryEntityReference component, int property)
      throws HibernateException {
    switch (property) {
      case 0:
        return component.getId();
      case 1:
        return component.getType();
      default:
        throw new HibernateException("Illegal property index: " + property);
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public HistoryEntityReference instantiate(
      ValueAccess values, SessionFactoryImplementor sessionFactory) {
    if (values.getValue(0, Long.class) == null && values.getValue(1, Class.class) == null) {
      return null;
    }
    return new HistoryEntityReference(
        values.getValue(1, Class.class), values.getValue(0, Long.class));
  }

  @Override
  public Class<HistoryEntityReference> returnedClass() {
    return HistoryEntityReference.class;
  }

  @Override
  public boolean equals(HistoryEntityReference x, HistoryEntityReference y) {
    return x.equals(y);
  }

  @Override
  public int hashCode(HistoryEntityReference x) {
    return x.hashCode();
  }

  @Override
  public HistoryEntityReference deepCopy(HistoryEntityReference value) {
    return value; // immutable
  }

  @Override
  public boolean isMutable() {
    return false;
  }

  @Override
  public Serializable disassemble(HistoryEntityReference value) {
    return value;
  }

  @Override
  public HistoryEntityReference assemble(Serializable cached, Object owner) {
    return (HistoryEntityReference) cached;
  }

  @Override
  public HistoryEntityReference replace(
      HistoryEntityReference original, HistoryEntityReference target, Object owner) {
    return original;
  }
}
