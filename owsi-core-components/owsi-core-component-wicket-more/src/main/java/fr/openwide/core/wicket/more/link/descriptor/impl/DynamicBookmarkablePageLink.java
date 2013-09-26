package fr.openwide.core.wicket.more.link.descriptor.impl;

import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.lang.Args;

import fr.openwide.core.wicket.more.link.descriptor.AbstractDynamicBookmarkableLink;
import fr.openwide.core.wicket.more.link.descriptor.LinkInvalidTargetRuntimeException;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.ILinkParameterValidator;


/**
 * An {@link AbstractDynamicBookmarkableLink} whose targeted {@link Page} and {@link PageParameters} may change during the page life cycle.
 * <p>This implementation could not derive from {@link BookmarkablePageLink}, whose target Page class is inherently static.
 * @see BookmarkablePageLink
 */
public class DynamicBookmarkablePageLink extends AbstractDynamicBookmarkableLink {

	private static final long serialVersionUID = -7297463634865525448L;
	
	private final IModel<Class<? extends Page>> pageClassModel;

	public DynamicBookmarkablePageLink(
			String wicketId,
			IModel<Class<? extends Page>> pageClassModel,
			IModel<PageParameters> parametersMapping,
			ILinkParameterValidator parametersValidator) {
		super(wicketId, parametersMapping, parametersValidator);
		Args.notNull(pageClassModel, "pageClassModel");
		this.pageClassModel = wrap(pageClassModel); 
	}

	protected final Class<? extends Page> getPageClass() {
		return pageClassModel.getObject();
	}
	
	@Override
	protected final boolean isTargetValid() {
		Class<? extends Page> pageClass = getPageClass();
		return pageClass != null && Session.get().getAuthorizationStrategy().isInstantiationAuthorized(pageClass);
	}
	
	/**
	 * @see BookmarkablePageLink
	 */
	@Override
	protected boolean linksTo(Page page) {
		return page.getClass() == getPageClass();
	}

	/**
	 * @see BookmarkablePageLink
	 */
	@Override
	protected CharSequence getURL(PageParameters pageParameters) {
		Class<? extends Page> pageClass = getPageClass();
		if (pageClass == null) {
			throw new LinkInvalidTargetRuntimeException("The target page class of a link of type " + getClass() + " was null when trying to render the URL.");
		}
		if (!Session.get().getAuthorizationStrategy().isInstantiationAuthorized(pageClass)) {
			throw new LinkInvalidTargetRuntimeException("The instantiation of the target page class of a link of type " + getClass() + " was not authorized when trying to render the URL.");
		}
		return urlFor(getPageClass(), pageParameters);
	}
	
	@Override
	protected void onDetach() {
		super.onDetach();
		pageClassModel.detach();
	}

}
