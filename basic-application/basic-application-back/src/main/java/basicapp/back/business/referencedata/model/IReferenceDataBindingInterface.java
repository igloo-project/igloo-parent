package basicapp.back.business.referencedata.model;

import org.bindgen.Bindable;
import org.iglooproject.jpa.more.business.referencedata.model.IGenericReferenceDataBindingInterface;

import basicapp.back.business.common.model.embeddable.LocalizedText;

@Bindable
public interface IReferenceDataBindingInterface extends IGenericReferenceDataBindingInterface<LocalizedText> {

}
