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
import fr.openwide.core.wicket.more.link.descriptor.LinkInvalidTargetRuntimeException;
import fr.openwide.core.wicket.more.link.descriptor.parameter.extractor.IPageLinkParametersExtractor;
import fr.openwide.core.wicket.more.link.descriptor.parameter.injector.LinkParameterInjectionRuntimeException;
import fr.openwide.core.wicket.more.link.descriptor.parameter.mapping.LinkParametersMapping;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.ILinkParameterValidator;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.LinkParameterValidationRuntimeException;
import fr.openwide.core.wicket.more.markup.html.template.model.NavigationMenuItem;

public class CorePageLinkDescriptorImpl extends AbstractCoreLinkDescriptor implements IPageLinkDescriptor {
	
	private static final long serialVersionUID = -9139677593653180236L;
	
	private static final Logger EXTRACTOR_INTERFACE_LOGGER = LoggerFactory.getLogger(IPageLinkParametersExtractor.class);
	
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

	protected Class<? extends Page> getNonNullPageClass() throws LinkInvalidTargetRuntimeException {
		Class<? extends Page> pageClass = pageClassModel.getObject();
		if (pageClass == null) {
			throw new LinkInvalidTargetRuntimeException("The target page of this ILinkDescriptor was null");
		}
		return pageClass;
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
			EXTRACTOR_INTERFACE_LOGGER.error("Error while extracting page parameters", e);
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
		Class<? extends Page> pageClass = getNonNullPageClass();
		PageParameters parameters = getValidatedParameters();
		
		return requestCycle.getUrlRenderer()
				.renderFullUrl(
						Url.parse(requestCycle.urlFor(pageClass, parameters))
				);
	}
	
	@Override
	public void setResponsePage() throws LinkParameterValidationRuntimeException {
		Class<? extends Page> pageClass = getNonNullPageClass();
		PageParameters parameters = getValidatedParameters();
		
		RequestCycle.get().setResponsePage(pageClass, parameters);
	}

	@Override
	public RestartResponseException newRestartResponseException() throws LinkParameterValidationRuntimeException {
		Class<? extends Page> pageClass = getNonNullPageClass();
		PageParameters parameters = getValidatedParameters();
		
		return new RestartResponseException(pageClass, parameters);
	}
	
	@Override
	public RestartResponseAtInterceptPageException newRestartResponseAtInterceptPageException() throws LinkParameterValidationRuntimeException {
		Class<? extends Page> pageClass = getNonNullPageClass();
		PageParameters parameters = getValidatedParameters();
		
		return new RestartResponseAtInterceptPageException(pageClass, parameters);
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
		PageParameters parameters = getValidatedParameters();
		
		return new NavigationMenuItem(labelModel, getNonNullPageClass(), parameters, this, subMenuItems);
	}
	
	@Override
	public boolean isAccessible() {
		Class<? extends Page> pageClass = pageClassModel.getObject();
		if (pageClass == null) {
			return false;
		} else {
			return Session.get().getAuthorizationStrategy().isInstantiationAuthorized(pageClass);
		}
	}
	
	@Override
	public void detach() {
		super.detach();
		pageClassModel.detach();
	}

}
