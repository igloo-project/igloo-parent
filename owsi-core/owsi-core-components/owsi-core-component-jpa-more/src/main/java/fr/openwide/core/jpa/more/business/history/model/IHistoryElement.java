package fr.openwide.core.jpa.more.business.history.model;

import org.bindgen.Bindable;

import fr.openwide.core.commons.util.fieldpath.FieldPath;

@Bindable
public interface IHistoryElement<HL extends AbstractHistoryLog<HL, ?, ?>> {

	HL getRootLog();

	FieldPath getParentAbsolutePath();

	FieldPath getAbsolutePath();

	FieldPath getRelativePath();

}