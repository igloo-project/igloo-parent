package org.iglooproject.wicket.more.link.descriptor.impl;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import org.iglooproject.wicket.more.link.descriptor.AbstractDynamicBookmarkableLink;
import org.iglooproject.wicket.more.link.descriptor.generator.IPageLinkGenerator;


/**
 * An {@link AbstractDynamicBookmarkableLink} whose targeted {@link Page} and {@link PageParameters} may change during
 * the page life cycle.
 * <p>This implementation could not derive from {@link BookmarkablePageLink}, whose target Page class is inherently
 * static.
 * @see AbstractDynamicBookmarkableLink
 * @see BookmarkablePageLink
 */
public class DynamicBookmarkablePageLink extends DynamicBookmarkableLink {

	private static final long serialVersionUID = -7297463634865525448L;
	
	public DynamicBookmarkablePageLink(String wicketId, IPageLinkGenerator pageLinkGenerator) {
		super(wicketId, pageLinkGenerator);
	}
	
	@Override
	protected IPageLinkGenerator getLinkGenerator() {
		return (IPageLinkGenerator) super.getLinkGenerator();
	}
	
	/**
	 * @see BookmarkablePageLink
	 */
	@Override
	protected boolean linksTo(Page page) {
		return getLinkGenerator().isActive(page.getPageClass());
	}

}
