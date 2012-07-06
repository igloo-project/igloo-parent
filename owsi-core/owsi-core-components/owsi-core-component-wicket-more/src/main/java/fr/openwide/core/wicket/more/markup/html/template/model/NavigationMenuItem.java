package fr.openwide.core.wicket.more.markup.html.template.model;

import java.io.Serializable;

import org.apache.wicket.Page;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class NavigationMenuItem implements Serializable {

	private static final long serialVersionUID = -833923931725195545L;
	
	private IModel<String> labelModel;
	
	private Class<? extends Page> pageClass;
	
	private PageParameters pageParameters;
	
	public NavigationMenuItem(IModel<String> labelModel) {
		this(labelModel, null);
	}
	
	public NavigationMenuItem(IModel<String> labelModel, Class<? extends Page> pageClass) {
		this(labelModel, pageClass, null);
	}
	
	public NavigationMenuItem(IModel<String> labelModel, Class<? extends Page> pageClass, PageParameters pageParameters) {
		this.labelModel = labelModel;
		this.pageClass = pageClass;
		this.pageParameters = pageParameters;
	}
	
	public void detach() {
		if (labelModel != null) {
			labelModel.detach();
		}
	}
	
	public IModel<String> getLabelModel() {
		return labelModel;
	}
	
	public void setLabelModel(IModel<String> labelModel) {
		this.labelModel = labelModel;
	}
	
	public Class<? extends Page> getPageClass() {
		return pageClass;
	}
	
	public void setPageClass(Class<? extends Page> pageClass) {
		this.pageClass = pageClass;
	}
	
	public PageParameters getPageParameters() {
		return pageParameters;
	}
	
	public void setPageParameters(PageParameters pageParameters) {
		this.pageParameters = pageParameters;
	}

}
