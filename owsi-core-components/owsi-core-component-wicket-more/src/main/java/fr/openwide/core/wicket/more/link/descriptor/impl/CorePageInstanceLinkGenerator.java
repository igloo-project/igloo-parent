package fr.openwide.core.wicket.more.link.descriptor.impl;

import java.util.Collection;

import org.apache.wicket.Page;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.Session;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.core.request.handler.PageProvider;
import org.apache.wicket.core.request.handler.RenderPageRequestHandler;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.flow.RedirectToUrlException;

import com.google.common.collect.Lists;

import fr.openwide.core.wicket.more.link.descriptor.AbstractDynamicBookmarkableLink;
import fr.openwide.core.wicket.more.link.descriptor.LinkInvalidTargetRuntimeException;
import fr.openwide.core.wicket.more.link.descriptor.generator.IPageLinkGenerator;
import fr.openwide.core.wicket.more.link.descriptor.parameter.injector.LinkParameterInjectionRuntimeException;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.LinkParameterValidationRuntimeException;
import fr.openwide.core.wicket.more.markup.html.template.model.NavigationMenuItem;

public class CorePageInstanceLinkGenerator implements IPageLinkGenerator {
	
	private static final long serialVersionUID = -1568375367568280290L;

	private static final String ANCHOR_ROOT = "#";
	
	private final IModel<? extends Page> pageInstanceModel;

	private final IModel<? extends Class<? extends Page>> expectedPageClassModel;

	public CorePageInstanceLinkGenerator(IModel<? extends Page> pageInstanceModel, IModel<? extends Class<? extends Page>> expectedPageClassModel) {
		this.pageInstanceModel = pageInstanceModel;
		this.expectedPageClassModel = expectedPageClassModel;
	}

	protected Page getValidPageInstance() throws LinkInvalidTargetRuntimeException {
		Page pageInstance = pageInstanceModel.getObject();
		if (pageInstance == null) {
			throw new LinkInvalidTargetRuntimeException("The target page instance was null");
		}
		Class<? extends Page> expectedPageClass = expectedPageClassModel.getObject();
		if (expectedPageClass != null && expectedPageClass.isAssignableFrom(pageInstance.getClass())) {
			throw new LinkInvalidTargetRuntimeException("The target page instance '" + pageInstance + "' had unexpected type : got " + pageInstance.getClass().getName() + ", expected " + expectedPageClass.getName());
		}
		return pageInstance;
	}

	@Override
	public String fullUrl() throws LinkInvalidTargetRuntimeException {
		return fullUrl(RequestCycle.get());
	}

	@Override
	public String fullUrl(RequestCycle requestCycle) throws LinkInvalidTargetRuntimeException {
		return requestCycle.getUrlRenderer().renderFullUrl(
				Url.parse(
						requestCycle.urlFor(
								new RenderPageRequestHandler(new PageProvider(getValidPageInstance()))
						)
				)
		);
	}

	@Override
	public AbstractDynamicBookmarkableLink link(String wicketId) {
		return new DynamicBookmarkablePageInstanceLink(wicketId, pageInstanceModel, expectedPageClassModel);
	}
	
	@Override
	public AbstractDynamicBookmarkableLink link(String wicketId, String anchor) {
		AbstractDynamicBookmarkableLink link = link(wicketId);
		link.add(new AttributeAppender("href", ANCHOR_ROOT + anchor));
		
		return link;
	}

	@Override
	public void setResponsePage() throws LinkInvalidTargetRuntimeException, LinkParameterValidationRuntimeException, LinkParameterInjectionRuntimeException {
		RequestCycle.get().setResponsePage(getValidPageInstance());
	}

	@Override
	public RestartResponseException newRestartResponseException() throws LinkInvalidTargetRuntimeException {
		return new RestartResponseException(getValidPageInstance());
	}

	@Override
	public RestartResponseAtInterceptPageException newRestartResponseAtInterceptPageException()
			throws LinkInvalidTargetRuntimeException {
		return new RestartResponseAtInterceptPageException(getValidPageInstance());
	}

	@Override
	public RedirectToUrlException newRedirectToUrlException() throws LinkInvalidTargetRuntimeException {
		return new RedirectToUrlException(fullUrl());
	}

	@Override
	public RedirectToUrlException newRedirectToUrlException(String anchor) throws LinkInvalidTargetRuntimeException {
		return new RedirectToUrlException(fullUrl() + ANCHOR_ROOT + anchor);
	}

	@Override
	public NavigationMenuItem navigationMenuItem(IModel<String> labelModel) throws LinkInvalidTargetRuntimeException {
		return navigationMenuItem(labelModel, Lists.<NavigationMenuItem>newArrayListWithExpectedSize(0));
	}

	@Override
	public NavigationMenuItem navigationMenuItem(IModel<String> labelModel, Collection<NavigationMenuItem> subMenuItems)
			throws LinkInvalidTargetRuntimeException {
		Page pageInstance = getValidPageInstance();
		return new NavigationMenuItem(labelModel, pageInstance.getClass(), pageInstance.getPageParameters(), this, subMenuItems);
	}

	@Override
	public boolean isAccessible() {
		Page pageInstance = pageInstanceModel.getObject();
		if (pageInstance == null) {
			return false;
		}
		Class<? extends Page> expectedPageClass = expectedPageClassModel.getObject();
		if (expectedPageClass != null && !expectedPageClass.isAssignableFrom(pageInstance.getClass())) {
			return false;
		}
		return Session.get().getAuthorizationStrategy().isActionAuthorized(pageInstance, Page.RENDER);
	}

	@Override
	public void detach() {
		pageInstanceModel.detach();
	}

}
