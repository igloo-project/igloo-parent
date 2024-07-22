package org.iglooproject.jpa.more.business.referencedata.model;

import org.bindgen.Bindable;
import org.iglooproject.jpa.business.generic.model.IGenericEntityBindingInterface;

@Bindable
public interface IGenericReferenceDataBindingInterface<T> extends IGenericEntityBindingInterface {

  @Override
  Long getId();

  @Override
  boolean isNew();

  T getLabel();

  Integer getPosition();

  boolean isEnabled();

  boolean isEditable();

  boolean isDisableable();

  boolean isDeleteable();
}
