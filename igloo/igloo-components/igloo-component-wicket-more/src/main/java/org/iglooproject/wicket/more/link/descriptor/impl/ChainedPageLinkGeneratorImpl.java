package org.iglooproject.wicket.more.link.descriptor.impl;

import java.util.Collection;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.core.request.handler.PageProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.flow.RedirectToUrlException;

import com.google.common.collect.ImmutableList;

import org.iglooproject.wicket.more.link.descriptor.AbstractDynamicBookmarkableLink;
import org.iglooproject.wicket.more.link.descriptor.LinkInvalidTargetRuntimeException;
import org.iglooproject.wicket.more.link.descriptor.generator.ILinkGenerator;
import org.iglooproject.wicket.more.link.descriptor.generator.IPageLinkGenerator;
import org.iglooproject.wicket.more.link.descriptor.parameter.injector.LinkParameterInjectionRuntimeException;
import org.iglooproject.wicket.more.link.descriptor.parameter.validator.LinkParameterValidationRuntimeException;
import org.iglooproject.wicket.more.markup.html.template.model.NavigationMenuItem;

public class ChainedPageLinkGeneratorImpl extends AbstractChainedLinkGenerator<IPageLinkGenerator>
		implements IPageLinkGenerator {

	private static final long serialVersionUID = -2475023459193693212L;

	public ChainedPageLinkGeneratorImpl(Iterable<? extends IPageLinkGenerator> chain) {
		super(chain);
	}

	@Override
	public ILinkGenerator chain(ILinkGenerator other) {
		if (other instanceof IPageLinkGenerator) {
			return chain((IPageLinkGenerator) other);
		}
		return new ChainedLinkGeneratorImpl(
				ImmutableList.<ILinkGenerator>builder().addAll(getChain()).add(other).build()
		);
	}

	@Override
	public IPageLinkGenerator chain(IPageLinkGenerator other) {
		return new ChainedPageLinkGeneratorImpl(
				ImmutableList.<IPageLinkGenerator>builder().addAll(getChain()).add(other).build()
		);
	}

	@Override
	public IPageLinkGenerator wrap(Component component) {
		ImmutableList.Builder<IPageLinkGenerator> builder = ImmutableList.builder();
		for (IPageLinkGenerator element : getChain()) {
			builder.add(element.wrap(component));
		}
		return new ChainedPageLinkGeneratorImpl(builder.build());
	}

	@Override
	public AbstractDynamicBookmarkableLink link(String wicketId, String anchor) {
		return delegate().link(wicketId, anchor);
	}

	@Override
	public void setResponsePage() throws LinkInvalidTargetRuntimeException, LinkParameterValidationRuntimeException,
			LinkParameterInjectionRuntimeException {
		delegate().setResponsePage();
	}

	@Override
	public RestartResponseException newRestartResponseException() throws LinkInvalidTargetRuntimeException,
			LinkParameterValidationRuntimeException, LinkParameterInjectionRuntimeException {
		return delegate().newRestartResponseException();
	}

	@Override
	public RestartResponseAtInterceptPageException newRestartResponseAtInterceptPageException()
			throws LinkInvalidTargetRuntimeException, LinkParameterValidationRuntimeException,
			LinkParameterInjectionRuntimeException {
		return delegate().newRestartResponseAtInterceptPageException();
	}

	@Override
	public RedirectToUrlException newRedirectToUrlException() throws LinkInvalidTargetRuntimeException,
			LinkParameterValidationRuntimeException, LinkParameterInjectionRuntimeException {
		return delegate().newRedirectToUrlException();
	}

	@Override
	public RedirectToUrlException newRedirectToUrlException(String anchor) throws LinkInvalidTargetRuntimeException,
			LinkParameterValidationRuntimeException, LinkParameterInjectionRuntimeException {
		return delegate().newRedirectToUrlException(anchor);
	}

	@Override
	public NavigationMenuItem navigationMenuItem(IModel<String> labelModel) throws LinkInvalidTargetRuntimeException,
			LinkParameterValidationRuntimeException {
		return delegate().navigationMenuItem(labelModel);
	}

	@Override
	public NavigationMenuItem navigationMenuItem(IModel<String> labelModel, Collection<NavigationMenuItem> subMenuItems)
			throws LinkInvalidTargetRuntimeException, LinkParameterValidationRuntimeException {
		return delegate().navigationMenuItem(labelModel, subMenuItems);
	}

	@Override
	public boolean isActive(Class<? extends Page> selectedPage) {
		return delegate().isActive(selectedPage);
	}

	@Override
	public PageProvider newPageProvider() throws LinkInvalidTargetRuntimeException,
			LinkParameterValidationRuntimeException {
		return delegate().newPageProvider();
	}

	@Override
	@Deprecated
	public IPageLinkGenerator bypassPermissions() {
		throw new UnsupportedOperationException();
	}

}
