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
import fr.openwide.core.wicket.more.link.descriptor.validator.IParameterValidator;
import fr.openwide.core.wicket.more.link.descriptor.validator.ParameterValidationException;
import fr.openwide.core.wicket.more.link.descriptor.validator.ParameterValidators;

public class CorePageLinkDescriptorImpl implements IPageLinkDescriptor {
	
	private static final long serialVersionUID = -9139677593653180236L;

	private final IModel<Class<? extends Page>> pageClassModel;
	private final PageParametersModel parametersModel;
	private final IParameterValidator validator;

	public CorePageLinkDescriptorImpl(
			IModel<Class<? extends Page>> pageClassModel,
			PageParametersModel parametersModel,
			IParameterValidator validator) {
		super();
		Args.notNull(pageClassModel, "pageClassModel");
		Args.notNull(parametersModel, "parametersModel");
		Args.notNull(validator, "validator");
		this.pageClassModel = pageClassModel;
		this.parametersModel = parametersModel;
		this.validator = validator;
	}

	protected Class<? extends Page> getPageClass() {
		return pageClassModel.getObject();
	}

	protected PageParameters getValidatedParameters() throws ParameterValidationException {
		PageParameters parameters = parametersModel.getObject();
		ParameterValidators.check(parameters, validator);
		return parameters;
	}

	@Override
	public AbstractDynamicBookmarkableLink link(String wicketId) {
		return new DynamicBookmarkablePageLink(wicketId, pageClassModel, parametersModel, validator);
	}

	@Override
	public String fullUrl() throws ParameterValidationException {
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
	public void setResponsePage() throws ParameterValidationException {
		PageParameters parameters = getValidatedParameters();
		
		RequestCycle.get().setResponsePage(getPageClass(), parameters);
	}

	@Override
	public RestartResponseException newRestartResponseException() throws ParameterValidationException {
		PageParameters parameters = getValidatedParameters();
		
		return new RestartResponseException(getPageClass(), parameters);
	}
	
	@Override
	public RestartResponseAtInterceptPageException newRestartResponseAtInterceptPageException() throws ParameterValidationException {
		PageParameters parameters = getValidatedParameters();
		
		return new RestartResponseAtInterceptPageException(getPageClass(), parameters);
	}

	@Override
	public void detach() {
		pageClassModel.detach();
		parametersModel.detach();
	}

}
