package org.iglooproject.jpa.hibernate.usertype;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.type.IntegerType;
import org.hibernate.usertype.UserType;

/**
 * A Hibernate {@link UserType} for {@link AbstractMaterializedPrimitiveValue} stored as Integers.
 *
 * <p><strong>CAUTION</strong>: in order for this {@link UserType} to be used by Hibernate, you must
 * either register it as the default for your {@link AbstractMaterializedPrimitiveValue} using
 * {@link TypeDef} or add the {@link Type} annotation to the entity properties.
 *
 * @see AbstractMaterializedPrimitiveValue
 */
public abstract class AbstractImmutableMaterializedIntegerValueUserType<
        T extends AbstractMaterializedPrimitiveValue<Integer, T>>
    extends AbstractImmutableMaterializedPrimitiveValueUserType<Integer, T> {

  public AbstractImmutableMaterializedIntegerValueUserType() {
    super(new IntegerType());
  }
}
