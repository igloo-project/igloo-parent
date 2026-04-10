package org.iglooproject.jpa.more.business.history.model.embeddable;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import java.util.Objects;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.bindgen.Bindable;
import org.hibernate.Length;
import org.hibernate.annotations.Type;
import org.iglooproject.commons.util.fieldpath.FieldPath;
import org.iglooproject.jpa.more.business.history.hibernate.FieldPathType;

@Embeddable
@Bindable
@Access(AccessType.FIELD)
public class HistoryDifferencePath {

  @Basic(optional = false)
  @Column(length = Length.LONG32)
  @Type(FieldPathType.class)
  private FieldPath path;

  @Embedded private HistoryValue key;

  public HistoryDifferencePath() {
    // nothing to do
  }

  public HistoryDifferencePath(FieldPath path) {
    this(path, null);
  }

  public HistoryDifferencePath(FieldPath path, HistoryValue key) {
    super();
    this.path = path;
    this.key = key;
  }

  public FieldPath getPath() {
    return path;
  }

  public HistoryValue getKey() {
    return key;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof HistoryDifferencePath other)) {
      return false;
    }
    return Objects.equals(path, other.path) && Objects.equals(key, other.key);
  }

  @Override
  public int hashCode() {
    return Objects.hash(path, key);
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append(path).append(key).build();
  }
}
