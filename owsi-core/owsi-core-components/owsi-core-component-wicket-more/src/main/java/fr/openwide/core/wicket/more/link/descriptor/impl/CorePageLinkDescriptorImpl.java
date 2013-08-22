package fr.openwide.core.wicket.more.link.descriptor.impl;

import org.apache.wicket.Page;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.lang.Args;

import fr.openwide.core.wicket.more.link.descriptor.AbstractDynamicBookmarkableLink;
import fr.openwide.core.wicket.more.link.descriptor.IPageLinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.parameter.mapping.LinkParametersMapping;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.ILinkParameterValidator;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.LinkParameterValidationException;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.LinkParameterValidationRuntimeException;

public class CorePageLinkDescriptorImpl extends AbstractCoreLinkDescriptor implements IPageLinkDescriptor {
	
	private static final long serialVersionUID = -9139677593653180236L;

	final IModel<Class<? extends Page>> pageClassModel;
	
	public CorePageLinkDescriptorImpl(
			IModel<Class<? extends Page>> pageClassModel,
			LinkParametersMapping parametersMapping,
			ILinkParameterValidator validator) {
		super(parametersMapping, validator);
		Args.notNull(pageClassModel, "pageClassModel");
		this.pageClassModel = pageClassModel;
	}

	protected Class<? extends Page> getPageClass() {
		return pageClassModel.getObject();
	}

	@Override
	public AbstractDynamicBookmarkableLink link(String wicketId) {
		return new DynamicBookmarkablePageLink(wicketId, pageClassModel, parametersMapping, parametersValidator);
	}

	@Override
	public String fullUrl() throws LinkParameterValidationException {
		PageParameters parameters = getValidatedParameters();
		
		RequestCycle requestCycle = RequestCycle.get();
		return requestCycle.getUrlRenderer()
				.renderFullUrl(
						Url.parse(
								requestCycle.urlFor(getPageClass(), parameters)
						)
				);
	}
	
	@Override
	public void setResponsePage() throws LinkParameterValidationRuntimeException {
		PageParameters parameters;
		try {
			parameters = getValidatedParameters();
		} catch (LinkParameterValidationException e) {
			throw new LinkParameterValidationRuntimeException(e);
		}
		
		RequestCycle.get().setResponsePage(getPageClass(), parameters);
	}

	@Override
	public RestartResponseException newRestartResponseException() throws LinkParameterValidationRuntimeException {
		PageParameters parameters;
		try {
			parameters = getValidatedParameters();
		} catch (LinkParameterValidationException e) {
			throw new LinkParameterValidationRuntimeException(e);
		}
		
		return new RestartResponseException(getPageClass(), parameters);
	}
	
	@Override
	public RestartResponseAtInterceptPageException newRestartResponseAtInterceptPageException() throws LinkParameterValidationRuntimeException {
		PageParameters parameters;
		try {
			parameters = getValidatedParameters();
		} catch (LinkParameterValidationException e) {
			throw new LinkParameterValidationRuntimeException(e);
		}
		
		return new RestartResponseAtInterceptPageException(getPageClass(), parameters);
	}
	
	@Override
	public void detach() {
		super.detach();
		pageClassModel.detach();
	}

}
