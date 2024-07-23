package org.iglooproject.jpa.more.test.junit.difference;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.iglooproject.commons.util.fieldpath.FieldPath;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.business.generic.model.GenericEntityReference;
import org.iglooproject.jpa.more.business.history.model.AbstractHistoryDifference;
import org.iglooproject.jpa.more.business.history.model.embeddable.HistoryValue;

public class TestHistoryDifferenceKey {

  private final FieldPath fieldPath;

  private final GenericEntityReference<?, ?> keyAsGenericEntityReference;

  private final String keyAsString;

  @SuppressWarnings("rawtypes")
  public TestHistoryDifferenceKey(FieldPath fieldPath, Object key) {
    super();
    this.fieldPath = fieldPath;
    if (key instanceof GenericEntity) {
      keyAsGenericEntityReference = GenericEntityReference.ofUnknownIdType((GenericEntity) key);
      keyAsString = null;
    } else if (key != null) {
      keyAsGenericEntityReference = null;
      keyAsString = key.toString();
    } else {
      keyAsGenericEntityReference = null;
      keyAsString = null;
    }
  }

  public TestHistoryDifferenceKey(AbstractHistoryDifference<?, ?> input) {
    super();
    this.fieldPath = input.getPath().getPath();
    HistoryValue key = input.getPath().getKey();
    if (key != null && key.getReference() != null) {
      keyAsGenericEntityReference = key.getReference();
      keyAsString = null;
    } else {
      keyAsGenericEntityReference = null;
      keyAsString = key == null ? null : key.getLabel();
    }
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder()
        .append(fieldPath)
        .append(keyAsGenericEntityReference)
        .append(keyAsString)
        .build();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof TestHistoryDifferenceKey) {
      TestHistoryDifferenceKey other = (TestHistoryDifferenceKey) obj;
      return new EqualsBuilder()
          .append(fieldPath, other.fieldPath)
          .append(keyAsGenericEntityReference, other.keyAsGenericEntityReference)
          .append(keyAsString, other.keyAsString)
          .build();
    }
    return false;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append(fieldPath)
        .append(keyAsGenericEntityReference)
        .append(keyAsString)
        .build();
  }
}
