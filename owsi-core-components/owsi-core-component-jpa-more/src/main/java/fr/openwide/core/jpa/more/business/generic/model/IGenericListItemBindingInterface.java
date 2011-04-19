package fr.openwide.core.jpa.more.business.generic.model;

import org.bindgen.Bindable;

@Bindable
public interface IGenericListItemBindingInterface {
	
	public Integer getId();
	
	public String getLabel();
	
	public String getShortLabel();
	
	public Integer getPosition();
	
	public boolean isEnabled();
	
}
