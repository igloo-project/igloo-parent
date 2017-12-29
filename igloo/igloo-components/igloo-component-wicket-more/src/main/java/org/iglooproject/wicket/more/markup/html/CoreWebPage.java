package org.iglooproject.wicket.more.markup.html;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import org.iglooproject.wicket.more.link.descriptor.generator.IPageLinkGenerator;

public abstract class CoreWebPage extends WebPage {

	private static final long serialVersionUID = 607411189304353902L;

	public CoreWebPage() {
	}

	public CoreWebPage(IModel<?> model) {
		super(model);
	}

	public CoreWebPage(PageParameters parameters) {
		super(parameters);
	}
	
	/**
	 * @deprecated This hides the exception throwing, which makes dead code harder to spot. Just throw a
	 * {@link RestartResponseException} yourself. Note that if you're using a {@link IPageLinkGenerator}, it
	 * can instantiate the exception for you.
	 */
	@Deprecated
	public final void redirect(final Class<? extends Page> clazz) {
		throw new RestartResponseException(clazz);
	}

	/**
	 * @deprecated This hides the exception throwing, which makes dead code harder to spot. Just throw a
	 * {@link RestartResponseException} yourself. Note that if you're using a {@link IPageLinkGenerator}, it
	 * can instantiate the exception for you.
	 */
	@Deprecated
	public final void redirect(final Class<? extends Page> clazz, PageParameters parameters) {
		throw new RestartResponseException(clazz, parameters);
	}

	/**
	 * @deprecated This hides the exception throwing, which makes dead code harder to spot. Just throw a
	 * {@link RestartResponseException} yourself. Note that if you're using a {@link IPageLinkGenerator}, it
	 * can instantiate the exception for you.
	 */
	@Deprecated
	public final void redirect(final Page page) {
		throw new RestartResponseException(page);
	}
	
	/**
	 * @deprecated This hides the exception throwing, which makes dead code harder to spot. Just throw a
	 * {@link RedirectToUrlException} yourself. Note that if you're using a {@link IPageLinkGenerator}, it
	 * can instantiate the exception for you.
	 */
	@Deprecated
	public final void redirect(String url) {
		throw new RedirectToUrlException(url);
	}
	
	/**
	 * @deprecated Just use {@link Component#setVisible(boolean)} or {@link Component#setVisibilityAllowed(boolean)}, or
	 * (better) add an {@link EnclosureBehavior} to manage the component's visibility declaratively.
	 */
	@Deprecated
	protected Component visible(Component component, boolean visible) {
		component.setVisible(visible);
		return component;
	}
	
	protected boolean isPageAccessible(Class<? extends Page> pageClass) {
		return Session.get().getAuthorizationStrategy().isInstantiationAuthorized(pageClass);
	}

}
