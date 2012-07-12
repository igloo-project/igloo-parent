package fr.openwide.core.wicket.more.markup.html.navigation.paging;

import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigationBehavior;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigationLink;
import org.apache.wicket.markup.html.navigation.paging.IPageable;

public class AjaxBootstrapPagingNavigationLink extends AjaxPagingNavigationLink {

	private static final long serialVersionUID = 7335503096497367205L;
	
	public AjaxBootstrapPagingNavigationLink(String id, IPageable pageable, int pageNumber) {
		super(id, pageable, pageNumber);
	}
	
	protected AjaxPagingNavigationBehavior newAjaxPagingNavigationBehavior(IPageable pageable, String event) {
		return new AjaxBootstrapPagingNavigationBehavior(this, pageable, event);
	}

}
