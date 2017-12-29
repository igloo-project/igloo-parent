package org.iglooproject.jpa.more.business.generic.model;

import org.bindgen.Bindable;

import org.iglooproject.jpa.business.generic.model.IGenericEntityBindingInterface;

@Bindable
public interface IGenericListItemBindingInterface extends IGenericEntityBindingInterface {
	
	@Override
	Long getId();
	
	@Override
	boolean isNew();
	
	String getLabel();
	
	String getShortLabel();
	
	String getCode();
	
	Integer getPosition();
	
	boolean isEnabled();
	
	boolean isEditable();
	
	boolean isDisableable();
	
	boolean isDeleteable();
	
}
