package fr.openwide.core.wicket.markup.html.navigation.paging;

import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.navigation.paging.IPagingLabelProvider;

public class HideablePagingNavigator extends
		org.apache.wicket.markup.html.navigation.paging.PagingNavigator {

	private static final long serialVersionUID = 1L;

	public HideablePagingNavigator(String id, IPageable pageable) {
		this(id, pageable, null);
	}
	
	public HideablePagingNavigator(final String id, final IPageable pageable,
			final IPagingLabelProvider labelProvider) {
		super(id, pageable, labelProvider);
	}

	@Override
	public boolean isVisible() {
		IPageable pageable = getPageable();
		return (pageable.getPageCount() > 1);
	}

}
