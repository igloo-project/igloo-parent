package fr.openwide.core.wicket.more.markup.html.navigation.paging;

import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.navigation.paging.IPagingLabelProvider;

public class HideableAjaxPagingNavigator extends AjaxPagingNavigator {
	private static final long serialVersionUID = -4406782762372796027L;
	
	public HideableAjaxPagingNavigator(String id, IPageable pageable) {
		this(id, pageable, null);
	}
	
	public HideableAjaxPagingNavigator(String id, IPageable pageable, IPagingLabelProvider labelProvider) {
		super(id, pageable, labelProvider);
	}
	
	@Override
	public boolean isVisible() {
		IPageable pageable = getPageable();
		return (pageable.getPageCount() > 1);
	}
}
