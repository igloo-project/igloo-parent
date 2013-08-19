package fr.openwide.core.wicket.more.link.descriptor.impl;

import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.util.lang.Args;

import fr.openwide.core.wicket.more.link.descriptor.IResourceLinkDescriptor;

public class CoreResourceLinkDescriptorImpl implements IResourceLinkDescriptor {

	private static final long serialVersionUID = -2046898427977725120L;
	
	private final IModel<? extends ResourceReference> resourceReferenceModel;
	private final PageParametersModel parametersModel;

	public CoreResourceLinkDescriptorImpl(IModel<? extends ResourceReference> resourceReferenceModel, PageParametersModel parametersModel) {
		super();
		Args.notNull(resourceReferenceModel, "resourceReferenceModel");
		Args.notNull(parametersModel, "parametersModel");
		this.resourceReferenceModel = resourceReferenceModel;
		this.parametersModel = parametersModel;
	}

	protected ResourceReference getResourceReference() {
		return resourceReferenceModel.getObject();
	}

	protected PageParameters getParameters() {
		return parametersModel.getObject();
	}

	@Override
	public AbstractLink link(String wicketId) {
		return new DynamicResourceLink(wicketId, resourceReferenceModel, parametersModel);
	}

	@Override
	public String fullUrl() {
		RequestCycle requestCycle = RequestCycle.get();
		return requestCycle.getUrlRenderer()
				.renderFullUrl(
						Url.parse(
								requestCycle.urlFor(getResourceReference(), getParameters())
						)
				);
	}

	@Override
	public void detach() {
		resourceReferenceModel.detach();
		parametersModel.detach();
	}

}
