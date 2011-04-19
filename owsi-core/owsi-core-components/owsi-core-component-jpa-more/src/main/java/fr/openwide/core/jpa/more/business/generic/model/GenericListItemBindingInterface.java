package fr.openwide.core.jpa.more.business.generic.model;

import org.bindgen.Bindable;

@Bindable
public interface GenericListItemBindingInterface {
	
	public Integer getId();
	
	public String getLabel();
	
	public String getShortLabel();
	
	public Integer getPosition();
	
	public boolean isEnabled();
	
}
