package fr.openwide.core.wicket.more.markup.html.template.model;

import java.io.Serializable;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import fr.openwide.core.wicket.more.link.descriptor.generator.ILinkGenerator;
import fr.openwide.core.wicket.more.markup.html.template.component.LinkBreadCrumbElementPanel;
import fr.openwide.core.wicket.more.markup.html.template.component.LinkGeneratorBreadCrumbElementPanel;
import fr.openwide.core.wicket.more.markup.html.template.component.SimpleBreadCrumbElementPanel;

@SuppressWarnings("deprecation")
public class BreadCrumbElement implements Serializable {
	
	private static final long serialVersionUID = -44367801976105581L;

	private IModel<String> labelModel;
	
	private ILinkGenerator linkGenerator;
	
	private Class<? extends Page> pageClass;
	
	private PageParameters pageParameters;
	
	public BreadCrumbElement(IModel<String> labelModel) {
		this.labelModel = labelModel;
	}
	
	@Deprecated
	public BreadCrumbElement(IModel<String> labelModel, Class<? extends Page> pageClass) {
		this(labelModel, pageClass, null);
	}
	
	@Deprecated
	public BreadCrumbElement(IModel<String> labelModel, Class<? extends Page> pageClass, PageParameters pageParameters) {
		this.labelModel = labelModel;
		this.pageClass = pageClass;
		this.pageParameters = pageParameters;
	}
	
	public BreadCrumbElement(IModel<String> labelModel, ILinkGenerator linkGenerator) {
		this.labelModel = labelModel;
		this.linkGenerator = linkGenerator;
	}

	public IModel<String> getLabelModel() {
		return labelModel;
	}

	@Deprecated
	public Class<? extends Page> getPageClass() {
		return pageClass;
	}

	@Deprecated
	public PageParameters getPageParameters() {
		return pageParameters;
	}
	
	public ILinkGenerator getLinkGenerator() {
		return linkGenerator;
	}
	
	public Component component(String wicketId, BreadCrumbMarkupTagRenderingBehavior renderingBehavior) {
		if (linkGenerator != null) {
			return new LinkGeneratorBreadCrumbElementPanel(wicketId, this, renderingBehavior);
		} else {
			if (pageClass != null) {
				return new LinkBreadCrumbElementPanel(wicketId, this, renderingBehavior);
			} else {
				return new SimpleBreadCrumbElementPanel(wicketId, this, renderingBehavior);
			}
		}
	}

	public void detach() {
		if (labelModel != null) {
			labelModel.detach();
		}
	}

}
