package org.iglooproject.jpa.more.business.history.model;

import org.bindgen.Bindable;

import org.iglooproject.commons.util.fieldpath.FieldPath;

@Bindable
public interface IHistoryElement<HL extends AbstractHistoryLog<HL, ?, ?>> {

	HL getRootLog();

	FieldPath getParentAbsolutePath();

	FieldPath getAbsolutePath();

	FieldPath getRelativePath();

}