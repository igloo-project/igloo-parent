package org.iglooproject.jpa.more.business.history.model.embeddable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.bindgen.Bindable;
import org.iglooproject.commons.util.fieldpath.FieldPath;

@Embeddable
@Bindable
@Access(AccessType.FIELD)
public class HistoryDifferencePath {

  @Basic(optional = false)
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
    if (obj instanceof HistoryDifferencePath) {
      HistoryDifferencePath other = (HistoryDifferencePath) obj;
      return new EqualsBuilder().append(path, other.path).append(key, other.key).build();
    } else {
      return false;
    }
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(path).append(key).build();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append(path).append(key).build();
  }
}
