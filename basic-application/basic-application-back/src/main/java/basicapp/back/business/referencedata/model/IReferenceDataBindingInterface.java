package basicapp.back.business.referencedata.model;

import basicapp.back.business.common.model.embeddable.LocalizedText;
import org.bindgen.Bindable;
import org.iglooproject.jpa.more.business.referencedata.model.IGenericReferenceDataBindingInterface;

@Bindable
public interface IReferenceDataBindingInterface
    extends IGenericReferenceDataBindingInterface<LocalizedText> {}
