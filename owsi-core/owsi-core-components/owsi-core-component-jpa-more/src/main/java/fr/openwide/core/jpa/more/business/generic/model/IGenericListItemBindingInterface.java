package fr.openwide.core.jpa.more.business.generic.model;

import org.bindgen.Bindable;

import fr.openwide.core.jpa.business.generic.model.IGenericEntityBindingInterface;

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
