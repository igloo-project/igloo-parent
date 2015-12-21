package fr.openwide.core.jpa.more.business.history.model;

import org.bindgen.Bindable;

import fr.openwide.core.jpa.more.util.fieldpath.model.FieldPath;

@Bindable
public interface IHistoryElement<HL extends AbstractHistoryLog<HL, ?, ?>> {

	HL getRootLog();

	FieldPath getParentAbsolutePath();

	FieldPath getAbsolutePath();

	FieldPath getRelativePath();

}