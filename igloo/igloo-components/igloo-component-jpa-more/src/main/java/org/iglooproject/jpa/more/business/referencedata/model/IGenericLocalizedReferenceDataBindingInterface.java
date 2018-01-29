package org.iglooproject.jpa.more.business.referencedata.model;

import org.bindgen.Bindable;
import org.iglooproject.jpa.more.business.localization.model.AbstractLocalizedText;

@Bindable
public interface IGenericLocalizedReferenceDataBindingInterface<T extends AbstractLocalizedText> extends IGenericReferenceDataInterface<T> {

}
