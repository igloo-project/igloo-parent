package fr.openwide.core.jpa.more.business.generic.model;

import org.bindgen.Bindable;

@Bindable
public interface IGenericListItemBindingInterface {
	
	Long getId();
	
	String getLabel();
	
	String getShortLabel();
	
	Integer getPosition();
	
	boolean isEnabled();
	
}
