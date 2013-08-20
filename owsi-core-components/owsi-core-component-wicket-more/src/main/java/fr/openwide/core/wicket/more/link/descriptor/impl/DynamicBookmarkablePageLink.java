package fr.openwide.core.wicket.more.link.descriptor.impl;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.lang.Args;

import fr.openwide.core.wicket.more.link.descriptor.AbstractDynamicBookmarkableLink;
import fr.openwide.core.wicket.more.link.descriptor.validator.IParameterValidator;


/**
 * An {@link AbstractDynamicBookmarkableLink} targeting a {@link Page} that may change during the page life cycle.
 * <p>This implementation could not derive from {@link BookmarkablePageLink}, whose target Page class is inherently static.
 * @see BookmarkablePageLink
 */
public class DynamicBookmarkablePageLink extends AbstractDynamicBookmarkableLink {

	private static final long serialVersionUID = -7297463634865525448L;
	
	private final IModel<Class<? extends Page>> pageClassModel;

	public DynamicBookmarkablePageLink(
			String wicketId,
			IModel<Class<? extends Page>> pageClassModel,
			IModel<PageParameters> parametersModel,
			IParameterValidator parametersValidator) {
		super(wicketId, parametersModel, parametersValidator);
		Args.notNull(pageClassModel, "pageClassModel");
		this.pageClassModel = wrap(pageClassModel); 
	}

	protected final Class<? extends Page> getPageClass() {
		Class<? extends Page> pageClass = pageClassModel.getObject();
		if (pageClass == null) {
			throw new IllegalStateException("The page class of a link of type " + getClass() + " was found to be null.");
		}
		return pageClass;
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
		return urlFor(getPageClass(), pageParameters);
	}
	
	@Override
	protected void onDetach() {
		super.onDetach();
		pageClassModel.detach();
	}

}
