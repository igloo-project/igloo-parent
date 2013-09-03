package fr.openwide.core.wicket.more.link.descriptor.impl;

import java.util.Collection;

import org.apache.wicket.Page;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.Session;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.lang.Args;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import fr.openwide.core.spring.util.StringUtils;
import fr.openwide.core.wicket.more.link.descriptor.AbstractDynamicBookmarkableLink;
import fr.openwide.core.wicket.more.link.descriptor.IPageLinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.parameter.injector.LinkParameterInjectionRuntimeException;
import fr.openwide.core.wicket.more.link.descriptor.parameter.mapping.LinkParametersMapping;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.ILinkParameterValidator;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.LinkParameterValidationException;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.LinkParameterValidationRuntimeException;
import fr.openwide.core.wicket.more.markup.html.template.model.NavigationMenuItem;

public class CorePageLinkDescriptorImpl extends AbstractCoreLinkDescriptor implements IPageLinkDescriptor {
	
	private static final long serialVersionUID = -9139677593653180236L;
	
	private static final Logger INTERFACE_LOGGER = LoggerFactory.getLogger(IPageLinkDescriptor.class);
	
	private static final String ANCHOR_ROOT = "#";

	private final IModel<Class<? extends Page>> pageClassModel;
	
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
	public AbstractDynamicBookmarkableLink link(String wicketId, String anchor) {
		AbstractDynamicBookmarkableLink link = link(wicketId);
		link.add(new AttributeAppender("href", ANCHOR_ROOT + anchor));
		
		return link;
	}
	
	@Override
	public void extractSafely(PageParameters parameters, IPageLinkDescriptor fallbackLink) throws RestartResponseException {
		extractSafely(parameters, fallbackLink, null);
	}
	
	@Override
	public void extractSafely(PageParameters parameters, IPageLinkDescriptor fallbackLink, String errorMessage)
			throws RestartResponseException {
		try {
			extract(parameters);
		} catch (Exception e) {
			INTERFACE_LOGGER.error("Error while extracting page parameters", e);
			if (StringUtils.hasText(errorMessage)) {
				Session.get().error(errorMessage);
			}
			throw fallbackLink.newRestartResponseException();
		}
	}

	@Override
	public String fullUrl() {
		return fullUrl(RequestCycle.get());
	}
	
	@Override
	public String fullUrl(RequestCycle requestCycle) {
		PageParameters parameters;
		try {
			parameters = getValidatedParameters();
		} catch (LinkParameterValidationException e) {
			throw new LinkParameterValidationRuntimeException(e);
		}
		
		return requestCycle.getUrlRenderer()
				.renderFullUrl(
						Url.parse(requestCycle.urlFor(getPageClass(), parameters))
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
	public RedirectToUrlException newRedirectToUrlException() throws LinkParameterValidationRuntimeException {
		return new RedirectToUrlException(fullUrl());
	}

	@Override
	public RedirectToUrlException newRedirectToUrlException(String anchor)
			throws LinkParameterValidationRuntimeException, LinkParameterInjectionRuntimeException {
		return new RedirectToUrlException(fullUrl() + ANCHOR_ROOT + anchor);
	}
	
	@Override
	public NavigationMenuItem navigationMenuItem(IModel<String> labelModel) throws LinkParameterValidationRuntimeException {
		return navigationMenuItem(labelModel, Lists.<NavigationMenuItem>newArrayListWithExpectedSize(0));
	}
	
	@Override
	public NavigationMenuItem navigationMenuItem(IModel<String> labelModel, Collection<NavigationMenuItem> subMenuItems)
			throws LinkParameterValidationRuntimeException {
		PageParameters parameters;
		try {
			parameters = getValidatedParameters();
		} catch (LinkParameterValidationException e) {
			throw new LinkParameterValidationRuntimeException(e);
		}
		
		return new NavigationMenuItem(labelModel, getPageClass(), parameters, this, subMenuItems);
	}
	
	@Override
	public boolean isAccessible() {
		return Session.get().getAuthorizationStrategy().isInstantiationAuthorized(getPageClass());
	}
	
	@Override
	public void detach() {
		super.detach();
		pageClassModel.detach();
	}

}
