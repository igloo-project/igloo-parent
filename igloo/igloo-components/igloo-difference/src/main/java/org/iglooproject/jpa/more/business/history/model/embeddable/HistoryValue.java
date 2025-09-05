package org.iglooproject.jpa.more.business.history.model.embeddable;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.bindgen.Bindable;
import org.hibernate.Length;
import org.hibernate.search.mapper.pojo.bridge.mapping.annotation.ValueBridgeRef;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.business.generic.model.GenericEntityReference;
import org.iglooproject.jpa.more.business.history.model.AbstractHistoryDifference;
import org.iglooproject.jpa.more.business.history.model.AbstractHistoryLog;
import org.iglooproject.jpa.search.bridge.GenericEntityReferenceIdBridge;

/**
 * {@link AbstractHistoryLog} / {@link AbstractHistoryDifference} version of {@link IHistoryValue}.
 *
 * @see IHistoryValue
 */
@Embeddable
@Bindable
@Access(AccessType.FIELD)
public class HistoryValue implements IHistoryValue, Serializable {

  private static final long serialVersionUID = 1251495816635000683L;

  public static final String REFERENCE = "reference";

  /** Human-readable string */
  @Basic
  @Column(length = Length.LONG32)
  private String label;

  /** Machine-readable string (for instance MyEnum.VALUE.name()) */
  @Basic
  @Column(length = Length.LONG32)
  private String serialized;

  @Embedded
  @GenericField(
      name = REFERENCE,
      valueBridge = @ValueBridgeRef(type = GenericEntityReferenceIdBridge.class))
  @SuppressWarnings("squid:S1845") // attribute name differs only by case on purpose
  private HistoryEntityReference reference;

  public HistoryValue() {
    // nothing to do
  }

  public HistoryValue(String label) {
    this(label, null, null);
  }

  public HistoryValue(String label, String serialized) {
    this(label, serialized, null);
  }

  public HistoryValue(String label, GenericEntityReference<Long, ?> entityValueReference) {
    this(label, null, entityValueReference);
  }

  private HistoryValue(
      String label, String serialized, GenericEntityReference<Long, ?> entityValueReference) {
    this(label, serialized, HistoryEntityReference.from(entityValueReference));
  }

  public HistoryValue(String label, String serialized, HistoryEntityReference reference) {
    super();
    this.label = label;
    this.serialized = serialized;
    this.reference = reference;
  }

  @Override
  public String getLabel() {
    return label;
  }

  @Override
  public String getSerialized() {
    return serialized;
  }

  @Override
  public GenericEntityReference<Long, GenericEntity<Long, ?>> getReference() {
    return reference;
  }

  @Override
  public String toString() {
    if (reference != null) {
      return reference.toString();
    } else {
      return label;
    }
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof HistoryValue)) {
      return false;
    }
    HistoryValue other = (HistoryValue) obj;
    return new EqualsBuilder()
        .append(getLabel(), other.getLabel())
        .append(getSerialized(), other.getSerialized())
        .append(getReference(), other.getReference())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder()
        .append(getLabel())
        .append(getSerialized())
        .append(getReference())
        .build();
  }

  public static final HistoryValue build(
      String label, String serialized, GenericEntityReference<Long, ?> reference) {
    return new HistoryValue(label, serialized, reference);
  }
}
