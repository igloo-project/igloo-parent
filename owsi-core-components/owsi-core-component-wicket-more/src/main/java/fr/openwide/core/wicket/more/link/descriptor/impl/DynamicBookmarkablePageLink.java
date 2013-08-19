package fr.openwide.core.wicket.more.link.descriptor.impl;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.lang.Args;

/**
 * A {@link BookmarkablePageLink} whose target page and parameters may change during the page life cycle.
 * <p>Uses {@link PageParametersModel}.
 * <p>Cannot derive from {@link BookmarkablePageLink}, whose target Page class is inherently static.
 * @see BookmarkablePageLink
 */
public class DynamicBookmarkablePageLink extends Link<Void> {

	private static final long serialVersionUID = -7297463634865525448L;
	
	private final IModel<Class<? extends Page>> pageClassModel;
	private final IModel<PageParameters> parametersModel;

	public DynamicBookmarkablePageLink(String wicketId, IModel<Class<? extends Page>> pageClassModel, IModel<PageParameters> parametersModel) {
		super(wicketId);
		Args.notNull(pageClassModel, "pageClassModel");
		Args.notNull(parametersModel, "dynamicParameters");
		this.pageClassModel = wrap(pageClassModel); 
		this.parametersModel = wrap(parametersModel);
	}

	protected final Class<? extends Page> getPageClass() {
		Class<? extends Page> pageClass = pageClassModel.getObject();
		if (pageClass == null) {
			throw new IllegalStateException("The page class of a link of type " + getClass() + " was found to be null.");
		}
		return pageClass;
	}
	
	protected final PageParameters getPageParameters() {
		return parametersModel.getObject();
	}
	
	/**
	 * @see BookmarkablePageLink
	 */
	@Override
	protected boolean linksTo(Page page) {
		return page.getClass() == getPageClass();
	}

	@Override
	protected boolean getStatelessHint() {
		return false; // This component might be stateful (due to pageClassModel and dynamicParameters)
	}

	/**
	 * @see BookmarkablePageLink
	 */
	@Override
	public final void onClick() {
		// Unused
	}

	/**
	 * @see BookmarkablePageLink
	 */
	@Override
	protected CharSequence getURL() {
		return urlFor(getPageClass(), getPageParameters());
	}
	
	@Override
	protected void onDetach() {
		super.onDetach();
		pageClassModel.detach();
		parametersModel.detach();
	}

}
