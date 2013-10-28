package fr.openwide.core.wicket.more.markup.html.template.model;

import java.io.Serializable;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import fr.openwide.core.wicket.more.link.descriptor.ILinkDescriptor;
import fr.openwide.core.wicket.more.markup.html.template.component.LinkBreadCrumbElementPanel;
import fr.openwide.core.wicket.more.markup.html.template.component.SimpleBreadCrumbElementPanel;

public class BreadCrumbElement implements Serializable {
	
	private static final long serialVersionUID = -44367801976105581L;

	private IModel<String> labelModel;
	
	private ILinkDescriptor linkDescriptor;
	
	private Class<? extends Page> pageClass;
	
	private PageParameters pageParameters;
	
	public BreadCrumbElement(IModel<String> labelModel) {
		this.labelModel = labelModel;
	}
	
	public BreadCrumbElement(IModel<String> labelModel, Class<? extends Page> pageClass) {
		this(labelModel, pageClass, null);
	}
	
	public BreadCrumbElement(IModel<String> labelModel, Class<? extends Page> pageClass, PageParameters pageParameters) {
		this.labelModel = labelModel;
		this.pageClass = pageClass;
		this.pageParameters = pageParameters;
	}
	
	public BreadCrumbElement(IModel<String> labelModel, ILinkDescriptor linkDescriptor) {
		this.labelModel = labelModel;
		this.linkDescriptor = linkDescriptor;
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
	
	public Component component(String wicketId) {
		if (linkDescriptor != null) {
			return linkDescriptor.link(wicketId).setAutoHideIfInvalid(true);
		} else {
			if (pageClass != null && Session.get().getAuthorizationStrategy().isInstantiationAuthorized(pageClass)) {
				return new LinkBreadCrumbElementPanel(wicketId, this);
			} else {
				return new SimpleBreadCrumbElementPanel(wicketId, this);
			}
		}
	}

	public void detach() {
		if (labelModel != null) {
			labelModel.detach();
		}
	}

}
