package org.iglooproject.jpa.more.business.referencedata.model;

import javax.persistence.Basic;
import javax.persistence.MappedSuperclass;

import org.bindgen.Bindable;

@MappedSuperclass
@Bindable
public class GenericBasicReferenceData<E extends GenericBasicReferenceData<?>>
		extends GenericReferenceData<E, String>
		implements IGenericBasicReferenceDataBindingInterface {

	private static final long serialVersionUID = 5452588262088929894L;

	@Basic
	private String label;

	protected GenericBasicReferenceData() {
	}

	public GenericBasicReferenceData(String label) {
		this(label, 0);
	}

	public GenericBasicReferenceData(String label, Integer position) {
		super(label, position);
	}

	@Override
	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public String getLabel() {
		return label;
	}

}
