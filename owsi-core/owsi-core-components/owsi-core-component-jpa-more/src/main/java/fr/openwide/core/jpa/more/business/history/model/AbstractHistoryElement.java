package fr.openwide.core.jpa.more.business.history.model;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import fr.openwide.core.commons.util.fieldpath.FieldPath;
import fr.openwide.core.jpa.business.generic.model.GenericEntity;

@MappedSuperclass
public abstract class AbstractHistoryElement<HE extends AbstractHistoryElement<HE, HL>, HL extends AbstractHistoryLog<HL, ?, ?>> extends GenericEntity<Long, HE>
		implements IHistoryElement<HL> {
	
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
	 * @see fr.openwide.core.jpa.more.business.history.model.IHistoryElement#getAbsolutePath()
	 */
	@Override
	@Transient
	public FieldPath getAbsolutePath() {
		return getParentAbsolutePath().append(getRelativePath());
	}

}
