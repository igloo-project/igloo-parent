package org.iglooproject.jpa.more.business.referencedata.model;

import javax.persistence.MappedSuperclass;

import org.bindgen.Bindable;
import org.iglooproject.jpa.more.business.localization.model.AbstractLocalizedText;

@MappedSuperclass
@Bindable
public abstract class GenericLocalizedReferenceData<E extends GenericLocalizedReferenceData<?, T>, T
		extends AbstractLocalizedText> extends GenericReferenceData<E, T>
		implements IGenericLocalizedReferenceDataBindingInterface<T> {

	private static final long serialVersionUID = 8911580209164607172L;

}
