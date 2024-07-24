package org.iglooproject.jpa.more.business.history.model;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import org.iglooproject.commons.util.fieldpath.FieldPath;
import org.iglooproject.jpa.business.generic.model.GenericEntity;

@MappedSuperclass
public abstract class AbstractHistoryElement<
        HE extends AbstractHistoryElement<HE, HL>, HL extends AbstractHistoryLog<HL, ?, ?>>
    extends GenericEntity<Long, HE> implements IHistoryElement<HL> {

  private static final long serialVersionUID = -4168127417871120993L;

  @Override
  @Transient
  public abstract HL getRootLog();

  @Transient
  protected abstract AbstractHistoryElement<?, ?> getParent();

  @Override
  @Transient
  public abstract FieldPath getRelativePath();

  @Override
  @Transient
  public FieldPath getParentAbsolutePath() {
    AbstractHistoryElement<?, ?> parent = getParent();
    if (parent != null) {
      return parent.getAbsolutePath();
    } else {
      return FieldPath.ROOT;
    }
  }

  /* (non-Javadoc)
   * @see org.iglooproject.jpa.more.business.history.model.IHistoryElement#getAbsolutePath()
   */
  @Override
  @Transient
  public FieldPath getAbsolutePath() {
    return getParentAbsolutePath().append(getRelativePath());
  }
}
