package fr.openwide.core.wicket.more.markup.html.template.model;

import java.io.Serializable;

import org.apache.wicket.Page;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class BreadCrumbElement implements Serializable {
	
	private static final long serialVersionUID = -44367801976105581L;

	private IModel<String> labelModel;
	
	private Class<? extends Page> pageClass;
	
	private PageParameters pageParameters;
	
	public BreadCrumbElement(IModel<String> labelModel) {
		this(labelModel, null);
	}
	
	public BreadCrumbElement(IModel<String> labelModel, Class<? extends Page> pageClass) {
		this(labelModel, pageClass, null);
	}
	
	public BreadCrumbElement(IModel<String> labelModel, Class<? extends Page> pageClass, PageParameters pageParameters) {
		this.labelModel = labelModel;
		this.pageClass = pageClass;
		this.pageParameters = pageParameters;
	}

	public IModel<String> getLabelModel() {
		return labelModel;
	}

	public Class<? extends Page> getPageClass() {
		return pageClass;
	}

	public PageParameters getPageParameters() {
		return pageParameters;
	}

	public void detach() {
		if (labelModel != null) {
			labelModel.detach();
		}
	}

}
